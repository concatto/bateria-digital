package br.univali.digidrum;

import java.util.Arrays;
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
		int[] test = {0, 5, 15, 15, 2, 9};
		int[] r = new int[(int) Math.ceil(test.length / 2f)];
		for (int i = 0; i < test.length; i++) {
			int ri = i / 2;
			if (i % 2 == 0) {
				r[ri] = test[i] << 4;
			} else {
				r[ri] = r[ri] | test[i];
			}
		}
		System.out.println("Enviar " + Arrays.toString(r));
		for (int i : r) {
			System.out.println(i >> 4);
			System.out.println(i & 0xF);
		}
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
