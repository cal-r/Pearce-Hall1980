package Helpers;

import Models.Graphing.Graph;
import Models.Graphing.GraphLine;
import Models.Graphing.GraphLineGroup;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.geom.Point2D;

/**
 * Created by Rokas on 26/01/2016.
 */
public class GraphDatasetHelper {
    public static XYDataset createDataset(Graph graph){
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (GraphLine line : graph.getAllLines()){
            XYSeries series = new XYSeries(GraphStringsHelper.getLineInGroupName(line.getGroup(), line));
            for(Point2D.Double point : line.getDataPoints()){
                series.add(point.x, point.y);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }
}
