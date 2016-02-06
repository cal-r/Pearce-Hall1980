package Models.Parameters;

import Helpers.DefaultValuesHelper;

import java.io.Serializable;

/**
 * Created by Rokas on 07/11/2015.
 */
public abstract class Parameter implements Serializable {
    protected double value;
    protected boolean isSet;
    protected String name;

    protected Parameter(String name){
        this.name = name;
        isSet = false;
    }

    public double getValue() {
        if (isSet) {
            return value;
        }
        return DefaultValuesHelper.GetDefaultValue(name);
    }

    public void setValue(double value){
        this.value = value;
        isSet = true;
    }

    public String getDisplayName(){
        return name;
    }
}
