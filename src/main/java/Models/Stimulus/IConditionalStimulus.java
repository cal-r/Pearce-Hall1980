package Models.Stimulus;

/**
 * Created by Rokas on 13/03/2016.
 */
public interface IConditionalStimulus extends IStimulus {
    IConditionalStimulus getCopy();
    void reset(IConditionalStimulus cs);
}
