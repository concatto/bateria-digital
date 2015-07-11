package br.univali.digidrum;

public interface ProtocoloSaida {
	public static final byte COMM_CMD = 0b0000;
	public static final byte ON_CMD = 0b0001;
	public static final byte OFF_CMD = 0b0010;
	
	public static final byte KEEPALIVE_DATA = 0b0001;
	public static final byte HANDSHAKE_DATA = 0b0010;
}
