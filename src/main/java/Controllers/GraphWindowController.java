package Controllers;

import Constants.GuiStringConstants;
import Helpers.GraphDatasetHelper;
import Helpers.GraphStringsHelper;
import Helpers.GuiHelper;
import Models.Graphing.Graph;
import Models.Graphing.GraphLine;
import Models.Graphing.GraphLineGroup;
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
public class GraphWindowController implements ActionListener{
    private Graph graphData;
    private GraphWindow window;
    private JFreeChart chart;
    private XYLineAndShapeRenderer renderer;
    private Map<String, java.util.List<JCheckBox>> groupedCheckboxesMap;

    public GraphWindowController(Graph graphData){
        groupedCheckboxesMap = new HashMap<>();
        this.graphData = graphData;
        this.renderer = new XYLineAndShapeRenderer();
        this.chart = createChart();
        window = new GraphWindow(chart);
        addCheckboxes();
    }

    private void addCheckboxes(){
        GridBagConstraints constraints = createGridBagConstraints();
        for(GraphLineGroup lineGroup : graphData.getGroups()){
            addCheckBox(constraints, true, lineGroup);
            for (GraphLine line : lineGroup.getLines()){
               addCheckBox(constraints, false, lineGroup, line);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JCheckBox){
            onCheckBoxClick((JCheckBox)e.getSource(), e.getActionCommand());
        }
    }

    private void onCheckBoxClick(JCheckBox box, String command){
        boolean isCheckboxTicked = box.isSelected();
        graphData.setVisibility(command, isCheckboxTicked);
        if(groupedCheckboxesMap.containsKey(command)){
            for(JCheckBox childBox : groupedCheckboxesMap.get(command)){
                childBox.setSelected(box.isSelected());
            }
        }
        refreshView();
    }

    private void refreshView(){
        for(GraphLineGroup lineGroup : graphData.getGroups()){
            for (GraphLine line : lineGroup.getLines()) {
                renderer.setSeriesLinesVisible(line.getDisplayId(), line.isVisible());
                renderer.setSeriesShapesVisible(line.getDisplayId(), line.isVisible());
            }
        }
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

        NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domainAxis.setAutoRange(true);
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
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
        JCheckBox checkBox = createCheckBox(line.getName(), GraphStringsHelper.getLineCommand(lineGroup, line));
        addCheckBoxToPanel(constraints, startNewLine, checkBox);
        groupedCheckboxesMap.get(GraphStringsHelper.getGroupCommand(lineGroup)).add(checkBox);
    }

    private JCheckBox createCheckBox(String label, String command){
        JCheckBox checkBox = new JCheckBox(label, true);
        checkBox.addActionListener(this);
        checkBox.setActionCommand(command);
        return checkBox;
    }
}
