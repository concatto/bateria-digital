package br.univali.digidrum;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FrameTeclado extends FrameBase {
	private JPanel raiz;
	private JPanel painelEntrada;

	public FrameTeclado() {
		super("Controlador");
		GerenciadorAudio.iniciar();
	}
	
	@Override
	protected void inicializarComponentes() {
		raiz = new JPanel();
		painelEntrada = new JPanel();
		
		raiz.setFocusable(true);
		painelEntrada.setPreferredSize(new Dimension(200, 100));
		painelEntrada.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
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
		
		painelEntrada.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int nota = Character.getNumericValue(e.getKeyChar());
				if (nota >= 0 && nota < instrumentos.length) {
					GerenciadorAudio.tocar(instrumentos[nota].getNota(), 127);
				}
			}
		});
		
		raiz.add(painelEntrada);
		add(raiz);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) painelEntrada.requestFocusInWindow();
	}
}
