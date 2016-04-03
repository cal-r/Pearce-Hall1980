package Models.Stimulus;

/**
 * Created by Rokas on 16/03/2016.
 */
public class Probe {
    private IStimulus stimulus;
    private String label;

    public Probe(IStimulus stimulus, String probeLabel) {
        this.stimulus = stimulus;
        this.label = probeLabel;
    }

    public IStimulus getStimulus() {
        return stimulus;
    }

    public String getLabel() {
        return label;
    }
}
