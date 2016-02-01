package Models.Graphing;

import Helpers.Graphing.GraphStringsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphLineGroup implements Comparable<GraphLineGroup> {
    private Integer groupId;
    private String name;
    private Map<String, GraphLine> linesMap;

    public GraphLineGroup(String name, int groupId){
        this.name = name;
        this.groupId = groupId;
        linesMap = new HashMap<>();
    }

    public void addLine(GraphLine line){
        linesMap.put(line.getName(), line);
        line.setGroup(this);
    }

    public GraphLine getLine(String name){
        return linesMap.get(name);
    }

    public List<GraphLine> getLines(){
        return  new ArrayList<>(linesMap.values());
    }

    public void setVisible(String command, boolean visible){
        if(GraphStringsHelper.isGroupCommand(command)){
            for(GraphLine line : linesMap.values()) {
                line.setVisible(visible);
            }
        }else {
            linesMap.get(GraphStringsHelper.getLineNameFromCommand(command)).setVisible(visible);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(GraphLineGroup other) {
        return groupId.compareTo(other.groupId);
    }
}
