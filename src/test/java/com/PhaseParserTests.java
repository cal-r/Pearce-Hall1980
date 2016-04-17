package com;

import Helpers.ListCaster;
import Helpers.ModelBuilding.PhaseParser;
import Helpers.ModelBuilding.PhaseStringTokenizer;
import Models.SimulatorSettings;
import Models.Stimulus.ConditionalStimulus;
import Models.GroupPhase;
import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Stimulus.IConditionalStimulus;

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
        trialTypeTokens.cueNames = ListCaster.toStringArray("A");
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';
        trialTypeTokens.description = "2A+";
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);
        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<String, IConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 2);
        assertTrue(groupPhase.trials.get(0).getLearningPeriods().get(0).usPresent);
        assertTrue(groupPhase.trials.get(1).getLearningPeriods().get(0).usPresent);
    }

    @org.junit.Test
    public void testParsePhase2() throws Exception {
        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = ListCaster.toStringArray("AB");
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';
        trialTypeTokens.description = "2BA+";
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<String, IConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 2);
        assertTrue(groupPhase.trials.get(0).getLearningPeriods().get(0).usPresent);
        assertTrue(groupPhase.trials.get(1).getLearningPeriods().get(0).usPresent);
    }

    @org.junit.Test
    public void testParsePhase3() throws Exception {
        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = ListCaster.toStringArray("AB");
        trialTypeTokens.numberOfTrials = 1;
        trialTypeTokens.reinforcer = '-';
        trialTypeTokens.description = "BA-";
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<String, IConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 1);
        assertFalse(groupPhase.trials.get(0).getLearningPeriods().get(0).usPresent);
    }

    @org.junit.Test
    public void testParsePhase4() throws Exception {

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = ListCaster.toStringArray("AB");
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';
        trialTypeTokens.description = "2BA+";
        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens2 = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens2.cueNames = ListCaster.toStringArray("A");
        trialTypeTokens2.numberOfTrials = 1;
        trialTypeTokens2.reinforcer = '-';
        trialTypeTokens2.description = "2A-";
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);
        tokensArrayList.add(trialTypeTokens2);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<String, IConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 3);
        assertTrue(groupPhase.trials.get(0).getLearningPeriods().get(0).usPresent);
        assertTrue(groupPhase.trials.get(0).getStims().size()==2);


        assertTrue(groupPhase.trials.get(1).getLearningPeriods().get(0).usPresent);
        assertTrue(groupPhase.trials.get(1).getStims().size()==2);

        assertFalse(groupPhase.trials.get(2).getLearningPeriods().get(0).usPresent);
        assertTrue(groupPhase.trials.get(2).getStims().size()==1);
    }

    public void testParsePhase5() throws Exception {

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = ListCaster.toStringArray("AAA");
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';
        trialTypeTokens.description = "2AAA+";
        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<String, IConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertTrue(groupPhase != null);
        assertTrue(groupPhase.trials.size() == 2);
        assertTrue(groupPhase.trials.get(0).getLearningPeriods().get(0).usPresent);
        assertTrue(groupPhase.trials.get(0).getStims().size()==1);
    }

    @org.junit.Test
    public void testParsePhase6() throws Exception {

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens.cueNames = ListCaster.toStringArray("AB");
        trialTypeTokens.numberOfTrials = 2;
        trialTypeTokens.reinforcer = '+';
        trialTypeTokens.description = "2BA+";

        PhaseStringTokenizer.TrialTypeTokens trialTypeTokens2 = new PhaseStringTokenizer.TrialTypeTokens();
        trialTypeTokens2.cueNames = ListCaster.toStringArray("A");
        trialTypeTokens2.numberOfTrials = 1;
        trialTypeTokens2.reinforcer = '-';
        trialTypeTokens2.description = "A-";

        ArrayList<PhaseStringTokenizer.TrialTypeTokens> tokensArrayList = new ArrayList<>();
        tokensArrayList.add(trialTypeTokens);
        tokensArrayList.add(trialTypeTokens2);

        CsParameterPool cspPool = createCsParameterPool(trialTypeTokens.cueNames);
        Map<String, IConditionalStimulus> csMap = getCsMap(trialTypeTokens.cueNames, cspPool);
        GroupPhase groupPhase = PhaseParser.ParsePhase(tokensArrayList, csMap, 0, getTestSettings(), null, 0);

        assertEquals(groupPhase.trials.get(0).getStims().size(), 2);
    }

    private CsParameterPool createCsParameterPool(String[] cueNames){
        CsParameterPool pool = new CsParameterPool();
        for(String cueName : cueNames){
            pool.createParameters(cueName);
        }
        return pool;
    }

    private Map<String, IConditionalStimulus> getCsMap(String[] cueNames, CsParameterPool csParameterPool){
        Map<String, IConditionalStimulus> csMap = new HashMap<>();
        for(String cueName : cueNames){
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
        testSettings.setCompoundResults(false);
        testSettings.setContextSimulation(false);
        return testSettings;
    }
}
