package StringConstants;

/**
 * Created by Rokas on 03/11/2015.
 */
public class TableConstants {
    public static final String GROUP_NAME = "Group Name";
    public static final String PHASE = "Phase";
    public static final String PARAMETER = "Parameter";
    public static final String VALUE = "Value";

    public static final String DEFAULT_PHASE = "2A+";


    private static final String DEFAULT_GROUP_NAME = "Group";
    private static int GroupCount = 1;
    public static String GetDefaultGroupName() {
        return DEFAULT_GROUP_NAME + " " + GroupCount;
    }
}
