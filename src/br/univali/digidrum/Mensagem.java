package br.univali.digidrum;

public class Mensagem {
	private int pin;
	private int dado;
	
	public Mensagem() {
		this(0, 0);
	}
	
	public Mensagem(int pin, int dado) {
		setAtributos(pin, dado);
	}

	public int getPin() {
		return pin;
	}

	public int getDado() {
		return dado;
	}
	
	public void setAtributos(int pin, int dado) {
		this.pin = pin;
		this.dado = dado;
	}
	
	@Override
	public String toString() {
		return String.format("[Mensagem: Pin=%d, Força=%d]", pin, dado);
	}
}
