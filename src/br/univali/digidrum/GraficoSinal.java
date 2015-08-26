package br.univali.digidrum;

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
	private long tempo;

	public GraficoSinal() {
		super("Gráfico");
	}

	@Override
	protected void inicializarComponentes() {
		root = new JPanel();
		series = new XYSeries("test");
		dataset = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart("Sinal", "Tempo", "Voltagem (V)", dataset);
		chartPanel = new ChartPanel(chart);
		
		dataset.addSeries(series);
		root.add(chartPanel);
		chart.getXYPlot().getRangeAxis().setUpperBound(256);
		
		setContentPane(root);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		for (int i = 0; i < dataset.getSeriesCount(); i++) {
			dataset.getSeries(i).clear();
		}
		tempo = System.currentTimeMillis();
	}
	
	public void adicionarSinal(int pin, int sinal) {
		if (pin == 0) {
			long y = System.currentTimeMillis() - tempo;
			series.add(y, sinal);
			chart.getXYPlot().getDomainAxis().setLowerBound(y - 10000);
			chart.getXYPlot().getDomainAxis().setUpperBound(y);
		}
	}
}
