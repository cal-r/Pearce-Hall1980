package Views;

import javax.swing.*;

//demo
import javax.swing.JPanel;

import Helpers.ResourceHelper;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphWindow extends JFrame{
    private JPanel rootPane;
    private JPanel checkboxesPanel;
    private ChartPanel chartPanel;

    private JFreeChart chart;

    public GraphWindow(JFreeChart chart){
        super();
        this.chart = chart;

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.9;
        c.weighty = 0.9;
        c.fill = GridBagConstraints.BOTH;
        rootPane.add(chartPanel, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        rootPane.add(checkboxesPanel, c);


        setContentPane(rootPane);
        setIconImage(ResourceHelper.getIconImage());
        pack();
        setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        chartPanel = createChartPanel();
    }

    private ChartPanel createChartPanel(){
        return new ChartPanel( chart );
    }

    public JPanel getCheckboxesPanel() {
        return checkboxesPanel;
    }
}
