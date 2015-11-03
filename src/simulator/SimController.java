/**
 * SimController.java
 * 
 * Created on 10-Mar-2005
 * City University
 * BSc Computing with Distributed Systems
 * Project title: Simulating Animal Learning
 * Project supervisor: Dr. Eduardo Alonso 
 * @author Dionysios Skordoulis
 *
 * Modified in October-2009
 * The Centre for Computational and Animal Learning Research 
 * @supervisor Dr. Esther Mondragon 
 * email: e.mondragon@cal-r.org
 * @author Rocio Garcia Duran
 *
 * Modified in July-2011
 * The Centre for Computational and Animal Learning Research 
 * @supervisor Dr. Esther Mondragon 
 * email: e.mondragon@cal-r.org
 * @author Dr. Alberto Fernandez
 * email: alberto.fernandez@urjc.es
  *
 * Modified in July-2012
 * The Centre for Computational and Animal Learning Research 
 * @supervisor Dr. Esther Mondragon 
 * email: e.mondragon@cal-r.org
 * @author Dr. Alberto Fernandez
 * email: alberto.fernandez@urjc.es
*
 */
package simulator;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.*;
import javax.swing.*;

//import sunw.util.EventListener; // modified by Alberto Fernandez: 18 July 2011


import java.io.*;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

//AF July-2012
import simulator.configurables.ContextConfig;
import simulator.configurables.ContextConfig.Context;
//import simulator.graph.SimGraph;
import simulator.util.ExampleFileFilter;
import simulator.util.ValuesTableModel;
//import simulator.util.io.SimExport;

// AF Sep-2012
import simulator.editor.ContextEditor;


/**
 * SimController is the main class of the simulator project. It controls 
 * the behavior of the model which includes the groups and their phases 
 * and handles the view components. The user has direct interaction through
 * the controller class.
 */
// AF Aug-2012 from TD
//public class SimController implements ActionListener {
public class SimController implements ActionListener, PropertyChangeListener {
 
	private SimModel model;
	private SimView view;
	
	private String lastDirectory = "."; // Alberto Fernández Sept-2011 

	// AF Aug-2012 from TD
	/** Monitor/progress bar. **/
    private ProgressMonitor progressMonitor;
    /** Task for running the simulation. **/
    private RunTask task;
    /** Thread for simulating. **/
    private volatile Thread simulate;
    /** Excel exporter. **/
    private SimExport exporter;

	private ExportTask exporterTask;
	private volatile ModelControl control;
	private int totalProgress;

    
	/**
	 * SimController's Constructor method. 
	 * @param m the SimPhase Object, the model on the structure.
	 * @param v the SimView Object, the view in the application.
	 */
	public SimController(SimModel m, SimView v) {
		model = m;
		view = v;
		view.addButtonListeners(this);  //add actionListeners on the buttons
		view.addMenuListeners(this);	//add actionListeners on the menuitems
		view.setStatusComponent(false, "run");
		view.setStatusComponent(false, "dispGraph");
		view.setStatusComponent(false, "Export");
		view.setStatusComponent(false, "Save");
	}
	
	/*
	 * Actions performed whenever the user clicks a button or a menu item.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	    // The user chooses to start a new experiment, a new model is created
	    // and all components status are being reset to their initial value.
	    if (e.getActionCommand() == "New") {
	    	newModel();
		}
	    
	    // The user chooses to open a saved experiment from the the saved files.
	    // The file contains the values that were added to run the experiment.
	    if (e.getActionCommand() == "Open") { //$NON-NLS-1$
	    	// Alberto Fernandez. July-2012
	        JFileChooser fc = new JFileChooser();
		    // Modified Alberto Fernandez Sept-2011 : manage current directory
	        //fc.setCurrentDirectory(new File("."));
		    fc.setCurrentDirectory(new File(lastDirectory));
	        ExampleFileFilter filter = new ExampleFileFilter();
	        filter.addExtension(Messages.getString("SimController.extension")); //$NON-NLS-1$
	        filter.setDescription(Messages.getString("SimController.fileType")); //$NON-NLS-1$
	        fc.setFileFilter(filter);
	        int returnVal = fc.showOpenDialog(view);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	            try {
	                File file = fc.getSelectedFile();
	                String path = fc.getSelectedFile().getParent();
	                lastDirectory = path; // Alberto Fernandez Sept-2011
	                loadObjects(file);
	            } catch (FileNotFoundException fe) {
	                view.showError(Messages.getString("SimController.fileError")); //$NON-NLS-1$
	            } catch (IOException ioe) {
	            	view.showError(Messages.getString("SimController.fileError")); //$NON-NLS-1$
				} catch (VersionException ve) {
					view.showError(Messages.getString("SimController.versionError")); //$NON-NLS-1$
				} catch (ClassNotFoundException ce) {
					view.showError(Messages.getString("SimController.fileError")); //$NON-NLS-1$
				}
	        }
		}
		
	    // The user chooses to save the current experiment from into a specific file.
	    // The file will contain the values that were added to run the experiment.
		if (e.getActionCommand() == "Save") {
			// Alberto Fernandez July-2012
		    JFileChooser fc = new JFileChooser();
		    // Modified Alberto Fernandez Sept-2011 : manage current directory
		    //fc.setCurrentDirectory(new File("."));
		    fc.setCurrentDirectory(new File(lastDirectory));
	        ExampleFileFilter filter = new ExampleFileFilter();
	        filter.addExtension(Messages.getString("SimController.extension")); //$NON-NLS-1$
	        filter.setDescription(Messages.getString("SimController.fileType")); //$NON-NLS-1$
	        fc.setFileFilter(filter);
	        int returnVal = fc.showSaveDialog(view);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	            try {
	                String path = fc.getSelectedFile().getParent();
	                lastDirectory = path; // Alberto Fernï¿½ndez Sept-2011
	                
	                String name = fc.getSelectedFile().getName();
	                
	                if (!name.contains(Messages.getString("SimController.dotExtension"))) name += Messages.getString("SimController.dotExtension"); //$NON-NLS-1$ //$NON-NLS-2$
	                
	                File file = new File(path, name);
	                saveToObjects(file);
	            } catch (FileNotFoundException fe) {
	                view.showError(Messages.getString("SimController.fileError")); //$NON-NLS-1$
	            } catch (IOException ioe) {
	            	 view.showError(Messages.getString("SimController.fileError")); //$NON-NLS-1$
				}
	        }
		}
		
		// The uses chooses to save his results into a spreadsheet.
		
		// AF Aug-2012. Adoption form TD to include progress monitor
//		if (e.getActionCommand() == "Export") {
//			// Alberto Fernandez Sept-2011
//		    // new SimExport(view, model);
//			String[] dir = new String[1];
//			dir[0] = lastDirectory;
//		    new SimExport(view, model, dir);
//		    lastDirectory = dir[0];
//		}
		
		if (e.getActionCommand() == "Export") { //$NON-NLS-1$
			// Alberto Fernandez Sept-2011
		    // new SimExport(view, model);
			String[] dir = new String[1];
			dir[0] = lastDirectory;
			// Choose a file to store the values.
	        JFileChooser fc = new JFileChooser();
	        fc.setCurrentDirectory(new File(dir[0])); // AF Sept-2011
		    ExampleFileFilter filter = new ExampleFileFilter();
	        filter.addExtension("xlsx");
	        filter.setDescription("Spreadsheet");
	        fc.setFileFilter(filter);
	        int returnVal = fc.showSaveDialog(view);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	        	String path = fc.getSelectedFile().getParent();
	            dir[0] = path; // Alberto Fernï¿½ndez Sept-2011
	            String name = fc.getSelectedFile().getName();
	            if (!name.contains(".xlsx")) name += ".xlsx"; // Alberto Fernandez: changed from xls -> xlsx
	            boolean okToContinue = true;
	            File file = new File(path, name);
	            if (file.exists ()) {
	                int response = JOptionPane.showConfirmDialog (null, "Overwrite existing file?","Confirm Overwrite",
	                   JOptionPane.OK_CANCEL_OPTION,
	                   JOptionPane.QUESTION_MESSAGE);
	                if (response == JOptionPane.CANCEL_OPTION) {okToContinue = false;}
	                
	            }
	            if(okToContinue) {
	            	exporter = new SimExport(view, getModel(), name, file);
	            	progressMonitor = new ProgressMonitor(view, Messages.getString("SimController.exportMessage"), "", 0, 100); //$NON-NLS-1$ //$NON-NLS-2$
			    	progressMonitor.setMillisToPopup(0);
			    	progressMonitor.setMillisToDecideToPopup(0);
			    	progressMonitor.setProgress(0);
			    	exporterTask = new ExportTask();
			    	exporterTask.addPropertyChangeListener(this);
			    	exporterTask.execute();
	            }
	        }
		    lastDirectory = dir[0];
		}
		
		
		
		// The user chooses to quit. The application closes.
		if (e.getActionCommand() == "Quit") {
			System.exit(0);
		}

		// The user chooses to change the default value of number of combinations
		if (e.getActionCommand() == "Combinations") {
		    int n = view.getIntInput(Messages.getString("SimController.randomMessage"), "" + getModel().getCombinationNo()); //$NON-NLS-1$ //$NON-NLS-2$
		    // Check if 'Cancel' was pressed
		    if (n != -1 ) {
		        model.setCombinationNo(n);
		    }
		}

		// The user chooses to select/deselect setting the US across phases
		if (e.getActionCommand() == "SetUSAcrossPhases") {
			view.setIsUSAcrossPhases(!(view.getIsUSAcrossPhases()));
			view.setStatusComponent(view.getIsUSAcrossPhases(), "SetUSAcrossPhases");
			
			if (view.getIsUSAcrossPhases()) view.addUSPhases();
		    else view.removeUSPhases();
		    
		    if (!view.getIsUSAcrossPhases() || (view.getIsUSAcrossPhases() && model.getPhaseNo()==1))
		    	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        	else {
	        	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	        	view.updateUSValuesColumnsWidth();
	        }
		}		
		
		// The user chooses to select/deselect the compound values of CS
		if (e.getActionCommand() == "SetCompound") {
			view.setIsSetCompound(!(view.getIsSetCompound()));
			view.setStatusComponent(view.getIsSetCompound(), "SetCompound");
		}		
		
		// Added by Alberto Fern‡ndez August-2011
		// The user chooses to select/deselect the configural compounds option
		if (e.getActionCommand() == "SetConfiguralCompounds") {
			view.setIsSetConfiguralCompounds(!(view.getIsSetConfiguralCompounds()));
			view.setStatusComponent(view.getIsSetConfiguralCompounds(), "SetConfiguralCompounds");
		}		
		
		// Alberto Fernandez. July-2012
		
		//Update the context switch.
		if (e.getActionCommand() == "SetContextNo") { //$NON-NLS-1$
			view.toggleContext(view.useContext());
			getModel().setUseContext(view.useContext());
		}
		
		// The user chooses to select/deselect the different contexts option
		//if (e.getActionCommand() == "SetContextAcrossPhases") { //$NON-NLS-1$
		if (e.getActionCommand() == "SetContextYes") { //$NON-NLS-1$
			getModel().setContextAcrossPhase(view.getIsOmegaAcrossPhases());
			view.toggleContext(view.useContext());
			getModel().setUseContext(view.useContext());

			// AF Sep-2012. Receive alpha from user instead of default alpha

			
			// AF. Aug-2012
			// get default alpha
			//ContextConfig cc = new ContextConfig();			
			
			// and write defaut alpha into the cells
			//view.setOmegaSalience(cc.getAlpha());

			// Sep-2012
//			ContextEditor ce = new ContextEditor();
//			ce.actionPerformed(new ActionEvent(this,0,"edit"));
//		
//			ContextConfig cc = (ContextConfig)ce.getCellEditorValue();
//			view.setOmegaSalience(cc.getAlpha());
			
//			view.setOmegaSalience(ce.getCellEditorValue())

			// Only a pop up for introducing alpha
			double n = view.getDoubleInput(Messages.getString("SimController.contextAlphaMessage"), "" + view.getContextAlpha()); //$NON-NLS-1$ //$NON-NLS-2$
			if(n <= 0) {
				n = view.getContextAlpha();
			}

			Object[] possibleValues = Context.getList();
			Object selectedValue = JOptionPane.showInputDialog(null,Messages.getString("SimController.contextSymbolMessage"), "Input", 
					JOptionPane.QUESTION_MESSAGE, null, possibleValues, Context.PHI);

			if (selectedValue != null) {
				view.setOmegaSalience((Context)selectedValue, n);
			}
			else {
				view.setOmegaSalience(n); // only update alpha
			}
			
			// "SetCompound" when context is activated
			view.setIsSetCompound(true);
			view.setStatusComponent(view.getIsSetCompound(), "SetCompound");

		}
		
		// The user chooses to select/deselect the same context option
		// AF. Aug-2012. Removed
//		if (e.getActionCommand() == "SingleContext") { //$NON-NLS-1$
//			getModel().setContextAcrossPhase(false);
//			double n = view.getDoubleInput(Messages.getString("SimController.contextAlphaMessage"), "" + view.getContextAlpha()); //$NON-NLS-1$ //$NON-NLS-2$
//			if(n > 0) {
//				view.toggleContext(view.useContext());
//				getModel().setUseContext(view.useContext());
//				view.setOmegaSalience(n);
//			}
//		}

		
		
		// The user chooses to add a phase 
		if (e.getActionCommand() == "addPhase") {
		        model.setPhaseNo(model.getPhaseNo()+1);
		        view.addPhase();
		}
		
		// The user chooses to remove the last phase 
		if (e.getActionCommand() == "removePhase") {
			if (model.getPhaseNo()>1) {
				model.setPhaseNo(model.getPhaseNo()-1);
				view.removePhase();
			}
		}
		
		// The user chooses to add a group
		if (e.getActionCommand() == "addGroup") {
	        model.setGroupNo(model.getGroupNo()+1);
	        view.addGroup();
		}
		
		// The user chooses to remove the last group
		if (e.getActionCommand() == "removeGroup") {
			if (model.getGroupNo()>1) {
				model.setGroupNo(model.getGroupNo()-1);
				view.removeGroup();
			}
		}
		
		
		// The user chooses to read the guide
		if (e.getActionCommand() == "Guide") {
		    try {
				java.awt.Desktop.getDesktop().browse(new URI("http://www.cal-r.org/RWSimulator/RW_Simulator4_Guide.pdf"));
//		        SimGuide simGuide = new SimGuide("User's Guide");
//		        simGuide.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
//			    simGuide.pack();
//			    simGuide.setVisible(true);
		    } catch (Exception ex) { }
		}
		
		// The user chooses to read the information about the Simulator
		if (e.getActionCommand() == "About") {
			// AF Aug-2012
		    //view.showAboutLogo("/Extras/R&W_About");//("/Extras/logo5-final.jpg");E.Mondragon 30 Sept 2011
		    view.showAboutLogo();
		}
		
		// The user presses the 'Set Variables' button which will set initial values for the variables.
		if (e.getActionCommand() == "setVariables") {
			// AF Juy-2012. This lines from TD. Not sure whether they are important
			view.setStatusComponent(false, "dispGraph"); //$NON-NLS-1$
    		view.setOutput("");  
    		view.setStatusComponent(false, "Export"); //$NON-NLS-1$
    		view.setStatusComponent(false, "Save"); //$NON-NLS-1$

    		if (checkModelTable()) {
				view.setStatusComponent(true, "run");
				view.getCSValuesTableModel().setValuesTable(false);
				view.getUSValuesTableModel().setValuesTable(false,view.getIsUSAcrossPhases());
				
			    if (!view.getIsUSAcrossPhases() || (view.getIsUSAcrossPhases() && model.getPhaseNo()==1)){
			    	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        		}
		        else {
		        	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		        	view.updateUSValuesColumnsWidth();
		        } 
			} 
		}
		
		// The user presses the 'Clear All' button which clears all the values from the table.
		if (e.getActionCommand() == "clearAll") {
		    clearModel(model.getGroupNo(), model.getPhaseNo(), model.getCombinationNo());
		    // AF July-2012
		    getModel().setUseContext(view.useContext());
		}
		
		// The user presses the 'Run' button which updates the model with the values and runs the algorithm
		if (e.getActionCommand() == "run") {
			// check again the model and also the values
		    if (checkModelTable()) {
		    	// update the CS and US tables following the model
		    	view.getCSValuesTableModel().setValuesTable(false);
				view.getUSValuesTableModel().setValuesTable(false,view.getIsUSAcrossPhases());
				if (!view.getIsUSAcrossPhases() || (view.getIsUSAcrossPhases() && model.getPhaseNo()==1))
			    	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				else {
		        	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		        	view.updateUSValuesColumnsWidth();
		        } 
				
		    	// check the CS and US values
		    	if (checkCSValuesTable() && checkUSValuesTable()) {
		    		view.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			    	view.getGlassPane().setVisible(true);
			    	
			    	// Update values from the CS view to the model
			    	AbstractTableModel cstm = view.getCSValuesTableModel();
			    	for (int i=0; i<cstm.getRowCount(); i++)
			    		for (int j=1; j<cstm.getColumnCount(); j++){
			    			model.updateValues((String)cstm.getValueAt(i, 0), j, (String)cstm.getValueAt(i, j));
			    		}
			    	// Update values from the US view to the model
			    	AbstractTableModel ustm = view.getUSValuesTableModel();
			    	for (int i=0; i<ustm.getRowCount(); i++)
			    		for (int j=1; j<=model.getPhaseNo(); j++){
			    			if (!view.getIsUSAcrossPhases()){ 
			    				model.updateValues((String)ustm.getValueAt(i,0), j, (String)ustm.getValueAt(i,1));
			    			}
			    			else{
			    				model.updateValues((String)ustm.getValueAt(i,0), j, (String)ustm.getValueAt(i,j));
			    			}
			    		}
			    	
			    	// Alberto Fernandez. July-2012
	                // Update values from context view to the model
//	                if (Context.isContext(tempCue.getSymbol())) {
//	                    tempCue.setAlpha(tempCue.getAlpha());g
//	                }
//	                else {

			    	// update CS and US values on all the groups
			    	model.updateValuesOnGroups();
			    	
			    	// AF July-2012
			    	//Update the context switch.
			    	getModel().setUseContext(view.useContext());
			    	getModel().setContextAcrossPhase(view.getIsOmegaAcrossPhases());

			    	// AF Aug-2012 from TD

			    	// execute the algorithm
//			    	model.startCalculations();   
//			    	
//			    	view.setStatusComponent(true, "dispGraph");
//			    	view.setOutput(model.textOutput(view.getIsSetCompound()));
//			    	view.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//			    	view.getGlassPane().setVisible(false);  
//			    	view.setStatusComponent(true, "Export");
//			    	view.setStatusComponent(true, "Save");
			    	
			    	//getModel().setCSC(cscMode);
			    	task = new RunTask();
			    	task.addPropertyChangeListener(this);
			    	task.execute();

			    }
		    }
		}
		
		// The user presses the 'Display Graph' button which displays the graphs with the current results
		if (e.getActionCommand() == "dispGraph") {
		    view.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		    view.getGlassPane().setVisible(true);
		    for (int i=0; i<model.getPhaseNo(); i++) {
			   // SimGraph simGraph = new SimGraph("RESCORLA & WAGNER SIMULATOR © ver. 4.01 - Figure Phase: "+(i+1), model,i,view.getIsSetCompound());
              //SimGraph simGraph = new SimGraph("Simulator's Graph - Phase: "+(i+1), model,i,view.getIsSetCompound());-- E. Mondragon 28 Sept 2011 
			    SimGraph simGraph =  
		    		new SimGraph(Messages.getString("SimController.meanCSGraphTitle")+(i+1), getModel(),i,view.getIsSetCompound()); //$NON-NLS-1$
			  
			    simGraph.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
		        simGraph.pack();
		        simGraph.setLocation(50+i*20, 50+i*20);
		        simGraph.setVisible(true);

		        ImageIcon icon = createImageIcon("/simulator/extras/icon_16.png" , "");      
		        simGraph.setIconImage(icon.getImage());

		    }
	        view.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	        view.getGlassPane().setVisible(false); 
		}
		
	}



    /** Returns an ImageIcon, or null if the path was invalid. */ //E. Mondragon 28 Sept 2011
    
    private ImageIcon createImageIcon(String path, String description) {
       java.net.URL imgURL =  this.getClass().getResource(path);
       if (imgURL != null) {
       return new ImageIcon(imgURL, description);
       } else ;
       System.err.println("Couldn't find file: " + path);
       return null;
       }
    
  
   
//    

	/**
	 * Returns true if the ModelTable has been checked successfully
     */
	private boolean checkModelTable() {
	   
		boolean cont = true;
	    // Get the experiment's model table so we can process the information.
	    AbstractTableModel tm = view.getPhasesTableModel();
	    
	    //First check that table contains legal values.
	    for (int row = 0; row < model.getGroupNo(); row ++) {
	        //Checking group names
	        if (((String) tm.getValueAt(row , 0)).length() == 0) {
	            cont = false;
	            view.showError(Messages.getString("SimController.groupNameInRow") + (row + 1) + Messages.getString("SimController.isEmpty")); //$NON-NLS-1$ //$NON-NLS-2$
	            break;
	        }
	        //Checking phases values
	        for (int col = 1; col < model.getPhaseNo() + 1; col++) {
	            // AF July-2012
	        	//String tmp = (String) tm.getValueAt(row , 2*col-1);
	            String tmp = (String) tm.getValueAt(row , 4*col-3);
	            if (tmp.length() == 0) {
	                view.showAbout(Messages.getString("SimController.phaseWarningOne")+col+Messages.getString("SimController.phaseWarningTwo")+tm.getValueAt(row, 0)+Messages.getString("SimController.phaseWarningThree")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		            //tm.setValueAt("0", row, 2*col-1);
		            tm.setValueAt("0", row, 4*col-3);
	            }
	        }
	        // AF Sep-2012
	        // Checking ITI/CS Ratio
	        for (int col = 1; col < model.getPhaseNo() + 1; col++) {
	        	// accept String and Integer values for compatibility with a previous version
	            Object otmp = tm.getValueAt(row , 4*col-1);
	            String tmp = otmp.toString();
	            tm.setValueAt(tmp, row , 4*col-1);
//	            String tmp = (String) tm.getValueAt(row , 4*col-1);
	            try {
		            new Integer(tmp);
	            }
	            catch (NumberFormatException ex) {
	                view.showAbout(Messages.getString("SimController.ITICSRatioWarningOne")+col+Messages.getString("SimController.ITICSRatioWarningTwo")+tm.getValueAt(row, 0)+Messages.getString("SimController.ITICSRatioWarningThree")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		            //tm.setValueAt("0", row, 4*col-1);
		            tm.setValueAt(SimView.DEFAULT_ITI_CS_RATIO, row, 4*col-1);
	            }
	        }
	    }
	    if (cont) {
	    	model.reinitialize();	    	
	    	for (int row = 0; row < model.getGroupNo(); row ++) {
	    		String gName = (String) tm.getValueAt(row , 0); // first column is the group's name
		        // Create a new group for every row of the table.
	    		// AF July-2012. Added model as parameter
		        SimGroup sg = new SimGroup(gName,  model.getPhaseNo(), model.getCombinationNo(), getModel());
		        for (int c = 1; c < model.getPhaseNo() + 1; c++) {
		        	// AF July-2012, adapted from TD
		        	//boolean isRandom = ((Boolean) tm.getValueAt(row , c*2)).booleanValue();
		        	boolean isRandom = ((Boolean) tm.getValueAt(row , c*4-0)).booleanValue();
		        	getModel().setContextAlpha(view.getContextAlpha());
		        	//If context is off, use the empty context for this simulation.
		        	ContextConfig context = !getModel().isUseContext() ? ContextConfig.EMPTY : (ContextConfig) tm.getValueAt(row, c*4-2);
		        	
		        	//Integer CS_ITI_ratio = (Integer) tm.getValueAt(row, c*4-1);
		        	Integer CS_ITI_ratio = Integer.parseInt((String)tm.getValueAt(row, c*4-1));
		        	
		        	String trialString = (String)tm.getValueAt(row , c*4-3);
		        	//Remove whitespace
		        	trialString = trialString.replaceAll("\\s", ""); //$NON-NLS-1$ //$NON-NLS-2$
		        	if(!sg.addPhase(trialString, isRandom, c, 
		        			        view.getIsSetConfiguralCompounds(),model.getConfigCuesNames(),context,CS_ITI_ratio)) { // Modified Alberto Fern‡ndez August-2011
		                view.showError(Messages.getString("SimController.phase") + c + Messages.getString("SimController.hasError")); //$NON-NLS-1$ //$NON-NLS-2$
			            cont = false;
		                break;
		            }
		        }
		        if (cont) model.addGroupIntoMap(gName, sg);
		    }
	    	model.addValuesIntoMap();
	    }
	    return cont;
	}

	
	/**
	 * Returns true if the ValuesTable has been checked successfully
     */
	private boolean checkCSValuesTable(){
		boolean cont = true;
		AbstractTableModel tm = view.getCSValuesTableModel();
	    
	    // First check that table contains legal values.
	    for (int row = 0; row < tm.getRowCount(); row ++) {
	        //Checking values
	        String tmp = (String) tm.getValueAt(row , 1);
	        
	        // NOT EMPTY ALPHA FIELD
	        if (tmp.length()>0){
	        	try {
	        		new Double(tmp);
	        	} catch (Exception ex){
	        		cont = false;
	        		view.showError(Messages.getString("SimController.entryWrongMessage")); //$NON-NLS-1$
	        		break;
	        	}
	        }   		
	        // EMPTY ALPHA FIELD 
	        else {
                cont = false;
	            view.showError(Messages.getString("SimController.alphaEmptyMessage")); //$NON-NLS-1$
	            break;
	        } 
	    }
	    return cont;
	}
	
	/**
	 * Returns true if the ValuesTable has been checked successfully
     */
	private boolean checkUSValuesTable(){
		boolean cont = true;
		AbstractTableModel tm = view.getUSValuesTableModel();
	    
	    // First check that table contains legal values.
	    for (int row = 0; row < tm.getRowCount(); row ++) {	    	
	    	for (int col = 1; col<tm.getColumnCount(); col++){    	
	    		String tmp = (String) tm.getValueAt(row,col);
	    		
	    		// PHASE 1 IS EMPTY
	    		if (col==1 && tmp.length()==0 && !tm.getValueAt(row,0).equals("beta-")) {
	    			cont = false;
	    			view.showError(Messages.getString("SimController.USEmptyMessage")); //$NON-NLS-1$
	    			break;
	    		}
	    		// NOT EMPTY FIELD
	    		if (tmp.length()>0){
	    			try {
	    				new Double(tmp);
	    			} catch (Exception ex){
	    				cont = false;
	    				view.showError(Messages.getString("SimController.entryWrongMessage")); //$NON-NLS-1$
	    				break;
	    			}
	    		}
	    	}
	    	if (!cont) break;
	    }
	    return cont;
	}
	
	/**
	 * Clears up the tables from any values that they may contain and also 
	 * re-initiate the initial status on the menuitems and buttons.
	 * @param g the previous number of groups.
	 * @param p the previous number of phases.
	 * @param c the previous number of combination.
	 */
	private void clearModel(int g, int p, int c) {
		view.getPhasesTable().setEnabled(true);

		//  AF July-2012. Moved several resets to SimView (similar to TD)
		view.reset();

	    view.clearHidden(); // AF July-2012
	    
	    model = new SimModel();
	    view.updateModel(model);
	    
	    model.setGroupNo(g);
	    model.setPhaseNo(p);
	    model.setCombinationNo(c);
	    
	    view.getPhasesTableModel().setPhasesTable();
	    view.updatePhasesColumnsWidth();
	    
        view.getCSValuesTableModel().setValuesTable(true);
        
	    view.getUSValuesTableModel().setInitialValuesTable();
	    if (view.getIsUSAcrossPhases()) view.addUSPhases();
	    
	    if (!view.getIsUSAcrossPhases() || (view.getIsUSAcrossPhases() && model.getPhaseNo()==1))
	    	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	else {
        	view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        	view.updateUSValuesColumnsWidth();
        }
	    
	}
	
	/**
	 * Clears up the tables from any values that they may contain and also 
	 * re-initiate the initial status on the menuitems and buttons.
	 */
	private void newModel() {
		      
		view.reset();
	 	    
	 	view.clearHidden();    // AF July-2012

	    model = new SimModel();
	    view.updateModel(model);
	    
	    view.getPhasesTableModel().setPhasesTable();
	    view.updatePhasesColumnsWidth();
	
        view.getCSValuesTableModel().setValuesTable(true);
        
	    view.getUSValuesTableModel().setInitialValuesTable();
	    view.getUSValuesTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	}

	// AF July-2012
	/**
	 * @return the model
	 */
	public SimModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(SimModel model) {
		this.model = model;
	}

    /**
     * Helper function for saving a simulation configuration to a tdl file.
     * @param file to save to.
     * @throws IOException if there's a problem reading the file.
     */
    
    private void saveToObjects(final File file) throws IOException {
        	boolean okToContinue = true;
    		if (file.exists ()) {
                int response = JOptionPane.showConfirmDialog (null, Messages.getString("SimController.overwrite"),Messages.getString("SimController.confirmOverwrite"), //$NON-NLS-1$ //$NON-NLS-2$
                   JOptionPane.OK_CANCEL_OPTION,
                   JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {okToContinue  = false;}
            }
            if(okToContinue) {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            	//Save file version
            	out.writeDouble(Simulator.VERSION);
            	//Number of groups
                out.writeInt(getModel().getGroupNo());
                //Number of phases
                out.writeInt(getModel().getPhaseNo());
                //Number of combinations for randomness
                out.writeInt(getModel().getCombinationNo());
                //Whether US is across phases
                out.writeBoolean(view.getIsUSAcrossPhases());
                // Alberto Fernandez August-2011, whether configurals are used
                //out.writeBoolean(!getModel().getConfigCuesNames().isEmpty());
                out.writeBoolean(view.getIsSetConfiguralCompounds());

                //Whether single context is set
                out.writeBoolean(getModel().isUseContext() && !getModel().contextAcrossPhase());
                //Whether no context is set
                out.writeBoolean(getModel().isUseContext());
                //Default context salience
                out.writeDouble(view.getContextAlpha());
                
                //Whether compounds are on
                out.writeBoolean(view.getIsSetCompound());

                ValuesTableModel tmp = view.getPhasesTableModel();
                out.writeObject(tmp.getData());
                
                ValuesTableModel tmv = view.getCSValuesTableModel();
                out.writeObject(tmv.getData());
                
                ValuesTableModel ustmv = view.getUSValuesTableModel();
                out.writeObject(ustmv.getData());
                
            }
    }


    
    /**
     * Helper function for loading a td experiment configuration serialized to a tdl file.
     * @param file to load.
     * @throws IOException if there's a problem loading the file.
     * @throws VersionException if the file is of an incompatible version.
     * @throws ClassNotFoundException if an object can't be unflattened.
     */
    
    private void loadObjects(final File file) throws IOException, VersionException, ClassNotFoundException {
    	ObjectInputStream in = new ObjectInputStream(new FileInputStream (file));
        double version = in.readDouble();
        if(version < 4.0) {
        	throw new VersionException();
        }
        clearModel(in.readInt(), in.readInt(), in.readInt());
        
        view.setIsUSAcrossPhases(in.readBoolean());
    	view.setStatusComponent(view.getIsUSAcrossPhases(), "SetUSAcrossPhases");
    		
        view.setIsSetConfiguralCompounds(in.readBoolean());
        view.setStatusComponent(view.getIsSetConfiguralCompounds(), "SetConfiguralCompounds"); //$NON-NLS-1$

        //Set the context configuration.
        boolean context = true;
        if(in.readBoolean()) {
        	in.readBoolean();
        	//view.setStatusComponent(true, "SingleContext"); //$NON-NLS-1$
        	view.setStatusComponent(true, "SetContextYes"); //$NON-NLS-1$
        } else if(!in.readBoolean()) {
        	view.setStatusComponent(true, "SetContextNo"); //$NON-NLS-1$
        	context = false;
        } else {
        	//view.setStatusComponent(true, "SetContextAcrossPhases"); //$NON-NLS-1$
        	view.setStatusComponent(true, "SetContextYes"); //$NON-NLS-1$
        }
        
        // AF Oct-2012: this solved the problem of no allowing edit the context after opening a saved file
        getModel().setContextAcrossPhase(view.getIsOmegaAcrossPhases());
        
        double omegaAlpha = in.readDouble();
        view.setOmegaSalience(omegaAlpha);
        getModel().setUseContext(context);
        
        view.setIsSetCompound(in.readBoolean());
		view.setStatusComponent(view.getIsSetCompound(), "SetCompound");
        
        view.clearHidden();
        view.getPhasesTableModel().setData((Vector) in.readObject());
        view.getPhasesTable().createDefaultColumnsFromModel();
        view.updatePhasesColumnsWidth();
        view.toggleContext(context);
        view.getCSValuesTableModel().setData((Vector) in.readObject());
        view.getUSValuesTableModel().setData((Vector) in.readObject());
    }

	/**
	 * Background task for running a simulation.
	 * @author J Gray
	 *
	 * AF August-2012. New approach is followed. Estimated cycle time is updated 
	 * in doInBackground(), after sleeping.
	 */
	
	class RunTask extends SwingWorker<Void, Void> {
        

		/**
         * Run the simulation in a worker thread, starts simulating and
         * attaches a progress monitor periodically updated with task
         * progress.
         */
        @Override
        public Void doInBackground() {
            int progress = 0;
            view.setUILocked(true);
            control = new ModelControl();
            getModel().setControl(control);
            // execute the algorithm
	    	simulate = new Thread(getModel());
	    	simulate.start();
            //Initialize progress property.
            setProgress(1);
            totalProgress = getModel().totalNumPhases();//+1;
            progressMonitor = new ProgressMonitor(view, Messages.getString("SimController.runMessage"), "", 0, totalProgress+1); //$NON-NLS-1$ //$NON-NLS-2$
            //progressMonitor = new ProgressMonitor(view, Messages.getString("SimController.runMessage"), "", 0, totalProgress); //$NON-NLS-1$ //$NON-NLS-2$
	    	progressMonitor.setMillisToPopup(0);
	    	progressMonitor.setMillisToDecideToPopup(0);
	    	progressMonitor.setProgress(0);
	    	
	    	
	    	// new approach
	    	int previousProgress = 0;
	    	int currentProgress;
	        long previousTime = System.currentTimeMillis();

            try {
                while (!isCancelled() && simulate.isAlive() && !progressMonitor.isCanceled() && !control.isComplete() && !control.isCancelled()) {
                    //Update progress
                	Thread.sleep(1000);
                    progress = (int)control.getProgress();
                    //setProgress(Math.min(progress, totalProgress));
                    //int progress = (Integer) evt.getNewValue();
                    progressMonitor.setProgress(progress);
                    
//                    long remaining = (long) (control.getEstimatedCycleTime()*(totalProgress-control.getProgress()));
//                    System.out.println("estimatedCycleTime = "+ control.getEstimatedCycleTime() + " totalProgress = " + totalProgress + " progress = " + progress + " remaining = " + remaining);

                    long currentTime = System.currentTimeMillis();
                    currentProgress = (int) control.getProgress();
                    
                    //System.out.println("currentProgress = " + currentProgress + " previousProgress = " + previousProgress);
                    
                    if ((currentProgress-previousProgress) > 0) {
                        control.setEstimatedCycleTime((currentTime-previousTime)/(currentProgress-previousProgress));                    	
                    }
                    previousTime = currentTime;
                    previousProgress = currentProgress;
                   
                    long remaining = (long) (control.getEstimatedCycleTime()*(totalProgress-currentProgress));

                    //System.out.println("Current Time = "+ currentTime);
                    //System.out.println("estimatedCycleTime = "+ control.getEstimatedCycleTime() + " totalProgress = " + totalProgress + " progress = " + currentProgress + " %progress " + progress + " remaining = " + remaining);

                    String timeRemaining = String.format("%d min, %d sec", 
                    	    TimeUnit.MILLISECONDS.toMinutes(remaining),
                    	    TimeUnit.MILLISECONDS.toSeconds(remaining) - 
                    	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remaining))
                    					);
                    String message = timeRemaining; //$NON-NLS-1$
                    progressMonitor.setNote(message);
                }
                //if(progressMonitor.isCanceled()) {
                if(progressMonitor.isCanceled() || control.isCancelled()) {
                	cancel(true);
                }
            } catch (InterruptedException ignore) {
            	System.err.println("Interrupted!");
            } catch (Exception e) { // AF Aug-2012
            	System.err.println(e);
            }
            return null;
        }

        /**
         * Kill the progress monitor, re-enable the gui and
         * update the output if the simulation wasn't cancelled.
         */
        @Override
        public void done() {
        	view.setUILocked(false);
            if(!isCancelled()) {
            	view.setStatusComponent(true, "dispGraph"); //$NON-NLS-1$
	    		view.setOutput(getModel().textOutput(view.getIsSetCompound()));  
	    		view.setStatusComponent(true, "Export"); //$NON-NLS-1$
	    		view.setStatusComponent(true, "Save"); //$NON-NLS-1$
            } else {
            	//getModel().cancel();
            	control.setCancelled(true);
            	simulate.interrupt();
            	simulate = null;
            	view.setStatusComponent(false, "dispGraph"); //$NON-NLS-1$
	    		view.setOutput("");  
	    		view.setStatusComponent(false, "Export"); //$NON-NLS-1$
	    		view.setStatusComponent(false, "Save"); //$NON-NLS-1$
            }
            view.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    		view.getGlassPane().setVisible(false);
            progressMonitor.setProgress(0);
            progressMonitor.close();
        }
    }
    
	/**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName() ) { //$NON-NLS-1$
//            int progress = (Integer) evt.getNewValue();
//            progressMonitor.setProgress(progress);
//            long remaining = (long) (control.getEstimatedCycleTime()*(totalProgress-control.getProgress()));
//            String timeRemaining = String.format("%d min, %d sec", 
//            	    TimeUnit.MILLISECONDS.toMinutes(remaining),
//            	    TimeUnit.MILLISECONDS.toSeconds(remaining) - 
//            	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remaining))
//            					);
//            String message = timeRemaining; //$NON-NLS-1$
//            progressMonitor.setNote(message);
            if (progressMonitor.isCanceled() || task.isDone()) {
                if (progressMonitor.isCanceled()) {
                    task.cancel(true);
                    control.setCancelled(true);
                } 
            }
        }

    }

    /**
	 * Background task for running a simulation.
	 * @author J Gray
	 *
	 * AF August-2012. New approach is followed. Estimated cycle time is updated 
	 * in doInBackground(), after sleeping.
	 */
	
	class ExportTask extends SwingWorker<Void, Void> {
        /**
         * Run the simulation in a worker thread, starts simulating and
         * attaches a progress monitor periodically updated with task
         * progress.
         */
        @Override
        public Void doInBackground() {
        	control = new ModelControl();
            int progress = 0;
            view.setUILocked(true);
            // execute the algorithm
            exporter.setControl(control);
	    	simulate = new Thread(exporter);
	    	simulate.start();
            //Initialize progress property.
            setProgress(0);
            
            // AF Aug-2012: progress based on number of AssocValues of cues
            //totalProgress = getModel().getGroupNo();
            Map<String, SimGroup> groups = model.getGroups();
            Set<String> setGroups = groups.keySet();
            Iterator<String> iterGroups = setGroups.iterator();
            
            totalProgress = 0;
            while(iterGroups.hasNext() ) {
            	SimGroup gr = groups.get(iterGroups.next());
            	totalProgress += gr.getNumberOfValuesInResults();
            }
            
	    	// new approach
	    	int previousProgress = 0;
	    	int currentProgress;
	        long previousTime = System.currentTimeMillis();
	    	
            try {    	        
                //while (progress < 100 && !isCancelled() && simulate.isAlive() && !progressMonitor.isCanceled()) {
                while (!isCancelled() && simulate.isAlive() && !progressMonitor.isCanceled()) {
                    //Update progress
                	Thread.sleep(1000);
                	progress = (int) Math.floor((control.getProgress()/totalProgress)*100);
                    //setProgress(Math.min(progress, 100));
                    progressMonitor.setProgress(Math.min(progress, 100));
                    
//                    progress = (int)control.getProgress();
//                    //setProgress(Math.min(progress, totalProgress));
//                    //int progress = (Integer) evt.getNewValue();
//                    progressMonitor.setProgress(progress);
                    
                    long currentTime = System.currentTimeMillis();
                    currentProgress = (int) control.getProgress();
                    
                    if ((currentProgress-previousProgress) > 0) {
                        control.setEstimatedCycleTime((currentTime-previousTime)/(currentProgress-previousProgress));                    	
                    }
                    previousTime = currentTime;
                    previousProgress = currentProgress;
                   
                    long remaining = (long) (control.getEstimatedCycleTime()*(totalProgress-currentProgress));

                    //System.out.println("Current Time = "+ currentTime);
                    //System.out.println("estimatedCycleTime = "+ control.getEstimatedCycleTime() + " totalProgress = " + totalProgress + " progress = " + currentProgress + " %progress " + progress + " remaining = " + remaining);

                    String timeRemaining = String.format("%d min, %d sec", 
                    	    TimeUnit.MILLISECONDS.toMinutes(remaining),
                    	    TimeUnit.MILLISECONDS.toSeconds(remaining) - 
                    	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remaining))
                    					);
                    String message = timeRemaining; //$NON-NLS-1$
                    progressMonitor.setNote(message);
                }
                //Halt processing asap.
                if(isCancelled() || progressMonitor.isCanceled()) {
                	control.setCancelled(true);
                	simulate.interrupt();
                	simulate = null;
                }
            } catch (InterruptedException ignore) {
            	System.err.println("Interrupted!");
            } catch (Exception e) {  // AF Aug-2012
            	System.err.println(e);
            }
            
            return null;
        }


        /**
         * Kill the progress monitor, re-enable the gui and
         * update the output if the simulation wasn't cancelled.
         */
        @Override
        public void done() {
            view.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    		view.getGlassPane().setVisible(false);
            progressMonitor.setProgress(0);
            progressMonitor.close();
            if(!exporter.isSuccess()) {
                view.showError("This process encountered an error.\n Please try again.");
            }
            view.setUILocked(false);
        }
    }
 
	
}



