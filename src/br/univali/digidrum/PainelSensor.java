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
		if (primeiro.charAt(0) == '<' || segundo.charAt(0) == '<') {
			return -1;
		} else {
			int um = Integer.parseInt(primeiro);
			int dois = Integer.parseInt(segundo);
			return um > dois ? 1 : um == dois ? 0 : -1;
		}
	};
	
	private JLabel labelPin = new JLabel("Pin");
	private JLabel labelInstrumento = new JLabel("Instrumento");
	private SortedComboBoxModel<String> modeloPin = new SortedComboBoxModel<>(PINS, PIN_COMPARATOR);
	private SortedComboBoxModel<Instrumento> modeloInstrumento = new SortedComboBoxModel<>(Instrumento.values(), INSTRUMENTO_COMPARATOR);
	private JComboBox<String> seletorPin = new JComboBox<>(modeloPin);
	private JComboBox<Instrumento> seletorInstrumento = new JComboBox<>(modeloInstrumento);
	
	int pinAntigo = -1;
	
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
			if (e.getStateChange() == ItemEvent.SELECTED) {
				boolean pinMudou = e.getSource().equals(seletorPin);
				int pinNovo = lerPinSelecionado();
				int tipo = pinMudou ? SensorEvent.PIN_CHANGED : SensorEvent.INSTRUMENTO_CHANGED;
				Instrumento instrumento = (Instrumento) seletorInstrumento.getSelectedItem();
				
				listener.accept(new SensorEvent(tipo, pinAntigo, pinNovo, instrumento));
				pinAntigo = pinNovo;
			}
		};
		
		seletorPin.addItemListener(itemListener);
		seletorInstrumento.addItemListener(itemListener);
	}

	private int lerPinSelecionado() {
		String s = (String) seletorPin.getSelectedItem();
		return (s.charAt(0) == '<') ? -1 : Integer.parseInt(s);
	}

	public void adicionarPin(int pin) {
		if (pinAntigo != pin) modeloPin.addElement(PINS[++pin]);
	}

	public void removerPin(int pin) {
		if (lerPinSelecionado() != pin)	modeloPin.removeElement(PINS[++pin]);
	}
}
