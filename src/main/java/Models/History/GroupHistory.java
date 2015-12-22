package Models.History;

import Models.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 16/11/2015.
 */
public class GroupHistory {
    public Group group;
    public List<PhaseHistory> phaseHistories;
    public GroupHistory(Group group){
        this.group = group;
        phaseHistories = new ArrayList<>();
    }
}
