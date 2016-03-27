package Helpers.ModelBuilding;

import Controllers.MainWindowController;
import Controllers.MenuController;
import Models.DTOs.ModelDto;
import Models.DTOs.ParamsTableModelDto;
import Models.DTOs.TableModelDto;
import Models.DTOs.TrialTableModelDto;
import ViewModels.TableModels.BaseParamsTableModel;
import ViewModels.TableModels.BaseTableModel;
import ViewModels.TableModels.GlobalPramsTableModel;
import ViewModels.TableModels.TrialTableModel;

/**
 * Created by Rokas on 06/02/2016.
 */
public class ModelDtoHelper {
    public static ModelDto buildModelDto(MainWindowController mainWindowController){
        ModelDto dto = new ModelDto();
        dto.setSimulator(mainWindowController.getSimulator());
        dto.setTrialTableModel(buildTrialTableModelDto(mainWindowController.getTrialTableModel()));
        dto.setCsParamsTableModel(buildParamsTableModelDto(mainWindowController.getCsParamsTableModel()));
        dto.setGlobalPramsTableModel(buildParamsTableModelDto(mainWindowController.getGlobalParamsTableModel()));
        return dto;
    }

    private static ParamsTableModelDto buildParamsTableModelDto(BaseParamsTableModel tableModel) {
        return new ParamsTableModelDto(tableModel.columnHeaders, tableModel.data, tableModel.parameters);
    }

    private static TrialTableModelDto buildTrialTableModelDto(TrialTableModel tableModel){
        return new TrialTableModelDto(tableModel.columnHeaders, tableModel.data, tableModel.simulateContext);
    }

    public static void loadModelDto(ModelDto dto, MainWindowController mainWindowController, MenuController menuController){
        mainWindowController.setSimulator(dto.getSimulator());
        mainWindowController.getTrialTableModel().copyData(dto.getTrialTableModel());
        mainWindowController.getCsParamsTableModel().copyData(dto.getCsParamsTableModel());
        mainWindowController.getGlobalParamsTableModel().copyData(dto.getGlobalPramsTableModel());
        menuController.setSettings(dto.getSimulator().getSettings());
    }
}
