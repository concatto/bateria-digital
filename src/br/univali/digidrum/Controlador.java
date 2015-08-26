package br.univali.digidrum;

import java.util.HashMap;
import java.util.Map;

public class Controlador {	
	private UserInterface userInterface;
	private GerenciadorPortas gerenciador;
	private ConsumidorBateria consumidorBateria;
	private Map<Integer, Sensor> sensores = new HashMap<>();
	
	public Controlador() {
		gerenciador = new GerenciadorPortas();
		userInterface = new UserInterface();
		userInterface.onEstabelecerConexao(this::inicializar);
		
		userInterface.setSensorListener(e -> {
			if (e.getTipo() == SensorEvent.PIN_CHANGED) {
				//Alteração no pin
				Sensor removido = sensores.remove(e.getPinAntigo());
				if (e.getPinNovo() != -1) {
					//Se o antigo não existia, criar novo
					if (removido == null) removido = new Sensor(e.getInstrumento());
					sensores.put(e.getPinNovo(), removido);
				}
			} else {
				//Alteração no instrumento
				sensores.get(e.getPinNovo()).setInstrumento(e.getInstrumento());
			}
		});
		
		userInterface.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Controlador();
	}

	public void inicializar() {
		boolean sucesso = gerenciador.abrirExperimental();
		if (sucesso) {
			consumidorBateria = new ConsumidorBateria(sensores);
			
//			gerenciador.addConsumidor(System.out::println);
//			gerenciador.addConsumidor(consumidorBateria);
			gerenciador.addConsumidor(msg -> {
				userInterface.getGraficoSinal().ifPresent(grafico -> {
					grafico.adicionarSinal(msg.getPin(), msg.getDado());
				});
			});
			
			userInterface.habilitar();
		} else {
			userInterface.mensagem("Falha ao abrir a porta.");
		}
	}
}
