package br.univali.digibat;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PainelInstrumento extends JPanel {
	private int pin;
	
	private JLabel labelPin = new JLabel();
	private JPanel instrumentoContainer = new JPanel();
	private JComboBox<String> instrumento = new JComboBox<String>();
	
	public PainelInstrumento(int pin) {
		this.pin = pin;
		
		labelPin.setText(String.valueOf(pin));
		labelPin.setAlignmentX(CENTER_ALIGNMENT);
		instrumentoContainer.add(instrumento);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(labelPin);
		add(instrumentoContainer);
	}
	
	public int getPin() {
		return pin;
	}
	
	public void onEscolherInstrumento() {
		
	}
}
