package br.univali.digibat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class UserInterface extends JFrame {
	public static final String NENHUMA_PORTA = "<nenhuma porta>";
	
	private JMenu arquivo = new JMenu("Arquivo");
	private JMenuItem sair = new JMenuItem("Sair");
	private JMenu janela = new JMenu("Janela");
	private JMenuItem graficoSinal = new JMenuItem("Gráfico do Sinal");
	private JMenuBar menuBar = new JMenuBar();
	private JPanel root = new JPanel(new GridBagLayout());
	
	private JLabel definirPorta = new JLabel("Definir porta");
	private JComboBox<String> portas = new JComboBox<String>();
	private JButton atualizarPortas = new JButton("Atualizar");
	private JButton confirmarPorta = new JButton("Confirmar");
	private JPanel painelComboBox = new JPanel();
	private JPanel painelBotoesPortas = new JPanel();
	private JPanel painelPortas = new JPanel();
	
	private DefaultComboBoxModel<String> modelPortas = new DefaultComboBoxModel<String>();
	private GridBagConstraints constraints = new GridBagConstraints();

	private Controlador controlador;
	
	public UserInterface(Controlador controlador) {
		super("Digibat");
		this.controlador = controlador;
		
		definirListeners();
		
		modelPortas.addElement(NENHUMA_PORTA);
		definirPorta.setAlignmentX(CENTER_ALIGNMENT);
		portas.setModel(modelPortas);
		confirmarPorta.setEnabled(false);
		
		arquivo.add(sair);
		janela.add(graficoSinal);
		menuBar.add(arquivo);
		menuBar.add(janela);
		
		painelBotoesPortas.add(atualizarPortas);
		painelBotoesPortas.add(confirmarPorta);
		
		painelComboBox.add(portas);
		
		painelPortas.setLayout(new BoxLayout(painelPortas, BoxLayout.Y_AXIS));
		painelPortas.add(definirPorta);
		painelPortas.add(painelComboBox);
		painelPortas.add(painelBotoesPortas);
		
		constraints.insets = new Insets(10, 20, 10, 20);
		root.add(painelPortas, constraints);
		
		setJMenuBar(menuBar);
		setContentPane(root);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void definirListeners() {
		portas.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String item = (String) e.getItem();
					
					//Desativado se for igual à constante NENHUMA_PORTA
					confirmarPorta.setEnabled(!item.equals(NENHUMA_PORTA)); 
				}
			}
		});
		
		atualizarPortas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.atualizarPortas();
			}
		});
		
		confirmarPorta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.confirmarPorta();
			}
		});
	}

	public void definirPortas(String[] nomesPortas) {
		modelPortas.removeAllElements();
		if (nomesPortas.length > 0) {
			for (String porta : nomesPortas) {
				modelPortas.addElement(porta);
			}
		} else {
			modelPortas.addElement(NENHUMA_PORTA);
		}
	}

	public void mensagem(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem);
	}
	
	public void erro(Exception e) {
		JOptionPane.showMessageDialog(this, e);
	}
	
	public String getPortaSelecionada() {
		return portas.getItemAt(portas.getSelectedIndex());
	}
}
