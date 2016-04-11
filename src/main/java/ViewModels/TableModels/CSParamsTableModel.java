package ViewModels.TableModels;

import Constants.GuiStringConstants;
import Models.Parameters.ConditionalStimulus.CsParameter;
import Models.Parameters.Parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class CSParamsTableModel extends BaseParamsTableModel implements Serializable {

    @Override
    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(GuiStringConstants.CS_PARAMETER);
        columnHeaders.add(GuiStringConstants.VALUE);
        return columnHeaders;
    }

    public void setUpCsParameters(List<CsParameter> csParameters){
        List<Parameter> paramsToShow = new ArrayList<>();
        for(CsParameter csParameter : csParameters){
            if(csParameter.visibleInCsParamsTable)
                paramsToShow.add(csParameter);
        }
        setUpParameters(paramsToShow);
    }
}
