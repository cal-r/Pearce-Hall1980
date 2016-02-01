package Helpers.Random;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Rokas on 24/12/2015.
 */
public class RandomArrayGenerator {
    public static int[] createRandomDistinctArray(int size) {
        Random random = new Random(System.nanoTime());
        HashMap<Integer, Boolean> usedValuesMap = new HashMap<>(size);
        int[] array = new int[size];
        int arrayIndex = 0;
        while (usedValuesMap.size() < size) {
            int newInt = random.nextInt(size);
            if (!usedValuesMap.containsKey(newInt)) {
                usedValuesMap.put(newInt, true);
                array[arrayIndex] = newInt;
                arrayIndex++;
            }
        }
        return array;
    }
}
