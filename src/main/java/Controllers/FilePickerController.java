package Controllers;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by Rokas on 22/12/2015.
 */
public class FilePickerController {
    private JFileChooser picker;
    private JFrame frame;

    private static final String excelExtension = "xlsx";

    public FilePickerController(JFrame frame){
        this.frame = frame;
        picker = new JFileChooser();
    }

    public String pickExcelExportPath(){
        FileFilter filter = new FileNameExtensionFilter(String.format("Spreadsheet (.%s)",  excelExtension), excelExtension);
        picker.setFileFilter(filter);
        String path = pickPath();
        picker.removeChoosableFileFilter(filter);
        if(path!=null && !path.endsWith(".xlsx")){
            path+= ("." + excelExtension);
        }
        return path;
    }

    private String pickPath(){
        if(picker.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
            return picker.getSelectedFile().getPath();
        }
        //cancelled
        return null;
    }
}
