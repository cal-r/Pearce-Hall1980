package Constants;

/**
 * Created by Rokas on 03/11/2015.
 */
public class GuiStringConstants {
    public static final String TOPBAR_TEXT = "PEARCE & HALL SIMULATOR \u00A9 ver. 1.0";

    public static final String ERROR = "ERROR";
    public static final String TRAIL_TABLE_ERROR = "ERROR: invalid input in trial table";
    public static final String RODRIGUEZ_TRIAL_TABLE_ERROR = "Your design includes a reinforcer. Hall & Rodriguez model does not envisage reinforced trials";
    public static final String RODRIGUEZ_MULTIPLE_US_ERROR = "ERROR: multiple USs are not allowed in Hall & Rodriguez model";
    public static final String RODRIGUEZ_CONTEXT = "ERROR: Context is not defined in Hall & Rodriguez model";

    public static final String GROUP_NAME = "Group";
    public static final String CS_PARAMETER = "CS Parameter";
    public static final String GLOBAL_PARAMETER = "Global Parameter";
    public static final String US_PARAMETER = "US Parameter";
    public static final String VALUE = "Value";
    public static final String RANDOM = "Random";
    public static final String CONTEXT = "Context";
    public static final String ITI_CS_RATION = "ITI/CS";

    public static final String FILE = "File";
    public static final String HELP = "Help";
    public static final String SAVE = "Save";
    public static final String OPEN = "Open";
    public static final String NEW = "New";
    public static final String EXIT = "Quit";
    public static final String SETTINGS = "Settings";
    public static final String RANDOM_TRIALS_SETTING = "Number of Random Trials Combinations";
    public static final String COMPOUND_RESULTS_SETTING = "Show Compound Results";
    public static final String SIMULATE_CONTEXT = "Context Simulation";
    public static final String USE_DIFFERENT_US_STRENGTHS = "Use Different US Strengts";
	public static final String USE_DIFFERENT_US = "Use Different USs";
    public static final String RODRIGUEZ_MODE = "Hall & Rodriguez";
    public static final String ELABORATIONS_MENU = "Model Elaborations";
    public static final String INITIAL_V_FOR_PH = "Set initial V excitatory for P&H";
    public static final String GUIDE = "Guide";
    public static final String ABOUT = "About";
    public static final String GUIDE_URL = "https://www.cal-r.org/PHSimulator/PH_Simulator_Guide.pdf";

    public static final String DEFAULT_PHASE = "";
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
