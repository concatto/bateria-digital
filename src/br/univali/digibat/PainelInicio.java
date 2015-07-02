package br.univali.digibat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PainelInicio extends PainelBase {	
	private JButton iniciar = new JButton("Iniciar!");
	private JButton controleTeclado = new JButton("Experimento: controlar com o teclado");	
	private JPanel painelPortas = new JPanel();

	public PainelInicio(Controlador controlador) {
		super(controlador);
		
		iniciar.setAlignmentX(CENTER_ALIGNMENT);
		controleTeclado.setAlignmentX(CENTER_ALIGNMENT);
		
		painelPortas.setLayout(new BoxLayout(painelPortas, BoxLayout.Y_AXIS));
		painelPortas.add(iniciar);
		painelPortas.add(Box.createVerticalStrut(10));
		painelPortas.add(controleTeclado);
		
		definirListeners();
		
		add(painelPortas);
	}

	private void definirListeners() {		
		iniciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.inicializar();
			}
		});
		
		controleTeclado.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.alterarInterface(ModoInterface.TECLADO);
			}
		});
	}
}
