package br.univali.digibat;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class GerenciadorAudio {
	private static Synthesizer synth;
	private static MidiChannel canal;
	
	public static void iniciar() {
		if (synth != null) return;
		
		try {
			synth = MidiSystem.getSynthesizer();
			File file = new File("C:/Users/Fernando/bateria-digital/FluidR3.sf2");
			Soundbank bank;
			try {
				bank = MidiSystem.getSoundbank(file);
			} catch (IOException | InvalidMidiDataException e) {
				e.printStackTrace();
				return;
			}
			synth.open();
			canal = synth.getChannels()[9];
			if (synth.isSoundbankSupported(bank)) {
				Instrument instrument = bank.getInstruments()[10];
				synth.loadInstrument(instrument);
				canal.programChange(instrument.getPatch().getProgram());
			}
			
			
			
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
