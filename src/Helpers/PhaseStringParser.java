package Helpers;

import Models.SimPhase;
import Models.SimTrail;

/**
 * Created by Rokas on 03/11/2015.
 */
public class PhaseStringParser {

    private static final String TRAIL_TYPE_REGEX = "(\\d*)([a-zA-Z]+)([\\+\\-])";

    public static SimPhase ParsePhaseDescription(String description) {
        return new SimPhase();
    }
}
