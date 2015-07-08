package br.univali.digidrum;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ConsumidorBateria implements Consumer<Byte[]> {
	private Map<Integer, Sensor> instrumentos = new HashMap<>();
	
	public ConsumidorBateria() {
		GerenciadorAudio.iniciar();
	}
	
	public static int unirBytes(byte primeiro, byte segundo) {
		return (primeiro & 0xFF) | ((segundo & 0xFF) << 8);
	}
	
	public void definirInstrumento(int indice, int instrumento) {
		Sensor sensor = instrumentos.get(indice);
		if (sensor == null) {
			instrumentos.put(indice, new Sensor(instrumento));
		} else {
			sensor.setInstrumento(instrumento);
		}
	}
	
	@Override
	public void accept(Byte[] bytes) {
		int sinal = unirBytes(bytes[1], bytes[2]);
		
		Sensor sensor = instrumentos.get((int) bytes[0]);
		
		if (sensor != null) {
			int forca = (sinal * 87 / 600) + 40;
			if (forca > 127) forca = 127;
			
			sensor.atualizarForca(sinal);
			
			if (sensor.isPronto()) {
				System.out.printf("Inst: %d. Forca: %d\n", sensor.getInstrumento(), converterForca(sensor.getForcaMaxima()));
				if (sensor.getInstrumento() != -1) GerenciadorAudio.tocar(sensor.getInstrumento(), converterForca(sensor.getForcaMaxima()));
				sensor.resetar();
			}
		}
	}
	
	private int converterForca(int sinal) {
		int forca = (sinal * 87 / 600) + 40;
		if (forca > 127) forca = 127;
		
		return forca;
	}
}
