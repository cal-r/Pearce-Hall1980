package Helpers.Graphing;

import Helpers.Ordering.StimulusOrderingHelper;
import Models.Graphing.Graph;
import Models.Graphing.GraphLine;
import Models.History.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 21/01/2016.
 */
public class GraphBuilder {

    public static List<Graph> BuildGraphs(SimulationHistory history, List<Variable> variablesToDisplay){
        Map<String, List<GraphLine>> linkedLinesMap = new HashMap<>();
        List<Graph> graphs = new ArrayList<>();
        for(PhaseHistory phaseHistory : history.getPhases()){
            LineDisplayIdCounter lineCounter = new LineDisplayIdCounter();
            Graph graph = new Graph(phaseHistory.getPhaseName());
            for(GroupPhaseHistory gpHist : phaseHistory){
                for(String stimName : StimulusOrderingHelper.orderStimNamesByDescription(gpHist.getStimsNames(), gpHist.getDescription())){
                   addLines(stimName, gpHist, graph, linkedLinesMap, lineCounter, variablesToDisplay);
                }
            }
            graph.setYUnits(UnitsHelper.getYUnits(variablesToDisplay));
            graphs.add(graph);
        }
        return graphs;
    }


    private static void addLines(String stimName, GroupPhaseHistory gpHist, Graph graph, Map<String, List<GraphLine>> linkedLinesMap, LineDisplayIdCounter lineCounter, List<Variable> variablesToDisplay){
        for(Variable var : variablesToDisplay){
            if(var == Variable.VNET){
                addGraphLine(stimName, gpHist, graph, linkedLinesMap, lineCounter, var);
            }else if(gpHist.getState(stimName, 1) instanceof ConditionalStimulusState){
                addGraphLine(stimName, gpHist, graph, linkedLinesMap, lineCounter, var);
            }
        }
    }

    private static void addGraphLine(String stimName, GroupPhaseHistory gpHist, Graph graph, Map<String, List<GraphLine>> linkedLinesMap, LineDisplayIdCounter lineCounter, Variable variable){
        GraphLine line = new GraphLine(getLineName(stimName, variable));
        addLinePoints(line, stimName, gpHist, variable);
        graph.addLine(gpHist.getGroupName(), line);
        setLink(linkedLinesMap, line);
        line.setDisplayId(lineCounter.getDisplayId());
    }

    private static String getLineName(String stimName, Variable variable){
        if(variable == Variable.VNET || variable == Variable.Alpha){
            return stimName;
        }
        return String.format("%s(%s)", stimName, variable);
    }


    public enum Variable { VNET, VnE, VE, Alpha }
    private static void addLinePoints(GraphLine line, String stimName, GroupPhaseHistory gpHist, Variable variable){
        for(int periodNo = 1; periodNo<=gpHist.getNumberOfPeriods(); periodNo++){
            StimulusState state = gpHist.getState(stimName, periodNo);
            if(state!=null) {
                if(variable == Variable.VNET) {
                    line.addPoint(periodNo, state.Vnet);
                }else{
                    //only for rodriguez
                    ConditionalStimulusState csState = (ConditionalStimulusState) state;
                    if(variable == Variable.VE)
                        line.addPoint(periodNo, csState.Ve);
                    if(variable == Variable.VnE)
                        line.addPoint(periodNo, csState.Vi);
                    if(variable == Variable.Alpha)
                        line.addPoint(periodNo, csState.Alpha);
                }
            }
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

    private static class LineDisplayIdCounter {
        private int currCount;
        public LineDisplayIdCounter(){
            currCount = 0;
        }
        public int getDisplayId(){
            return currCount++;
        }
    }
}
