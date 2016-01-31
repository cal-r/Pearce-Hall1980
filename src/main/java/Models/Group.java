package Models;

import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class Group {
    private Map<Character, ConditionalStimulus> csMap;
    public List<GroupPhase> groupPhases;
    public String Name;

    public Group(String name,
                 Map<Character, ConditionalStimulus> csMap,
                 List<GroupPhase> groupPhases){
        Name = name;
        this.csMap = csMap;
        this.groupPhases = groupPhases;
    }
}
