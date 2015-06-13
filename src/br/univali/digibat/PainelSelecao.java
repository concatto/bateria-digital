package br.univali.digibat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PainelSelecao extends PainelBase {
	public static final String NENHUMA_PORTA = "<nenhuma porta>";
	
	private JLabel definirPorta = new JLabel("Definir porta");
	private JComboBox<String> portas = new JComboBox<String>();
	private JButton atualizarPortas = new JButton("Atualizar");
	private JButton confirmarPorta = new JButton("Confirmar");
	private JButton controleTeclado = new JButton("Experimento: controlar com o teclado");	
	private JPanel painelComboBox = new JPanel();
	private JPanel painelBotoesPortas = new JPanel();
	private JPanel painelControleTeclado = new JPanel();
	private JPanel painelPortas = new JPanel();
	
	private DefaultComboBoxModel<String> modelPortas = new DefaultComboBoxModel<String>();

	public PainelSelecao(Controlador controlador) {
		super(controlador);
		
		modelPortas.addElement(NENHUMA_PORTA);
		definirPorta.setAlignmentX(CENTER_ALIGNMENT);
		portas.setModel(modelPortas);
		confirmarPorta.setEnabled(false);
		
		painelControleTeclado.add(controleTeclado);
		painelBotoesPortas.add(atualizarPortas);
		painelBotoesPortas.add(confirmarPorta);
		painelComboBox.add(portas);
		
		painelPortas.setLayout(new BoxLayout(painelPortas, BoxLayout.Y_AXIS));
		painelPortas.add(definirPorta);
		painelPortas.add(painelComboBox);
		painelPortas.add(painelBotoesPortas);
		painelPortas.add(painelControleTeclado);
		
		definirListeners();
		
		add(painelPortas);
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
		
		controleTeclado.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.alterarInterface(ModoInterface.TECLADO);
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

	public String getPortaSelecionada() {
		return portas.getItemAt(portas.getSelectedIndex());
	}
}
