package Helpers;

import Models.Stimulus.IStimulus;

import java.util.List;

/**
 * Created by Rokas on 29/04/2016.
 */
public class MultipleUsLabelingHelper {

    //(A+)- => (A+)
    public static String getNameWithoutNegativeLabeling(String name){
        if (name.endsWith("-")){
            return name.substring(0, name.length()-1);
        }
        return name;
    }

    //(A+) => (A+)-
    public static String getNegativeLabel(String name){
        return String.format("%s-", name);
    }

    // { (A+), (B+) } => (AB+)
    public static String getCompoundName(List<String> compoundedNames){
        String name = "";
        Character reinforcer = null;
        for(String stimNameWithReinforcer : compoundedNames){
            String stimNameWithoutReinforcer = stimNameWithReinforcer.substring(1, stimNameWithReinforcer.length()-2);
            reinforcer = stimNameWithReinforcer.charAt(stimNameWithReinforcer.length()-2);
            name += stimNameWithoutReinforcer;
        }
        return String.format("(%s%s)", name, reinforcer);
    }

    // A, + => (A+)
    public static String getCueName(String originalName, char us){
        return String.format("(%s%s)", originalName, us);
    }

    public static String getOriginalName(String label){
        if(label.endsWith(")")){
            return label.substring(1, label.length()-2);
        }
        return label;
    }
}
