package br.univali.digibat;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class FrameBase extends JFrame {
	protected Controlador controlador;
	
	public FrameBase(Controlador controlador, String titulo) {
		super(titulo);
		this.controlador = controlador;
		
		inicializarComponentes();
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	protected abstract void inicializarComponentes();
}
