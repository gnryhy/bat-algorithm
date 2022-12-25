package optimization.bat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class ConvergenceChart {

    public ConvergenceChart(String chartName, double[] best_30, double[] best_40, double[] best_50) {

        YIntervalSeries pop30 = new YIntervalSeries("30 Bats");
        YIntervalSeries pop40 = new YIntervalSeries("40 Bats");
        YIntervalSeries pop50 = new YIntervalSeries("50 Bats");

        for (int i = 0; i <= 1000; i++) {

            pop30.add(i, best_30[i], best_30[i], best_30[i]);
            pop40.add(i, best_40[i], best_40[i], best_40[i]);
            pop50.add(i, best_50[i], best_50[i], best_50[i]);
        }

        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
        dataset.addSeries(pop30);
        dataset.addSeries(pop40);
        dataset.addSeries(pop50);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                chartName,          // chart title
                "Iterations",                   // x axis label
                "Fitness",       // y axis label
                dataset,                  // data
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(false);
        plot.setInsets(new RectangleInsets(5, 5, 5, 20));

        DeviationRenderer renderer = new DeviationRenderer(true, false);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setSeriesStroke(1, new BasicStroke(3.0f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setSeriesFillPaint(0, new Color(255, 0, 0));
        renderer.setSeriesFillPaint(1, new Color(0, 255, 0));
        renderer.setSeriesFillPaint(2, new Color(0, 0, 255));
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        JFrame f = new JFrame("deneme");
        f.getContentPane().add(new ChartPanel(chart));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}
