package Controllers;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Created by Rokas on 22/12/2015.
 */
public class FilePickerController {

    public enum FileMode { ExcelExport, ModelSave, ModelLoad }

    private JFileChooser picker;
    private FileMode fileMode;
    private FileType fileType;

    public FilePickerController(FileMode fileMode){
        this.fileMode = fileMode;
        fileType = new FileType(fileMode);
        picker = new JFileChooser();
    }

    public String pickPath(){
        FileFilter filter = new FileNameExtensionFilter(fileType.getFilter(), fileType.getExtension());
        picker.setFileFilter(filter);

        if(showForm() != JFileChooser.APPROVE_OPTION) {
            //cancelled
            return null;
        }

        String path = picker.getSelectedFile().getPath();
        picker.removeChoosableFileFilter(filter);

        return appendExtension(path);
    }

    private int showForm(){
        Frame frame = new Frame();
        if(isSaveMode()) {
            return picker.showSaveDialog(frame);
        }
        return picker.showOpenDialog(frame);
    }

    private boolean isSaveMode(){
        return fileMode == FileMode.ExcelExport || fileMode == FileMode.ModelSave;
    }

    private String appendExtension(String path){
        if(path!=null && !path.endsWith(fileType.getExtension())){
            path+= ("." + fileType.getExtension());
        }
        return path;
    }

    class FileType{
        private final String excelExtension = "xlsx";
        private final String modelExtension = "phs";
        private final String excelFilter = String.format("Spreadsheet (.%s)",  excelExtension);
        private final String modelFilter = String.format("P&H Simulator Files (.%s)",  modelExtension);

        private FileMode fileMode;

        public FileType(FileMode fileMode){
            this.fileMode = fileMode;
        }

        public String getExtension(){
            if(fileMode == FileMode.ExcelExport){
                return excelExtension;
            }
            return modelExtension;
        }

        public String getFilter(){
            if(fileMode == FileMode.ExcelExport){
                return excelFilter;
            }
            return modelFilter;
        }
    }
}
