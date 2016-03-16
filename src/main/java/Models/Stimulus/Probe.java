package Models.Stimulus;

/**
 * Created by Rokas on 16/03/2016.
 */
public class Probe {
    private IStimulus stimulus;
    private String trialTypeDescription;

    public Probe(IStimulus stimulus, String trialTypeDescription) {
        this.stimulus = stimulus;
        this.trialTypeDescription = trialTypeDescription;
    }

    public IStimulus getStimulus() {
        return stimulus;
    }

    public String getTrialTypeDescription() {
        return trialTypeDescription;
    }
}
