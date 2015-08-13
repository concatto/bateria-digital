package br.univali.digidrum;

public interface Protocolo {
	public static final byte MENSAGEM = 0b0001;
	public static final byte KEEPALIVE = 0b1000;
	public static final byte HANDSHAKE = 0b0100;
}
