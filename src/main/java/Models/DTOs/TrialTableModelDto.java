package Models.DTOs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 27/03/2016.
 */
public class TrialTableModelDto extends TableModelDto implements Serializable {
    public boolean simContext;

    public TrialTableModelDto(List<String> headers, List<List<Object>> data, boolean simContext) {
        super(headers, data);
        this.simContext = simContext;
    }
}
