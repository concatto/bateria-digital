package br.univali.digidrum;

import java.util.function.Consumer;

public class Decodificador {
	private Runnable keepaliveAction;
	private Consumer<Mensagem> mensagemAction;
	private Mensagem mensagem = new Mensagem();
	
	private int pin;
	private boolean proximoForca;
	
	public Decodificador() {
		
	}
	
	public void onKeepalive(Runnable keepaliveAction) {
		this.keepaliveAction = keepaliveAction;
	}
	
	public void onMensagem(Consumer<Mensagem> mensagemAction) {
		this.mensagemAction = mensagemAction;
	}
	
	public void decodificar(byte[] dados) {
		for (byte dado : dados) {
			decodificar(dado);
		}
	}
	
	public void decodificar(byte dado) {
		if (proximoForca) {
			mensagem.setDados(pin, dado);
			mensagemAction.accept(mensagem);
			proximoForca = false;
		} else {
			switch ((dado & 0xFF) >> 4) {
			case Protocolo.KEEPALIVE:
				if ((dado & 0xF) == 0 && keepaliveAction != null) {
					keepaliveAction.run();
				}
				
				break;
			case Protocolo.MENSAGEM:
				pin = dado & 0xF;
				proximoForca = true;
				break;
			}
		}
	}
	
	public byte testarHandshake(byte dado) {
		if ((dado & 0xFF) >> 4 == Protocolo.HANDSHAKE) {
			return (byte) (dado & 0xF);
		}
		
		return -1;
	}
}
