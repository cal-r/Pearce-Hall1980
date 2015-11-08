package Models.Parameters;

import Helpers.DefaultValuesHelper;
import Models.ConditionalStimulus;

/**
 * Created by Rokas on 05/11/2015.
 */
public abstract class CsParameter extends Parameter {

    public ConditionalStimulus cue;

    public CsParameter(ConditionalStimulus cue, String name) {
        super(name);
        this.cue = cue;
    }

    @Override
    public String getDisplayName(){
        return String.format("%s (%s)", name, cue.Name);
    }
}
