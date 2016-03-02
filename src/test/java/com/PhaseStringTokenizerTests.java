package com;

import Helpers.ModelBuilding.PhaseStringTokenizer;
import Models.Simulator;
import Models.SimulatorSettings;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Rokas on 03/11/2015.
 */
public class PhaseStringTokenizerTests extends junit.framework.TestCase {

    @Test
    public void testParsePhaseDescription1() throws Exception {
        List<PhaseStringTokenizer.TrialTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens(getTestSettings(), "2A+");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 1);
        assertTrue(tokens.get(0).numberOfTrials == 2);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==1);
        assertTrue(tokens.get(0).cueNames[0].equals("A"));
    }

    @Test
    public void testParsePhaseDescription2() throws Exception {
        List<PhaseStringTokenizer.TrialTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens(getTestSettings(), "2AB+");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 1);
        assertTrue(tokens.get(0).numberOfTrials == 2);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0].equals("A"));
        assertTrue(tokens.get(0).cueNames[1].equals("B"));
    }

    @Test
    public void testParsePhaseDescription3() throws Exception {
        List<PhaseStringTokenizer.TrialTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens(getTestSettings(), "AB-");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 1);
        assertTrue(tokens.get(0).numberOfTrials == 1);
        assertTrue(tokens.get(0).reinforcer == '-');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0].equals("A"));
        assertTrue(tokens.get(0).cueNames[1].equals("B"));
    }

    @Test
    public void testParsePhaseDescription4() throws Exception {
        List<PhaseStringTokenizer.TrialTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens(getTestSettings(), "2AB+/A-");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 2);
        assertTrue(tokens.get(0).numberOfTrials == 2);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0].equals("A"));
        assertTrue(tokens.get(0).cueNames[1].equals("B"));

        assertTrue(tokens.get(1).numberOfTrials == 1);
        assertTrue(tokens.get(1).reinforcer == '-');
        assertTrue(tokens.get(1).cueNames.length==1);
        assertTrue(tokens.get(1).cueNames[0].equals("A"));
    }

    @Test
    public void testParsePhaseDescription5() throws Exception {
        List<PhaseStringTokenizer.TrialTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens(getTestSettings(),"60AX+/60AY-/69BX-/60BY+");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 4);
        assertTrue(tokens.get(0).numberOfTrials == 60);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0].equals("A"));
        assertTrue(tokens.get(0).cueNames[1].equals("X"));

        assertTrue(tokens.get(1).numberOfTrials == 60);
        assertTrue(tokens.get(1).reinforcer == '-');
        assertTrue(tokens.get(1).cueNames.length==2);
        assertTrue(tokens.get(1).cueNames[0].equals("A"));
        assertTrue(tokens.get(1).cueNames[1].equals("Y"));

        assertTrue(tokens.get(2).numberOfTrials == 69);
        assertTrue(tokens.get(2).reinforcer == '-');
        assertTrue(tokens.get(2).cueNames.length==2);
        assertTrue(tokens.get(2).cueNames[0].equals("B"));
        assertTrue(tokens.get(2).cueNames[1].equals("X"));

        assertTrue(tokens.get(3).numberOfTrials == 60);
        assertTrue(tokens.get(3).reinforcer == '+');
        assertTrue(tokens.get(3).cueNames.length==2);
        assertTrue(tokens.get(3).cueNames[0].equals("B"));
        assertTrue(tokens.get(3).cueNames[1].equals("Y"));
    }

    @Test
    public void testParsePhaseDescription6() throws Exception {
        try {
            PhaseStringTokenizer.getPhaseTokens(getTestSettings(),"2AB");
            fail("No exception was thrown");
        }catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testParsePhaseDescription7() throws Exception {
        try {
            PhaseStringTokenizer.getPhaseTokens(getTestSettings(),"-2+");
            fail("No exception was thrown");
        }catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testParsePhaseDescription8() throws Exception {
        try {
            PhaseStringTokenizer.getPhaseTokens(getTestSettings(), "A+2B+");
            fail("No exception was thrown");
        }catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testParsePhaseDescription9() throws Exception {
        List<PhaseStringTokenizer.TrialTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens(getTestSettings(), "2AB#/A-");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 2);
        assertTrue(tokens.get(0).numberOfTrials == 2);
        assertTrue(tokens.get(0).reinforcer == '#');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0].equals("A"));
        assertTrue(tokens.get(0).cueNames[1].equals("B"));

        assertTrue(tokens.get(1).numberOfTrials == 1);
        assertTrue(tokens.get(1).reinforcer == '-');
        assertTrue(tokens.get(1).cueNames.length==1);
        assertTrue(tokens.get(1).cueNames[0].equals("A"));
    }

    private SimulatorSettings getTestSettings(){
        SimulatorSettings settings = new SimulatorSettings();
        settings.UseDifferentUs = true;
        return settings;
    }
}