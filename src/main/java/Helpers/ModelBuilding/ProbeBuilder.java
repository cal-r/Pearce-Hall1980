package Helpers.ModelBuilding;

import Models.Stimulus.IConditionalStimulus;
import Models.Stimulus.IStimulus;
import Models.Stimulus.MultipleStimulus;
import Models.Stimulus.Probe;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rokas on 16/03/2016.
 */
public class ProbeBuilder {
    public static boolean containsProbe(String trialTypeDescription){
        return trialTypeDescription.contains("^");
    }

    public static Probe buildProbe(String trialTypeDescription, List<IStimulus> stims){
        char csName = trialTypeDescription.charAt(trialTypeDescription.indexOf('^') - 1); // char with ^
        char reinforcer = trialTypeDescription.charAt(trialTypeDescription.length()-1);
        IStimulus probeCs = null;
        for(IStimulus stim : stims){
            if(stim.getName().equals(String.valueOf(csName))){
                if(stim instanceof MultipleStimulus && reinforcer != '-'){
                    probeCs = ((MultipleStimulus)stim).getStimsMap().get(reinforcer);
                }else {
                    probeCs = stim;
                }
                break;
            }
        }

        String probeLabel = generateProbeLabel(trialTypeDescription, probeCs);
        return new Probe(probeCs, probeLabel);
    }

    private static String generateProbeLabel(String trialTypeDescription, IStimulus probeCs){
        Pattern pattern = Pattern.compile("(\\d*)(\\w*)(\\W*)");
        Matcher matcher = pattern.matcher(trialTypeDescription);
        matcher.find();
        String compoundString = matcher.group(2);
        return String.format("%s^{%s}", probeCs.getName(), compoundString);
    }
}
