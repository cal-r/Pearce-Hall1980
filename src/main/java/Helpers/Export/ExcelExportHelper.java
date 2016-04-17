package Helpers.Export;


import Controllers.FilePickerController;
import ViewModels.GroupReportViewModel;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
            OutputStream outputStream = ExportHelper.getOutputStream(FilePickerController.FileMode.ExcelExport);
            exportToStream(createWorkbook(groupReports), outputStream);
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
                    GroupReportViewModel.Cell vmCell = reportVM.getCell(rowId, colId);
                    if (vmCell.isNumeric()) {
                        cell.setCellValue(vmCell.getDouble());
                    } else {
                        cell.setCellValue(vmCell.toString());
                    }
                    formatCell(workbook, cell, vmCell.getFormat());
                }
            }
        }
        return workbook;
    }

    private static void formatCell(XSSFWorkbook workbook, Cell cell, GroupReportViewModel.Format format){
        XSSFFont font = workbook.createFont();
        font.setBold(format.IsBold);
        font.setItalic(format.IsItalic);
        if(format.IsUnderlined) {
            font.setUnderline(XSSFFont.U_SINGLE);
        }
        font.setFontHeight(format.FontSize);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        cell.setCellStyle(style);
    }
}
