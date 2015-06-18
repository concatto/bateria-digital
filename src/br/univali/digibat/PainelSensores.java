package br.univali.digibat;

@SuppressWarnings("serial")
public class PainelSensores extends PainelBase {
	private PainelInstrumento um;	
	
	public PainelSensores(Controlador controlador) {
		super(controlador);
		um = new PainelInstrumento(0);
		add(um);
	}
}
