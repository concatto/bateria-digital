package br.univali.digibat;

@SuppressWarnings("serial")
public class PainelSensores extends PainelBase {
	private PainelInstrumento um;	
	
	public PainelSensores(Controlador controlador) {
		super(controlador);
		
		um = new PainelInstrumento(0);
		um.onEscolherInstrumento(new Consumidor<Instrumento>() {
			@Override
			public void consumir(Instrumento t) {
				PainelSensores.this.controlador.definirInstrumento(um.getPin(), t);
			}
		});
		add(um);
	}
}
