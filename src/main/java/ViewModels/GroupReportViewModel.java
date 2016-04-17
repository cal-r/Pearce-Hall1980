package ViewModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Rokas on 22/12/2015.
 */
public class GroupReportViewModel implements Serializable {
    private List<List<Cell>> rows;
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

    public Cell getCell(int row, int col){
        if(row< getNumberOfRows() && col<rows.get(row).size()) {
            return rows.get(row).get(col);
        }
        return new Cell();
    }

    public void setCell(int row, int col, Object content){
        setCell(row, col, new Cell(content));
    }

    public void setBoldCell(int row, int col, Object content){
        setCell(row, col, new Cell(content, new Format(Format.DefaultFontSize, true, false, false)));
    }

    public void setLargeCell(int row, int col, Object content){
        setCell(row, col, new Cell(content, new Format(14, false, false, false)));
    }

    public void setBoldUnderlinedCell(int row, int col, Object content){
        setCell(row, col, new Cell(content, new Format(Format.DefaultFontSize, true, true, false)));
    }

    private void setCell(int row, int col, Cell content){
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
        if(col>=numberOfColumns){
            numberOfColumns = col+1;
        }
    }

    private void AddBlankCell(int row){
        rows.get(row).add(new Cell());
    }

    private void AddBlankRow(){
        rows.add(new ArrayList<Cell>());
    }

    public class Cell implements Serializable{
        private Object content;
        private Format format;


        public Cell(Object content, Format format) {
            this.content = content;
            this.format = format;
        }

        public Cell(Object content) {
            this(content, new Format());
        }

        private Cell(){
            this("");
        }

        public boolean isNumeric(){
            return content instanceof Double;
        }

        public double getDouble(){
            return (Double) content;
        }

        public Format getFormat() {
            return format;
        }

        public String toString(){
            return content.toString();
        }
    }

    public static class Format implements Serializable{
        public int FontSize;
        public boolean IsBold;
        public boolean IsUnderlined;
        public boolean IsItalic;

        public Format(){
            FontSize = DefaultFontSize;
            IsBold = false;
            IsItalic = false;
            IsUnderlined = false;
        }

        public Format(int fontSize, boolean isBold, boolean isUnderlined, boolean isItalic) {
            FontSize = fontSize;
            IsBold = isBold;
            IsUnderlined = isUnderlined;
            IsItalic = isItalic;
        }

        public static int DefaultFontSize = 11;
    }
}
