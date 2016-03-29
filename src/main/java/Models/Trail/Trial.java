package Models.Trail;

import Models.GroupPhase;
import Models.History.GroupPhaseHistory;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 03/11/2015.
 */
public class Trial implements Serializable{
    private List<LearningPeriod> learningPeriods;
    private Probe probe;
    public Trial(List<LearningPeriod> learningPeriods){
        this.learningPeriods = learningPeriods;
    }

    public void simulate(GroupPhaseHistory history, GlobalParameterPool globalParams, int phaseId) {
        for(LearningPeriod period : learningPeriods){
            if(period instanceof ItiPeriod) {
                history.recordState(period.stims, period.reinforcer);
                ((ItiPeriod)period).learn(globalParams);
            }else {
                history.recordState(period.stims, period.reinforcer);
                if(probe!=null){
                    history.recordProbeState(probe);
                }
                period.learn(calcVNetValue(period.reinforcer), globalParams, phaseId);
            }
        }
    }

    private double calcVNetValue(char reinforcer){
        double vNet = 0;
        for(IStimulus stim : getStims()){
            if(stim instanceof ConditionalStimulus || stim instanceof RodriguezStimulus) {
                vNet += stim.getAssociationNet();
            }
            if(stim instanceof MultipleStimulus){
                vNet += ((MultipleStimulus) stim).getAssociationNet(reinforcer);
            }
        }
        return vNet;
    }

    public List<IStimulus> getStims(){
        Map<IStimulus, Boolean> added = new HashMap<>();
        List<IStimulus> cues = new ArrayList<>();
        for(LearningPeriod period : learningPeriods){
            for(IStimulus stim : period.stims){
                if(!added.containsKey(stim)){
                    added.put(stim, true);
                    cues.add(stim);
                }
            }
        }
        return cues;
    }

    public List<LearningPeriod> getLearningPeriods(){
        return learningPeriods;
    }

    public void setProbe(Probe probe) {
        this.probe = probe;
    }
}
