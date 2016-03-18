package Models.Trail;

import Models.GroupPhase;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.IStimulus;
import Models.Stimulus.Probe;

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

    public void simulate(GroupPhase groupPhase, GlobalParameterPool globalParams) {
        for(LearningPeriod period : learningPeriods){
            if(period instanceof ItiPeriod) {
                groupPhase.recordItiPeriod(period.stims);
                ((ItiPeriod)period).learn(globalParams);
            }else {
                groupPhase.recordPeriod();
                if(probe!=null){
                    groupPhase.recordProbe(probe);
                }
                period.learn(calcVNetValue(), globalParams, groupPhase.getPhaseId());
            }
        }
    }

    private double calcVNetValue(){
        double vNet = 0;
        for(IStimulus stim : getStims()){
            vNet += stim.getAssociationNet();
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
