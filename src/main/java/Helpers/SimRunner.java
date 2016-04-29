package Helpers;

import Controllers.MainWindowController;
import Helpers.GUI.GuiHelper;
import Models.Simulator;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Rokas on 29/04/2016.
 */
public class SimRunner implements PropertyChangeListener {

    private JFrame frame;
    private Runner runner;
    private MainWindowController callback;

    public void RunAndOutput(Simulator simulator, JTextArea simOutputArea, MainWindowController callback){
        this.callback = callback;
        initProgressFrame();

        runner = new Runner(simulator, simOutputArea);
        runner.addPropertyChangeListener(this);
        runner.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            if(progress == 100){
                frame.dispose();
                if(runner.error.length() == 0){ // success
                    callback.onSimulationComplete();
                }else{
                    GuiHelper.displayErrorMessage(runner.error);
                }
            }
        }
    }


    class Runner extends SwingWorker<Void, Void>{

        private String error;
        private Simulator simulator;
        private JTextArea simOutputArea;

        public Runner(Simulator simulator, JTextArea simOutputArea){
            this.simulator = simulator;
            this.simOutputArea = simOutputArea;
            error = "";
        }

        @Override
        protected Void doInBackground() throws Exception {
            runSimulation();
            setProgress(100);
            return null;
        }

        private void runSimulation(){
            try {
                simulator.runSimulation();
                GuiHelper.outputHistory(simulator.getLatestReport(), simOutputArea);
            }catch (Exception ex){
                error = String.format("ERROR: %s", ex.getMessage());
            }
        }
    }

    private void initProgressFrame(){
        frame = new JFrame();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        frame.add(progressBar);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.pack();
        GuiHelper.centerFrame(frame);
        frame.setVisible(true);
    }
}
