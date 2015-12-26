package Helpers;

import Models.Parameters.Parameter;

import java.util.List;

/**
 * Created by Rokas on 26/12/2015.
 */
public class ListCaster{
    //stupid java
    public static <T> List<T> cast(List list){
        return ((List<T>)(List<?>)list);
    }
}
