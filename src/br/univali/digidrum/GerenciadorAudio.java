package br.univali.digidrum;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class GerenciadorAudio {
	private Synthesizer synth;
	private MidiChannel canal;
	
	public GerenciadorAudio() {		
		try {
			synth = MidiSystem.getSynthesizer();
			File file = new File(GerenciadorAudio.class.getClassLoader().getResource("res/FluidR3.SF2").toURI());
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
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void tocar(int nota, int forca) {
		canal.noteOn(nota, forca);
	}
	
	public void soltar(int nota) {
		canal.noteOff(nota);
	}
}
