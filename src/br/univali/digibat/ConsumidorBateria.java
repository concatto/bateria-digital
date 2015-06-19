package br.univali.digibat;

import java.util.HashMap;
import java.util.Map;


public class ConsumidorBateria implements Consumidor<Byte[]> {
	private Map<Integer, Integer> instrumentos = new HashMap<>();
	
	public ConsumidorBateria() {
		GerenciadorAudio.iniciar();
	}
	
	private int unirBytes(byte primeiro, byte segundo) {
		return (primeiro & 0xFF) | ((segundo & 0xFF) << 8);
	}
	
	public void definirInstrumento(int indice, int instrumento) {
		instrumentos.put(indice, instrumento);
	}

	@Override
	public void consumir(Byte[] bytes) {
		int sinal = unirBytes(bytes[1], bytes[2]);
		
		if (sinal > 300) {
			Integer instrumento = instrumentos.get((int) bytes[0]);
			System.out.println(instrumentos);
			if (instrumento != null) GerenciadorAudio.tocar(instrumento);
		}
	}
}
