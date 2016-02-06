package Models.History;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhaseHistory extends ArrayList<GroupPhaseHistory> implements Serializable {
    private String phaseName;
    public PhaseHistory(String phaseName){
        this.phaseName = phaseName;
    }

    public String getPhaseName() {
        return phaseName;
    }
}
