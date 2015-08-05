package br.univali.digidrum;

import java.util.Map;
import java.util.function.Consumer;

public class ConsumidorBateria implements Consumer<Byte[]> {
	private Map<Integer, Sensor> sensores;
	private GerenciadorAudio audio;
	
	public ConsumidorBateria(Map<Integer, Sensor> sensores) {
		this.sensores = sensores;
		audio = new GerenciadorAudio();
	}
	
	@Override
	public void accept(Byte[] bytes) {
		int sinal = ByteUtils.unirBytes(bytes[1], bytes[2]);
		
		Sensor sensor = sensores.get((int) bytes[0]);
		
		if (sensor != null) {
			sensor.atualizarForca(sinal);
			
			if (sensor.isPronto()) {
				Instrumento instr = sensor.getInstrumento();
				if (instr.isValido()) audio.tocar(instr.getNota(), ByteUtils.sinalParaByte(sensor.getForcaMaxima()));
				sensor.resetar();
			}
		}
	}
}
