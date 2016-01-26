package Controllers;

import Constants.GuiStringConstants;
import Helpers.GraphDatasetHelper;
import Models.Graphing.Graph;
import Views.GraphWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Rokas on 22/01/2016.
 */
public class GraphWindowController implements ActionListener{
    private Graph graphData;
    private GraphWindow window;
    private JFreeChart chart;
    private XYLineAndShapeRenderer renderer;

    public GraphWindowController(Graph graphData){
        this.graphData = graphData;
        this.renderer = new XYLineAndShapeRenderer();
        this.chart = createChart();
        window = new GraphWindow(chart);
        addCheckboxes();
    }

    private void addCheckboxes(){

        window.getCheckboxesPanel().add(new Label("Checkboxes under construction.."));
//        GridBagConstraints constants = new GridBagConstraints();
//        constants.fill = GridBagConstraints.HORIZONTAL;
//        constants.gridx = 0;
//        constants.gridy =0;
//        window.getCheckboxesPanel().add(new JCheckBox("lbl1", true),constants);
//        constants.gridx = 1;
//        constants.gridy =0;
//        window.getCheckboxesPanel().add(new JCheckBox("lbl2", true),constants);
//        constants.gridx = 0;
//        constants.gridy =1;
//        window.getCheckboxesPanel().add(new JCheckBox("lbl3", false),constants);
//        constants.gridx = 1;
//        constants.gridy = 1;
//        window.getCheckboxesPanel().add(new JCheckBox("lbl4", true),constants);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        renderer.setSeriesLinesVisible(0, false);
    }                               


    private JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createXYLineChart(
                graphData.getName(),
                GuiStringConstants.CHART_X_AXIS_LABEL,
                GuiStringConstants.CHART_Y_AXIS_LABEL,
                GraphDatasetHelper.createDataset(graphData),
                PlotOrientation.VERTICAL,
                true,   //legend
                false,  //tooltips
                false); //urls

        chart.getXYPlot().setRenderer(renderer);

        return chart;
    }
}
