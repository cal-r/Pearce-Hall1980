package Helpers.ModelBuilding;

import Controllers.MainWindowController;
import Models.DTOs.ModelDto;

/**
 * Created by Rokas on 06/02/2016.
 */
public class ModelDtoBuilder {
    public static ModelDto buildModelDto(MainWindowController mainWindowController){
        ModelDto dto = new ModelDto();
        dto.setSimulator(mainWindowController.getSimulator());
        dto.setTrialTableModel(mainWindowController.getTrialTableModel());
        dto.setCsParamsTableModel(mainWindowController.getCsParamsTableModel());
        dto.setGlobalPramsTableModel(mainWindowController.getGlobalParamsTableModel());
        return dto;
    }
}
