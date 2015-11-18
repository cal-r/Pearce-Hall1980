package com;

import Helpers.PhaseStringTokenizer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Rokas on 03/11/2015.
 */
public class PhaseStringTokenizerTests extends junit.framework.TestCase {

    @Test
    public void testParsePhaseDescription1() throws Exception {
        List<PhaseStringTokenizer.TrailTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens("2A+");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 1);
        assertTrue(tokens.get(0).numberOfTrails == 2);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==1);
        assertTrue(tokens.get(0).cueNames[0]=='A');
    }

    @Test
    public void testParsePhaseDescription2() throws Exception {
        List<PhaseStringTokenizer.TrailTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens("2AB+");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 1);
        assertTrue(tokens.get(0).numberOfTrails == 2);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0]=='A');
        assertTrue(tokens.get(0).cueNames[1]=='B');
    }

    @Test
    public void testParsePhaseDescription3() throws Exception {
        List<PhaseStringTokenizer.TrailTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens("AB-");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 1);
        assertTrue(tokens.get(0).numberOfTrails == 1);
        assertTrue(tokens.get(0).reinforcer == '-');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0]=='A');
        assertTrue(tokens.get(0).cueNames[1]=='B');
    }

    @Test
    public void testParsePhaseDescription4() throws Exception {
        List<PhaseStringTokenizer.TrailTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens("2AB+/A-");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 2);
        assertTrue(tokens.get(0).numberOfTrails == 2);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0]=='A');
        assertTrue(tokens.get(0).cueNames[1]=='B');

        assertTrue(tokens.get(1).numberOfTrails == 1);
        assertTrue(tokens.get(1).reinforcer == '-');
        assertTrue(tokens.get(1).cueNames.length==1);
        assertTrue(tokens.get(1).cueNames[0]=='A');
    }

    @Test
    public void testParsePhaseDescription5() throws Exception {
        List<PhaseStringTokenizer.TrailTypeTokens> tokens = PhaseStringTokenizer.getPhaseTokens("60AX+/60AY-/69BX-/60BY+");
        assertTrue(tokens != null);
        assertTrue(tokens.size() == 4);
        assertTrue(tokens.get(0).numberOfTrails == 60);
        assertTrue(tokens.get(0).reinforcer == '+');
        assertTrue(tokens.get(0).cueNames.length==2);
        assertTrue(tokens.get(0).cueNames[0]=='A');
        assertTrue(tokens.get(0).cueNames[1]=='X');

        assertTrue(tokens.get(1).numberOfTrails == 60);
        assertTrue(tokens.get(1).reinforcer == '-');
        assertTrue(tokens.get(1).cueNames.length==2);
        assertTrue(tokens.get(1).cueNames[0]=='A');
        assertTrue(tokens.get(1).cueNames[1]=='Y');

        assertTrue(tokens.get(2).numberOfTrails == 69);
        assertTrue(tokens.get(2).reinforcer == '-');
        assertTrue(tokens.get(2).cueNames.length==2);
        assertTrue(tokens.get(2).cueNames[0]=='B');
        assertTrue(tokens.get(2).cueNames[1]=='X');

        assertTrue(tokens.get(3).numberOfTrails == 60);
        assertTrue(tokens.get(3).reinforcer == '+');
        assertTrue(tokens.get(3).cueNames.length==2);
        assertTrue(tokens.get(3).cueNames[0]=='B');
        assertTrue(tokens.get(3).cueNames[1]=='Y');
    }

    @Test
    public void testParsePhaseDescription6() throws Exception {
        try {
            PhaseStringTokenizer.getPhaseTokens("2AB");
            fail("No exception was thrown");
        }catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testParsePhaseDescription7() throws Exception {
        try {
            PhaseStringTokenizer.getPhaseTokens("-2+");
            fail("No exception was thrown");
        }catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testParsePhaseDescription8() throws Exception {
        try {
            PhaseStringTokenizer.getPhaseTokens("A+2B+");
            fail("No exception was thrown");
        }catch (IllegalArgumentException ex) {}
    }
}