package com;

import Constants.DefaultValuesConstants;
import Helpers.ListCaster;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.ConditionalStimulus;
import Models.GroupPhase;
import Models.History.ConditionalStimulusState;
import Models.History.GroupPhaseHistory;
import Models.SimulatorSettings;
import Models.Stimulus.IConditionalStimulus;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 25/12/2015.
 */
public class PhaseTests extends junit.framework.TestCase {

    @org.junit.Test
    public void testSimPhase1() throws Exception{
        GlobalParameterPool globals = getGlobals();

        //40AB+/40AB-
        GroupPhase groupPhase = CreatePhase40(true, false, false);
        GroupPhaseHistory hist = groupPhase.simulateTrials(globals, new SimulatorSettings(), 1);

        double expectedVnet = 0.08747151;

        //test return value
        for(String stimName : hist.getStimsNames()) {
            assertEquals(expectedVnet, hist.getState(stimName, 80).Vnet, DefaultValuesConstants.ROUNDING_PRECISION);
        }
    }

    @org.junit.Test
    public void testSimPhase2() throws Exception{
        GlobalParameterPool globals = getGlobals();

        //40AB+/40AB-
        GroupPhase groupPhase = CreatePhase40(false, true, false);
        GroupPhaseHistory hist = groupPhase.simulateTrials(globals, new SimulatorSettings(), 1);

        double expectedVnet = 0.500430759;
        //        excel gives 0.500433494

        //test state of cues after simulation
        for(IConditionalStimulus cs : groupPhase.getPhaseCues()) {
            assertEquals(expectedVnet, cs.getAssociationNet(), DefaultValuesConstants.ROUNDING_PRECISION);
        }
    }

    @org.junit.Test
      public void testSimPhase3() throws Exception{
        GlobalParameterPool globals = getGlobals();

        //random test
        GroupPhase groupPhase = CreatePhase40(false, true, true);
        GroupPhaseHistory hist = groupPhase.simulateTrials(globals, new SimulatorSettings(), 1);

        //test state of cues after simulation
        for(IConditionalStimulus cs : groupPhase.getPhaseCues()) {
            assertTrue(cs.getAssociationNet() > 0.2);
            assertTrue(cs.getAssociationNet() < 0.5);
        }


        //test return value
        for(String stimName : hist.getStimsNames()) {
            assertTrue(hist.getState(stimName, 80).Vnet > 0.2);
            assertTrue(hist.getState(stimName, 80).Vnet < 0.5);
            assertTrue(((ConditionalStimulusState)hist.getState(stimName, 20)).Ve < 0.33);
            assertTrue(((ConditionalStimulusState) hist.getState(stimName, 20)).Vi < 0.1);
        }
    }

    @org.junit.Test
    public void testSimPhase4() throws Exception{
        GlobalParameterPool globals = getGlobals();

        //should be the same
        GroupPhase groupPhaseRand = CreatePhase40(true, true, true);
        GroupPhase groupPhaseSeq = CreatePhase40(true, true, false);
        GroupPhaseHistory histRand = groupPhaseRand.simulateTrials(globals, new SimulatorSettings(), 1);
        GroupPhaseHistory histSeq = groupPhaseSeq.simulateTrials(globals, new SimulatorSettings(), 1);

        //test state of cues after simulation
        IConditionalStimulus csRand = groupPhaseRand.getPhaseCues().get(0);
        IConditionalStimulus csSeq = groupPhaseSeq.getPhaseCues().get(0);
        assertEquals(csSeq.getAssociationNet(), csRand.getAssociationNet(), 0.0001);

        //test return value
        for(String stimName : histRand.getStimsNames()) {
            for(int tNum = 1;tNum<= groupPhaseRand.trials.size();tNum++){
                assertEquals(histSeq.getState(stimName, tNum).Vnet, histRand.getState(stimName, tNum).Vnet, DefaultValuesConstants.ROUNDING_PRECISION);
                assertEquals(((ConditionalStimulusState)histSeq.getState(stimName, tNum)).Vi, ((ConditionalStimulusState)histRand.getState(stimName, tNum)).Vi, DefaultValuesConstants.ROUNDING_PRECISION);
                assertEquals(((ConditionalStimulusState)histSeq.getState(stimName, tNum)).Ve, ((ConditionalStimulusState)histRand.getState(stimName, tNum)).Ve, DefaultValuesConstants.ROUNDING_PRECISION);
            }
        }
    }

    //40AB+/40AB-
    private static GroupPhase CreatePhase40(boolean usPresent1, boolean usPresent2, boolean isRandom){
        GroupPhase groupPhase = new GroupPhase(1, usPresent1 || usPresent2 ? '+' : '-');
        HashMap<String, ConditionalStimulus> phaseCues = LearningPeriodTests.createCsMap(ListCaster.toStringArray("AB"));
        groupPhase.addTrials(createTrialType(phaseCues, "AB", 40, usPresent1));
        groupPhase.addTrials(createTrialType(phaseCues, "AB", 40, usPresent2));
        groupPhase.setRandom(isRandom);
        return groupPhase;
    }

    private static List<Trial> createTrialType(HashMap<String, ConditionalStimulus> phaseCues, String presentCSs, int count, boolean usPresent){
        List<Trial> trialType = new ArrayList<>();
        for(int i=0;i<count;i++){
            List<LearningPeriod> periods = new ArrayList<>();
            periods.add(LearningPeriodTests.createLearningPeriod(phaseCues, ListCaster.toStringArray(presentCSs), usPresent));
            trialType.add(new Trial(periods));
        }
        return trialType;
    }

    private GlobalParameterPool getGlobals(){
        GlobalParameterPool globals = new GlobalParameterPool(new SimulatorSettings());
        globals.getGamma().setValue(0.1);
        return globals;
    }
}
