package br.univali.digidrum;

import java.nio.ByteBuffer;

public class ByteUtils {
	public static int sinalParaByte(int sinal) {
		int forca = (sinal * 87 / 600) + 40;
		if (forca > 127) forca = 127;
		
		return forca;
	}
	
	public static int unirBytes(byte primeiro, byte segundo) {
		return (primeiro & 0xFF) | ((segundo & 0xFF) << 8);
	}
	
	public static void bufferParaBytes(ByteBuffer origem, Byte[] destino) {
		for (int i = 0; i < destino.length; i++) {
			destino[i] = origem.get();
		}
	}
}
