package br.univali.digibat;

import java.util.HashMap;
import java.util.Map;

public class ConsumidorBateria implements Consumidor<Byte[]> {
	private Map<Integer, Sensor> instrumentos = new HashMap<>();
	
	public ConsumidorBateria() {
		GerenciadorAudio.iniciar();
	}
	
	public static int unirBytes(byte primeiro, byte segundo) {
		return (primeiro & 0xFF) | ((segundo & 0xFF) << 8);
	}
	
	public void definirInstrumento(int indice, int instrumento) {
		instrumentos.put(indice, new Sensor(instrumento, true));
	}

	@Override
	public void consumir(Byte[] bytes) {
		int sinal = unirBytes(bytes[1], bytes[2]);
		
		Sensor sensor = instrumentos.get((int) bytes[0]);
		System.out.printf("%d:%d ", bytes[0], sinal);
		if (bytes[0] == 1) System.out.println();
		if (sensor != null) {
			if (sinal > 400) {
				if (sensor.hasPermissao()) GerenciadorAudio.tocar(sensor.getInstrumento());
				sensor.setPermissao(false);
			} else {
				sensor.setPermissao(true);
			}
		}
	}
}
