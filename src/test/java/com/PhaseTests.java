package com;

import Constants.DefaultValuesConstants;
import Models.ConditionalStimulus;
import Models.History.PhaseHistory;
import Models.Parameters.GammaParameter;
import Models.Phase;
import Models.Trail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        Phase phase = CreatePhase40(true, false, false);
        PhaseHistory hist = phase.simulateTrails(gamma);

        double expectedVnet = 0.08489972;
        //        excel gives 0.08747151

        //test state of cues after simulation
        for(ConditionalStimulus cs : phase.getPhaseCues()) {
            assertEquals(expectedVnet, cs.getAssociationNet(), DefaultValuesConstants.ROUNDING_PRECISION);
        }

        //test return value
        for(Character csname : hist.getCues()) {
            assertEquals(expectedVnet, hist.getState(csname, 80).Vnet, DefaultValuesConstants.ROUNDING_PRECISION);
        }

    }

    @org.junit.Test
    public void testSimPhase2() throws Exception{
        GammaParameter gamma = new GammaParameter();
        gamma.setValue(0.1);

        //40AB+/40AB-
        Phase phase = CreatePhase40(false, true, false);
        PhaseHistory hist = phase.simulateTrails(gamma);

        double expectedVnet = 0.500572221;
        //        excel gives 0.500433494

        //test state of cues after simulation
        for(ConditionalStimulus cs : phase.getPhaseCues()) {
            assertEquals(expectedVnet, cs.getAssociationNet(), DefaultValuesConstants.ROUNDING_PRECISION);
        }

        //test return value
        for(Character csname : hist.getCues()) {
            assertEquals(expectedVnet, hist.getState(csname, 79).Vnet, DefaultValuesConstants.ROUNDING_PRECISION);
        }
    }

    @org.junit.Test
      public void testSimPhase3() throws Exception{
        GammaParameter gamma = new GammaParameter();
        gamma.setValue(0.1);

        //random test
        Phase phase = CreatePhase40(false, true, true);
        PhaseHistory hist = phase.simulateTrails(gamma);

        //test state of cues after simulation
        for(ConditionalStimulus cs : phase.getPhaseCues()) {
            assertTrue(cs.getAssociationNet() > 0.2);
            assertTrue(cs.getAssociationNet() < 0.5);
        }


        //test return value
        for(Character csname : hist.getCues()) {
            assertTrue(hist.getState(csname, 80).Vnet > 0.2);
            assertTrue(hist.getState(csname, 80).Vnet < 0.5);
            assertTrue(hist.getState(csname, 20).Ve < 0.33);
            assertTrue(hist.getState(csname, 20).Vi < 0.1);
        }
    }

    @org.junit.Test
    public void testSimPhase4() throws Exception{
        GammaParameter gamma = new GammaParameter();
        gamma.setValue(0.1);

        //should be the same
        Phase phaseRand = CreatePhase40(true, true, true);
            Phase phaseSeq = CreatePhase40(true, true, false);
        PhaseHistory histRand = phaseRand.simulateTrails(gamma);
        PhaseHistory histSeq = phaseSeq.simulateTrails(gamma);

        //test state of cues after simulation
        ConditionalStimulus csRand = phaseRand.getPhaseCues().get(0);
        ConditionalStimulus csSeq = phaseSeq.getPhaseCues().get(0);
        assertEquals(csSeq.getAssociationNet(), csRand.getAssociationNet(), DefaultValuesConstants.ROUNDING_PRECISION);

        //test return value
        for(Character csname : histRand.getCues()) {
            for(int tNum = 1;tNum<=phaseRand.trails.size();tNum++){
                assertEquals(histSeq.getState(csname, tNum).Vnet, histRand.getState(csname, tNum).Vnet, DefaultValuesConstants.ROUNDING_PRECISION);
                assertEquals(histSeq.getState(csname, tNum).Ve, histRand.getState(csname, tNum).Ve, DefaultValuesConstants.ROUNDING_PRECISION);
                assertEquals(histSeq.getState(csname, tNum).Vi, histRand.getState(csname, tNum).Vi, DefaultValuesConstants.ROUNDING_PRECISION);
            }
        }
    }

    //40AB+/40AB-
    private static Phase CreatePhase40(boolean usPresent1, boolean usPresent2, boolean isRandom){
        Phase phase = new Phase(1);
        HashMap<Character, ConditionalStimulus> phaseCues = TrailTests.createCsMap("AB".toCharArray());
        phase.addTrailType(createTrailType(phaseCues, "AB", 40, usPresent1));
        phase.addTrailType(createTrailType(phaseCues, "AB", 40, usPresent2));
        phase.setRandom(isRandom);
        return phase;
    }

    private static List<Trail> createTrailType(HashMap<Character, ConditionalStimulus> phaseCues, String presentCSs, int count, boolean usPresent){
        List<Trail> trailType = new ArrayList<>();
        for(int i=0;i<count;i++){
            trailType.add(TrailTests.createTrail(phaseCues, presentCSs.toCharArray(), usPresent));
        }
        return trailType;
    }
}
