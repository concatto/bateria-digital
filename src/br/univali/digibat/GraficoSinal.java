package br.univali.digibat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
public class GraficoSinal extends FrameBase {
	private XYSeries series;
	private XYSeriesCollection dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private JPanel root;

	public GraficoSinal(Controlador controlador) {
		super(controlador, "Gráfico");
	}

	@Override
	protected void inicializarComponentes() {
		root = new JPanel();
		series = new XYSeries("test");
		dataset = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart("Sinal", "Tempo (ms)", "Voltagem (V)", dataset);
		chartPanel = new ChartPanel(chart);
		
		dataset.addSeries(series);
		root.add(chartPanel);
		
		setContentPane(root);
	}
	
	public void adicionarSinal(int sinal, int pin) {
		
	}
}
