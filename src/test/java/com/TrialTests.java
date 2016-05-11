package com;

import Constants.DefaultValuesConstants;
import Models.GroupPhase;
import Models.History.GroupPhaseHistory;
import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.SimulatorSettings;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.IConditionalStimulus;
import Models.Stimulus.IStimulus;
import Models.Trail.ItiPeriod;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 26/02/2016.
 */
public class TrialTests extends TestCase {

    @Test
    public void testWithContext(){
        String contextName = "context";
        IConditionalStimulus contextStimulus = createContextStimulus(contextName);
        List<IStimulus> allStims = new ArrayList<>();
        allStims.add(createConditionalStimulus("A"));
        allStims.add(createConditionalStimulus("B"));

        List<Trial> trials = new ArrayList<>();
        trials.add(createTrial(true, getStims(allStims, "A"), contextStimulus));
        trials.add(createTrial(true, getStims(allStims, "A"), contextStimulus));
        trials.add(createTrial(false, getStims(allStims, "A,B"), contextStimulus));
        trials.add(createTrial(true, getStims(allStims, "A"), contextStimulus));
        GroupPhase groupPhase = new GroupPhase(1, '+');
        groupPhase.addTrials(trials);
        GroupPhaseHistory history = groupPhase.simulateTrials(getGlobals(), getSimulatorSettings(), 1);
        assertEquals(history.getState(contextName, 1).Vnet, 0.0);
        assertEquals(history.getState(contextName, 2).Vnet, 0.0);
        assertEquals(history.getState(contextName, 3).Vnet, 0.0);
        assertEquals(history.getState(contextName, 4).Vnet, 0.0);
        assertEquals(history.getState(contextName, 5).Vnet, 0.0003645, DefaultValuesConstants.ROUNDING_PRECISION);
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
        GlobalParameterPool globals = new GlobalParameterPool(new SimulatorSettings());
        globals.getGamma().setValue(0.1);
        globals.getBetaE().setValue(0.005);
        globals.getBetaE().setValue(0.005);
        return globals;
    }

    private SimulatorSettings getSimulatorSettings(){
        SimulatorSettings settings = new SimulatorSettings();
        settings.setContextSimulation(true);
        return settings;
    }

    private Trial createTrial(boolean usPresent, List<IStimulus> cs, IConditionalStimulus contextStim){
        int itiRatio = 3;
        List<LearningPeriod> learningPeriods = new ArrayList<>();
        for(int i =0;i<itiRatio;i++){
            learningPeriods.add(new ItiPeriod(contextStim));
        }
        cs.add(contextStim);
        learningPeriods.add(new LearningPeriod(usPresent, usPresent ? '+' : '-', cs));
        return new Trial(learningPeriods);
    }

    private IConditionalStimulus createContextStimulus(String contextName){
        InitialAlphaParameter alphaParameter = new InitialAlphaParameter(contextName);
        alphaParameter.setValue(0.1);
        return new ConditionalStimulus(contextName, alphaParameter);
    }

    private ConditionalStimulus createConditionalStimulus(String name){
        InitialAlphaParameter alphaParameter = new InitialAlphaParameter(name);
        alphaParameter.setValue(0.5);
        return new ConditionalStimulus(name, alphaParameter);
    }
}
