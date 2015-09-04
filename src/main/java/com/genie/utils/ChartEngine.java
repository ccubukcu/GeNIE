package com.genie.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 * @author ccubukcu
 *
 */
public class ChartEngine {
	private static final int LINE_CHART = 0;
	private static final int BAR_CHART = 1;
	private static final int PIE_CHART = 2;
	
	private static final int CHART_WIDTH = 960;
	private static final int CHART_HEIGHT = 400;
	private static final int STACKED_HEIGHT = 800;
	private static final int PIE_CHART_HEIGHT = 480;
	
	private static final Color[] colors = { Color.decode("#4bb2c5"), Color.decode("#EAA228"), Color.decode("#c5b47f"), Color.decode("#579575"), Color.decode("#839557"),
			Color.decode("#958c12"), Color.decode("#953579"), Color.decode("#d8b83f"), Color.decode("#ff5800"), Color.decode("#0085cc"),
			Color.decode("#c747a3"), Color.decode("#c747a3"), Color.decode("#cddf54"), Color.decode("#FBD178"), Color.decode("#26B4E3"),
			Color.decode("#bd70c7")};

	public static byte[] createBarChart(String title, CartesianChartModel model, boolean stacked) {
		return createBarChart(title, null, null, model, stacked, (stacked ? STACKED_HEIGHT : CHART_HEIGHT), CHART_WIDTH);
	}
	
	public static byte[] createLineChart(String title, CartesianChartModel model) {
		return createLineChart(title, null, null, model, CHART_HEIGHT, CHART_WIDTH);
	}
	
	public static byte[] createBarChart(String title, CartesianChartModel model, boolean stacked, int height) {
		return createBarChart(title, null, null, model, stacked, height, CHART_WIDTH);
	}

	public static byte[] createLineChart(String title, CartesianChartModel model, int height) {
		return createLineChart(title, null, null, model, height, CHART_WIDTH);
	}

	public static byte[] createPieChart(String title, PieChartModel model, String noDataMessage) {
		return createPieChart(title, model, noDataMessage, PIE_CHART_HEIGHT, CHART_WIDTH);
	}
	
	public static byte[] createPieChart(String title, PieChartModel model, String noDataMessage, int height) {
		return createPieChart(title, model, noDataMessage, height, CHART_WIDTH);
	}
	
	/**Verilen model uzerinden olusturulan chartin byte arrayini dondurur*/
	public static byte[] createBarChart(String title, String xAxisLabel, String yAxisLabel, CartesianChartModel model, boolean stacked, int height, int width) {
		CategoryDataset dataset = getDatasetFromCartesianModel(model);
		JFreeChart chart = null;
		if(stacked)
			chart = createStackedBarChart(dataset, title, xAxisLabel, yAxisLabel);
		else
			chart = createBarChart(dataset, title, xAxisLabel, yAxisLabel);    
        
		return createByteArray(chart, BAR_CHART, width, height);
	}

	/**Verilen model uzerinden olusturulan chartin byte arrayini dondurur*/
	public static byte[] createLineChart(String title, String xAxisLabel, String yAxisLabel, CartesianChartModel model, int height, int width) {
		CategoryDataset dataset = getDatasetFromCartesianModel(model);
		JFreeChart chart = createLineChart(dataset, title, xAxisLabel, yAxisLabel);    
    	return createByteArray(chart, LINE_CHART, width, height);
	}
	
	/**Verilen model uzerinden olusturulan chartin byte arrayini dondurur*/
	public static byte[] createPieChart(String title, PieChartModel model, String noDataMessage, int height, int width) {
		PieDataset dataset = getDatasetFromPieModel(model);
		JFreeChart chart = createPieChart(dataset, title, noDataMessage);    
        
		return createByteArray(chart, PIE_CHART, width, height);
	}
	
	/**Olusturulan chart objesini byte array'e donusturur.*/
	private static byte[] createByteArray(JFreeChart chart, int chartType, int width, int height) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ChartUtilities.writeChartAsPNG(out, chart, width, height);
	        return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**Primefaces chartlarinda kullanilan CartesianChartModel'den JFreeChartta kullanilmak uzere Dataset'i olusturur*/
	private static PieDataset getDatasetFromPieModel(PieChartModel model) {
		DefaultPieDataset dataset = new DefaultPieDataset();
			for(int i=0; i<model.getData().size();i++) {
				for (Entry<String, Number> data : model.getData().entrySet()) {
					dataset.insertValue(i, data.getKey(), data.getValue());
			}
		}
		return dataset;
	}
	
	/**Primefaces chartlarinda kullanilan CartesianChartModel'den JFreeChartta kullanilmak uzere Dataset'i olusturur*/
	private static CategoryDataset getDatasetFromCartesianModel(CartesianChartModel model) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (ChartSeries series : model.getSeries()) {
			for (Entry<Object, Number> data : series.getData().entrySet()) {
				dataset.addValue(data.getValue(), series.getLabel(), data.getKey().toString());
			}
		}
		return dataset;
	}

	/** Line Chart objesini olusturur*/
	private static JFreeChart createLineChart(CategoryDataset dataset, String chartTitle, String xAxisLabel, String yAxisLabel) {
		final JFreeChart chart = ChartFactory.createLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, true, false, false);
        
        chart.setBackgroundPaint(Color.white);
        
        final CategoryPlot plot = (CategoryPlot) getPlot(chart, LINE_CHART);

        CategoryItemLabelGenerator generator = createItemLabelGenerator();
        
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        
        renderer.setDrawOutlines(true);
    	renderer.setUseFillPaint(true);
    	renderer.setBaseShapesVisible(true); 
    	renderer.setBaseFillPaint(Color.white); 
    	
        for(int i=0;i<dataset.getRowCount(); i++) {
        	renderer.setSeriesStroke(i, new BasicStroke(3.0f));
        	renderer.setSeriesOutlineStroke(i, new BasicStroke(2.0f)); 
        	renderer.setSeriesShape(i, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));  
        	renderer.setSeriesShapesVisible(i, true);
        	renderer.setSeriesItemLabelGenerator(i, generator);
        	renderer.setSeriesItemLabelsVisible(i, true);
        	renderer.setSeriesPaint(i, colors[i%colors.length]);
        	
        }
        return chart;
    }

	/** Bar Chart objesini olusturur*/
	private static JFreeChart createBarChart(CategoryDataset dataset, String chartTitle, String xAxisLabel, String yAxisLabel) {
		final JFreeChart chart = ChartFactory.createBarChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, true, false, false);
        
        chart.setBackgroundPaint(Color.white);
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setBackgroundPaint(new Color(255, 253, 246, 255));
        
        final CategoryPlot plot = (CategoryPlot) getPlot(chart, BAR_CHART);

        CategoryItemLabelGenerator generator = createItemLabelGenerator();
        
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
        renderer.setItemMargin(0.10);
    	renderer.setBaseFillPaint(Color.white); 
    	renderer.setBarPainter(new StandardBarPainter());
    	renderer.setDataBoundsIncludesVisibleSeriesOnly(true);
    	renderer.setMaximumBarWidth(.20);
    	
        for(int i=0;i<dataset.getRowCount(); i++) {
        	renderer.setSeriesStroke(i, new BasicStroke(3.0f));
        	renderer.setSeriesOutlineStroke(i, new BasicStroke(2.0f)); 
        	renderer.setSeriesShape(i, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));  
        	renderer.setSeriesItemLabelGenerator(i, generator);
        	renderer.setSeriesItemLabelsVisible(i, true);
        	renderer.setSeriesPaint(i, colors[i%colors.length]);
        }
        
        if(dataset.getColumnCount() * dataset.getRowCount() > 30) {
        	renderer.setShadowVisible(false);
        }
        
        plot.setRenderer(renderer);
        return chart;
	}

	/** Bar Chart objesini olusturur*/
	private static JFreeChart createStackedBarChart(CategoryDataset dataset, String chartTitle, String xAxisLabel, String yAxisLabel) {
		final JFreeChart chart = ChartFactory.createStackedBarChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, true, false, false);
        
        chart.setBackgroundPaint(Color.white);
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setBackgroundPaint(new Color(255, 253, 246, 255));
        
        final CategoryPlot plot = (CategoryPlot) getPlot(chart, BAR_CHART);
        
        CategoryItemLabelGenerator generator = createItemLabelGenerator();
        
        final StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        
        renderer.setItemMargin(0.1);
    	renderer.setBaseFillPaint(Color.white); 
    	renderer.setBarPainter(new StandardBarPainter());
    	renderer.setBaseItemLabelsVisible(true);
    	renderer.setMaximumBarWidth(.20);
    	
        for(int i=0;i<dataset.getRowCount(); i++) {
        	renderer.setSeriesStroke(i, new BasicStroke(3.0f));
        	renderer.setSeriesOutlineStroke(i, new BasicStroke(2.0f)); 
        	renderer.setSeriesShape(i, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));  
        	renderer.setSeriesItemLabelGenerator(i, generator);
        	renderer.setSeriesItemLabelsVisible(i, true);
        	renderer.setSeriesPaint(i, colors[i%colors.length]);
        	
        }
        plot.setRenderer(renderer);
        
        return chart;
	}
	
	/** Pie Chart objesini olusturur*/
	private static JFreeChart createPieChart(PieDataset dataset, String title, String noDataMessage) {
		JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, false, false);
		
        chart.setBackgroundPaint(Color.white);
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setBackgroundPaint(new Color(255, 253, 246, 255));
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 253, 246, 255));
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        plot.setNoDataMessage(noDataMessage);
        plot.setCircular(true);

        final ChartPanel chartPanel = new ChartPanel(chart, true);
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}\n{1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        
        plot.setLabelGenerator(gen);

//    	plot.setBaseSectionOutlineStroke(new BasicStroke(2f));        	
//    	plot.setBaseSectionPaint(Color.white);

    	plot.setBaseSectionOutlineStroke(new BasicStroke(0f));  
        for(int i=0; i<dataset.getKeys().size(); i++) {
        	plot.setExplodePercent(dataset.getKey(i), 0.15);
        	plot.setSectionPaint(dataset.getKey(i), colors[i%colors.length]);
        }
        
        return chart;
	}
	
	/**Chartlarin temel planini ayarlar*/
	private static Plot getPlot(JFreeChart chart, int chartType) {
		Color gridColor = new Color(204, 204, 204, 255);
        Stroke gridStroke = new BasicStroke(0.5f);
        
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 253, 246, 255));
        plot.setRangeGridlinesVisible(true);
        if(chartType != BAR_CHART) {
            plot.setDomainGridlinesVisible(true);
        }
        if(chartType == BAR_CHART) {
//        	plot.setDomainGridlinePosition(CategoryAnchor.START);
        }
        
        plot.setRangeGridlinePaint(gridColor);
        plot.setDomainGridlinePaint(gridColor);
        plot.setRangeGridlineStroke(gridStroke);
        plot.setDomainGridlineStroke(gridStroke);
        
        plot.setOutlinePaint(new Color(153, 153, 153, 255));
        plot.setOutlineStroke(new BasicStroke(1.75f));
        
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.1);
        rangeAxis.setLowerMargin(0.1);
        
        CategoryDataset dataset = ((CategoryPlot) plot).getDataset();
        boolean dataFound = false;

    	outerloop:
        for (int r = 0; r < dataset.getRowCount(); r++) {
            for (int c = 0; c < dataset.getColumnCount(); c++) {
                Number number = dataset.getValue(r, c);
                double value = number == null ? Double.NaN : number.doubleValue();
                if (value > 0) {
                	dataFound = true;
                	break outerloop;
                }
            }
        }
        
        if(!dataFound)
        	rangeAxis.setRange(0, 1);
        
        final CategoryAxis categoryAxis = plot.getDomainAxis();
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        if(chartType == LINE_CHART) {
	        categoryAxis.setUpperMargin(0.0025);
	        categoryAxis.setLowerMargin(0.0025);
        }
        if(chartType == BAR_CHART) {
	        categoryAxis.setUpperMargin(0.01);
	        categoryAxis.setLowerMargin(0.01);
	        
	        categoryAxis.setCategoryMargin(0.35);
        }
        
        Font bold = rangeAxis.getTickLabelFont().deriveFont(Font.BOLD);
        rangeAxis.setTickLabelFont(bold);
        Font boldLabel = rangeAxis.getLabelFont().deriveFont(Font.BOLD);
        rangeAxis.setLabelFont(boldLabel);
        
        return plot;
	}
	
	private static CategoryItemLabelGenerator createItemLabelGenerator() {
        DecimalFormat decf = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("tr", "TR"));
        decf.setMaximumFractionDigits(3);
        CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator("{2}", decf);
        
        return generator;
	}
}
