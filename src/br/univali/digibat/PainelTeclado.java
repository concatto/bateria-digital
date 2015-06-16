package br.univali.digibat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PainelTeclado extends PainelSensores {
	private JPanel painelEntrada = new JPanel();

	public PainelTeclado(Controlador controlador) {
		super(controlador);
		
		setFocusable(true);
		painelEntrada.setLayout(new BorderLayout());
		painelEntrada.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		painelEntrada.setFocusable(true);
		final int[] instrumentos = {
				Instrumentos.CRASH_CYMBAL,
				Instrumentos.BASS_DRUM,
				Instrumentos.LOW_FLOOR_TOM,
				Instrumentos.ACOUSTIC_SNARE,
				Instrumentos.RIDE_CYMBAL,
				Instrumentos.SPLASH_CYMBAL,
				Instrumentos.CHINESE_CYMBAL,
				Instrumentos.HI_MID_TOM,
				Instrumentos.HIGH_TOM,
				Instrumentos.OPEN_HI_HAT
		};
		
		GerenciadorAudio.iniciar();
		
		painelEntrada.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int nota = Character.getNumericValue(e.getKeyChar());
				if (nota >= 0 && nota < instrumentos.length) {
					GerenciadorAudio.tocar(instrumentos[nota]);
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