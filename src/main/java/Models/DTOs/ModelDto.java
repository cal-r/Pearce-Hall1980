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
    private CSParamsTableModel csParamsTableModel;
    private GlobalPramsTableModel globalPramsTableModel;
    private TrialTableModel trialTableModel;

    public TrialTableModel getTrialTableModel() {
        return trialTableModel;
    }

    public void setTrialTableModel(TrialTableModel trialTableModel) {
        this.trialTableModel = trialTableModel;
    }

    public GlobalPramsTableModel getGlobalPramsTableModel() {
        return globalPramsTableModel;
    }

    public void setGlobalPramsTableModel(GlobalPramsTableModel globalPramsTableModel) {
        this.globalPramsTableModel = globalPramsTableModel;
    }

    public CSParamsTableModel getCsParamsTableModel() {
        return csParamsTableModel;
    }

    public void setCsParamsTableModel(CSParamsTableModel csParamsTableModel) {
        this.csParamsTableModel = csParamsTableModel;
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
}
