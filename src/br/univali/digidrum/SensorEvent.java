package br.univali.digidrum;

public class SensorEvent {
	public static final int PIN_CHANGED = 1;
	public static final int INSTRUMENTO_CHANGED = 2;
	
	private int tipo;
	private byte pinAntigo;
	private byte pinNovo;
	private Instrumento instrumento;
	
	public SensorEvent(int tipo, byte pinAntigo, byte pinNovo, Instrumento instrumento) {
		this.tipo = tipo;
		this.pinAntigo = pinAntigo;
		this.pinNovo = pinNovo;
		this.instrumento = instrumento;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public byte getPinAntigo() {
		return pinAntigo;
	}
	
	public byte getPinNovo() {
		return pinNovo;
	}
	
	public Instrumento getInstrumento() {
		return instrumento;
	}	
}
