package br.univali.digibat;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class PainelBase extends JPanel {
	protected Controlador controlador;
	protected GridBagConstraints constraints = new GridBagConstraints();
	
	public PainelBase(Controlador controlador) {
		super(new GridBagLayout());
		this.controlador = controlador;
		
		constraints.insets = new Insets(10, 20, 10, 20);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
	}
	
	/**
	 * Adiciona um componente com constraints. Não retorna nenhum valor.
	 * @param comp O componente a ser adicionado
	 * @return null
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	@Override
	public Component add(Component comp) {
		add(comp, constraints);
		return null;
	}
}
