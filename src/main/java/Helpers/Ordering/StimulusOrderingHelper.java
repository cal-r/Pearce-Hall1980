package Helpers.Ordering;

import Helpers.MultipleUsLabelingHelper;

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
                if(description.substring(i,i+1).equals(MultipleUsLabelingHelper.getOriginalName(stimNamesList.get(j)))){
                    orderedNames.add(stimNamesList.get(j));
                    stimNamesList.remove(j);
                }
            }
        }

        //order stims with negative labels
        for(int i=0;i<description.length();i++){
            for(int j=0;j<stimNamesList.size();j++){
                if(description.substring(i,i+1).equals(
                        MultipleUsLabelingHelper.getOriginalName(
                                MultipleUsLabelingHelper.getNameWithoutNegativeLabeling(stimNamesList.get(j))))){
                    orderedNames.add(stimNamesList.get(j));
                    stimNamesList.remove(j);
                }
            }
        }

        //order remainder by length
        int len = 0;
        List<String> stimNamesRemainderList = new ArrayList<>(stimNamesList);
        while(!stimNamesRemainderList.isEmpty()){
            for(String name : stimNamesList){
                if(name.length()==len) {
                    orderedNames.add(name);
                    stimNamesRemainderList.remove(name);
                }
            }
            len++;
        }

        return orderedNames;
    }
}
