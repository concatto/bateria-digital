package br.univali.digibat;

@SuppressWarnings("serial")
public class PainelSensores extends PainelBase {
	private PainelInstrumento um;
	private PainelInstrumento dois;	
	
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
		
		dois = new PainelInstrumento(1);
		dois.onEscolherInstrumento(new Consumidor<Instrumento>() {
			@Override
			public void consumir(Instrumento t) {
				PainelSensores.this.controlador.definirInstrumento(dois.getPin(), t);
			}
		});
		add(dois);
	}
}
