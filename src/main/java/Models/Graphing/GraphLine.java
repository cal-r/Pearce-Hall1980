package Models.Graphing;


import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphLine {
    public boolean visible;
    private String name;
    private java.util.List<Point2D.Double> dataPoints;
    private double maxX;
    private double maxY;

    public GraphLine(String name){
        this.name = name;
        dataPoints = new ArrayList<>();
        maxX = Double.MIN_VALUE;
        maxY = Double.MIN_VALUE;
        visible = true;
    }

    public void addPoint(double x, double y){
        dataPoints.add(new Point2D.Double(x,y));
        updateMax(x, y);
    }

    public java.util.List<Point2D.Double> getDataPoints(){
        return dataPoints;
    }

    public double getMaxX(){
        return maxX;
    }

    public double getMaxY(){
        return maxY;
    }

    public String getName(){
        return name;
    }

    private void updateMax(double x, double y) {
        maxX = Math.max(x, maxX);
        maxY = Math.max(y, maxY);
    }
}
