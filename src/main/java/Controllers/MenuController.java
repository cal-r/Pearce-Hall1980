package Controllers;

import Constants.GuiStringConstants;
import Helpers.Export.ModelExportHelper;
import Helpers.GuiHelper;
import Helpers.ModelBuilding.ModelDtoHelper;
import Models.DTOs.ModelDto;
import Models.SimulatorSettings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Rokas on 01/02/2016.
 */
public class MenuController implements ActionListener {

    private SimulatorSettings settings;
    private MainWindowController mainWindowController;
    private JMenuBar menuBar;

    public MenuController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        settings = mainWindowController.getSimulator().getSimulatorSettings();
        initMenuBar();
    }

    //init stuff
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

    //action events
    private void onCompoundResultsSettingTicked(boolean isSelected){
        settings.CompoundResults = isSelected;
    }

    private void onSaveModel(){
        ModelExportHelper.exportModel(ModelDtoHelper.buildModelDto(mainWindowController));
    }

    private void onLoadModel() {
        try {
            ModelDto modelDto = ModelExportHelper.readModel();
            ModelDtoHelper.loadModelDto(modelDto, mainWindowController);
        }catch(Exception ex){}
    }

    @Override
    public void actionPerformed(ActionEvent e){
        switch (e.getActionCommand()){
            case GuiStringConstants.COMPOUND_RESULTS_SETTING:
                onCompoundResultsSettingTicked(GuiHelper.isMenuItemSelected(e));
                break;
            case GuiStringConstants.SAVE:
                onSaveModel();
                break;
            case GuiStringConstants.OPEN:
                onLoadModel();
                break;
            default:
                GuiHelper.displayErrorMessage("Nicht implementiert!");
        }
    }

    //getters
    public JMenuBar getBar(){
        return menuBar;
    }
}
