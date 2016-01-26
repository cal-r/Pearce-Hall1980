package Models.Graphing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphLineGroup {
    private String name;
    private boolean visible;
    private Map<String, GraphLine> linesMap;

    public GraphLineGroup(String name){
        this.name = name;
        visible = true;
        linesMap = new HashMap<>();
    }

    public void addLine(GraphLine line){
        linesMap.put(line.getName(), line);
    }

    public List<GraphLine> getLines(){
        return  new ArrayList<>(linesMap.values());
    }

    public void setLineGroupVisible(boolean visible){
        this.visible = visible;
        for(GraphLine line : linesMap.values()) {
            line.visible = false;
        }
    }

    public boolean getVisible(){
        return visible;
    }

    public void setLineVisible(String name, boolean visible){
        linesMap.get(name).visible = visible;
    }

    public double getMaxX(){
        double max = Double.MIN_VALUE;
        for(GraphLine line : linesMap.values()){
            max = Math.max(line.getMaxX(), max);
        }
        return max;
    }

    public double getMaxY(){
        double max = Double.MIN_VALUE;
        for(GraphLine line : linesMap.values()){
            max = Math.max(line.getMaxY(), max);
        }
        return max;
    }

    public String getName() {
        return name;
    }

    public String getLineInGroupName(GraphLine line){
        return String.format("%s - %s", name, line.getName());
    }
}
