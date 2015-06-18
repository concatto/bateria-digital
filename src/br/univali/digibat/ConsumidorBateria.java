package br.univali.digibat;


public class ConsumidorBateria implements ByteArrayConsumer {
	public ConsumidorBateria() {
		GerenciadorAudio.iniciar();
	}
	
	@Override
	public void accept(byte[] bytes) {
		int sinal = unirBytes(bytes[1], bytes[2]);
		
		if (sinal > 300) {
//			GerenciadorAudio.tocar(bytes[0] == 0 ? Instrumento.CRASH_CYMBAL : Instrumento.SPLASH_CYMBAL);
		}
	}
	
	private int unirBytes(byte primeiro, byte segundo) {
		return (primeiro & 0xFF) | ((segundo & 0xFF) << 8);
	}
}
