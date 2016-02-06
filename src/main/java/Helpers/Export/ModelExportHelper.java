package Helpers.Export;

import Controllers.FilePickerController;
import Helpers.GuiHelper;
import Models.DTOs.ModelDto;

import java.io.*;

/**
 * Created by Rokas on 04/02/2016.
 */
public class ModelExportHelper{
    public static void exportModel(ModelDto dto){
        try {
            OutputStream outputStream = ExportHelper.getOutputStream(FilePickerController.FileMode.ModelSave);
            exportToStream(dto, outputStream);
        }catch (Exception ex){

        }
    }

    public static ModelDto readModel() throws IOException, ClassNotFoundException {
        InputStream inputStream = ExportHelper.getInputStream(FilePickerController.FileMode.ModelLoad);
        return readFromStream(inputStream);
    }

    private static void exportToStream(ModelDto dto, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(dto);
    }

    private static ModelDto readFromStream(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (ModelDto) objectInputStream.readObject();
    }
}
