package br.univali.digibat;

import java.nio.ByteBuffer;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class GerenciadorPortas {
	private int baudRate = SerialPort.BAUDRATE_9600;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;
	private ByteArrayConsumer consumidor;
	
	private SerialPort porta;
	
	public GerenciadorPortas() {
		
	}
	
	public String[] obterPortas() {
		return SerialPortList.getPortNames();
	}
	
	public boolean abrirPorta(String nomePorta) throws SerialPortException {
		porta = new SerialPort(nomePorta);
		if (porta.openPort()) {
			if (porta.setParams(baudRate, dataBits, stopBits, parity)) {
				aplicarListener();
				return true;
			}
		}
		
		return false;
	}
	
	private void aplicarListener() throws SerialPortException {
		final ByteBuffer buffer = ByteBuffer.allocate(3);
		
		porta.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				try {
					byte[] bytes = porta.readBytes(serialPortEvent.getEventValue());

					buffer.put(bytes);
					if (buffer.remaining() == 0) {
						byte[] end = new byte[buffer.capacity()];
						buffer.position(0);
						buffer.get(end);
						consumidor.accept(end);
						
//						int high = end[end.length - 1] & 0xFF;
//						int low = end[end.length - 2] & 0xFF;
//
//						int sinal = low | (high << 8);
//						int pin = end[0];
//						
//						System.out.printf("Pin %d: %d. ", pin, sinal);
//						if (pin == 1) System.out.println();
//						
//						if (sinal > 300) {
//							if (permissoes[pin]) {
//								canal.noteOn(instrumentos[pin], 127);
//								permissoes[pin] = false;
//							}
//						} else {
//							permissoes[pin] = true;
//						}
						buffer.clear();
					}
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setConsumidor(ByteArrayConsumer consumidor) {
		this.consumidor = consumidor;
	}
}
