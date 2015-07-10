package br.univali.digidrum;

import java.util.Map;
import java.util.function.Consumer;

public class ConsumidorBateria implements Consumer<Byte[]> {
	private Map<Integer, Sensor> sensores;
	
	public ConsumidorBateria(Map<Integer, Sensor> sensores) {
		this.sensores = sensores;
		GerenciadorAudio.iniciar();
	}
	
	public static int unirBytes(byte primeiro, byte segundo) {
		return (primeiro & 0xFF) | ((segundo & 0xFF) << 8);
	}
	
	@Override
	public void accept(Byte[] bytes) {
		int sinal = unirBytes(bytes[1], bytes[2]);
		
		Sensor sensor = sensores.get((int) bytes[0]);
		
		if (sensor != null) {
			sensor.atualizarForca(sinal);
			
			if (sensor.isPronto()) {
				Instrumento inst = sensor.getInstrumento();
				if (inst.isValido()) GerenciadorAudio.tocar(inst.getNota(), converterForca(sensor.getForcaMaxima()));
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
