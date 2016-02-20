package com;

import Helpers.ModelBuilding.PhaseParser;
import Helpers.ModelBuilding.PhaseStringTokenizer;
import Models.SimulatorSettings;
import Models.Stimulus.ConditionalStimulus;
import Models.GroupPhase;
import Models.Parameters.CsParameterPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParserTests extends junit.framework.TestCase {

    @org.junit.Test
    public void testParsePhase1() throws Exception {

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = "A".toCharArray();
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);
        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 2);
        assertTrue(groupPhase.trials.get(0).usPresent);
        assertTrue(groupPhase.trials.get(1).usPresent);
    }

    @org.junit.Test
    public void testParsePhase2() throws Exception {
        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = "AB".toCharArray();
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 2);
        assertTrue(groupPhase.trials.get(0).usPresent);
        assertTrue(groupPhase.trials.get(1).usPresent);
    }

    @org.junit.Test
    public void testParsePhase3() throws Exception {
        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = "AB".toCharArray();
        trialTypeTokens.numberOfTrials = 1;
        trialTypeTokens.reinforcer = '-';
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 1);
        assertTrue(!groupPhase.trials.get(0).usPresent);
    }

    @org.junit.Test
    public void testParsePhase4() throws Exception {

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = "AB".toCharArray();
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens2 = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens2.cueNames = "A".toCharArray();
        trialTypeTokens2.numberOfTrials = 1;
        trialTypeTokens2.reinforcer = '-';

        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);
        tokensArrayList.add(trialTypeTokens2);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 3);
        assertTrue(groupPhase.trials.get(0).usPresent);
        assertTrue(groupPhase.trials.get(0).cuesPresent.size() == 2);
        assertTrue(groupPhase.trials.get(1).usPresent);
        assertTrue(groupPhase.trials.get(1).cuesPresent.size() == 2);
        assertTrue(!groupPhase.trials.get(2).usPresent);
        assertTrue(groupPhase.trials.get(2).cuesPresent.size() == 1);
    }

    public void testParsePhase5() throws Exception {

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = "AAA".toCharArray();
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';

        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 2);
        assertTrue(groupPhase.trials.get(0).usPresent);
        assertTrue(groupPhase.trials.get(0).cuesPresent.size() == 1);
    }

    @org.junit.Test
    public void testParsePhase6() throws Exception {

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = "AB".toCharArray();
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens2 = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens2.cueNames = "A".toCharArray();
        trialTypeTokens2.numberOfTrials = 1;
        trialTypeTokens2.reinforcer = '-';

        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);
        tokensArrayList.add(trialTypeTokens2);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<Character, ConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        ConditionalStimulus csA_T1 = (ConditionalStimulus)groupPhase.trials.get(0).cuesPresent.get(0);
        ConditionalStimulus csA_T2 = (ConditionalStimulus)groupPhase.trials.get(1).cuesPresent.get(0);
        assertEquals(csA_T1, csA_T2);
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

    private SimulatorSettings getTestSettings(){
        SimulatorSettings testSettings = new SimulatorSettings();
        testSettings.CompoundResults = false;
        testSettings.ContextSimulation = false;
        return testSettings;
    }
}
