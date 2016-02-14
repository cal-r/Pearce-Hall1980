package Helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Rokas on 14/02/2016.
 */
public class StimulusOrderingHelper {
    public static Collection<String> orderStimNamesByDescription(Collection<String> stimNames, String description){
        List<String> stimNamesList = new ArrayList<>(stimNames);
        Collection<String> orderedNames = new ArrayList();
        for(int i=0;i<description.length();i++){
            for(int j=0;j<stimNamesList.size();j++){
                if(description.substring(i,i+1).equals(stimNamesList.get(j))){
                    orderedNames.add(stimNamesList.get(j));
                    stimNamesList.remove(j);
                }
            }
        }
        orderedNames.addAll(stimNamesList);
        return orderedNames;
    }
}
