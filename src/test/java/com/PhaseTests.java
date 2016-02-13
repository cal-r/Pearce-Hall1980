package com;

import Constants.DefaultValuesConstants;
import Models.ConditionalStimulus;
import Models.GroupPhase;
import Models.History.ConditionalStimulusState;
import Models.History.GroupPhaseHistory;
import Models.Parameters.GammaParameter;
import Models.SimulatorSettings;
import Models.Trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 25/12/2015.
 */
public class PhaseTests extends junit.framework.TestCase {

    @org.junit.Test
    public void testSimPhase1() throws Exception{
        GammaParameter gamma = new GammaParameter();
        gamma.setValue(0.1);

        //40AB+/40AB-
        GroupPhase groupPhase = CreatePhase40(true, false, false);
        GroupPhaseHistory hist = groupPhase.simulateTrials(gamma, new SimulatorSettings());

        double expectedVnet = 0.08724879;
        //        excel gives 0.08747151

        //test return value
        for(String stimName : hist.getStimsNames()) {
            assertEquals(expectedVnet, hist.getState(stimName, 80).Vnet, DefaultValuesConstants.ROUNDING_PRECISION);
        }
    }

    @org.junit.Test
    public void testSimPhase2() throws Exception{
        GammaParameter gamma = new GammaParameter();
        gamma.setValue(0.1);

        //40AB+/40AB-
        GroupPhase groupPhase = CreatePhase40(false, true, false);
        GroupPhaseHistory hist = groupPhase.simulateTrials(gamma, new SimulatorSettings());

        double expectedVnet = 0.500572221;
        //        excel gives 0.500433494

        //test state of cues after simulation
        for(ConditionalStimulus cs : groupPhase.getPhaseCues()) {
            assertEquals(expectedVnet, cs.getAssociationNet(), DefaultValuesConstants.ROUNDING_PRECISION);
        }

        //test return value
        for(String stimName : hist.getStimsNames()) {
            assertEquals(expectedVnet, hist.getState(stimName, 79).Vnet, DefaultValuesConstants.ROUNDING_PRECISION);
        }
    }

    @org.junit.Test
      public void testSimPhase3() throws Exception{
        GammaParameter gamma = new GammaParameter();
        gamma.setValue(0.1);

        //random test
        GroupPhase groupPhase = CreatePhase40(false, true, true);
        GroupPhaseHistory hist = groupPhase.simulateTrials(gamma, new SimulatorSettings());

        //test state of cues after simulation
        for(ConditionalStimulus cs : groupPhase.getPhaseCues()) {
            assertTrue(cs.getAssociationNet() > 0.2);
            assertTrue(cs.getAssociationNet() < 0.5);
        }


        //test return value
        for(String stimName : hist.getStimsNames()) {
            assertTrue(hist.getState(stimName, 80).Vnet > 0.2);
            assertTrue(hist.getState(stimName, 80).Vnet < 0.5);
            assertTrue(((ConditionalStimulusState)hist.getState(stimName, 20)).Ve < 0.33);
            assertTrue(((ConditionalStimulusState)hist.getState(stimName, 20)).Vi < 0.1);
        }
    }

    @org.junit.Test
    public void testSimPhase4() throws Exception{
        GammaParameter gamma = new GammaParameter();
        gamma.setValue(0.1);

        //should be the same
        GroupPhase groupPhaseRand = CreatePhase40(true, true, true);
        GroupPhase groupPhaseSeq = CreatePhase40(true, true, false);
        GroupPhaseHistory histRand = groupPhaseRand.simulateTrials(gamma, new SimulatorSettings());
        GroupPhaseHistory histSeq = groupPhaseSeq.simulateTrials(gamma, new SimulatorSettings());

        //test state of cues after simulation
        ConditionalStimulus csRand = groupPhaseRand.getPhaseCues().get(0);
        ConditionalStimulus csSeq = groupPhaseSeq.getPhaseCues().get(0);
        assertEquals(csSeq.getAssociationNet(), csRand.getAssociationNet(), DefaultValuesConstants.ROUNDING_PRECISION);

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
        GroupPhase groupPhase = new GroupPhase(1);
        HashMap<Character, ConditionalStimulus> phaseCues = TrialTests.createCsMap("AB".toCharArray());
        groupPhase.addTrialType(createTrialType(phaseCues, "AB", 40, usPresent1));
        groupPhase.addTrialType(createTrialType(phaseCues, "AB", 40, usPresent2));
        groupPhase.setRandom(isRandom);
        return groupPhase;
    }

    private static List<Trial> createTrialType(HashMap<Character, ConditionalStimulus> phaseCues, String presentCSs, int count, boolean usPresent){
        List<Trial> trialType = new ArrayList<>();
        for(int i=0;i<count;i++){
            trialType.add(TrialTests.createTrial(phaseCues, presentCSs.toCharArray(), usPresent));
        }
        return trialType;
    }
}
