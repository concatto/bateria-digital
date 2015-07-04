package br.univali.digibat;

public class Controlador {	
	private UserInterface userInterface;
	private GerenciadorPortas gerenciador;
	private ConsumidorBateria consumidor;
	
	public Controlador() {
		gerenciador = new GerenciadorPortas(3);
		userInterface = new UserInterface(this);
		userInterface.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Controlador();
	}

	public void alterarInterface(ModoInterface modo) {
		userInterface.alterarModo(modo);
	}
	
	public void definirInstrumento(int pin, Instrumento instrumento) {
		consumidor.definirInstrumento(pin, instrumento.getNota());
	}

	public void inicializar() {
		boolean sucesso = gerenciador.abrirExperimental();
		if (sucesso) {
			alterarInterface(ModoInterface.SENSORES);
			consumidor = new ConsumidorBateria();
			gerenciador.addConsumidor(consumidor);
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
