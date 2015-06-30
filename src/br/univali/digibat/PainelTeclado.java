package br.univali.digibat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PainelTeclado extends PainelBase {
	private JPanel painelEntrada = new JPanel();

	public PainelTeclado(Controlador controlador) {
		super(controlador);
		
		setFocusable(true);
		painelEntrada.setLayout(new BorderLayout());
		painelEntrada.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		painelEntrada.setFocusable(true);
		final Instrumento[] instrumentos = {
				Instrumento.SIDE_STICK,
				Instrumento.BASS_DRUM,
				Instrumento.LOW_FLOOR_TOM,
				Instrumento.ACOUSTIC_SNARE,
				Instrumento.RIDE_CYMBAL,
				Instrumento.CRASH_CYMBAL,
				Instrumento.CHINESE_CYMBAL,
				Instrumento.HI_MID_TOM,
				Instrumento.HIGH_TOM,
				Instrumento.OPEN_HI_HAT
		};
		
		GerenciadorAudio.iniciar();
		
		painelEntrada.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int nota = Character.getNumericValue(e.getKeyChar());
				if (nota >= 0 && nota < instrumentos.length) {
					GerenciadorAudio.tocar(instrumentos[nota].getNota(), 127);
				}
			}
		});
		
		add(painelEntrada);
	}
	
	@Override
	public void validate() {
		super.validate();
		painelEntrada.requestFocusInWindow();
	}
}
