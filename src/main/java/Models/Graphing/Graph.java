package Models.Graphing;

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

    public List<GraphLineGroup> getGroups(){
        return new ArrayList<>(linesGroupsMap.values());
    }

    public String getName() {
        return name;
    }
}
