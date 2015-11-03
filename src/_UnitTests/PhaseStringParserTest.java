package _UnitTests;

import Helpers.PhaseStringParser;
import Models.SimPhase;

import static org.junit.Assert.*;

/**
 * Created by Rokas on 03/11/2015.
 */
public class PhaseStringParserTest extends junit.framework.TestCase {

    @org.junit.Test
    public void testParsePhaseDescription1() throws Exception {
        SimPhase phase = PhaseStringParser.ParsePhaseDescription("2A+");
        assertTrue(phase != null);
    }
}