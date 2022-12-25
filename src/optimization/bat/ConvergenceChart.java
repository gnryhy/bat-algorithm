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

public class ConvergenceChart {

    public ConvergenceChart(String chartName, double[] best_30, double[] best_40, double[] best_50) {

        XYSeries pop30 = new XYSeries("30 Bats");
        XYSeries pop40 = new XYSeries("40 Bats");
        XYSeries pop50 = new XYSeries("50 Bats");

        for (int i = 0; i <= 50; i++) {

            pop30.add(i, best_30[i]);
            pop40.add(i, best_40[i]);
            pop50.add(i, best_50[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(pop30);
        dataset.addSeries(pop40);
        dataset.addSeries(pop50);

        JFreeChart chart = ChartFactory.createXYLineChart(chartName, "Iterations", "Fitness", dataset);

        // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(false);
        plot.setInsets(new RectangleInsets(5, 5, 5, 20));

        DeviationRenderer renderer = new DeviationRenderer(true, false);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        renderer.setSeriesStroke(1, new BasicStroke(4.0f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setSeriesStroke(2, new BasicStroke(4.0f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(2, Color.BLACK);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        try {

            if (!new File("./charts").exists()) {

                Files.createDirectories(Paths.get("./charts"));
            }

            ChartUtils.saveChartAsPNG(new File("./charts/" + chartName + ".png"), chart, 600, 600);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
