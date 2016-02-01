package Models.Graphing;


import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphLine implements Comparable<GraphLine> {
    private boolean visible;
    private Integer displayId;
    private Marker marker;

    private String name;

    private GraphLineGroup group;
    private java.util.List<Point2D.Double> dataPoints;
    private java.util.List<GraphLine> linkedLines;

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

    public void setGroup(GraphLineGroup group) {
        this.group = group;
    }

    public GraphLineGroup getGroup() {
        return group;
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

    public void setLinkedLines(java.util.List<GraphLine> linkedLines){
        this.linkedLines = linkedLines;
    }

    public boolean isMarkerSet(){
        return marker != null;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        if(!isMarkerSet()) {
            this.marker = marker;
            for(GraphLine linkedLine : linkedLines){
                linkedLine.marker = marker;
            }
        }
    }

    public java.util.List<Point2D.Double> getDataPoints(){
        return dataPoints;
    }

    public String getName(){
        return name;
    }

    @Override
    public int compareTo(GraphLine other) {
        return displayId.compareTo(other.displayId);
    }
}
