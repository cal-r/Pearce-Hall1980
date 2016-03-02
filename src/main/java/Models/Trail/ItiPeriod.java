package Models.Trail;

import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.ContextStimulus;
import Models.Stimulus.Stimulus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 21/02/2016.
 */
public class ItiPeriod extends LearningPeriod {

    private ContextStimulus context;

    public ItiPeriod(ContextStimulus context){
        super(false, makeList(context));
        this.context = context;
    }

    private static List<Stimulus> makeList(ContextStimulus contextStimulus) { //stupid java syntax mad me do this
        List<Stimulus> list = new ArrayList<>();
        list.add(contextStimulus);
        return list;
    }

    public void learn(GlobalParameterPool globalParams) {
        learn(context.getAssociationNet(), globalParams);
    }
}
