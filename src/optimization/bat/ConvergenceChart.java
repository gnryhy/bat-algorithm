package optimization.bat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ConvergenceChart {

    public static final Map<Integer, Color> colorMap = Map.of(30, Color.RED, 40, Color.BLUE, 50, Color.BLACK);

    public ConvergenceChart(String chartName, double[] convergenceValues, int populationSize) {

        XYSeries convergenceSeries = new XYSeries(populationSize + " Bats");

        for (int i = 0; i <= 1000; i++) {

            convergenceSeries.add(i, convergenceValues[i]);

        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(convergenceSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(chartName + " N=" + populationSize, "Iterations",
                "Fitness",
                dataset);

        // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(false);
        plot.setInsets(new RectangleInsets(5, 5, 5, 20));

        DeviationRenderer renderer = new DeviationRenderer(true, false);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        renderer.setSeriesPaint(0, colorMap.get(populationSize));
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        try {

            if (!new File("./charts").exists()) {

                Files.createDirectories(Paths.get("./charts"));
            }

            ChartUtils.saveChartAsPNG(new File("./charts/" + chartName + "___" + populationSize + ".png"),
                    chart, 1200, 1200);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
