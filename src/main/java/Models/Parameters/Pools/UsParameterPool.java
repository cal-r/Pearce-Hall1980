package Models.Parameters.Pools;

import Constants.ParameterNamingConstants;
import Models.Group;
import Models.Parameters.UnconditionalStimulus.SingleUsParamater;
import Models.Parameters.UnconditionalStimulus.UsParameter;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;

import java.io.Serializable;
import java.util.*;

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
        String singleLambdaKey = getLambdaKey('+', null);
        usParameterMap.put(singleLambdaKey, new SingleUsParamater(ParameterNamingConstants.LAMBDA));
    }

    public void adjustLamdbas(List<Group> groups){
        //whatever works..
        removeSingle();
        Map<String, List<Integer>> lambdaAvailabilityMap = new HashMap<>();
        int displayId = 0;
        for(Group group : groups){
            for(int phaseId = 0; phaseId < group.groupPhases.size(); phaseId++){
                for(Trial trial : group.groupPhases.get(phaseId).trials){
                    for(LearningPeriod period : trial.getLearningPeriods()){
                        if(period.usPresent){
                            String lambdaKey = getLambdaKey(period.reinforcer, group);
                            if(!usParameterMap.containsKey(lambdaKey)){
                                usParameterMap.put(lambdaKey, new UsParameter(getLambdaName(period.reinforcer), group.Name));
                            }
                            usParameterMap.get(lambdaKey).setDisplayId(displayId++);
                            if(!lambdaAvailabilityMap.containsKey(lambdaKey)){
                                lambdaAvailabilityMap.put(lambdaKey, new ArrayList<Integer>());
                            }
                            lambdaAvailabilityMap.get(lambdaKey).add(phaseId);
                        }
                    }
                }
            }
        }

        List<String> keysToRemove = new ArrayList<>();
        for(String lambdaKey : usParameterMap.keySet()){
            if(lambdaAvailabilityMap.containsKey(lambdaKey)) {
                usParameterMap.get(lambdaKey).adjust(lambdaAvailabilityMap.get(lambdaKey), groups.get(0).groupPhases.size());
            }else {
                keysToRemove.add(lambdaKey);
            }
        }
        for(String key : keysToRemove){
            usParameterMap.remove(key);
        }

        if(usParameterMap.isEmpty())
            addLambdaPlus();
    }

    private void removeSingle() {
        if((getLambda('+', null) instanceof SingleUsParamater)) {
            usParameterMap = new HashMap<>();
        }
    }

    public double getLamdbaValue(char reinforcer, Group group, int phaseId) {
        UsParameter lambda = getLambda(reinforcer, group);

        if(lambda == null)
            return 0;
        return lambda.getValue(phaseId);
    }

    public List<UsParameter> getUsParameters(){
        List<UsParameter> paramsList = new ArrayList<>(usParameterMap.values());
        Collections.sort(paramsList);
        return paramsList;
    }

    public UsParameter getLambda(char usSymbol, Group group){
        String lambdaName = getLambdaKey(usSymbol, group);
        String singleLambdaName = getLambdaKey(usSymbol, null);
        if(usParameterMap.containsKey(lambdaName))
            return usParameterMap.get(lambdaName);

        return usParameterMap.get(singleLambdaName);
    }

    private String getLambdaKey(char us, Group group){
        if(group == null) {
            return String.format("%s %s", ParameterNamingConstants.LAMBDA, us);
        }
        return String.format("%s %s (%s)", ParameterNamingConstants.LAMBDA, us, group.Name);
    }

    private String getLambdaName(char us){
        return getLambdaKey(us, null);
    }

    public void adjustSingleMode() {
        String singleLambdaKey = getLambdaKey('+', null);
        if(!usParameterMap.containsKey(singleLambdaKey)) {
            usParameterMap = new HashMap<>();
            usParameterMap.put(singleLambdaKey, new SingleUsParamater(ParameterNamingConstants.LAMBDA));
        }
    }
}
