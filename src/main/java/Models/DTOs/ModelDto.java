package Models.DTOs;

import Models.Simulator;
import ViewModels.TableModels.CSParamsTableModel;
import ViewModels.TableModels.GlobalPramsTableModel;
import ViewModels.TableModels.TrialTableModel;

import java.io.Serializable;

/**
 * Created by Rokas on 06/02/2016.
 */
public class ModelDto implements Serializable {
    private Simulator simulator;
    private ParamsTableModelDto csParamsTableModel;
    private ParamsTableModelDto globalPramsTableModel;
    private TrialTableModelDto trialTableModel;

    public TrialTableModelDto getTrialTableModel() {
        return trialTableModel;
    }

    public void setTrialTableModel(TrialTableModelDto trialTableModel) {
        this.trialTableModel = trialTableModel;
    }

    public ParamsTableModelDto getGlobalPramsTableModel() {
        return globalPramsTableModel;
    }

    public void setGlobalPramsTableModel(ParamsTableModelDto globalPramsTableModel) {
        this.globalPramsTableModel = globalPramsTableModel;
    }

    public ParamsTableModelDto getCsParamsTableModel() {
        return csParamsTableModel;
    }

    public void setCsParamsTableModel(ParamsTableModelDto csParamsTableModel) {
        this.csParamsTableModel = csParamsTableModel;
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
}
