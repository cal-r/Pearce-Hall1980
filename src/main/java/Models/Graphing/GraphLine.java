package Models.Graphing;


import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphLine {
    private boolean visible;
    private int displayId;
    private String name;
    private java.util.List<Point2D.Double> dataPoints;

    public GraphLine(String name){
        this.name = name;
        dataPoints = new ArrayList<>();
        visible = true;
    }

    public int getDisplayId() {
        return displayId;
    }

    public void setDisplayId(int displayId) {
        this.displayId = displayId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void addPoint(double x, double y){
        dataPoints.add(new Point2D.Double(x, y));
    }

    public java.util.List<Point2D.Double> getDataPoints(){
        return dataPoints;
    }

    public String getName(){
        return name;
    }
}
