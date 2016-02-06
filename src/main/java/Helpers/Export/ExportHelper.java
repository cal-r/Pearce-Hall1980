package Helpers.Export;

import Controllers.FilePickerController;
import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.io.*;

/**
 * Created by Rokas on 04/02/2016.
 */
public class ExportHelper {
    public static OutputStream getOutputStream(FilePickerController.FileMode fileMode) throws FileNotFoundException {

        String path = new FilePickerController(fileMode).pickPath();

        File file = new File(path);

        return new FileOutputStream(file);
    }

    public static InputStream getInputStream(FilePickerController.FileMode fileMode) throws FileNotFoundException {

        String path = new FilePickerController(fileMode).pickPath();

        return new FileInputStream(path);
    }
}
