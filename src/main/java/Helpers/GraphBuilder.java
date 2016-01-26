package Helpers;

import Models.Graphing.Graph;
import Models.Graphing.GraphLine;
import Models.History.GroupPhaseHistory;
import Models.History.PhaseHistory;
import Models.History.SimulationHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphBuilder {

    public static List<Graph> BuildGraphs(SimulationHistory history){
        List<Graph> phaseGraphs = new ArrayList<>();
        for(PhaseHistory phaseHistory : history.getPhases()){
            Graph graph = new Graph(phaseHistory.getPhaseName());
            for(GroupPhaseHistory gpHist : phaseHistory){
                for(Character cue : gpHist.getCues()){
                    GraphLine line = new GraphLine(cue.toString());
                    addLinePoints(line, cue, gpHist);
                    graph.addLine(gpHist.getGroupName(), line);
                }
            }
            phaseGraphs.add(graph);
        }
        return phaseGraphs;
    }

    private static void addLinePoints(GraphLine line, Character cue, GroupPhaseHistory gpHist){
        for(int trailNo = 1; trailNo<=gpHist.getNumberOfTrails(); trailNo++){
            line.addPoint(trailNo, gpHist.getState(cue, trailNo).Vnet);
        }
    }
}
