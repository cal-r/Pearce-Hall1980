package Controllers;

import Constants.GuiStringConstants;
import Helpers.Export.ModelExportHelper;
import Helpers.GuiHelper;
import Helpers.ModelBuilding.ModelDtoHelper;
import Launch.Launcher;
import Models.DTOs.ModelDto;
import Models.SimulatorSettings;
import Views.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rokas on 01/02/2016.
 */
public class MenuController implements ActionListener {

    private SimulatorSettings settings;
    private MainWindowController mainWindowController;
    private JMenuBar menuBar;
    private Map<String, JCheckBoxMenuItem> checkboxesMap;
    private MainWindow.FrameCloser frameCloser;

    public MenuController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        initMenuBar();
        setSettings(mainWindowController.getSimulator().getSettings());
    }

    //init stuff
    private void initMenuBar(){
        checkboxesMap = new HashMap<>();
        menuBar = new JMenuBar();
        //file menu
        JMenu fileMenu = new JMenu(GuiStringConstants.FILE);
        createMenuItem(fileMenu, GuiStringConstants.NEW, MenuItemType.BASIC);
        createMenuItem(fileMenu, GuiStringConstants.OPEN, MenuItemType.BASIC);
        createMenuItem(fileMenu, GuiStringConstants.SAVE, MenuItemType.BASIC);
        createMenuItem(fileMenu, GuiStringConstants.EXIT, MenuItemType.BASIC);
        menuBar.add(fileMenu);
        //settings menu
        JMenu settingsMenu = new JMenu(GuiStringConstants.SETTINGS);
        createMenuItem(settingsMenu, GuiStringConstants.RANDOM_TRIALS_SETTING, MenuItemType.BASIC);
        createMenuItem(settingsMenu, GuiStringConstants.COMPOUND_RESULTS_SETTING, MenuItemType.CHECKBOX);
        createMenuItem(settingsMenu, GuiStringConstants.SIMULATE_CONTEXT, MenuItemType.CHECKBOX);
        createMenuItem(settingsMenu, GuiStringConstants.USE_DIFFERENT_US, MenuItemType.CHECKBOX);
        menuBar.add(settingsMenu);

        //rodriguez menu
        JMenu elaborationsMenu = new JMenu(GuiStringConstants.ELABORATIONS_MENU);
        createMenuItem(elaborationsMenu, GuiStringConstants.RODRIGUEZ_MODE, MenuItemType.CHECKBOX);
        createMenuItem(elaborationsMenu, GuiStringConstants.INITIAL_V_FOR_PH, MenuItemType.CHECKBOX);
        menuBar.add(elaborationsMenu);

        //help!!
        JMenu helpMenu = new JMenu(GuiStringConstants.HELP);
        menuBar.add(helpMenu);
    }

    public void setFrameCloser(MainWindow.FrameCloser frameCloser) {
        this.frameCloser = frameCloser;
    }

    private enum MenuItemType { BASIC, CHECKBOX }

    private void createMenuItem(JMenu menu, String nameAndCommand, MenuItemType type){
        JMenuItem menuItem = type == MenuItemType.CHECKBOX ? new JCheckBoxMenuItem(nameAndCommand) : new JMenuItem(nameAndCommand);
        menuItem.setActionCommand(nameAndCommand);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        if(type == MenuItemType.CHECKBOX){
            checkboxesMap.put(nameAndCommand, (JCheckBoxMenuItem)menuItem);
        }
    }

    //action events
    private void onCompoundResultsSettingTicked(boolean isSelected){
        settings.setCompoundResults(isSelected);
        mainWindowController.disableAllButtons();
    }

    private void onSimulateContext(boolean isSelected){
        if(settings.isRodriguezMode()) {
            GuiHelper.displayErrorMessage(GuiStringConstants.RODRIGUEZ_CONTEXT);
            return;
        }
        settings.setContextSimulation(isSelected);
        mainWindowController.onSimulateContextChange();
    }

    private void onSaveModel(){
        ModelExportHelper.exportModel(ModelDtoHelper.buildModelDto(mainWindowController));
    }

    private void onLoadModel() {
        try {
            ModelDto modelDto = ModelExportHelper.readModel();
            ModelDtoHelper.loadModelDto(modelDto, mainWindowController, this);
        }catch(Exception ex){
            if(ex.getMessage().length()>0) {
                GuiHelper.displayErrorMessage(ex.getMessage());
            }
        }
    }

    private void onRandomTrialsSetting(){
        settings.setNumberOfRandomCombination(GuiHelper.getIntFromUser(GuiStringConstants.RANDOM_TRIALS_SETTING, settings.getNumberOfRandomCombination()));
    }

    private void onUseDifferentUs(boolean menuItemSelected) {
        if(settings.isRodriguezMode()) {
            GuiHelper.displayErrorMessage(GuiStringConstants.RODRIGUEZ_MULTIPLE_US_ERROR);
            return;
        }
        settings.setUseDifferentUs(menuItemSelected);
        mainWindowController.onUseDifferentUsChange();
    }

    private void onExit() {
        frameCloser.closeFrame();
    }

    private void onRestart(){
        onExit();
        Launcher.startSimulator();
    }

    private void onRodriguez(boolean menuItemSelected){
        settings.setRodriguezMode(menuItemSelected);
        mainWindowController.onRodriguezChange();
    }

    private void onInitialVe(boolean menuItemSelected) {
        settings.setUseInitialVe(menuItemSelected);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case GuiStringConstants.COMPOUND_RESULTS_SETTING:
                onCompoundResultsSettingTicked(GuiHelper.isMenuItemSelected(e));
                break;
            case GuiStringConstants.SAVE:
                onSaveModel();
                break;
            case GuiStringConstants.OPEN:
                onLoadModel();
                break;
            case GuiStringConstants.RANDOM_TRIALS_SETTING:
                onRandomTrialsSetting();
                break;
            case GuiStringConstants.SIMULATE_CONTEXT:
                onSimulateContext(GuiHelper.isMenuItemSelected(e));
                break;
            case GuiStringConstants.USE_DIFFERENT_US:
                onUseDifferentUs(GuiHelper.isMenuItemSelected(e));
                break;
            case GuiStringConstants.EXIT:
                onExit();
                break;
            case GuiStringConstants.NEW:
                onRestart();
                break;
            case GuiStringConstants.RODRIGUEZ_MODE:
                onRodriguez(GuiHelper.isMenuItemSelected(e));
                break;
            case GuiStringConstants.INITIAL_V_FOR_PH:
                onInitialVe(GuiHelper.isMenuItemSelected(e));
                break;
            default:
                GuiHelper.displayErrorMessage("Nicht implementiert!");
        }
        setSettings(settings);
    }

    public void setSettings(SimulatorSettings settings) {
        this.settings = settings;
        //set up checkboxes states
        checkboxesMap.get(GuiStringConstants.COMPOUND_RESULTS_SETTING).setState(settings.isCompoundResults());
        checkboxesMap.get(GuiStringConstants.SIMULATE_CONTEXT).setState(settings.isContextSimulation());
        checkboxesMap.get(GuiStringConstants.USE_DIFFERENT_US).setState(settings.isUseDifferentUs());
        checkboxesMap.get(GuiStringConstants.RODRIGUEZ_MODE).setState(settings.isRodriguezMode());
        checkboxesMap.get(GuiStringConstants.INITIAL_V_FOR_PH).setState(settings.isUseInitialVe());
    }

    //getters
    public JMenuBar getBar(){
        return menuBar;
    }
}
