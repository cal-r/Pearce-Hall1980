package _UnitTests;

import Helpers.PhaseParser;
import Helpers.PhaseStringTokenizer;
import Models.ConditionalStimulus;
import Models.Parameters.CsParameterPool;
import Models.Phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        CsParameterPool cspPool = createCsParameterPool(trailTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trailTypeTokens.cueNames, cspPool);
        Phase phase = PhaseParser.ParsePhase(tokensArrayList, csMap);

        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 2);
        assertTrue(phase.trails.get(0).usPresent);
        assertTrue(phase.trails.get(1).usPresent);
    }

    @org.junit.Test
    public void testParsePhase2() throws Exception {
        PhaseStringTokenizer.TrailTypeTokens trailTypeTokens = new PhaseStringTokenizer.TrailTypeTokens();
        trailTypeTokens.cueNames = "AB".toCharArray();
        trailTypeTokens.numberOfTrails = 2;
        trailTypeTokens.reinforcer = '+';
        ArrayList<PhaseStringTokenizer.TrailTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trailTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trailTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trailTypeTokens.cueNames, cspPool);
        Phase phase = PhaseParser.ParsePhase(tokensArrayList, csMap);

        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 2);
        assertTrue(phase.trails.get(0).usPresent);
        assertTrue(phase.trails.get(1).usPresent);
    }

    @org.junit.Test
    public void testParsePhase3() throws Exception {
        PhaseStringTokenizer.TrailTypeTokens trailTypeTokens = new PhaseStringTokenizer.TrailTypeTokens();
        trailTypeTokens.cueNames = "AB".toCharArray();
        trailTypeTokens.numberOfTrails = 1;
        trailTypeTokens.reinforcer = '-';
        ArrayList<PhaseStringTokenizer.TrailTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trailTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trailTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trailTypeTokens.cueNames, cspPool);
        Phase phase = PhaseParser.ParsePhase(tokensArrayList, csMap);

        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 1);
        assertTrue(!phase.trails.get(0).usPresent);
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

        CsParameterPool cspPool = createCsParameterPool(trailTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trailTypeTokens.cueNames, cspPool);
        Phase phase = PhaseParser.ParsePhase(tokensArrayList, csMap);

        assertTrue(phase != null);
        assertTrue(phase.trails.size() == 3);
        assertTrue(phase.trails.get(0).usPresent);
        assertTrue(phase.trails.get(0).cuesPresent.size() == 2);
        assertTrue(phase.trails.get(1).usPresent);
        assertTrue(phase.trails.get(1).cuesPresent.size() == 2);
        assertTrue(!phase.trails.get(2).usPresent);
        assertTrue(phase.trails.get(2).cuesPresent.size() == 1);
    }

    private CsParameterPool createCsParameterPool(char[] cueNames){
        CsParameterPool pool = new CsParameterPool();
        for(char cueName : cueNames){
            pool.createParameters(cueName);
        }
        return pool;
    }

    private Map<Character, ConditionalStimulus> getCsMap(char[] cueNames, CsParameterPool csParameterPool){
        Map<Character, ConditionalStimulus> csMap = new HashMap<>();
        for(char cueName : cueNames){
            csMap.put(cueName, new ConditionalStimulus(
                    cueName,
                    csParameterPool.getInitialAlpha(cueName),
                    csParameterPool.getSeParameter(cueName),
                    csParameterPool.getSiParamter(cueName)));
        }
        return csMap;
    }
}
