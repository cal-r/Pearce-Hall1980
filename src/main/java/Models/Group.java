package Models;

import Models.Parameters.Pools.CsParameterPool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 13/11/2015.
 */
public class Group implements Serializable {
    public List<GroupPhase> groupPhases;
    private CsParameterPool contextParameterPool;
    public String Name;

    public Group(String name,
                 List<GroupPhase> groupPhases){
        Name = name;
        this.groupPhases = groupPhases;
    }

    public CsParameterPool getContextParameterPool() {
        return contextParameterPool;
    }

    public void setContextParameterPool(CsParameterPool contextParameterPool) {
        this.contextParameterPool = contextParameterPool;
    }
}
