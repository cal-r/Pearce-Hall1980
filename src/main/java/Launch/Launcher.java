package Launch;

import Controllers.MainWindowController;
import Controllers.MenuController;
import Models.Simulator;
import Views.MainWindow;

import javax.swing.*;

/**
 * Created by Rokas on 03/11/2015.
 */
public class Launcher {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startSimulator();
            }
        });
    }

    public static void startSimulator(){
        Simulator simulator = new Simulator();
        MainWindowController mainWindowController = new MainWindowController(simulator);
        MenuController menuController = new MenuController(mainWindowController);

        MainWindow mainWindow = new MainWindow(mainWindowController, menuController);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
