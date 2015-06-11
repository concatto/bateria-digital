package br.univali.digibat;

import jssc.SerialPortException;

public class Controlador {
	private UserInterface userInterface;
	private GerenciadorPortas gerenciador;
	
	public Controlador() {
		gerenciador = new GerenciadorPortas();
		userInterface = new UserInterface(this);
		userInterface.setVisible(true);
	}
	
	public void atualizarPortas() {
		userInterface.definirPortas(gerenciador.obterPortas());
	}
	
	public void confirmarPorta() {
		String porta = userInterface.getPortaSelecionada();
		try {
			boolean sucesso = gerenciador.abrirPorta(porta);
			if (!sucesso) userInterface.mensagem("Falha ao abrir a porta.");
		} catch (SerialPortException e) {
			userInterface.erro(e);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Controlador();
	}
}
