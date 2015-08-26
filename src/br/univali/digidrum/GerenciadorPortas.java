package br.univali.digidrum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

public class GerenciadorPortas {
	private static final int TIMEOUT_HANDSHAKE = 4000;
	
	private int baudRate = SerialPort.BAUDRATE_9600;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;

	private List<Consumer<Mensagem>> consumidores = new ArrayList<>();
	private Decodificador decodificador = new Decodificador();
	private KeepaliveService keepaliveService;
	private SerialPort porta;
	
	public GerenciadorPortas() {
		
	}
	
	public boolean abrirExperimental() {
 		String[] portas = SerialPortList.getPortNames();
 		if (portas.length == 0) {
 			throw new IllegalStateException("Nenhuma porta encontrada. Verifique as permissões.");
 		}
		ExecutorService executor = Executors.newFixedThreadPool(portas.length);
		ExecutorCompletionService<SerialPort> completion = new ExecutorCompletionService<>(executor);
		
		for (final String porta : portas) {
			completion.submit(() -> tentarAbrir(porta));
		}
		
		for (int i = 0; i < portas.length; i++) {
			try {
				SerialPort porta = completion.take().get();
				if (porta != null) {
					executor.shutdownNow();
					this.porta = porta;
					iniciarComunicacao();
					return true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private SerialPort tentarAbrir(String porta) throws SerialPortException {
		SerialPort serial = new SerialPort(porta);
		serial.openPort();
		serial.setParams(baudRate, dataBits, stopBits, parity);
		byte[] bytes;
		
		try {
			bytes = serial.readBytes(1, TIMEOUT_HANDSHAKE);
		} catch (SerialPortTimeoutException e) {
			if (!Thread.currentThread().isInterrupted()) e.printStackTrace();
			System.out.println(Thread.interrupted());
			serial.closePort();
			return null;
		}
		
		byte mensagemHandshake = decodificador.testarHandshake(bytes[0]);
		if (mensagemHandshake != -1) {
			//Caso necessário, realizar alguma ação com a mensagem
			return serial;
		} else {
			serial.closePort();
			return null;
		}
	}

	private void iniciarComunicacao() throws SerialPortException {
		keepaliveService = new KeepaliveService(porta);
		
		decodificador.onKeepalive(() -> keepaliveService.receber());
		decodificador.onMensagem(msg -> consumidores.forEach(c -> c.accept(msg)));
		
		keepaliveService.iniciar();
		
		porta.addEventListener(serialPortEvent -> {
			try {
				byte[] bytes = porta.readBytes(serialPortEvent.getEventValue());
				decodificador.decodificar(bytes);
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		});
	}
	
	public boolean fecharPorta() throws SerialPortException {
		/* Curto circuito importante */
		if (porta == null || !porta.isOpened()) throw new IllegalStateException("A porta não está aberta.");
		return porta.closePort();
	}

	public void addConsumidor(Consumer<Mensagem> consumidor) {
		consumidores.add(consumidor);
	}
}
