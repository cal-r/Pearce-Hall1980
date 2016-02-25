package com;

import Constants.DefaultValuesConstants;
import Helpers.ListCaster;
import Models.Stimulus.ConditionalStimulus;
import Models.Parameters.InitialAlphaParameter;
import Models.Parameters.SalienceExcitatoryParameter;
import Models.Parameters.SalienceInhibitoryParameter;
import Models.Stimulus.Stimulus;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;
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
        double gamma = 0.1;
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0, gamma);
        assertEquals(0.025, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.55, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
   }

    @Test
    public void testSimulate2() throws Exception {
        double gamma = 0.1;
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0, gamma);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0.025, gamma);
        assertEquals(0.0525, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5925, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    @Test
    public void testSimulate3() throws Exception {
        double gamma = 0.1;
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod learn = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        learn.learn(0, gamma);

        learn = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        learn.learn(0.025, gamma);

        learn = createLearningPeriod(allCues, ListCaster.toStringArray("AB"), true);
        learn.learn(0.0525, gamma);
        assertEquals(0.082125, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.628, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0.025, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.54475, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    @Test
    public void testSimulate4() throws Exception {
        double gamma = 0.1;
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("AB"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0, gamma);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0.025, gamma);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("AB"), true);
        period.learn(0.0525, gamma);

        period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        period.learn(0.107125, gamma);
        assertEquals(0.113525, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.6544875, allCues.get("A").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0.025, allCues.get("B").getAssociationExcitatory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get("B").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.54475, allCues.get("B").getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    @Test
    public void testSimulate200() throws Exception {
        //Ve shouldn't go above 1
        double gamma = 0.1;
        HashMap<String, ConditionalStimulus> allCues = createCsMap(ListCaster.toStringArray("A"));
        LearningPeriod period = createLearningPeriod(allCues, ListCaster.toStringArray("A"), true);
        for(int i=0;i<2000;i++) {
            period.learn(allCues.get("A").getAssociationNet(), gamma);
        }

        assertEquals(1, allCues.get("A").getAssociationExcitatory(), DefaultValuesConstants.ASYMPTOTE_EXCEED_ALLOWANCE);
        assertEquals(0, allCues.get("A").getAssociationInhibitory(), DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(1, allCues.get("A").getAssociationNet(), DefaultValuesConstants.ASYMPTOTE_EXCEED_ALLOWANCE);
    }

    public static LearningPeriod createLearningPeriod(HashMap<String, ConditionalStimulus> allCues, String[] presentCss, boolean usPresent){
        List<Stimulus> presentCues = new ArrayList<>();
        for(String c : presentCss) {
            presentCues.add(allCues.get(c));
        }
        return new LearningPeriod(usPresent, presentCues);
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
}
