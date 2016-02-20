package Models.Parameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class CsParameterPool implements Serializable {
    Map<String, InitialAlphaParameter> initialAlphaParameterMap;
    Map<String, SalienceExcitatoryParameter> salienceExcitatoryParameterMap;
    Map<String, SalienceInhibitoryParameter> salienceInhibitoryParameterMap;
    public CsParameterPool(){
        initialAlphaParameterMap = new HashMap<>();
        salienceExcitatoryParameterMap = new HashMap<>();
        salienceInhibitoryParameterMap = new HashMap<>();
    }

    public void createParameters(String cueName){
        initialAlphaParameterMap.put(cueName, new InitialAlphaParameter(cueName));
        salienceExcitatoryParameterMap.put(cueName, new SalienceExcitatoryParameter(cueName));
        salienceInhibitoryParameterMap.put(cueName, new SalienceInhibitoryParameter(cueName));
    }

    public boolean contains(String cueName){
        return initialAlphaParameterMap.containsKey(cueName);
    }

    public InitialAlphaParameter getInitialAlpha(String cueName){
        return initialAlphaParameterMap.get(cueName);
    }

    public SalienceExcitatoryParameter getSeParameter(String cueName){
        return salienceExcitatoryParameterMap.get(cueName);
    }

    public SalienceInhibitoryParameter getSiParamter(String cueName){
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
