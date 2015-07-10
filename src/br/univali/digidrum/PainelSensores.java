package br.univali.digidrum;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PainelSensores extends JPanel {
	private JButton estabelecerConexao = new JButton("Estabelecer vínculo simbiótico");
	private JButton adicionarSensor = new JButton("Adicionar sensor");
	private JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
	private JPanel[] linhas = new JPanel[4];
	
	private List<PainelSensor> paineisInstrumento = new ArrayList<>();
	private Consumer<SensorEvent> listener;
	
	public PainelSensores() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		painelSuperior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		painelSuperior.add(estabelecerConexao);
		painelSuperior.add(adicionarSensor);
		add(painelSuperior);
		
		adicionarSensor.addActionListener(e -> {
			adicionarPainelInstrumento();
		});
	}
	
	private void adicionarPainelInstrumento() {
		int indice = paineisInstrumento.size();
		int linha = indice / 4;
		PainelSensor painel = new PainelSensor(listener);
		paineisInstrumento.add(painel);
		
		if (indice == linhas.length * 4) adicionarSensor.setEnabled(false);
		
		if (linhas[linha] == null) {
			linhas[linha] = new JPanel();
			add(new JSeparator());
			add(linhas[linha]);
		}
		
		if (indice % 4 != 0) {
			JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
			separator.setPreferredSize(new Dimension(1, 100));
			linhas[linha].add(separator);
		}
		
		linhas[linha].add(painel);
		
		JFrame janela = (JFrame) getTopLevelAncestor();
		janela.pack();
		janela.setLocationRelativeTo(null);
	}
	
	public void onEstabelecerConexao(Runnable acao) {
		estabelecerConexao.addActionListener(e -> {
			acao.run();
		});
	}

	public void habilitar() {
		
	}

	public void setSensorListener(Consumer<SensorEvent> listener) {
		this.listener = listener;
		/*
		 * TODO Desenvolver a remoção do novo pin selecionado caso não seja o <inativo>.
		 * Possivelmente utilizar andThen() com um loop na lista paineisInstrumento.
		 */
	}
}
