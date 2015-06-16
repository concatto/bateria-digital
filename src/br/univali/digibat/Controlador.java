package br.univali.digibat;

import jssc.SerialPortException;

public class Controlador {
	private UserInterface userInterface;
	private GerenciadorPortas gerenciador;
	private ConsumidorBateria consumidor;
	
	public Controlador() {
		gerenciador = new GerenciadorPortas(3);
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
			if (sucesso) {
				alterarInterface(ModoInterface.SENSORES);
				consumidor = new ConsumidorBateria();
				gerenciador.setConsumidor(consumidor);
			} else {
				userInterface.mensagem("Falha ao abrir a porta.");
			}
		} catch (SerialPortException e) {
			userInterface.erro(e);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Controlador();
	}

	public void alterarInterface(ModoInterface modo) {
		userInterface.alterarModo(modo);
	}
}
