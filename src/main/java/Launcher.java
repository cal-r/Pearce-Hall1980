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
        Simulator simulator = new Simulator();

        MainWindowController mainWindowController = new MainWindowController(simulator);
        MenuController menuController = new MenuController(simulator);

        MainWindow mainWindow = new MainWindow(mainWindowController, menuController);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
