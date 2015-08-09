package br.univali.digidrum;

import java.util.HashMap;
import java.util.Map;

import jssc.SerialPort;
import jssc.SerialPortException;

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
	
	//TODO transferir para o lugar certo
	public static void protoEnviar(int msb, int lsb, SerialPort p) throws SerialPortException {
		byte b = (byte) ((msb << 4) | lsb);
		p.writeByte(b);
	}
	
	public static void main(String[] args) {
		new Controlador();
	}

	public void inicializar() {
		boolean sucesso = gerenciador.abrirExperimental();
		if (sucesso) {
			consumidorBateria = new ConsumidorBateria(sensores);
			
			gerenciador.addConsumidor(consumidorBateria);
			gerenciador.addConsumidor(bytes -> {
				userInterface.getGraficoSinal().ifPresent(grafico -> {
					grafico.adicionarSinal(bytes[0], ByteUtils.unirBytes(bytes[1], bytes[2]));
				});
			});
			
			userInterface.habilitar();
		} else {
			userInterface.mensagem("Falha ao abrir a porta.");
		}
	}
}
