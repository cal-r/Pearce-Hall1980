package Models.Trail;

import Models.GroupPhase;
import Models.Stimulus.Stimulus;

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
    public Trial(List<LearningPeriod> learningPeriods){
        this.learningPeriods = learningPeriods;
    }

    public void simulate(GroupPhase groupPhase, double gammaValue) {
        for(LearningPeriod period : learningPeriods){
            if(period instanceof ItiPeriod) {
                groupPhase.recordItiPeriod(period.stims);
                ((ItiPeriod)period).learn(gammaValue);
            }else {
                groupPhase.recordPeriod();
                period.learn(groupPhase.calcVNetValue(), gammaValue);
            }
        }
    }

    public List<Stimulus> getStims(){
        Map<Stimulus, Boolean> added = new HashMap<>();
        List<Stimulus> cues = new ArrayList<>();
        for(LearningPeriod period : learningPeriods){
            for(Stimulus stim : period.stims){
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
}
