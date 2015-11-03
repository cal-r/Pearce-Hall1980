/**
 * SimExport.java
 * 
 * The Centre for Computational and Animal Learning Research (CAL-R)
* @title Rescorla & Wagner Model Simulator
* @author Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray

* Preliminary version 10-Mar-2005 Esther Mondragón, Eduardo Alonso & Dioysios Skordoulis.
* Modified October 2009  Esther Mondragón, Eduardo Alonso & Rocío García Durán.
* Modified July 2011 Esther Mondragón, Eduardo Alonso & Alberto Fernández.
* Modified October 2012 Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray.
 *
 */
package simulator;

import java.io.*;

// Alberto Fernandez Oct-2011
// change from HSSF (xls) to XSSF (xlsx) to solve the limitation of more that 255 columns
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.util.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress; // Alberto Fernandez Oct-2011
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.util.*;
import java.util.*;

// AF July-2012
import org.apache.poi.ss.usermodel.Sheet;

import simulator.configurables.ContextConfig.Context;
import simulator.util.ExampleFileFilter;



import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * Exports the results from the simulator to a spreadsheet. It uses the HSSF
 * free library provided from apache jakarta project. The HSSF allows numeric, 
 * string, date or formula cell values to be written to or read from an XLS file. Also
 * we can do row and column sizing, cell styling (bold, italics, borders,etc), and 
 * support for both built-in and user defined data formats. It creates a workbook
 * that has a different sheet for every model's group. Every phase is represented
 * with a different table.
 */
//public class SimExport {
public class SimExport implements Runnable {

//    private SimView view;
//    private SimModel model;
//    private TreeMap groups; 
//    private XSSFFont groupFont, titleFont, tableTopFont, tableContFont;
//    private XSSFWorkbook wb;
//    private XSSFRow row;
//    private XSSFCell cell;
//    private XSSFCellStyle cs1, cs2, cs3, cs4;
 
    // AF Aug-2012 from TD

    private Map<String, SimGroup> groups; 
    private Font groupFont, titleFont, tableTopFont, tableContFont;
    private Workbook wb;
    private Row row;
    private Cell cell;
    private CellStyle cs1,cs2,cs3,cs4;
    private final SimModel model;
    private SimView view;
    private String name;
	private File file;
	private boolean success;
	private ModelControl control;

    
    
    /**
     * SimExport's Constructor method.
     * @param view the application's view.
     * @param model the current model where that values will come from.
     * @param directory the last chosen directory
     */
    public SimExport(SimView view, SimModel model, String name, File file) {
    	this.view = view;
        this.model = model;
        this.file = file;
        this.name = name;
        success = true;
    }


    public void doExport() throws IOException {
        row = null;
        cell = null;
    	wb = new SXSSFWorkbook();
    	createStyles();        
    	FileOutputStream fileOut = new FileOutputStream(file);
                
    	groups = model.getGroups();
        Set<String> setGroups = groups.keySet();
        Iterator<String> iterGroups = setGroups.iterator();
        
        
        //Generate iteration between groups.
        while(iterGroups.hasNext() && !control.isCancelled()) {
        	//long estimatedCycle = System.currentTimeMillis();
        	exportGroup(iterGroups.next(), name);
        	//control.incrementProgress(1);
        	//control.setEstimatedCycleTime(System.currentTimeMillis()-estimatedCycle);
        }
        if(!control.isCancelled()) {
        	wb.write(fileOut);
        }
        fileOut.close();
        control.setProgress(100);
    }
    
                    	
    private void exportGroup(String groupName, String name) {
    	int rowPos = 0;
    	int colPos = 0;
                        
//    	SimGroup group = (SimGroup) groups.get(iterGroups.next());              
//    	XSSFSheet sheet = wb.createSheet(group.getNameOfGroup());
    	final SimGroup group = (SimGroup) groups.get(groupName);
        final Sheet sheet = wb.createSheet(group.getNameOfGroup());
        
        // createStyles(); // already in doExport()
        
    	// Modified by Alberto Fernández July-2011
       
        // Title = file name
                       	
        row = sheet.createRow(rowPos);
        cell = row.createCell(colPos);
        
        // Alberto Fernandez Oct-2011
        // sheet.addMergedRegion(new Region(rowPos, colPos, rowPos, (short)(colPos + 3)));
        // Deprecated: Region(int rowFrom, short colFrom, int rowTo, short colTo) 
        // Current:    CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) 
        
        sheet.addMergedRegion(new CellRangeAddress(rowPos, rowPos, colPos, (colPos + 3)));
        
        
        cell.setCellValue(name); // write file name
        cell.setCellStyle(cs1);

       
    	// Show Parameters

       // CS model

    	rowPos += 2;
    	
    	row = sheet.createRow(rowPos);
    	cell = row.createCell(colPos);
    	cell.setCellValue("CS alpha:");
    	cell.setCellStyle(cs2);
    	//sheet.addMergedRegion(new Region(rowPos, (short)(colPos), rowPos, (short)(colPos + 1)));
    	
    	rowPos++;
    	
        AbstractTableModel cstmv = view.getCSValuesTableModel();
        for (int r = 0; r < cstmv.getRowCount(); r++){
        	//if (group.getCuesMap().containsKey(cstmv.getValueAt(r,0))){
        	if (group.getCuesMap().containsKey(model.interfaceName2cueName((String)cstmv.getValueAt(r,0)))){
           		double value = new Double((String) cstmv.getValueAt(r, 1));
            	row = sheet.createRow(rowPos);
            	cell = row.createCell(colPos);
            	cell.setCellValue((String)cstmv.getValueAt(r,0));
            	cell.setCellStyle(cs3);
            	cell = row.createCell((colPos + 1));
            	cell.setCellValue(value);
            	cell.setCellStyle(cs4);
            	rowPos++;
        	}
        }

        // US model

    	rowPos += 2;
    	
    	row = sheet.createRow(rowPos);
    	cell = row.createCell(colPos);
    	cell.setCellValue("US: ");
    	cell.setCellStyle(cs2);
    	//sheet.addMergedRegion(new Region(rowPos, (short)(colPos), rowPos, (short)(colPos + 1)));
    	
    	rowPos++;
    	
	    AbstractTableModel ustmv = view.getUSValuesTableModel();
        for (int r = 0; r < ustmv.getRowCount(); r++){
        	Object ovalue = ustmv.getValueAt(r, 1);
        	row = sheet.createRow(rowPos);
        	cell = row.createCell(colPos);
        	cell.setCellValue((String)ustmv.getValueAt(r,0));
        	cell.setCellStyle(cs3);
        	//cell = row.createCell((short)(colPos + 1));
        	cell = row.createCell((colPos + 1));
        	if (!((String) ovalue).equals("")){
           		double value = new Double((String) ovalue);
            	cell.setCellValue(value);		                		
        	}
        	cell.setCellStyle(cs4);
        	rowPos++;
        }

        /* This is another version of showing the CS and US tables
    	SortedMap<String,Double> variables = model.getValues();
        Set<String> keys = variables.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
        	String element = (String)it.next(); // variable name
        	if (variables.get(element) != null){
        		double value = (Double) variables.get(element);
            	row = sheet.createRow(rowPos);
            	cell = row.createCell(colPos);
            	cell.setCellValue(element);
            	cell.setCellStyle(cs3);
            	cell = row.createCell((short)(colPos + 1));
            	cell.setCellValue(value);
            	cell.setCellStyle(cs4);
            	rowPos++;
        	}
        }
        */

    	// End modification
        
        
        // Alberto Fernandez. July-2012
        // From TD

        if(model.isUseContext()) {
        	
            // Context alphas

        	rowPos += 2;
        	
        	row = sheet.createRow(rowPos);
        	cell = row.createCell(colPos);
        	cell.setCellValue("Context Alphas: ");
        	cell.setCellStyle(cs2);
        	//sheet.addMergedRegion(new Region(rowPos, (short)(colPos), rowPos, (short)(colPos + 1)));
        	
        	rowPos++;
        	row = sheet.createRow(rowPos);
        	for(int c = 1; c < group.getNoOfPhases() + 1; c++) {
        		cell = row.createCell(colPos+c);
        		cell.setCellValue("Phase "+c);
        		cell.setCellStyle(cs3);
        	}
        	rowPos++;
        	for(String cue : group.getCuesMap().keySet()) {
        		if(Context.isContext(cue)) {
        			row = sheet.createRow(rowPos);
        			cell = row.createCell(colPos);
        			cell.setCellValue(cue);		                		
        			cell.setCellStyle(cs3);
        			for(int c = 1; c < group.getNoOfPhases() + 1; c++) {
        				cell = row.createCell(colPos+c);
        				try {
        					double alpha = ((SimPhase)group.getPhases().get(c-1)).isCueInStimuli(cue) ? ((SimPhase)group.getPhases().get(c-1)).getResults().get(cue).getAlpha() : null;
        					cell.setCellValue(alpha);
        				} catch (NullPointerException e) {
        					//Ignore that, this context isn't here now.
        				}
                		cell.setCellStyle(cs4);
        			}
        			rowPos++;
        		}
        	}
        	
        	// ITI/CS ratio
        	
        	rowPos += 2;
        	
        	row = sheet.createRow(rowPos);
        	cell = row.createCell(colPos);
        	cell.setCellValue("ITI/CS ratio: ");
        	cell.setCellStyle(cs2);
        	//sheet.addMergedRegion(new Region(rowPos, (short)(colPos), rowPos, (short)(colPos + 1)));
        	
        	rowPos++;
        	row = sheet.createRow(rowPos);
        	for(int c = 1; c < group.getNoOfPhases() + 1; c++) {
        		cell = row.createCell(colPos+c);
        		cell.setCellValue("Phase "+c);
        		cell.setCellStyle(cs3);
        	}
        	rowPos++;
        	
        	row = sheet.createRow(rowPos);
        	for(int c = 1; c < group.getNoOfPhases() + 1; c++) {
        		cell = row.createCell(colPos+c);
				try {
					cell.setCellValue(view.getPhasesTableModel().getCS_ITI_ratio(group, c));
				} catch (NullPointerException e) {
					//Ignore that, this context isn't here now.
				}
                cell.setCellStyle(cs4);
        	}		                	
        	
        }
        
    	rowPos += 2;


        // Create group title on top of the page
        row = sheet.createRow(rowPos);
        cell = row.createCell(colPos);
        
        //sheet.addMergedRegion(new Region(rowPos, colPos, rowPos, (short)(colPos + 3)));
        sheet.addMergedRegion(new CellRangeAddress(rowPos, rowPos, colPos, (short)(colPos + 3)));
        
        
        cell.setCellValue(group.getNameOfGroup());
        cell.setCellStyle(cs1);
        
        // Generate iteration between phases
        for (int i = 0; i < group.getPhases().size(); i++) {
            rowPos += 2;
            SimPhase curPhase = (SimPhase) group.getPhases().get(i);
            
         	row = sheet.createRow(rowPos);
         	cell = row.createCell(colPos);
         	cell.setCellValue("Phase " + (i+1));
         	cell.setCellStyle(cs2);
         	// sheet.addMergedRegion(new Region(rowPos, (short)(colPos), rowPos, (short)(colPos + 1)));
         	sheet.addMergedRegion(new CellRangeAddress(rowPos, rowPos,  (short)colPos,  (short)(colPos + 1)));
         	
         	cell = row.createCell((colPos + 2));
         	cell.setCellValue("Random : " + curPhase.isRandom()); 
         	cell.setCellStyle(cs2);
         	sheet.addMergedRegion(new CellRangeAddress(rowPos, rowPos, (short)(colPos + 2), (short)(colPos + 3)));

         	cell = row.createCell((colPos + 4));
         	cell.setCellValue("Sequence : " + curPhase.intialSequence());
         	cell.setCellStyle(cs2);
         	sheet.addMergedRegion(new CellRangeAddress(rowPos, rowPos, (short)(colPos + 4), (short)(colPos + 10)));                   		

         	rowPos += 2;
         	
         	TreeMap results = (TreeMap)curPhase.getResults();
         	Object cueNames[] = results.keySet().toArray();
          	
         	// max = maximum number of trial of all the cues in the phase
                         	
         	int max = 0;
         	for (int j=0; j<cueNames.length; j++){
         		SimCue curcue = (SimCue)results.get(cueNames[j]);
         		if (curcue.getAssocValueSize()>max) max = curcue.getAssocValueSize();
         	}
         	if (max>0) max = max-1;
 
            // Alberto Fernandez August-2011: export (1) cues, (2) compounds, (3) configural cues

         	// first row: Trial names                         	
            row = sheet.createRow(rowPos);
    		for (int x = 1; x <= max; x++) {
 	            cell = row.createCell(colPos + x);
 	            cell.setCellValue("Trial " + x);
 	            cell.setCellStyle(cs3);
 	      	}
    		
         	rowPos++;

    		// export Cues
         	for (int y = 1; y < cueNames.length + 1; y++) {
            	String cueName = (String)cueNames[y-1];
            	String interfaceName;
         		if (cueName.length() == 1 && Character.isUpperCase(cueName.charAt(cueName.length()-1))) {
            		interfaceName = (String)cueNames[y-1]; // no change
                	SimCue curcue = (SimCue)results.get(cueNames[y-1]);
                	
                	if (curcue.getAssocValueSize() > 1) { // AF Sep-2012
         	   			row = sheet.createRow(rowPos); // Alberto Fernandez Oct-2011
         	   			
         	   			// AF Aug-2012. This loop takes time
         	   			
//         	        	long initialCycleTime = System.currentTimeMillis();

             	   		for (int x = 0; x <= curcue.getAssocValueSize()-1; x++){ 
             	   			//row = sheet.createRow(rowPos);
             	   	        if (x == 0) {
             	   	            cell = row.createCell((colPos + x));
             	   	        	//cell.setCellValue((String)cueNames[y-1]);
                        		cell.setCellValue(interfaceName);
             	   	        	cell.setCellStyle(cs3);
             	   	        } else {
             	   	        	//String res = ((SimCue)results.get((String)cueNames[y-1])).getAssocValueAt(x).toString();
             	   	            cell = row.createCell((colPos + x));
             	   	            //cell.setCellValue(res.length() > 10 ? res.substring(0, 10) : res);
             	   	            cell.setCellValue(((SimCue)results.get((String)cueNames[y-1])).getAssocValueAt(x));
             	   	            cell.setCellStyle(cs4);
             	   	            sheet.setColumnWidth(x, (short) ((50 * 4) / ((double) 1 / 20)));

             	   	            // AF. Aug.2012. increment progress
                 	        	control.incrementProgress(1);
                 	        	// AF Sep-2012. Exit if export was canceled
                 	        	if (control.isCancelled()) {
                 	        		return;
                 	        	}
             	   	        }
             	   	    }
             	   		rowPos++;
                	}

//     	        	long currentTime = System.currentTimeMillis();
//     	        	// estimated time per 1000 units
//         	   		control.setEstimatedCycleTime((currentTime-initialCycleTime)/curcue.getAssocValueSize()*1000);

         		}
         	}
    		// export Compounds (or configural compounds)
         	for (int y = 1; y < cueNames.length + 1; y++) {
            	String cueName = (String)cueNames[y-1];
            	String interfaceName;
         		if (cueName.length() > 1) {
         			if (Character.isUpperCase(cueName.charAt(cueName.length()-1))) { //compound
                		interfaceName = (String)cueNames[y-1]; // no change
         			}
         			else { // configural compound
                   		String compoundName = cueName.substring(0,cueName.length()-1);
   						//interfaceName = "[" + compoundName + "¢]";
   						interfaceName = "[" + compoundName + "]";
         			}               						
                	SimCue curcue = (SimCue)results.get(cueNames[y-1]);
                	
                	if (curcue.getAssocValueSize() > 1) { // AF Sep-2012
                		row = sheet.createRow(rowPos);
                		for (int x = 0; x <= curcue.getAssocValueSize()-1; x++){ 
                			//row = sheet.createRow(rowPos);
                			if (x == 0) {
                				cell = row.createCell(colPos + x);
                				cell.setCellValue(interfaceName);
                				cell.setCellStyle(cs3);
                			} else {
                				cell = row.createCell(colPos + x);
                				cell.setCellValue(((SimCue)results.get((String)cueNames[y-1])).getAssocValueAt(x));
                				cell.setCellStyle(cs4);
                				sheet.setColumnWidth(x, (short) ((50 * 4) / ((double) 1 / 20)));

                				// AF. Aug.2012. increment progress
                				control.incrementProgress(1);
                			}
                		}
                		rowPos++;
                	}
         		}
         	}
     		
    		// export configural cues (if any)
        	//rowPos++;

         	for (int y = 1; y < cueNames.length + 1; y++) {
            	String cueName = (String)cueNames[y-1];
            	String interfaceName;
         		if (cueName.length() == 1 && Character.isLowerCase(cueName.charAt(cueName.length()-1))) {
         			// configural cue
            		String compoundName;
           			// retrieve compound name
               		compoundName = model.getConfigCuesNames().get(cueName);
					//interfaceName = "¢(" + compoundName + ")";
					interfaceName = "c(" + compoundName + ")";
					
                	SimCue curcue = (SimCue)results.get(cueNames[y-1]);
                	
                	if (curcue.getAssocValueSize() > 1) { // AF Sep-2012
                		row = sheet.createRow(rowPos);
                		for (int x = 0; x <= curcue.getAssocValueSize()-1; x++){ 
                			//row = sheet.createRow(rowPos);
                			if (x == 0) {
                				cell = row.createCell((colPos + x));
                				cell.setCellValue(interfaceName);
                				cell.setCellStyle(cs3);
                			} else {
                				cell = row.createCell((colPos + x));
                				cell.setCellValue(((SimCue)results.get((String)cueNames[y-1])).getAssocValueAt(x));
                				cell.setCellStyle(cs4);
                				sheet.setColumnWidth(x, (short) ((50 * 4) / ((double) 1 / 20)));

                				// AF. Aug.2012. increment progress
                				control.incrementProgress(1);
                			}
                		}
                		rowPos++;
                	}
         		}
         	}
         	
         	// export probe stimulus
         	
        	if (!curPhase.getResultsProbe().isEmpty()) {
            	rowPos++;
        		row = sheet.createRow(rowPos);
        		cell = row.createCell(colPos);
        		cell.setCellValue("PROBE STIMULUS: ");
        		cell.setCellStyle(cs2);

        		rowPos++;

        		//System.out.println("row = " + rowPosI + "col = " + colPosI);
        		rowPos = exportCues(curPhase.getResultsProbe(), sheet, rowPos, colPos);
        		//System.out.println("row = " + rowPosI + "col = " + colPosI);
        	}
        }
    }


    private int exportCues(TreeMap<String, SimCue> results, Sheet sheet, Integer rowPos, Integer colPos) {
     	Object cueNames[] = results.keySet().toArray();

     	for (int y = 1; y < cueNames.length + 1; y++) {
        	String cueName = (String)cueNames[y-1];
        	String interfaceName;
     		if ((cueName.length() == 1 && Character.isUpperCase(cueName.charAt(cueName.length()-1)) ||
     		    cueName.contains("^"))) {
        		interfaceName = (String)cueNames[y-1]; // no change
            	SimCue curcue = (SimCue)results.get(cueNames[y-1]);
            	
            	if (curcue.getAssocValueSize() > 1) { // AF Sep-2012
            		row = sheet.createRow(rowPos); // Alberto Fernandez Oct-2011

            		// AF Aug-2012. This loop takes time

            		// 	        	long initialCycleTime = System.currentTimeMillis();

            		for (int x = 0; x <= curcue.getAssocValueSize()-1; x++){ 
            			//row = sheet.createRow(rowPos);
            			if (x == 0) {
            				cell = row.createCell((colPos + x));
            				//cell.setCellValue((String)cueNames[y-1]);
            				cell.setCellValue(interfaceName);
            				cell.setCellStyle(cs3);
            			} else {
            				//String res = ((SimCue)results.get((String)cueNames[y-1])).getAssocValueAt(x).toString();
            				cell = row.createCell((colPos + x));
            				//cell.setCellValue(res.length() > 10 ? res.substring(0, 10) : res);
            				cell.setCellValue(((SimCue)results.get((String)cueNames[y-1])).getAssocValueAt(x));
            				cell.setCellStyle(cs4);
            				sheet.setColumnWidth(x, (short) ((50 * 4) / ((double) 1 / 20)));

            				// AF. Aug.2012. increment progress
            				control.incrementProgress(1);
            				// AF Sep-2012. Exit if export was canceled
            				if (control.isCancelled()) {
            					return rowPos;
            				}
            			}
            		}
            	}
     	   		rowPos++;

// 	        	long currentTime = System.currentTimeMillis();
// 	        	// estimated time per 1000 units
//     	   		control.setEstimatedCycleTime((currentTime-initialCycleTime)/curcue.getAssocValueSize()*1000);

     		}
     	}
     	return rowPos;

    }
    
     
    /**
     * Create some cell styles for the workbooks's table.
     * This could be borders, font style and size or even the 
     * background color.  
     *
     */
    private void createStyles() {
        
        // First style
        cs1 = wb.createCellStyle();

        groupFont = wb.createFont();
        groupFont.setFontHeightInPoints((short)24);
        groupFont.setFontName("Courier New");
        groupFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        cs1.setFont(groupFont);
        cs1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cs1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cs1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cs1.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cs1.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cs1.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
        
        // Second style
        cs2 = wb.createCellStyle();
        
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)14);
        titleFont.setFontName("Courier New");
        cs2.setFont(titleFont);
        
        // Third style
        cs3 = wb.createCellStyle();
        
        tableTopFont = wb.createFont();
        tableTopFont.setFontHeightInPoints((short)12);
        tableTopFont.setFontName("Courier New");
        tableTopFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        cs3.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cs3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cs3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cs3.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cs3.setBorderTop(XSSFCellStyle.BORDER_THIN);
//        cs3.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        cs3.setFillBackgroundColor(HSSFColor.RED.index);
        cs3.setFont(tableTopFont);
        
        // Fourth style
        cs4 = wb.createCellStyle();
        
        tableContFont = wb.createFont();
        tableContFont.setFontHeightInPoints((short)12);
        tableContFont.setFontName("Courier New");
        cs4.setFont(tableContFont);
        cs4.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cs4.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cs4.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cs4.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cs4.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cs4.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
    }
    
    
    // AF. Aug-2012 from TD
    
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			doExport();
		} catch (IOException e) {
			success = false;
		}
	}

	/**
	 * @return true if the export succeeded
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param control
	 */
	public void setControl(ModelControl control) {
		this.control = control;
	}

}
