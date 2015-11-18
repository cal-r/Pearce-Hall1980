package Models;

import Models.ConditionalStimulus;
import Models.Phase;

import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class Group {
    private Map<Character, ConditionalStimulus> csMap;
    public List<Phase> phases;
    public String Name;

    public Group(String name,
                 Map<Character, ConditionalStimulus> csMap,
                 List<Phase> phases){
        Name = name;
        this.csMap = csMap;
        this.phases =phases;
    }
}
