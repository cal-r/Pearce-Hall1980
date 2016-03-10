package Models.Parameters.Pools;

import Constants.ParameterNamingConstants;
import Models.Group;
import Models.GroupPhase;
import Models.Parameters.Parameter;
import Models.Parameters.UsParameter;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rokasg on 09/03/2016.
 */
public class UsParameterPool {
    private Map<String, UsParameter> usParameterMap;

    public UsParameterPool(){
        usParameterMap = new HashMap<>();
        addLambdaPlus();
    }

    private void addLambdaPlus(){
        String name = getLambdaName('+');
        usParameterMap.put(name, new UsParameter(name));
    }

    public void adjustLamdbas(List<Group> groups){
        Map<String, List<Integer>> lambdaAvailabilityMap = new HashMap<>();
        for(Group group : groups){
            for(int phaseId = 0; phaseId < group.groupPhases.size(); phaseId++){
                for(Trial trial : group.groupPhases.get(phaseId).trials){
                    for(LearningPeriod period : trial.getLearningPeriods()){
                        if(period.usPresent){
                            String lamdbaName = getLambdaName(period.reinforcer);
                            if(!usParameterMap.containsKey(lamdbaName)){
                                usParameterMap.put(lamdbaName, new UsParameter(getLambdaName(period.reinforcer)));
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

    public List<UsParameter> getUsParameters(){
        return new ArrayList<>(usParameterMap.values());
    }

    public UsParameter getLambda(char usSymbol){
        return usParameterMap.get(getLambdaName(usSymbol));
    }

    private String getLambdaName(char us){
        return String.format("%s %s", ParameterNamingConstants.LAMBDA, us);
    }
}
