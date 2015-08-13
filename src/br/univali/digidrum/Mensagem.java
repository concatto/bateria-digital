package br.univali.digidrum;

public class Mensagem {
	private int pin;
	private int forca;
	
	public Mensagem() {
		this(0, 0);
	}
	
	public Mensagem(int pin, int forca) {
		setDados(pin, forca);
	}

	public int getPin() {
		return pin;
	}

	public int getForca() {
		return forca;
	}
	
	public void setDados(int pin, int forca) {
		this.pin = pin;
		this.forca = forca;
	}
}
