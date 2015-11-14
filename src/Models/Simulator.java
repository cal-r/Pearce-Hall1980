package Models;

import Models.Parameters.CsParameter;
import Models.Parameters.GammaParameter;
import Models.Parameters.Parameter;
import Models.Parameters.CsParameterPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 06/11/2015.
 */
public class Simulator {

    private List<Group> groups;
    private GammaParameter gamma;
    private CsParameterPool csParameterPool;

    public Simulator(CsParameterPool csParameterPool, List<Group> groups){
        this.csParameterPool = csParameterPool;
        this.groups = groups;
        gamma = new GammaParameter();
    }

    public List<CsParameter> getCsParameters(){
        return csParameterPool.getAllParameters();
    }

    public List<Parameter> getGlobalParameters(){
        List<Parameter> globals = new ArrayList<>();
        globals.add(gamma);
        return globals;
    }

    public List<PhaseHistory> runSimulation(){
        List<PhaseHistory> histories = new ArrayList<>();


        return histories;
    }
}
