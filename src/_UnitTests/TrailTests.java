package _UnitTests;

import Constants.DefaultValuesConstants;
import Models.ConditionalStimulus;
import Models.Trail;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 08/11/2015.
 */
public class TrailTests extends junit.framework.TestCase {

    @Test
    public void testSimulate1() throws Exception {
        double gamma = 0.1;
        HashMap<Character, ConditionalStimulus> allCues = createCsMap("AB".toCharArray());
        Trail trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0, gamma);
        assertEquals(0.025, allCues.get('A').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('A').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.55, allCues.get('A').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0, allCues.get('B').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('B').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5, allCues.get('B').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
   }

    @Test
    public void testSimulate2() throws Exception {
        double gamma = 0.1;
        HashMap<Character, ConditionalStimulus> allCues = createCsMap("AB".toCharArray());
        Trail trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0, gamma);

        trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0.025, gamma);
        assertEquals(0.0525, allCues.get('A').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('A').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5925, allCues.get('A').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0, allCues.get('B').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('B').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.5, allCues.get('B').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    @Test
    public void testSimulate3() throws Exception {
        double gamma = 0.1;
        HashMap<Character, ConditionalStimulus> allCues = createCsMap("AB".toCharArray());
        Trail trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0, gamma);

        trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0.025, gamma);

        trail = createTrail(allCues, "AB".toCharArray(), true);
        trail.simulate(0.0525, gamma);
        assertEquals(0.082125, allCues.get('A').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('A').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.628, allCues.get('A').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0.025, allCues.get('B').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('B').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.54475, allCues.get('B').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    public void testSimulate4() throws Exception {
        double gamma = 0.1;
        HashMap<Character, ConditionalStimulus> allCues = createCsMap("AB".toCharArray());
        Trail trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0, gamma);

        trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0.025, gamma);

        trail = createTrail(allCues, "AB".toCharArray(), true);
        trail.simulate(0.0525, gamma);

        trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0.107125, gamma);
        assertEquals(0.113525, allCues.get('A').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('A').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.6544875, allCues.get('A').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);

        assertEquals(0.025, allCues.get('B').AssociationExcitatory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0, allCues.get('B').AssociationInhibitory, DefaultValuesConstants.ROUNDING_PRECISION);
        assertEquals(0.54475, allCues.get('B').getAlpha(), DefaultValuesConstants.ROUNDING_PRECISION);
    }

    private Trail createTrail(HashMap<Character, ConditionalStimulus> allCues, char[] presentCss, boolean usPresent){
        List<ConditionalStimulus> presentCues = new ArrayList<>();
        for(char c : presentCss) {
            presentCues.add(allCues.get(c));
        }
        return new Trail(usPresent, presentCues);
    }

    private HashMap<Character, ConditionalStimulus> createCsMap(char[] chars){
        HashMap<Character, ConditionalStimulus> map = new HashMap<>();
        for(char c : chars){
            map.put(c, new ConditionalStimulus(c));
        }
        return map;
    }
}
