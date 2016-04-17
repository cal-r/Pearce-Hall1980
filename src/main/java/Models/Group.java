package Models;

import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Stimulus.IConditionalStimulus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 13/11/2015.
 */
public class Group implements Serializable {
    public List<GroupPhase> groupPhases;
    public String Name;

    public Group(String name,
                 List<GroupPhase> groupPhases){
        Name = name;
        this.groupPhases = groupPhases;
    }

    public List<IConditionalStimulus> getGroupCues(){
        List<IConditionalStimulus> list = new ArrayList<>();
        for (GroupPhase groupPhase : groupPhases){
            for(IConditionalStimulus cs : groupPhase.getPhaseCues()){
                if(!list.contains(cs))
                    list.add(cs);
            }
        }
        return list;
    }
}
