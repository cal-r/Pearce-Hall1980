package Views;

import javax.swing.*;

//demo
import javax.swing.JPanel;

import Helpers.ResourceHelper;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

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
        setVisible(true);
        setContentPane(rootPane);
        setIconImage(ResourceHelper.getIconImage());
        pack();
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
