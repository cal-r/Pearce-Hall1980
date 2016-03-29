package Models.Parameters.Pools.CsPools;

import Models.Parameters.ConditionalStimulus.CsParameter;

import java.util.List;

/**
 * Created by Rokas on 29/03/2016.
 */
public interface ICsParameterPool {
    public void createParameters(String cueName);
    public boolean contains(String cueName);
    public List<CsParameter> getAllParameters();
}
