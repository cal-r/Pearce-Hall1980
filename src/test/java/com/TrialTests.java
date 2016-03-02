package com;

import Constants.DefaultValuesConstants;
import Models.GroupPhase;
import Models.History.GroupPhaseHistory;
import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.SimulatorSettings;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.ContextStimulus;
import Models.Stimulus.Stimulus;
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
    public void testWithContext(){ //first 4 from P-H-MechanismContext.xlsx
        String contextName = "context";
        ContextStimulus contextStimulus = createContextStimulus(contextName);
        List<Stimulus> allStims = new ArrayList<>();
        allStims.add(createConditionalStimulus("A"));
        allStims.add(createConditionalStimulus("B"));

        List<Trial> trials = new ArrayList<>();
        trials.add(createTrial(true, getStims(allStims, "A"), contextStimulus));
        trials.add(createTrial(true, getStims(allStims, "A"), contextStimulus));
        trials.add(createTrial(false, getStims(allStims, "A,B"), contextStimulus));
        trials.add(createTrial(true, getStims(allStims, "A"), contextStimulus));
        GroupPhase groupPhase = new GroupPhase(1);
        groupPhase.addTrials(trials);
        GroupPhaseHistory history = groupPhase.simulateTrials(getGlobals(), getSimulatorSettings());
        assertEquals(history.getState(contextName, 1).Vnet, 0.0);
        assertEquals(history.getState(contextName, 2).Vnet, 0.0);
        assertEquals(history.getState(contextName, 3).Vnet, 0.0);
        assertEquals(history.getState(contextName, 4).Vnet, 0.0);
        assertEquals(history.getState(contextName, 5).Vnet, 0.0003645, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 6).Vnet, 0.000364198, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 7).Vnet, 0.000363927, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 8).Vnet, 0.000363682, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 9).Vnet, 0.000967824, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 10).Vnet, 0.000966827, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 11).Vnet, 0.000965929, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 12).Vnet, 0.000965121, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 13).Vnet, 0.000924865, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 14).Vnet, 0.000924214, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 15).Vnet, 0.000923627, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState(contextName, 16).Vnet, 0.0009231, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("A", 1).Vnet, 0.0, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("B", 1).Vnet, 0.0, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("A", 2).Vnet, 0.025, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("B", 2).Vnet, 0.0, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("A", 3).Vnet, 0.0525, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("B", 3).Vnet, 0.0, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("A", 4).Vnet, 0.050916193, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(history.getState("B", 4).Vnet, -0.001336628, DefaultValuesConstants.ROUNDING_PRECISION);
    }

    private List<Stimulus> getStims(List<Stimulus> allStims, String commaSeparated){
        List<Stimulus> ret = new ArrayList<>();
        for(String stimName : commaSeparated.split(",")){
            for(Stimulus stim : allStims){
                if(stim.getName().equals(stimName)){
                    ret.add(stim);
                }
            }
        }
        return ret;
    }

    private GlobalParameterPool getGlobals(){
        GlobalParameterPool globals = new GlobalParameterPool();
        globals.getGamma().setValue(0.1);
        globals.getLambda('+').setValue(1);
        return globals;
    }

    private SimulatorSettings getSimulatorSettings(){
        SimulatorSettings settings = new SimulatorSettings();
        settings.ContextSimulation = true;
        return settings;
    }

    private Trial createTrial(boolean usPresent, List<Stimulus> cs, ContextStimulus contextStim){
        int itiRatio = 3;
        List<LearningPeriod> learningPeriods = new ArrayList<>();
        for(int i =0;i<itiRatio;i++){
            learningPeriods.add(new ItiPeriod(contextStim));
        }
        cs.add(contextStim);
        learningPeriods.add(new LearningPeriod(usPresent, usPresent ? '+' : '-', cs));
        return new Trial(learningPeriods);
    }

    private ContextStimulus createContextStimulus(String contextName){
        InitialAlphaParameter alphaParameter = new InitialAlphaParameter(contextName);
        alphaParameter.setValue(0.1);
        SalienceExcitatoryParameter salienceExcitatoryParameter = new SalienceExcitatoryParameter(contextName);
        salienceExcitatoryParameter.setValue(0.005);
        SalienceInhibitoryParameter salienceInhibitoryParameter = new SalienceInhibitoryParameter(contextName);
        salienceInhibitoryParameter.setValue(0.005);
        return new ContextStimulus(contextName, alphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
    }

    private ConditionalStimulus createConditionalStimulus(String name){
        InitialAlphaParameter alphaParameter = new InitialAlphaParameter(name);
        alphaParameter.setValue(0.5);
        SalienceExcitatoryParameter salienceExcitatoryParameter = new SalienceExcitatoryParameter(name);
        salienceExcitatoryParameter.setValue(0.05);
        SalienceInhibitoryParameter salienceInhibitoryParameter = new SalienceInhibitoryParameter(name);
        salienceInhibitoryParameter.setValue(0.05);
        return new ConditionalStimulus(name, alphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
    }
}
