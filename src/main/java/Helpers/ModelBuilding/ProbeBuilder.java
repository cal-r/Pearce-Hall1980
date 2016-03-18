package Helpers.ModelBuilding;

import Models.Stimulus.IConditionalStimulus;
import Models.Stimulus.IStimulus;
import Models.Stimulus.Probe;

import java.util.List;

/**
 * Created by Rokas on 16/03/2016.
 */
public class ProbeBuilder {
    public static boolean containsProbe(String trialTypeDescription){
        return trialTypeDescription.contains("^");
    }

    public static Probe buildProbe(String trialTypeDescription, List<IStimulus> stims){
        char csName = trialTypeDescription.charAt(trialTypeDescription.indexOf('^') - 1); // char with ^
        IStimulus probeCs = null;
        for(IStimulus stim : stims){
            if(stim.getName().equals(String.valueOf(csName))){
                probeCs = stim;
                break;
            }
        }

        String probeName = trialTypeDescription.substring(0, trialTypeDescription.length()-1); //without reinforcer
        return new Probe(probeCs, probeName);
    }
}
