package br.univali.digibat;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

@SuppressWarnings("serial")
public class PainelSensores extends PainelBase {
	private JLabel seletorLabel = new JLabel("Sensores: ");
	private JComboBox<Integer> seletor = new JComboBox<>(new Integer[] {2, 4, 8, 16});
	private JPanel painelSuperior = new JPanel();
	private JPanel[] linhas = new JPanel[4];
	private List<PainelInstrumento> instrumentos = new ArrayList<>();
	private BiConsumidor<Integer, Instrumento> consumidor;
	
	public PainelSensores(Controlador controlador) {
		super(controlador);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		seletor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {	
					redimensionar((int) e.getItem());
				}
			}
		});
		
		painelSuperior.add(seletorLabel);
		painelSuperior.add(seletor);
		add(painelSuperior);
		
		addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorRemoved(AncestorEvent event) {
				//Nada
			}
			
			@Override
			public void ancestorMoved(AncestorEvent event) {
				//Nada
			}
			
			@Override
			public void ancestorAdded(AncestorEvent event) {
				redimensionar((int) seletor.getSelectedItem());
			}
		});
	}
	
	//TODO Embelezar
	private void redimensionar(int componentes) {
		if (componentes > instrumentos.size()) {
			for (int i = instrumentos.size(); i < componentes; i++) {
				PainelInstrumento p = new PainelInstrumento(i);
				if (consumidor != null) p.onEscolherInstrumento(consumidor);
				instrumentos.add(p);
				
				int iLinha = i / 4;
				if (linhas[iLinha] == null) {
					linhas[iLinha] = new JPanel();
					add(linhas[iLinha]);
				}
				
				linhas[iLinha].add(p);
			}
		} else {
			for (int i = instrumentos.size() - 1; i >= componentes; i--) {
				int iLinha = i / 4;
				PainelInstrumento p = instrumentos.get(i);
				linhas[iLinha].remove(p);
				instrumentos.remove(i);
				if (linhas[iLinha].getComponentCount() == 0) {
					remove(linhas[iLinha]);
					linhas[iLinha] = null;
				}
			}
		}
		JFrame janela = (JFrame) getTopLevelAncestor();
		janela.pack();
		janela.setLocationRelativeTo(null);
	}
	
	public void setConsumidorInstrumento(BiConsumidor<Integer, Instrumento> consumidor) {
		//Sendo chamado depois do redimensionar() inicial!
		this.consumidor = consumidor;
	}
}
