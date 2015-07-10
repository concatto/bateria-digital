package br.univali.digidrum;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Controlador {	
	private UserInterface userInterface;
	private GerenciadorPortas gerenciador;
	private ConsumidorBateria consumidorBateria;
	private Map<Integer, Sensor> sensores = new HashMap<>();
	
	public Controlador() {
		gerenciador = new GerenciadorPortas(3);
		userInterface = new UserInterface();
		userInterface.onEstabelecerConexao(this::inicializar);
		userInterface.setSensorListener(e -> {
			if (e.getTipo() == SensorEvent.PIN_CHANGED) {
				Sensor removido = sensores.remove(e.getPinAntigo());
				if (e.getPinNovo() != -1) {
					if (removido == null) removido = new Sensor(e.getInstrumento());
					sensores.put(e.getPinNovo(), removido);
				}
			} else {
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
