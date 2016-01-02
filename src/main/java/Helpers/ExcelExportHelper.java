package Helpers;

import Controllers.FilePickerController;
import ViewModels.GroupReportViewModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class ExcelExportHelper {
    public static void exportSimulation(FilePickerController filePickerController, List<GroupReportViewModel> groupReports){
        String path = filePickerController.pickExcelExportPath();
        if(path == null)
            return;

        try {
            File file = new File(path);
            FileOutputStream stream = new FileOutputStream(file);
            ExportToStream(groupReports, stream);
        }catch (Exception ex){

        }
    }

    private static void ExportToStream(List<GroupReportViewModel> groupReports, OutputStream outputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        for(GroupReportViewModel reportVM : groupReports) {
            Sheet sheet = workbook.createSheet(reportVM.title);
            sheet.setColumnWidth(0, 5000);
            for (int rowId = 0; rowId < reportVM.getNumberOfRows(); rowId++) {
                Row excelRow = sheet.createRow(rowId);
                for (int colId = 0; colId < reportVM.getColumnCount(); colId++) {
                    Cell cell = excelRow.createCell(colId);
                    Object content = reportVM.getCell(rowId, colId);
                    if (content instanceof Double) {
                        cell.setCellValue((Double) content);
                    } else {
                        cell.setCellValue(content.toString());
                    }
                }
            }
        }
        workbook.write(outputStream);
    }
}
