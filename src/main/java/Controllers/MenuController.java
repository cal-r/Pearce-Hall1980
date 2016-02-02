package Controllers;

import Constants.GuiStringConstants;
import Helpers.GuiHelper;
import Models.Simulator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Rokas on 01/02/2016.
 */
public class MenuController implements ActionListener {

    private Simulator simulator;
    private JMenuBar menuBar;

    public MenuController(Simulator simulator) {
        this.simulator = simulator;
        initMenuBar();
    }

    public JMenuBar getBar(){
        return menuBar;
    }

    private void initMenuBar(){
        menuBar = new JMenuBar();
        //file menu
        JMenu fileMenu = new JMenu(GuiStringConstants.FILE);
        createMenuItem(fileMenu, GuiStringConstants.SAVE, MenuItemType.BASIC);
        createMenuItem(fileMenu, GuiStringConstants.OPEN, MenuItemType.BASIC);
        menuBar.add(fileMenu);
        //settings menu
        JMenu settingsMenu = new JMenu(GuiStringConstants.SETTINGS);
        createMenuItem(settingsMenu, GuiStringConstants.RANDOM_TRAILS_SETTING, MenuItemType.BASIC);
        createMenuItem(settingsMenu, GuiStringConstants.COMPOUND_RESULTS_SETTING, MenuItemType.CHECKBOX);
        createMenuItem(settingsMenu, GuiStringConstants.CONIFGURAL_CUES_SETTING, MenuItemType.CHECKBOX);
        menuBar.add(settingsMenu);

    }

    private enum MenuItemType { BASIC, CHECKBOX }

    private void createMenuItem(JMenu menu, String nameAndCommand, MenuItemType type){
        JMenuItem menuItem = type == MenuItemType.CHECKBOX ? new JCheckBoxMenuItem(nameAndCommand) : new JMenuItem(nameAndCommand);
        menuItem.setActionCommand(nameAndCommand);
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiHelper.displayErrorMessage(e.getActionCommand());
    }
}
