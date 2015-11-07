package Models.Parameters;

import Helpers.DefaultValuesHelper;
import Models.ConditionalStimulus;

/**
 * Created by Rokas on 05/11/2015.
 */
public abstract class CsParameter {

    public ConditionalStimulus cue;

    private double value;
    private boolean isSet;
    private String name;

    public CsParameter(ConditionalStimulus cue, String name) {
        this.cue = cue;
        this.name = name;
        isSet = false;
    }

    public double getValue() {
        if (isSet) {
            return value;
        }
        return DefaultValuesHelper.GetDefaultValue(name);
    }

    public void setValue(double value){
        this.value = value;
        isSet = true;
    }

    public String getDisplayName(){
        return String.format("%s (%s)", name, cue.Name);
    }
}
