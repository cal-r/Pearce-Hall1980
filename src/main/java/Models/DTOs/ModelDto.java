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
}
