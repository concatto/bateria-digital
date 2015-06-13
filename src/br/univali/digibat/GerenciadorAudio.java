package br.univali.digibat;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class GerenciadorAudio {
	private static Synthesizer synth;
	private static MidiChannel canal;
	
	static {
		try {
			synth = MidiSystem.getSynthesizer();
			canal = synth.getChannels()[9];
			synth.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public static void tocar(int nota) {
		canal.noteOn(nota, 127);
	}
	
	public void soltar(int nota) {
		canal.noteOff(nota);
	}
}
