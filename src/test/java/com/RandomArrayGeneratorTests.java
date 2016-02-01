package com;

import Helpers.Random.RandomArrayGenerator;
import org.junit.Test;

/**
 * Created by Rokas on 24/12/2015.
 */
public class RandomArrayGeneratorTests extends junit.framework.TestCase {

    @Test
    public void testRandomArray1() throws Exception {
        int[] array = RandomArrayGenerator.createRandomDistinctArray(3);
        assertTrue(array.length == 3);
        for(int i=0;i<3;i++){
            assertTrue(indexOf(array, i) > -1);
        }
    }

    @Test
    public void testRandomArray2() throws Exception {
        int[] array = RandomArrayGenerator.createRandomDistinctArray(1);
        assertTrue(array.length == 1);
        assertTrue(indexOf(array, 0) == 0);
    }

    public void testRandomArray3() throws Exception {
        int[] array1 = RandomArrayGenerator.createRandomDistinctArray(40);
        int[] array2 = RandomArrayGenerator.createRandomDistinctArray(40);
        assertTrue(array1.length == 40);
        assertTrue(array2.length == 40);

        //check randomnes
        assertFalse(array1[0] == array2[0] && array1[20] == array2[20]);
    }

    private int indexOf(int[] arr, int num){
        for(int i=0;i<arr.length;i++){
            if(arr[i] == num){
                return i;
            }
        }
        return -1;
    }
}
