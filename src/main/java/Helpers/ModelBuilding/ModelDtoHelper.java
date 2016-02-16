package Helpers.ModelBuilding;

import Controllers.MainWindowController;
import Controllers.MenuController;
import Helpers.GuiHelper;
import Models.DTOs.ModelDto;

/**
 * Created by Rokas on 06/02/2016.
 */
public class ModelDtoHelper {
    public static ModelDto buildModelDto(MainWindowController mainWindowController){
        ModelDto dto = new ModelDto();
        dto.setSimulator(mainWindowController.getSimulator());
        dto.setTrialTableModel(mainWindowController.getTrialTableModel());
        dto.setCsParamsTableModel(mainWindowController.getCsParamsTableModel());
        dto.setGlobalPramsTableModel(mainWindowController.getGlobalParamsTableModel());
        return dto;
    }

    public static void loadModelDto(ModelDto dto, MainWindowController mainWindowController, MenuController menuController){
        mainWindowController.setSimulator(dto.getSimulator());
        mainWindowController.setTrialTableModel(dto.getTrialTableModel());
        mainWindowController.setCsParamsTableModel(dto.getCsParamsTableModel());
        mainWindowController.setGlobalParamsTableModel(dto.getGlobalPramsTableModel());
        menuController.setSettings(dto.getSimulator().getSimulatorSettings());
    }
}
