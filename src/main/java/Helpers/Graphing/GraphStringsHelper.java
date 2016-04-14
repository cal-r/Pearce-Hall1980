package Helpers.Graphing;

import Constants.ActionCommands;
import Models.Graphing.GraphLine;
import Models.Graphing.GraphLineGroup;

/**
 * Created by Rokas on 26/01/2016.
 */
public class GraphStringsHelper {
    public static String getLineInGroupName(GraphLineGroup group, GraphLine line){
        return String.format("%s - %s", group.getName(), line.getName());
    }

    public static String getLineCommand(GraphLine line){
        String lineName = getNameWithoutNegativeLabeling(line.getName());
        return String.format("%s%s%s", line.getGroup().getName(), ActionCommands.GROUP_LINE_SEPARATOR, lineName);
    }

    private static String getNameWithoutNegativeLabeling(String name){
        if (name.endsWith(")-")){
            return name.substring(1, name.length()-2);
        }
        return name;
    }

    public static String getGroupCommand(GraphLineGroup group){
        return group.getName();
    }

    public static String getGroupNameFromCommand(String command){
        return splitCommand(command)[0];
    }

    public static boolean isGroupCommand(String command){
        return !command.contains(ActionCommands.GROUP_LINE_SEPARATOR);
    }

    private static String[] splitCommand(String command){
        return command.split(ActionCommands.GROUP_LINE_SEPARATOR);
    }
}
