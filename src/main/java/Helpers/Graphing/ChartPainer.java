package Helpers.Graphing;

import Models.Graphing.Graph;
import Models.Graphing.GraphLine;
import Models.Graphing.GraphLineGroup;
import Models.Graphing.Marker;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.awt.*;
import java.util.Map;
import java.util.Random;

/**
 * Created by Rokas on 30/01/2016.
 */
public class ChartPainer{

    private Graph graphData;
    private XYLineAndShapeRenderer renderer;
    private Map<Paint, Boolean> interGraphColorMap;

    public ChartPainer(JFreeChart chart, XYLineAndShapeRenderer renderer, Graph graphData, Map<Paint, Boolean> interGraphColorMap) {
        this.renderer = renderer;
        this.graphData = graphData;
        this.interGraphColorMap = interGraphColorMap;
        new Caller(chart);
    }

    private void setUpMarkers(){
        for (GraphLine line : graphData.getAllLines()){
            if(line.isMarkerSet()){
                setMarkerOnRenderer(line);
            }else{
                Marker newMarker = createMarkerFromRenderer(line);
                if(!interGraphColorMap.containsKey(newMarker.getColor())) {
                    setNewMarker(newMarker, line);
                }else{
                    newMarker = createRandomMarker();
                    while (interGraphColorMap.containsKey(newMarker.getColor())){
                        newMarker = createRandomMarker();
                    }
                    setNewMarker(newMarker, line);
                    setMarkerOnRenderer(line);
                }
            }
        }
    }

    private void setNewMarker(Marker newMarker, GraphLine line){
        line.setMarker(newMarker);
        interGraphColorMap.put(newMarker.getColor(), true);
    }

    private Marker createMarkerFromRenderer(GraphLine line){
        return new Marker(
                renderer.getSeriesPaint(line.getDisplayId()),
                renderer.getSeriesShape(line.getDisplayId()));
    }

    private void setMarkerOnRenderer(GraphLine line){
        renderer.setSeriesPaint(line.getDisplayId(), line.getMarker().getColor());
        renderer.setSeriesShape(line.getDisplayId(), line.getMarker().getShape());
    }

    private Marker createRandomMarker() {
        Random random = new Random(System.nanoTime());
        return new Marker(
                new Color(
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)),
                renderer.getBaseShape());
    }

    private boolean isChartRendered(){
        return renderer.getSeriesPaint(0) != null;
    }

    class Caller implements ChartProgressListener{
        private boolean markersSet;

        public Caller(JFreeChart chart) {
            chart.addProgressListener(this);
            markersSet = false;
        }

        @Override
        public void chartProgress(ChartProgressEvent chartProgressEvent) {
            if(!markersSet && isChartRendered()){
                setUpMarkers();
                markersSet = true;
            }
        }
    }
}

