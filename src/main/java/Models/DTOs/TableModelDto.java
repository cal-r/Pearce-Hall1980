package Models.DTOs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 27/03/2016.
 */
public class TableModelDto implements Serializable{
    public List<String> headers;
    public List<List<Object>> data;
    public TableModelDto(List<String> headers, List<List<Object>> data){
        this.headers = headers;
        this.data = data;
    }
}
