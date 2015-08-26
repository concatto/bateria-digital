package br.univali.digidrum;

import java.util.Map;
import java.util.function.Consumer;

public class ConsumidorBateria implements Consumer<Mensagem> {
	private Map<Integer, Sensor> sensores;
	private GerenciadorAudio audio;
	
	public ConsumidorBateria(Map<Integer, Sensor> sensores) {
		this.sensores = sensores;
		audio = new GerenciadorAudio();
	}
	
	@Override
	public void accept(Mensagem mensagem) {
		Sensor sensor = sensores.get(mensagem.getPin());
		
		if (sensor != null) {
			sensor.atualizarForca(mensagem.getDado());
			
			if (sensor.isPronto()) {
				Instrumento instr = sensor.getInstrumento();
				if (instr.isValido()) audio.tocar(instr.getNota(), ByteUtils.sinalParaByte(sensor.getForcaMaxima()));
				sensor.resetar();
			}
		}
	}
}
