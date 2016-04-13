package Models.Parameters.Pools;

import Constants.ParameterNamingConstants;
import Models.Group;
import Models.Parameters.UnconditionalStimulus.SingleUsParamater;
import Models.Parameters.UnconditionalStimulus.UsParameter;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rokasg on 09/03/2016.
 */
public class UsParameterPool implements Serializable {
    private Map<String, UsParameter> usParameterMap;

    public UsParameterPool(){
        usParameterMap = new HashMap<>();
        addLambdaPlus();
    }

    private void addLambdaPlus(){
        String name = getLambdaName('+', null);
        usParameterMap.put(name, new SingleUsParamater(name));
    }

    public void adjustLamdbas(List<Group> groups){
        removeSingle();
        Map<String, List<Integer>> lambdaAvailabilityMap = new HashMap<>();
        for(Group group : groups){
            for(int phaseId = 0; phaseId < group.groupPhases.size(); phaseId++){
                for(Trial trial : group.groupPhases.get(phaseId).trials){
                    for(LearningPeriod period : trial.getLearningPeriods()){
                        if(period.usPresent){
                            String lamdbaName = getLambdaName(period.reinforcer, group);
                            if(!usParameterMap.containsKey(lamdbaName)){
                                usParameterMap.put(lamdbaName, new UsParameter(lamdbaName));
                            }
                            if(!lambdaAvailabilityMap.containsKey(lamdbaName)){
                                lambdaAvailabilityMap.put(lamdbaName, new ArrayList<Integer>());
                            }
                            lambdaAvailabilityMap.get(lamdbaName).add(phaseId);
                        }
                    }
                }
            }
        }

        List<String> keysToRemove = new ArrayList<>();
        for(UsParameter usParameter : usParameterMap.values()){
            if(lambdaAvailabilityMap.containsKey(usParameter.getDisplayName())) {
                usParameter.adjust(lambdaAvailabilityMap.get(usParameter.getDisplayName()), groups.get(0).groupPhases.size());
            }else {
                keysToRemove.add(usParameter.getDisplayName());
            }
        }
        for(String key : keysToRemove){
            usParameterMap.remove(key);
        }
    }

    private void removeSingle() {
        if((getLambda('+', null) instanceof SingleUsParamater)) {
            usParameterMap = new HashMap<>();
        }
    }

    public double getLamdbaValue(char reinforcer, Group group, int phaseId) {
        return getLambda(reinforcer, group).getValue(phaseId);
    }

    public List<UsParameter> getUsParameters(){
        return new ArrayList<>(usParameterMap.values());
    }

    public UsParameter getLambda(char usSymbol, Group group){
        return usParameterMap.get(getLambdaName(usSymbol, group));
    }

    private String getLambdaName(char us, Group group){
        if(group == null) {
            return String.format("%s %s", ParameterNamingConstants.LAMBDA, us);
        }
        return String.format("%s %s (%s)", ParameterNamingConstants.LAMBDA, us, group.Name);
    }

    private List<Character> getUsSymbols(){
        List<Character> usSymbols = new ArrayList<>();
        usSymbols.add('+');
        usSymbols.add('#');
        usSymbols.add('*');
        usSymbols.add('$');
        return usSymbols;
    }

    public void adjustSingleMode() {
        String lambdaPlusName = getLambdaName('+', null);
        if(!usParameterMap.containsKey(lambdaPlusName)) {
            usParameterMap = new HashMap<>();
            usParameterMap.put(lambdaPlusName, new SingleUsParamater(lambdaPlusName));
        }
    }

    public static double lambdaIfPositive(char reinforcer, double val){
        return reinforcer == '-' ? 0.0 : val;
    }
}
