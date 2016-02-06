package Helpers.Export;


import Controllers.FilePickerController;
import ViewModels.GroupReportViewModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class ExcelExportHelper {
    public static void exportSimulation(List<GroupReportViewModel> groupReports){
        try{
            OutputStream stream = ExportHelper.getOutputStream(FilePickerController.FileMode.ExcelExport);
            exportToStream(createWorkbook(groupReports), stream);
        }catch (Exception ex){

        }
    }

    private static void exportToStream(XSSFWorkbook workbook, OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
    }

    private static XSSFWorkbook createWorkbook(List<GroupReportViewModel> groupReports){
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
        return workbook;
    }
}
