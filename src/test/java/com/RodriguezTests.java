package com;

import Constants.DefaultValuesConstants;
import Models.GroupPhase;
import Models.History.GroupPhaseHistory;
import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.SalienceParameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.SimulatorSettings;
import Models.Stimulus.IStimulus;
import Models.Stimulus.RodriguezStimulus;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 29/03/2016.
 */
public class RodriguezTests extends TestCase {

    @Test
    public void testSimulate(){
        List<IStimulus> allStims = new ArrayList<>();
        allStims.add(createStimulus("A", 0.4));
        allStims.add(createStimulus("B", 0.1));
        List<Trial> trials = new ArrayList<>();
        trials.add(createTrial(false, getStims(allStims, "A")));
        trials.add(createTrial(false, getStims(allStims, "A")));
        trials.add(createTrial(false, getStims(allStims, "A")));
        trials.add(createTrial(false, getStims(allStims, "A")));
        GroupPhase groupPhase = new GroupPhase(1, '+');
        groupPhase.addTrials(trials);
        GroupPhaseHistory history = groupPhase.simulateTrials(getGlobals(), getSimulatorSettings());
        assertEquals(history.getState("A", 1).Vnet, 0.4, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("A", 2).Vnet, 0.24, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("A", 3).Vnet, 0.2016, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("A", 4).Vnet, 0.182246, DefaultValuesConstants.ROUNDING_PRECISION);
    }

    private Trial createTrial(boolean usPresent, List<IStimulus> cs){
        List<LearningPeriod> learningPeriods = new ArrayList<>();
        learningPeriods.add(new LearningPeriod(usPresent, usPresent ? '+' : '-', cs));
        return new Trial(learningPeriods);
    }

    private List<IStimulus> getStims(List<IStimulus> allStims, String commaSeparated){
        List<IStimulus> ret = new ArrayList<>();
        for(String stimName : commaSeparated.split(",")){
            for(IStimulus stim : allStims){
                if(stim.getName().equals(stimName)){
                    ret.add(stim);
                }
            }
        }
        return ret;
    }

    private GlobalParameterPool getGlobals(){
        GlobalParameterPool globals = new GlobalParameterPool();
        globals.getGamma().setValue(1);
        return globals;
    }

    private SimulatorSettings getSimulatorSettings(){
        SimulatorSettings settings = new SimulatorSettings();
        settings.RodriguezMode = true;
        return settings;
    }

    private RodriguezStimulus createStimulus(String name, double v){
        InitialAlphaParameter alphaParameter = new InitialAlphaParameter(name);
        alphaParameter.setValue(1);
        SalienceParameter salienceParameter = new SalienceParameter(name);
        salienceParameter.setValue(0.4);
        InitialAssociationParameter associationParameter = new InitialAssociationParameter(name);
        associationParameter.setValue(v);
        return new RodriguezStimulus(alphaParameter, associationParameter, salienceParameter, name);
    }
}
