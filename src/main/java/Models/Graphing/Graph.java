package Models.Graphing;

import Helpers.GraphStringsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 21/01/2016.
 */
public class Graph {
    private Map<String, GraphLineGroup> linesGroupsMap;

    private String name;

    public Graph(String name){
        linesGroupsMap = new HashMap<>();
        this.name = name;
    }

    public void addLine(String groupName, GraphLine line){
        if(!linesGroupsMap.containsKey(groupName)){
            linesGroupsMap.put(groupName, new GraphLineGroup(groupName));
        }
        linesGroupsMap.get(groupName).addLine(line);
    }

    public void setVisibility(String command, boolean visible){
        String groupName = GraphStringsHelper.getGroupNameFromCommand(command);
        linesGroupsMap.get(groupName).setVisible(command, visible);
    }

    public GraphLineGroup getGroup(String groupName){
        return linesGroupsMap.get(groupName);
    }

    public List<GraphLineGroup> getGroups(){
        return new ArrayList<>(linesGroupsMap.values());
    }

    public List<GraphLine> getAllLines(){
        List<GraphLine> lines = new ArrayList<>();
        for(GraphLineGroup group : getGroups()){
            for (GraphLine line : group.getLines()){
                lines.add(line);
            }
        }
        return lines;
    }

    public String getName() {
        return name;
    }
}
