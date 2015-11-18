package Models.Parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class CsParameterPool {
    Map<Character, InitialAlphaParameter> initialAlphaParameterMap;
    Map<Character, SalienceExcitatoryParameter> salienceExcitatoryParameterMap;
    Map<Character, SalienceInhibitoryParameter> salienceInhibitoryParameterMap;
    public CsParameterPool(){
        initialAlphaParameterMap = new HashMap<>();
        salienceExcitatoryParameterMap = new HashMap<>();
        salienceInhibitoryParameterMap = new HashMap<>();
    }

    public void createParameters(char cueName){
        initialAlphaParameterMap.put(cueName, new InitialAlphaParameter(cueName));
        salienceExcitatoryParameterMap.put(cueName, new SalienceExcitatoryParameter(cueName));
        salienceInhibitoryParameterMap.put(cueName, new SalienceInhibitoryParameter(cueName));
    }

    public boolean contains(char cueName){
        return initialAlphaParameterMap.containsKey(cueName);
    }

    public InitialAlphaParameter getInitialAlpha(char cueName){
        return initialAlphaParameterMap.get(cueName);
    }

    public SalienceExcitatoryParameter getSeParameter(char cueName){
        return salienceExcitatoryParameterMap.get(cueName);
    }

    public SalienceInhibitoryParameter getSiParamter(char cueName){
        return salienceInhibitoryParameterMap.get(cueName);
    }

    public List<CsParameter> getAllParameters(){
        List<CsParameter> list = new ArrayList<>();
        list.addAll(initialAlphaParameterMap.values());
        list.addAll(salienceExcitatoryParameterMap.values());
        list.addAll(salienceInhibitoryParameterMap.values());
        return list;
    }
}
