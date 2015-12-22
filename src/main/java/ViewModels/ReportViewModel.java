package ViewModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class ReportViewModel {
    private List<List<String>> rows;
    private int numberOfColumns;
    public ReportViewModel(){
        rows = new ArrayList();
        numberOfColumns = 0;
    }

    public int getNumberOfRows(){
        return rows.size();
    }

    public int getColumnCount() {
        return numberOfColumns;
    }

    public String getCell(int row, int col){
        if(row< getNumberOfRows() && col<rows.get(row).size()) {
            return rows.get(row).get(col);
        }
        return "";
    }

    public void setCell(int row, int col, String content){
        //add blank(s)
        while (row>=rows.size()){
            AddBlankRow();
        }
        while(col>=rows.get(row).size()){
            AddBlankCell(row);
        }
        //set cell
        rows.get(row).set(col, content);
        //update col count
        if(col>numberOfColumns){
            numberOfColumns = col;
        }
    }

    private void AddBlankCell(int row){
        rows.get(row).add("");
    }

    private void AddBlankRow(){
        rows.add(new ArrayList<String>());
    }
}
