package Helpers.Export;

import Controllers.FilePickerController;
import Models.DTOs.ModelDto;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Rokas on 04/02/2016.
 */
public class ModelExportHelper{
    public static void ExportModel(ModelDto dto){
        try {
            OutputStream outputStream = ExportHelper.getOutputStream(FilePickerController.FileMode.ModelSave);
            exportToStream(dto, outputStream);
        }catch (Exception ex){

        }
    }

    private static void exportToStream(ModelDto dto, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(dto);
    }
}
