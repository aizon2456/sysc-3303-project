package centralServer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

public class CentralServerView implements Observer{

	private JFrame frame;
	private JComboBox districtList;
	private JButton graphButton;

	public CentralServerView(ActionListener actionListener){
		frame = new JFrame("Central Server GUI");
		frame.setSize(300, 190);
		frame.setResizable(false);
		frame.setVisible(true);
		graphButton = new JButton("Graph!");
		graphButton.addActionListener(actionListener);
		districtList = new JComboBox<String>();
		districtList.addActionListener(actionListener);

		districtList.setVisible(true);
		frame.setLayout(null);

		frame.add(districtList);
		districtList.setBounds(50,80,200,30);

		frame.add(graphButton);
		graphButton.setBounds(50,50,200,30);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
        System.out.println("Received Update");
		if(arg1 instanceof String[]){
			String[] districts = (String[])arg1;
			for(Object i : (String[])arg1){
				districtList.addItem(i);
			}
		}
		else{
			Map<String,Map<String,Integer>> votesMap = (Map<String,Map<String,Integer>>)arg1;
			final CategoryDataset dataset = createDataset(votesMap);
			final JFreeChart chart = createChart(dataset);
			final ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(500, 270));
			BarGraphFrame barGraphFrame = new BarGraphFrame((String)votesMap.keySet().toArray()[0]);
			barGraphFrame.setContentPane(chartPanel);
			barGraphFrame.pack();
			RefineryUtilities.centerFrameOnScreen(barGraphFrame);
			barGraphFrame.setVisible(true);
            barGraphFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

	/**
	 * Returns a elections dataset.
	 * @return The dataset.
	 */
	private CategoryDataset createDataset(Map<String,Map<String,Integer>> votesMap) {

		//Get the name of the district and the candidateMap
		final String district = (String)votesMap.keySet().toArray()[0];
		Map<String,Integer> candidateMap = votesMap.get(district);

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		Iterator<Entry<String, Integer>> it = candidateMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)it.next();
			dataset.addValue(pair.getValue(), pair.getKey(), district);
			it.remove(); // avoids a ConcurrentModificationException
		}

		return dataset;
	}

	/**
	 * Creates a elections chart.
	 * @param dataset  the dataset.
	 * @return The chart.
	 */
	private JFreeChart createChart(final CategoryDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(
				"Election Results",         // chart title
				"Candidates",               // domain axis label
				"Votes",                  // range axis label
				dataset,                  // data
				PlotOrientation.VERTICAL, // orientation
				true,                     // include legend
				true,                     // tooltips?
				false                     // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(
				0.0f, 0.0f, Color.blue, 
				0.0f, 0.0f, Color.lightGray
				);
		final GradientPaint gp1 = new GradientPaint(
				0.0f, 0.0f, Color.green, 
				0.0f, 0.0f, Color.lightGray
				);
		final GradientPaint gp2 = new GradientPaint(
				0.0f, 0.0f, Color.red, 
				0.0f, 0.0f, Color.lightGray
				);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
				);
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	public String getSelectedDistrict(){
		return (String)districtList.getSelectedItem();
	}
}