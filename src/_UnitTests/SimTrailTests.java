package _UnitTests;

import Models.ConditionalStimulus;
import Models.SimTrail;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 08/11/2015.
 */
public class SimTrailTests extends junit.framework.TestCase {

    @Test
    public void testSimulate() throws Exception {
        HashMap<Character, ConditionalStimulus> allCues = createCsMap("AB".toCharArray());
        SimTrail trail = createTrail(allCues, "A".toCharArray(), true);
        trail.simulate(0);
        assertTrue(allCues.get('A').AssociationExcitatory == 0.025);
        assertTrue(allCues.get('A').AssociationInhibitory == 0);
        assertTrue(allCues.get('A').Alpha == 0.55);

        assertTrue(allCues.get('B').AssociationExcitatory == 0);
        assertTrue(allCues.get('B').AssociationInhibitory == 0);
        assertTrue(allCues.get('B').Alpha == 0.5);
   }

    private SimTrail createTrail(HashMap<Character, ConditionalStimulus> allCues, char[] presentCss, boolean usPresent){
        List<ConditionalStimulus> presentCues = new ArrayList<>();
        for(char c : presentCss) {
            presentCues.add(allCues.get(c));
        }
        return new SimTrail(usPresent, presentCues);
    }

    private HashMap<Character, ConditionalStimulus> createCsMap(char[] chars){
        HashMap<Character, ConditionalStimulus> map = new HashMap<>();
        for(char c : chars){
            map.put(c, new ConditionalStimulus(c));
        }
        return map;
    }
}
