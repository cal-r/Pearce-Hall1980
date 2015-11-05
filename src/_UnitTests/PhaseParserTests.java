package _UnitTests;

import Helpers.PhaseParser;
import Helpers.PhaseStringTokenizer;
import Models.SimPhase;

import java.util.ArrayList;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParserTests extends junit.framework.TestCase {

    @org.junit.Test
    public void testParsePhase1() throws Exception {

        PhaseStringTokenizer.TrailTypeTokens trailTypeTokens = new PhaseStringTokenizer.TrailTypeTokens();
        trailTypeTokens.cueNames = "A".toCharArray();
        trailTypeTokens.numberOfTrails = 2;
        trailTypeTokens.reinforcer = '+';
        ArrayList<PhaseStringTokenizer.TrailTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trailTypeTokens);
        SimPhase phase = PhaseParser.ParsePhase(tokensArrayList);

        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 2);
        assertTrue(phase.trails.get(0).usPresent);
        assertTrue(phase.trails.get(1).usPresent);
        assertTrue(phase.GetCues().size() == 1);
        assertTrue(phase.GetCues().get(0).Name == 'A');
    }

    @org.junit.Test
    public void testParsePhase2() throws Exception {
        PhaseStringTokenizer.TrailTypeTokens trailTypeTokens = new PhaseStringTokenizer.TrailTypeTokens();
        trailTypeTokens.cueNames = "AB".toCharArray();
        trailTypeTokens.numberOfTrails = 2;
        trailTypeTokens.reinforcer = '+';
        ArrayList<PhaseStringTokenizer.TrailTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trailTypeTokens);
        SimPhase phase = PhaseParser.ParsePhase(tokensArrayList);

        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 2);
        assertTrue(phase.trails.get(0).usPresent);
        assertTrue(phase.trails.get(1).usPresent);
        assertTrue(phase.GetCues().size() == 2);
        assertTrue(phase.GetCues().get(0).Name == 'A');
        assertTrue(phase.GetCues().get(1).Name == 'B');
    }

    @org.junit.Test
    public void testParsePhase3() throws Exception {
        PhaseStringTokenizer.TrailTypeTokens trailTypeTokens = new PhaseStringTokenizer.TrailTypeTokens();
        trailTypeTokens.cueNames = "AB".toCharArray();
        trailTypeTokens.numberOfTrails = 1;
        trailTypeTokens.reinforcer = '-';
        ArrayList<PhaseStringTokenizer.TrailTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trailTypeTokens);
        SimPhase phase = PhaseParser.ParsePhase(tokensArrayList);

        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 1);
        assertTrue(!phase.trails.get(0).usPresent);
        assertTrue(phase.GetCues().size() == 2);
        assertTrue(phase.GetCues().get(0).Name == 'A');
        assertTrue(phase.GetCues().get(1).Name == 'B');
    }

    @org.junit.Test
    public void testParsePhase4() throws Exception {

        PhaseStringTokenizer.TrailTypeTokens trailTypeTokens = new PhaseStringTokenizer.TrailTypeTokens();
        trailTypeTokens.cueNames = "AB".toCharArray();
        trailTypeTokens.numberOfTrails = 2;
        trailTypeTokens.reinforcer = '+';

        PhaseStringTokenizer.TrailTypeTokens trailTypeTokens2 = new PhaseStringTokenizer.TrailTypeTokens();
        trailTypeTokens2.cueNames = "A".toCharArray();
        trailTypeTokens2.numberOfTrails = 1;
        trailTypeTokens2.reinforcer = '-';

        ArrayList<PhaseStringTokenizer.TrailTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trailTypeTokens);
        tokensArrayList.add(trailTypeTokens2);

        SimPhase phase = PhaseParser.ParsePhase(tokensArrayList);
        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 3);
        assertTrue(phase.trails.get(0).usPresent);
        assertTrue(phase.trails.get(0).cuesPresent.size() == 2);
        assertTrue(phase.trails.get(1).usPresent);
        assertTrue(phase.trails.get(1).cuesPresent.size() == 2);
        assertTrue(!phase.trails.get(2).usPresent);
        assertTrue(phase.trails.get(2).cuesPresent.size() == 1);
        assertTrue(phase.GetCues().size() == 2);
        assertTrue(phase.GetCues().get(0).Name == 'A');
        assertTrue(phase.GetCues().get(1).Name == 'B');
    }
}
