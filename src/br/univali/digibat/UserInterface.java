package br.univali.digibat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class UserInterface extends FrameBase {
	private JMenu arquivo;
	private JMenuItem sair;
	private JMenu janela;
	private JMenuItem exibirGrafico;
	private JMenuBar menuBar;
	
	private GraficoSinal graficoSinal;
	private PainelInicio painelSelecao;
	private PainelSensores painelSensores;
	private PainelTeclado painelTeclado;
	
	private ModoInterface modo;
	
	public UserInterface(Controlador controlador) {
		super(controlador, "Digibat");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	@Override
	protected void inicializarComponentes() {
		arquivo = new JMenu("Arquivo");
		sair = new JMenuItem("Sair");
		janela = new JMenu("Janela");
		exibirGrafico = new JMenuItem("Gráfico do Sinal");
		menuBar = new JMenuBar();
		
		setModo(ModoInterface.SELECAO);
		
		arquivo.add(sair);
		janela.add(exibirGrafico);
		menuBar.add(arquivo);
		menuBar.add(janela);
		
		aplicarListeners();
		
		setJMenuBar(menuBar);
		setContentPane(painelSelecao);
	}
	
	private void aplicarListeners() {
		exibirGrafico.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (graficoSinal == null) graficoSinal = new GraficoSinal(controlador);
				graficoSinal.setVisible(true);
			}
		});
	}
	
	public void setModo(ModoInterface modo) {
		this.modo = modo;
		atualizarInterface();
	}
	
	public void atualizarInterface() {
		PainelBase painel;
		
		switch (modo) {
		case SELECAO:
			if (painelSelecao == null) painelSelecao = new PainelInicio(controlador);
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
		pack();
		setLocationRelativeTo(null);
		revalidate();
	}

	public void mensagem(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem);
	}
	
	public void erro(Exception e) {
		JOptionPane.showMessageDialog(this, e);
	}

	public void adicionarSinal(Byte pin, int sinal) {
		if (graficoSinal != null) graficoSinal.adicionarSinal(pin, sinal);
	}
	
	public void setConsumidorInstrumento(BiConsumidor<Integer, Instrumento> consumidor) {
		if (modo != ModoInterface.SENSORES) throw new IllegalStateException("O modo da interface não é SENSORES.");
		painelSensores.setConsumidorInstrumento(consumidor);
	}
}
