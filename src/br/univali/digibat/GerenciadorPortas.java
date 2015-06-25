package br.univali.digibat;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class GerenciadorPortas {
	private static final int FALHAS_HEARTBEAT = 3;
	private static final byte[] HEARTBEAT = {85, 78, 73};
	private static final Byte[] HEARTBEAT_BOXED = box(HEARTBEAT);
	private static final int TAMANHO_BUFFER = 64;
	
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
	
	public String[] obterPortas() {
		return SerialPortList.getPortNames();
	}
	
	public boolean abrirPorta(String nomePorta) throws SerialPortException {
		porta = new SerialPort(nomePorta);
		if (porta.openPort()) {
			if (porta.setParams(baudRate, dataBits, stopBits, parity)) {
				iniciarHeartbeat();
				aplicarListener();
				return true;
			}
		}
		
		return false;
	}
	
	private void iniciarHeartbeat() {
		heartbeatTask = heartbeatThread.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (vivo) {
					falhasHeartbeat++;
					if (falhasHeartbeat > FALHAS_HEARTBEAT) {
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

	private void aplicarListener() throws SerialPortException, IllegalStateException {
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
						
						if (Arrays.equals(bytesCompletos, HEARTBEAT_BOXED)) {
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
		if (origem.remaining() > destino.length) throw new BufferOverflowException();
		for (int i = 0; i < destino.length; i++) {
			destino[i] = origem.get();
		}
	}
	
	private static Byte[] box(byte[] origem) {
		Byte[] destino = new Byte[origem.length];
		for (int i = 0; i < origem.length; i++) {
			destino[i] = origem[i];
		}
		return destino;
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
