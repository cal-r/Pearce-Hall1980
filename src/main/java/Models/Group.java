package Models;

import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Stimulus.IConditionalStimulus;

import java.io.Serializable;
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

//    public List<IConditionalStimulus> getGroupCues(){
//        List<IConditionalStimulus> li
//        for (GroupPhase groupPhase : groupPhases){
//
//        }
//    }
}
