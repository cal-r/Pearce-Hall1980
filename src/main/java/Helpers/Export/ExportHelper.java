package Helpers.Export;

import Controllers.FilePickerController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Rokas on 04/02/2016.
 */
public class ExportHelper {
    public static OutputStream getOutputStream(FilePickerController.FileMode fileMode) throws FileNotFoundException {

        String path = new FilePickerController(fileMode).pickExcelExportPath();

        File file = new File(path);

        return new FileOutputStream(file);
    }
}
