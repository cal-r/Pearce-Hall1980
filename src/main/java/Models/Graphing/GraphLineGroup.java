package Models.Graphing;

import Helpers.Graphing.GraphStringsHelper;

import java.util.*;

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
        line.setGroup(this);
        linesMap.put(GraphStringsHelper.getLineCommand(line), line);
    }

    public List<GraphLine> getLines() {
        List<GraphLine> lines = new ArrayList<>(linesMap.values());
        Collections.sort(lines);
        return lines;
    }

    public void setVisible(String command, boolean visible){
        if(GraphStringsHelper.isGroupCommand(command)){
            for(GraphLine line : linesMap.values()) {
                line.setVisible(visible);
            }
        }else {
            linesMap.get(command).setVisible(visible);
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
