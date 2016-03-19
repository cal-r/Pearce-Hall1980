package com;

import Constants.DefaultValuesConstants;
import Helpers.ListCaster;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.ConditionalStimulus;
import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;
import Models.Stimulus.IStimulus;
import Models.Trail.LearningPeriod;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 08/11/2015.
 */
public class LearningPeriodTests extends junit.framework.TestCase {

    @Test
    public void testSimulate1() throws Exception {
        GlobalParameterPool globals = getGlobals();
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0, globals, 1);
        assertEquals(0.025, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.55, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
   }

    @Test
    public void testSimulate2() throws Exception {
        GlobalParameterPool globals = getGlobals();
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0, globals, 1);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0.025, globals, 1);
        assertEquals(0.0525, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5925, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    @Test
    public void testSimulate3() throws Exception {
        GlobalParameterPool globals = getGlobals();
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod learn = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        learn.learn(0, globals, 1);

        learn = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        learn.learn(0.025, globals, 1);

        learn = createLearningPeriod(allCues, ListCaster.toStringArray("AB"), true);
        learn.learn(0.0525, globals, 1);
        assertEquals(0.082125, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.628, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0.025, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.54475, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    @Test
    public void testSimulate4() throws Exception {
        GlobalParameterPool globals = getGlobals();
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0, globals, 1);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0.025, globals, 1);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("AB"), true);
        period.learn(0.0525, globals, 1);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0.107125, globals, 1);
        assertEquals(0.113525, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.6544875, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0.025, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.54475, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    @Test
    public void testSimulate200() throws Exception {
        //vNet shouldn't go much above 1
        GlobalParameterPool globals = getGlobals();
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("A"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        for(int i=0;i<2000;i++) {
            period.learn(allCues.get("A").getAssociationNet(), globals, 1);
        }

        assertEquals(1, allCues.get("A").getAssociationNet(), DefaultValuesConstants.ASYMPTOTE_EXCEED_ALLOWANCE);
    }

    public static LearningPeriod createLearningPeriod(HashMap<String, ConditionalStimulus> allCues, String[] presentCss, boolean usPresent){
        List<IStimulus> presentCues = new ArrayList<>();
        for(String c : presentCss) {
            presentCues.add(allCues.get(c));
        }
        return new LearningPeriod(usPresent, usPresent ? '+' : '-', presentCues);
    }

    public static HashMap<String, ConditionalStimulus> createCsMap(String[] chars){
        HashMap<String, ConditionalStimulus> map = new HashMap<>();
        for(String c : chars){
            InitialAlphaParameter alphaParam = new InitialAlphaParameter(c);
            alphaParam.setValue(0.5);
            map.put(c, new ConditionalStimulus(c, alphaParam, new SalienceExcitatoryParameter(c), new SalienceInhibitoryParameter(c)));
        }
        return map;
    }

    private GlobalParameterPool getGlobals(){
        GlobalParameterPool globals = new GlobalParameterPool();
        globals.getGamma().setValue(0.1);
        return globals;
    }
}
