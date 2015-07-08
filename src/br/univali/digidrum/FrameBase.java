package br.univali.digidrum;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class FrameBase extends JFrame {
	public FrameBase(String titulo) {
		super(titulo);
		
		inicializarComponentes();
		
		pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	protected abstract void inicializarComponentes();
}
