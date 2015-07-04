package br.univali.digibat;

public class Sensor {
	private static final int DELTA_FORCA = 5;
	
	private int instrumento;
	private boolean pronto = false;
	private boolean preparando = false;
	private int forca;
	private int forcaMaxima;
	
	public Sensor(int instrumento) {
		this.instrumento = instrumento;
	}
	
	public int getInstrumento() {
		return instrumento;
	}
	
	public void setInstrumento(int instrumento) {
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
}
