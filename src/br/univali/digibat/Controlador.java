package br.univali.digibat;

public class Controlador {	
	private UserInterface userInterface;
	private GerenciadorPortas gerenciador;
	private ConsumidorBateria consumidorBateria;
	
	public Controlador() {
		gerenciador = new GerenciadorPortas(3);
		userInterface = new UserInterface(this);
		userInterface.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Controlador();
	}

	public void alterarInterface(ModoInterface modo) {
		userInterface.setModo(modo);
	}
	
	public void definirInstrumento(int pin, Instrumento instrumento) {
		consumidorBateria.definirInstrumento(pin, instrumento.getNota());
	}

	public void inicializar() {
		boolean sucesso = gerenciador.abrirExperimental();
		if (sucesso) {
			alterarInterface(ModoInterface.SENSORES);
			consumidorBateria = new ConsumidorBateria();
			userInterface.setConsumidorInstrumento(new BiConsumidor<Integer, Instrumento>() {
				@Override
				public void consumir(Integer t, Instrumento u) {
					definirInstrumento(t, u);
				}
			});
			gerenciador.addConsumidor(consumidorBateria);
			gerenciador.addConsumidor(new Consumidor<Byte[]>() {
				@Override
				public void consumir(Byte[] t) {
					userInterface.adicionarSinal(t[0], ConsumidorBateria.unirBytes(t[1], t[2]));
				}
			});
		} else {
			userInterface.mensagem("Falha ao abrir a porta.");
		}
	}
}
