package ViewModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class GroupReportViewModel implements Serializable {
    private List<List<Object>> rows;
    private int numberOfColumns;
    public String title;
    public GroupReportViewModel(String title){
        rows = new ArrayList();
        numberOfColumns = 0;
        this.title = title;
    }

    public int getNumberOfRows(){
        return rows.size();
    }

    public int getColumnCount() {
        return numberOfColumns;
    }

    public Object getCell(int row, int col){
        if(row< getNumberOfRows() && col<rows.get(row).size()) {
            return rows.get(row).get(col);
        }
        return "";
    }

    public void setCell(int row, int col, Object content){
        //add blank(s)
        while (row>=rows.size()){
            AddBlankRow();
        }
        while(col>=rows.get(row).size()){
            AddBlankCell(row);
        }
        //set cell
        rows.get(row).set(col, content);

        //set cell type


        //update col count
        if(col>=numberOfColumns){
            numberOfColumns = col+1;
        }
    }

    private void AddBlankCell(int row){
        rows.get(row).add("");
    }

    private void AddBlankRow(){
        rows.add(new ArrayList<>());
    }

}
