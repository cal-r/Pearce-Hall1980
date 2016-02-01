package Models.Graphing;

import Helpers.Graphing.GraphStringsHelper;

import java.util.*;

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
            GraphLineGroup newGroup = new GraphLineGroup(groupName, linesGroupsMap.size());
            linesGroupsMap.put(groupName, newGroup);
        }
        linesGroupsMap.get(groupName).addLine(line);
    }

    public void setVisibility(String command, boolean visible){
        String groupName = GraphStringsHelper.getGroupNameFromCommand(command);
        linesGroupsMap.get(groupName).setVisible(command, visible);
    }

    public List<GraphLineGroup> getAllGroups(){
        List<GraphLineGroup> groups = new ArrayList<>(linesGroupsMap.values());
        Collections.sort(groups);
        return groups;
    }

    public List<GraphLine> getAllLines(){
        List<GraphLine> lines = new ArrayList<>();
        for(GraphLineGroup group : getAllGroups()){
            for (GraphLine line : group.getLines()){
                lines.add(line);
            }
        }
        Collections.sort(lines);
        return lines;
    }

    public String getName() {
        return name;
    }
}
