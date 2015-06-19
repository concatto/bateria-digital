package br.univali.digibat;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
	private int tamanhoMensagem;
	
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
				//TODO Heartbeat para o Arduino. Desenvolver um if.
				aplicarListener();
				return true;
			}
		}
		
		return false;
	}
	
	private void aplicarListener() throws SerialPortException {
		final ByteBuffer buffer = ByteBuffer.allocate(tamanhoMensagem);
		
		porta.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				try {
					byte[] bytes = porta.readBytes(serialPortEvent.getEventValue());

					buffer.put(bytes);
					if (buffer.remaining() == 0) {
						Byte[] bytesCompletos = new Byte[buffer.capacity()];
						buffer.position(0);
						transferirBytes(buffer, bytesCompletos);
						for (Consumidor<Byte[]> consumidor : consumidores) {
							if (consumidor != null) consumidor.consumir(bytesCompletos);
						}
						buffer.clear();
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
