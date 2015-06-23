package br.univali.digibat;

public class Sensor {
	private int instrumento;
	private boolean permissao;
	
	public Sensor(int instrumento, boolean permissao) {
		this.instrumento = instrumento;
		this.permissao = permissao;
	}
	
	public int getInstrumento() {
		return instrumento;
	}
	
	public boolean hasPermissao() {
		return permissao;
	}
	
	public void setInstrumento(int instrumento) {
		this.instrumento = instrumento;
	}
	
	public void setPermissao(boolean permissao) {
		this.permissao = permissao;
	}
}
