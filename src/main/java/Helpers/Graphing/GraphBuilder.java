package Helpers.Graphing;

import Models.Graphing.Graph;
import Models.Graphing.GraphLine;
import Models.History.GroupPhaseHistory;
import Models.History.PhaseHistory;
import Models.History.SimulationHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphBuilder {

    public static List<Graph> BuildGraphs(SimulationHistory history){
        Map<String, List<GraphLine>> linkedLinesMap = new HashMap<>();
        List<Graph> graphs = new ArrayList<>();
        for(PhaseHistory phaseHistory : history.getPhases()){
            int lineDisplayId = 0;
            Graph graph = new Graph(phaseHistory.getPhaseName());
            for(GroupPhaseHistory gpHist : phaseHistory){
                for(String stimName : gpHist.getStimsNames()){
                    GraphLine line = new GraphLine(String.valueOf(stimName));
                    addLinePoints(line, stimName, gpHist);
                    graph.addLine(gpHist.getGroupName(), line);
                    setLink(linkedLinesMap, line);
                    line.setDisplayId(lineDisplayId++);
                }
            }
            graphs.add(graph);
        }
        return graphs;
    }

    private static void addLinePoints(GraphLine line, String stimName, GroupPhaseHistory gpHist){
        for(int trialNo = 1; trialNo<=gpHist.getNumberOfTrials(); trialNo++){
            line.addPoint(trialNo, gpHist.getState(stimName, trialNo).Vnet);
        }
    }

    //used for colour consistency among graphs
    private static void setLink(Map<String, List<GraphLine>> linkedLinesMap, GraphLine line){
        String lineCommand = GraphStringsHelper.getLineCommand(line);
        if(!linkedLinesMap.containsKey(lineCommand)) {
            linkedLinesMap.put(lineCommand, new ArrayList<GraphLine>());
        }
        List<GraphLine> linkedLines = linkedLinesMap.get(lineCommand);
        linkedLines.add(line);
        line.setLinkedLines(linkedLines);
    }
}
