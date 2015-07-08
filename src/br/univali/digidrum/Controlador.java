package br.univali.digidrum;

import java.util.function.Consumer;

public class Controlador {	
	private UserInterface userInterface;
	private GerenciadorPortas gerenciador;
	private ConsumidorBateria consumidorBateria;
	
	public Controlador() {
		gerenciador = new GerenciadorPortas(3);
		userInterface = new UserInterface();
		userInterface.setAcaoPin(System.out::println);
		userInterface.onEstabelecerConexao(this::inicializar);
		userInterface.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Controlador();
	}

	public void inicializar() {
		boolean sucesso = gerenciador.abrirExperimental();
		if (sucesso) {
			consumidorBateria = new ConsumidorBateria();
			gerenciador.addConsumidor(consumidorBateria);
			gerenciador.addConsumidor(new Consumer<Byte[]>() {
				@Override
				public void accept(Byte[] t) {
					userInterface.adicionarSinal(t[0], ConsumidorBateria.unirBytes(t[1], t[2]));
				}
			});
			
			userInterface.habilitar();
		} else {
			userInterface.mensagem("Falha ao abrir a porta.");
		}
	}
}
