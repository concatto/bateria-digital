package br.univali.digibat;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PainelInstrumento extends JPanel {
	private int pin;
	
	private JLabel labelPin = new JLabel();
	private JPanel instrumentoContainer = new JPanel();
	private JComboBox<Instrumento> instrumento = new JComboBox<>();
	
	public PainelInstrumento(int pin) {
		this.pin = pin;
		
		labelPin.setText(String.valueOf(pin));
		labelPin.setAlignmentX(CENTER_ALIGNMENT);
		instrumentoContainer.add(instrumento);
		
		for (Instrumento instr : Instrumento.values()) {
			instrumento.addItem(instr);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(labelPin);
		add(instrumentoContainer);
	}
	
	public int getPin() {
		return pin;
	}
	
	public void onEscolherInstrumento(final BiConsumidor<Integer, Instrumento> acao) {
		instrumento.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {	
					acao.consumir(getPin(), (Instrumento) e.getItem());
				}
			}
		});
	}
}
