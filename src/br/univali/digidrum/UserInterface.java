package br.univali.digidrum;

import java.util.function.Consumer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class UserInterface extends FrameBase {
	private JMenu arquivo;
	private JMenuItem sair;
	private JMenu ferramentas;
	private JMenuItem controlarTeclado;
	private JMenuItem exibirGrafico;
	private JMenuBar menuBar;
	
	private GraficoSinal graficoSinal;
	private PainelSensores painelSensores;
	private FrameTeclado painelTeclado;
	
	public UserInterface() {
		super("DigiDrum");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	@Override
	protected void inicializarComponentes() {
		painelSensores = new PainelSensores();
		
		arquivo = new JMenu("Arquivo");
		sair = new JMenuItem("Sair");
		ferramentas = new JMenu("Ferramentas");
		controlarTeclado = new JMenuItem("Debug: controlar com teclado");
		exibirGrafico = new JMenuItem("Gráfico do Sinal");
		menuBar = new JMenuBar();
		
		arquivo.add(sair);
		ferramentas.add(controlarTeclado);
		ferramentas.add(exibirGrafico);
		menuBar.add(arquivo);
		menuBar.add(ferramentas);
		
		aplicarListeners();
		
		setJMenuBar(menuBar);
		setContentPane(painelSensores);
	}
	
	private void aplicarListeners() {
		controlarTeclado.addActionListener(e -> {
			if (painelTeclado == null) painelTeclado = new FrameTeclado();
			painelTeclado.setVisible(true);
		});
		
		exibirGrafico.addActionListener(e -> {
			if (graficoSinal == null) graficoSinal = new GraficoSinal();
			graficoSinal.setVisible(true);
		});
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
	
	//TODO Melhorar esta ponte
	public void onEstabelecerConexao(Runnable acao) {
		painelSensores.onEstabelecerConexao(acao);
	}
	
	public void setAcaoPin(Consumer<Integer> acaoPin) {
		painelSensores.setAcaoPin(acaoPin);
	}
	
	public void setAcaoInstrumento(Consumer<Instrumento> acaoInstrumento) {
		painelSensores.setAcaoInstrumento(acaoInstrumento);
	}

	public void habilitar() {
		painelSensores.habilitar();
	}
}
