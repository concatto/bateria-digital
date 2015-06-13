package br.univali.digibat;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class UserInterface extends JFrame {
	private JMenu arquivo = new JMenu("Arquivo");
	private JMenuItem sair = new JMenuItem("Sair");
	private JMenu janela = new JMenu("Janela");
	private JMenuItem graficoSinal = new JMenuItem("Gráfico do Sinal");
	private JMenuBar menuBar = new JMenuBar();	
	
	private PainelSelecao painelSelecao;
	private PainelSensores painelSensores;
	private PainelTeclado painelTeclado;
	private Controlador controlador;
	
	public UserInterface(Controlador controlador) {
		super("Digibat");
		this.controlador = controlador;	
		alterarModo(ModoInterface.SELECAO);
		
		arquivo.add(sair);
		janela.add(graficoSinal);
		menuBar.add(arquivo);
		menuBar.add(janela);
		
		setJMenuBar(menuBar);
		setContentPane(painelSelecao);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public void alterarModo(ModoInterface modo) {
		PainelBase painel;
		
		switch (modo) {
		case SELECAO:
			if (painelSelecao == null) painelSelecao = new PainelSelecao(controlador);
			painel = painelSelecao;
			break;
		case TECLADO:
			if (painelTeclado == null) painelTeclado = new PainelTeclado(controlador);
			painel = painelTeclado;
			break;
		case SENSORES:
			if (painelSensores == null) painelSensores = new PainelSensores(controlador);
			painel = painelSensores;
			break;
		default:
			throw new IllegalArgumentException("Modo " + modo + " não implementado.");
		}
		
		setContentPane(painel);
		revalidate();
	}

	public void mensagem(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem);
	}
	
	public void erro(Exception e) {
		JOptionPane.showMessageDialog(this, e);
	}
	
	public String getPortaSelecionada() {
		return painelSelecao.getPortaSelecionada();
	}

	public void definirPortas(String[] nomesPortas) {
		painelSelecao.definirPortas(nomesPortas);
	}
}
