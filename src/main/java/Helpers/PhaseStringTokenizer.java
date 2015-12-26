package Helpers;

import Constants.TableStringConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rokas on 03/11/2015.
 *
  using same rules of describing the phase as per R&W simulator:

    "Number of trials followed by Stimuli followed by Reinforcer(+ or –)
    Different trial types should be separated by a slash symbol (/).
    Note that no space should  appear  between  the characters. For instance,
    Phase  1 in Group  1 of  our biconditional discrimination example would read:
        60AX+/60AY-/60BX-/60BY+"

    taken from: https://www.cal-r.org/RWSimulator/RW_Simulator4_Guide.pdf

 *
 */
public class PhaseStringTokenizer {

    private static final String TRAIL_TYPE_REGEX = "(\\d*)([a-zA-Z]+)([\\+\\-])";

    public static List<TrailTypeTokens> getPhaseTokens(String phaseDescription) throws IllegalArgumentException {

        List<TrailTypeTokens> tokensList = new ArrayList<>();

        for(String trailTypeDescription : getTrailTypes(phaseDescription)) {

            TrailTypeTokens trailTokens = new TrailTypeTokens();

            Matcher matcher = matchTrailType(trailTypeDescription);

            trailTokens.numberOfTrails = getNumberOfTrails(matcher);
            trailTokens.cueNames = getCueNames(matcher);
            trailTokens.reinforcer = getReinforcer(matcher);

            tokensList.add(trailTokens);
        }

        return tokensList;
    }

    private static int getNumberOfTrails(Matcher matcher) {
        String str = matcher.group(1);
        if(str.isEmpty()){
            return 1;
        }
        return Integer.parseInt(str);
    }

    private static char[] getCueNames(Matcher matcher){
        return matcher.group(2).toCharArray();
    }

    private static char getReinforcer(Matcher matcher){
        return matcher.group(3).charAt(0);
    }

    private static String[] getTrailTypes(String phaseDescription) {
        return phaseDescription.split(TableStringConstants.TRAIL_TYPE_SEPARATOR);
    }

    private static Matcher matchTrailType(String trailTypeDescription) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile(TRAIL_TYPE_REGEX);
        Matcher matcher = pattern.matcher(trailTypeDescription);

        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        return matcher;
    }

    public static class TrailTypeTokens {
        public int numberOfTrails;
        public char[] cueNames;
        public char reinforcer;
    }
}
