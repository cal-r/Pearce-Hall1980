package Controllers;

import Constants.GuiStringConstants;
import Helpers.Graphing.GraphBuilder;
import Helpers.Graphing.GraphDatasetHelper;
import Helpers.Graphing.GraphStringsHelper;
import Helpers.Graphing.ChartPainer;
import Helpers.GuiHelper;
import Models.Graphing.Graph;
import Models.Graphing.GraphLine;
import Models.Graphing.GraphLineGroup;
import Models.History.SimulationHistory;
import Models.SimulatorSettings;
import Views.GraphWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Rokas on 22/01/2016.
 */
public class GraphWindowController implements ActionListener {
    private Graph graphData;
    private GraphWindow window;
    private JFreeChart chart;
    private XYLineAndShapeRenderer renderer;
    private Map<String, java.util.List<JCheckBox>> groupedCheckboxesMap;

    public GraphWindowController(Graph graphData, Map<Paint, Boolean> interGraphColorMap){
        groupedCheckboxesMap = new HashMap<>();
        this.graphData = graphData;
        this.renderer = new XYLineAndShapeRenderer();
        this.chart = createChart(interGraphColorMap);
        window = new GraphWindow(chart);
        addCheckboxes();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JCheckBox){
            onCheckBoxClick(GuiHelper.isCheckBoxSelected(e), e.getActionCommand());
        }
    }

    private void onCheckBoxClick(boolean isCheckboxTicked, String command){
        graphData.setVisibility(command, isCheckboxTicked);
        if(groupedCheckboxesMap.containsKey(command)){
            for(JCheckBox childBox : groupedCheckboxesMap.get(command)){
                childBox.setSelected(isCheckboxTicked);
            }
        }
        refreshView();
    }

    private void refreshView(){
        int maxX = -1;
        for (GraphLine line : graphData.getAllLines()) {
            renderer.setSeriesLinesVisible(line.getDisplayId(), line.isVisible());
            renderer.setSeriesShapesVisible(line.getDisplayId(), line.isVisible());
            if(line.isVisible()) {
                maxX = Math.max(line.getDataPoints().size(), maxX);
            }
        }
        if(maxX>0) {
            getDomainAxis(chart).setRange(1, maxX);
        }
    }

    private JFreeChart createChart(Map<Paint, Boolean> interGraphColorMap) {
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

        NumberAxis domainAxis = getDomainAxis(chart);
        domainAxis.setAutoRange(true);
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        new ChartPainer(chart, renderer, graphData, interGraphColorMap);

        return chart;
    }

    private void addCheckboxes(){
        GridBagConstraints constraints = createGridBagConstraints();
        for(GraphLineGroup lineGroup : graphData.getAllGroups()){
            addCheckBox(constraints, true, lineGroup);
            for (GraphLine line : lineGroup.getLines()){
                addCheckBox(constraints, false, lineGroup, line);
            }
        }
    }

    private GridBagConstraints createGridBagConstraints(){
        GridBagConstraints constants = new GridBagConstraints();
        constants.fill = GridBagConstraints.HORIZONTAL;
        constants.gridx = -1;
        constants.gridy = -1;
        return constants;
    }


    private void addCheckBoxToPanel(GridBagConstraints constraints, boolean startNewLine, JCheckBox checkBox) {
        if(startNewLine){
            constraints.gridx = 0;
            constraints.gridy++;
        }else{
            constraints.gridx++;
        }
        window.getCheckboxesPanel().add(checkBox, constraints);
    }

    private void addCheckBox(GridBagConstraints constraints, boolean startNewLine, GraphLineGroup lineGroup) {
        JCheckBox checkBox = createCheckBox(lineGroup.getName(), GraphStringsHelper.getGroupCommand(lineGroup));
        addCheckBoxToPanel(constraints, startNewLine, checkBox);
        groupedCheckboxesMap.put(GraphStringsHelper.getGroupCommand(lineGroup), new ArrayList<JCheckBox>());
    }

    private void addCheckBox(GridBagConstraints constraints, boolean startNewLine, GraphLineGroup lineGroup, GraphLine line){
        JCheckBox checkBox = createCheckBox(line.getName(), GraphStringsHelper.getLineCommand(line));
        addCheckBoxToPanel(constraints, startNewLine, checkBox);
        groupedCheckboxesMap.get(GraphStringsHelper.getGroupCommand(lineGroup)).add(checkBox);
    }

    private JCheckBox createCheckBox(String label, String command){
        JCheckBox checkBox = new JCheckBox(label, true);
        checkBox.addActionListener(this);
        checkBox.setActionCommand(command);
        return checkBox;
    }

    private NumberAxis getDomainAxis(JFreeChart chart){
        return (NumberAxis) chart.getXYPlot().getDomainAxis();
    }

    static void showGraphs(SimulationHistory history, SimulatorSettings settings){
        Map<Paint, Boolean> interGraphColorMap = new HashMap<>();
        for(Graph graph : GraphBuilder.BuildGraphs(history, settings.isRodriguezMode())){
            new GraphWindowController(graph, interGraphColorMap);
        }
    }
}
