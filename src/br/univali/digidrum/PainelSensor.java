package br.univali.digidrum;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Comparator;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PainelSensor extends JPanel {
	private static final String[] PINS = {
		"<inativo>",
		"0", "1", "2", "3", "4", "5", "6", "7",
		"8", "9", "10", "11", "12", "13", "14", "15"	
	};
	
	private static final Comparator<Instrumento> INSTRUMENTO_COMPARATOR = (primeiro, segundo) -> {
		return primeiro.getNome().compareTo(segundo.getNome());
	};
	
	private static final Comparator<String> PIN_COMPARATOR = (primeiro, segundo) -> {
		//TODO Desenvolver comparador onde <inativo> fica no topo
		return 1;
	};
	
	private JLabel labelPin = new JLabel("Pin");
	private SortedComboBoxModel<String> modeloPin = new SortedComboBoxModel<>(PINS, PIN_COMPARATOR);
	private JComboBox<String> seletorPin = new JComboBox<>(modeloPin);
	private JLabel labelInstrumento = new JLabel("Instrumento");
	private SortedComboBoxModel<Instrumento> modeloInstrumento = new SortedComboBoxModel<>(Instrumento.values(), INSTRUMENTO_COMPARATOR);
	private JComboBox<Instrumento> seletorInstrumento = new JComboBox<>(modeloInstrumento);
	
	public PainelSensor(Consumer<SensorEvent> listener) {
		add(labelPin);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(seletorPin);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(labelInstrumento);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(seletorInstrumento);
		
		for (Component comp : getComponents()) {
			((JComponent) comp).setAlignmentX(CENTER_ALIGNMENT);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		ItemListener itemListener = e -> {
			
		};
		
		seletorPin.addItemListener(itemListener);
		seletorInstrumento.addItemListener(itemListener);
	}
}
