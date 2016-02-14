package Constants;

/**
 * Created by Rokas on 03/11/2015.
 */
public class GuiStringConstants {
    public static final String ERROR = "ERROR";
    public static final String TRAIL_TABLE_ERROR = "ERROR: invalid input in trial table";

    public static final String GROUP_NAME = "Group Name";
    public static final String CS_PARAMETER = "Cs Parameter";
    public static final String GLOBAL_PARAMETER = "Global Parameter";
    public static final String VALUE = "Value";
    public static final String RANDOM = "Random";

    public static final String FILE = "File";
    public static final String SAVE = "Save";
    public static final String OPEN = "Open";
    public static final String SETTINGS = "Settings";
    public static final String RANDOM_TRIALS_SETTING = "Number of Random Trials Combinations";
    public static final String COMPOUND_RESULTS_SETTING = "Show Compound Results";
    public static final String ContextSimulation = "Context Simulation";


    public static final String DEFAULT_PHASE = "2A+";
    public static final String TRAIL_TYPE_SEPARATOR = "/";

    private static final String DEFAULT_GROUP_NAME = "Group";
    private static final String PHASE = "Phase";
    public static String getPhaseTitle(int phaseId){return PHASE + " " + (phaseId+1);}
    public static String getDefaultGroupName(int groupNo) {
        return DEFAULT_GROUP_NAME + " " + (groupNo+1);
    }

    public static final String CHART_Y_AXIS_LABEL = "Vnet";
    public static final String CHART_X_AXIS_LABEL = "Trials";
}
