package Models.Parameters.Pools.CsPools;

import Models.Group;
import Models.Parameters.ConditionalStimulus.CsParameter;
import _from_RW_simulator.ContextConfig;

import java.util.List;

/**
 * Created by Rokas on 29/03/2016.
 */
public interface ICsParameterPool {
    void createParameters(String cueName);
    boolean contains(String cueName);
    List<CsParameter> getAllParameters();
    List<CsParameter> getGroupParameters(Group group);
}
