package br.univali.digidrum;

public class Sensor {
	private static final int DELTA_FORCA = 50;
	
	private Instrumento instrumento;
	private boolean pronto = false;
	private boolean preparando = false;
	private int forca;
	private int forcaMaxima;
	
	public Sensor(Instrumento instrumento) {
		this.instrumento = instrumento;
	}
	
	public Instrumento getInstrumento() {
		return instrumento;
	}
	
	public void setInstrumento(Instrumento instrumento) {
		this.instrumento = instrumento;
	}

	public void atualizarForca(int forca) {
		if (forca - this.forca > DELTA_FORCA) {
			preparando = true;
		} else if (preparando && forca - this.forca < -DELTA_FORCA) {
			preparando = false;
			pronto = true;
			forcaMaxima = this.forca;
		}
		
		this.forca = forca;
	}
	
	public void resetar() {
		pronto = false;
	}

	public boolean isPronto() {
		return pronto;
	}
	
	public int getForcaMaxima() {
		return forcaMaxima;
	}
	
	@Override
	public String toString() {
		return instrumento.toString();
	}
}
