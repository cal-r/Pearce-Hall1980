package Helpers;

import Models.Parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 26/12/2015.
 */
public class ListCaster{
    //stupid java
    public static <T> List<T> cast(List list){
        return ((List<T>)(List<?>)list);
    }

    public static String[] toStringArray(char[] charArr){
        List<String> list = new ArrayList<>();
        for(char c : charArr){
            list.add(String.valueOf(c));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] toStringArray(String str) {
        return toStringArray(str.toCharArray());
    }
}
