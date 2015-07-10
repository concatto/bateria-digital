package br.univali.digidrum;

public enum Instrumento {
	NENHUM(-1, "<nenhum>"),
	ACOUSTIC_BASS(35, "Bumbo acústico"),
	BASS_DRUM(36, "Bumbo"),
	SIDE_STICK(37, "Rimshot"),
	ACOUSTIC_SNARE(38, "Caixa acústica"),
	HAND_CLAP(39, "Palmas"),
	ELECTRIC_SNARE(40, "Caixa elétrica"),
	LOW_FLOOR_TOM(41, "Surdo grave"),
	CLOSED_HI_HAT(42, "Chimbau fechado"),
	HIGH_FLOOR_TOM(43, "Surdo agudo"),
	PEDAL_HI_HAT(44, "Chimbau (pedal)"),
	LOW_TOM(45, "Tom-tom grave"),
	OPEN_HI_HAT(46, "Chimbau aberto"),
	LOW_MID_TOM(47, "Tom-tom médio grave"),
	HI_MID_TOM(48, "Tom-tom médio agudo"),
	CRASH_CYMBAL(49, "Prato de ataque"),
	HIGH_TOM(50, "Tom-tom agudo"),
	RIDE_CYMBAL(51, "Prato de condução"),
	CHINESE_CYMBAL(52, "Prato chinês"),
	RIDE_BELL(53, "Sino?"),
	TAMBOURINE(54, "Pandeiro"),
	SPLASH_CYMBAL(55, "Prato de corte"),
	COWBELL(56, "Campana"),
	CRASH_CYMBAL_2(57, "Prato de Ataque 2");
	
	private int nota;
	private String nome;
	
	Instrumento(int nota) {
		this(nota, "<desconhecido:" + nota + ">");
	}
	
	Instrumento(int nota, String nome) {
		this.nota = nota;
		this.nome = nome;
	}
	
	public int getNota() {
		return nota;
	}
	
	public String getNome() {
		return nome;
	}
	
	public boolean isValido() {
		return nota != -1;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
