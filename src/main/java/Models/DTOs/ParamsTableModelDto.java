package Models.DTOs;

import Models.Parameters.Parameter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 27/03/2016.
 */
public class ParamsTableModelDto extends TableModelDto implements Serializable {
    public List<Parameter> parameters;
    public ParamsTableModelDto(List<String> headers, List<List<Object>> data, List<Parameter> parameters) {
        super(headers, data);
        this.parameters = parameters;
    }
}
