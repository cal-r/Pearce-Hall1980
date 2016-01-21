package Views;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphWindow extends JFrame{
    private JPanel rootPane;
    private JPanel lenedPanel;
    private JPanel checkboxesPanel;
    private GraphPanel graphPanel;

    public GraphWindow(){
        super();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootPane);
        pack();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        List<Integer> scores = new ArrayList<Integer>();
        Random random = new Random();
        int maxDataPoints = 16;
        int maxScore = 20;
        for (int i = 0; i < maxDataPoints ; i++) {
            scores.add(random.nextInt(maxScore));
        }
        graphPanel = new GraphPanel(scores);
    }
}
