package Helpers.ModelBuilding;

import Constants.GuiStringConstants;
import Helpers.ListCaster;
import Models.SimulatorSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rokas on 03/11/2015.
 *
  using same rules of describing the phase as per R&W simulator:

    "Number of trials followed by Stimuli followed by Reinforcer(+ or �)
    Different trial types should be separated by a slash symbol (/).
    Note that no space should  appear  between  the characters. For instance,
    Phase  1 in Group  1 of  our biconditional discrimination example would read:
        60AX+/60AY-/60BX-/60BY+"

    taken from: https://www.cal-r.org/RWSimulator/RW_Simulator4_Guide.pdf

 *
 */
public class PhaseStringTokenizer {

    private static final String RODRIGUEZ_TRAIL_TYPE_REGEX = "(\\d*)([a-zA-Z\\^]*)";
    private static final String TRAIL_TYPE_REGEX = "(\\d*)([a-zA-Z\\^]*)([\\+\\-])";
    private static final String TRAIL_TYPE_REGEX_MULTIPLE_US = "(\\d*)([a-zA-Z\\^]*)([\\+\\*\\#\\$\\-])";

    public static List<TrialTypeTokens> getPhaseTokens(SimulatorSettings settings, String phaseDescription) throws IllegalArgumentException {

        List<TrialTypeTokens> tokensList = new ArrayList<>();

        for(String trialTypeDescription : getTrialTypes(phaseDescription)) {

            TrialTypeTokens trialTokens = new TrialTypeTokens();

            trialTokens.description = trialTypeDescription;

            if(!isEmpty(trialTypeDescription)) {

                Matcher matcher = matchTrialType(trialTypeDescription, settings);

                trialTokens.numberOfTrials = getNumberOfTrials(matcher);
                trialTokens.cueNames = getCueNames(matcher);
                if(!settings.RodriguezMode)
                    trialTokens.reinforcer = getReinforcer(matcher);
            }

            tokensList.add(trialTokens);
        }
        if(!settings.RodriguezMode)
            validateReinforcers(tokensList);

        return tokensList;
    }

    private static void validateReinforcers(List<TrialTypeTokens> groupPhaseTokens){
        for(TrialTypeTokens trialType1 : groupPhaseTokens){
            for(TrialTypeTokens trialType2 : groupPhaseTokens) {
                if(trialType1.reinforcer != '-' && trialType2.reinforcer != '-' && trialType1.reinforcer != trialType2.reinforcer){
                    throw new IllegalArgumentException("Only one reinforcer symbol is permitted per phase");
                }
            }
        }

    }

    private static int getNumberOfTrials(Matcher matcher) {
        String str = matcher.group(1);
        if(str.isEmpty()){
            return 1;
        }
        return Integer.parseInt(str);
    }

    private static String[] getCueNames(Matcher matcher){
        return ListCaster.toStringArray(matcher.group(2).replace("^", ""));
    }

    private static char getReinforcer(Matcher matcher){
        return matcher.group(3).charAt(0);
    }

    private static String[] getTrialTypes(String phaseDescription) {
        return phaseDescription.split(GuiStringConstants.TRAIL_TYPE_SEPARATOR);
    }

    private static Matcher matchTrialType(String trialTypeDescription, SimulatorSettings settings) throws IllegalArgumentException {
        String regexToUse = settings.RodriguezMode ?
                RODRIGUEZ_TRAIL_TYPE_REGEX :
                (settings.UseDifferentUs ? TRAIL_TYPE_REGEX_MULTIPLE_US : TRAIL_TYPE_REGEX);
        Pattern pattern = Pattern.compile(regexToUse);
        Matcher matcher = pattern.matcher(trialTypeDescription);

        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        return matcher;
    }

    private static boolean isEmpty(String trialTypeDescription){
        return trialTypeDescription.isEmpty() || trialTypeDescription.equals("0");
    }

    public static class TrialTypeTokens {
        public int numberOfTrials;
        public String[] cueNames;
        public char reinforcer;
        public String description;
        public TrialTypeTokens(){
            numberOfTrials = 0;
            cueNames = new String[0];
        }
    }
}
