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
		
		if (sensor != null) {
			int forca = (sinal * 87 / 600) + 40;
			if (forca > 127) forca = 127;
			
			if (sensor.isPronto()) {
				GerenciadorAudio.tocar(sensor.getInstrumento(), Math.max(forca, sensor.getForca()));
				sensor.resetar();
			} else if (sinal > 30) {
				if (sensor.hasPermissao()) {
					sensor.preparar(forca);
					GerenciadorAudio.tocar(sensor.getInstrumento(), forca);
				}
				sensor.setPermissao(false);
			} else {
				sensor.setPermissao(true);
			}
		}
	}
}
