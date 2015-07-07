package br.univali.digibat;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

public class GerenciadorPortas {
	private static final int TIMEOUT_HANDSHAKE = 4000;
	private static final byte[] HANDSHAKE = {85, 66, 68};
	private static final byte[] HANDSHAKE_OK = {72, 79, 75};
	private static final byte[] HEARTBEAT = {85, 78, 73};
	private static final int LIMITE_FALHAS_HEARTBEAT = 3;
	private static final int TAMANHO_BUFFER = 128;
	
	private int baudRate = SerialPort.BAUDRATE_9600;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;
	private int tamanhoMensagem;
	private boolean vivo = false;
	private int falhasHeartbeat = 0;
	
	private ScheduledExecutorService heartbeatThread = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> heartbeatTask;
	private List<Consumidor<Byte[]>> consumidores = new ArrayList<>();
	private SerialPort porta;
	
	public GerenciadorPortas(int tamanhoMensagem) {
		this.tamanhoMensagem = tamanhoMensagem;
	}
	
	public boolean abrirExperimental() {
 		String[] portas = SerialPortList.getPortNames();
		ExecutorService executor = Executors.newFixedThreadPool(portas.length);
		ExecutorCompletionService<SerialPort> completion = new ExecutorCompletionService<>(executor);
		
		for (final String porta : portas) {
			completion.submit(new Callable<SerialPort>() {
				@Override
				public SerialPort call() throws Exception {
					return tentarAbrir(porta);
				}
			});
		}
		
		for (int i = 0; i < portas.length; i++) {
			try {
				SerialPort porta = completion.take().get();
				if (porta != null) {
					executor.shutdown();
					this.porta = porta;
					iniciarHeartbeat();
					aplicarListener();
					return true;
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private SerialPort tentarAbrir(String porta) {
		SerialPort serial = new SerialPort(porta);
		try {
			serial.openPort();
			serial.setParams(baudRate, dataBits, stopBits, parity);
			byte[] bytes = serial.readBytes(HANDSHAKE.length, TIMEOUT_HANDSHAKE);
			if (Arrays.equals(bytes, HANDSHAKE)) {
				if (serial.writeBytes(HANDSHAKE_OK)) {
					return serial;
				}
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		} catch (SerialPortTimeoutException e) {
			System.out.println("Porta " + porta + " não respondeu.");
		}
		
		try {
			serial.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void iniciarHeartbeat() {
		heartbeatTask = heartbeatThread.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (vivo) {
					falhasHeartbeat++;
					if (falhasHeartbeat > LIMITE_FALHAS_HEARTBEAT) {
						vivo = false;
					}
				}
				try {
					if (!porta.writeBytes(HEARTBEAT)) {
						vivo = false;
					}
				} catch (SerialPortException e) {
					e.printStackTrace();
					vivo = false;
				}
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
	}

	private void aplicarListener() throws SerialPortException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(TAMANHO_BUFFER);
		
		porta.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				try {
					byte[] bytes = porta.readBytes(serialPortEvent.getEventValue());
					try {
						buffer.put(bytes);
					} catch (BufferOverflowException e) {
						e.printStackTrace();
						vivo = false;
					}
					
					while (buffer.position() > tamanhoMensagem) {
						Byte[] bytesCompletos = new Byte[tamanhoMensagem];
						buffer.flip();
						transferirBytes(buffer, bytesCompletos);
						buffer.compact();
						
						if (isHeartbeat(bytesCompletos)) {
							falhasHeartbeat = 0;
							vivo = true;
							System.out.println("It's alive!");
						} else {
							for (Consumidor<Byte[]> consumidor : consumidores) {
								if (consumidor != null) consumidor.consumir(bytesCompletos);
							}
						}
					}
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void transferirBytes(ByteBuffer origem, Byte[] destino) {
		for (int i = 0; i < destino.length; i++) {
			destino[i] = origem.get();
		}
	}
	
	private static boolean isHeartbeat(Byte[] teste) {
		if (teste.length != HEARTBEAT.length) return false;
		for (int i = 0; i < teste.length; i++) {
			if (teste[i] != HEARTBEAT[i]) return false;
		}
		return true;
	}
	
	public boolean fecharPorta() throws SerialPortException {
		/* Curto circuito importante */
		if (porta == null || !porta.isOpened()) throw new IllegalStateException("A porta não está aberta.");
		
		heartbeatTask.cancel(true);
		return porta.closePort();
	}
	
	public boolean isVivo() {
		return vivo;
	}
	
	public void setTamanhoMensagem(int tamanhoMensagem) {
		this.tamanhoMensagem = tamanhoMensagem;
	}
	
	public int getTamanhoMensagem() {
		return tamanhoMensagem;
	}

	public void addConsumidor(Consumidor<Byte[]> consumidor) {
		consumidores.add(consumidor);
	}
}
