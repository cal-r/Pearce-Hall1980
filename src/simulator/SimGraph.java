/**
 * SimGraph.java
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
 */
package simulator;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.util.*;
import java.util.List;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.*;
//import org.jfree.ui.action.ActionRadioButton;

import simulator.configurables.ContextConfig.Context;
//import simulator.graph.SimGraph.SeriesImage;

/**
 * SimGraph produces a new frame with the experiments graph line presentation. 
 * It retrieves the values from the results from all the phases in total and draws 
 * them on a panel having the option to choose another group or disable some of
 * cues. The class imports the free Java class library 'JFreeChart' for generating 
 * charts. It has the ability to zoom in and out on a specific area, save or print
 * the image and finally u can modify any available properties before output the 
 * graph. Window may still be open while the user chooses to do a new experiment so 
 * to compare values.  
 * 
 */

public class SimGraph extends JFrame implements ActionListener{

	private static final int LINE_WIDTH = 2;    	
	private static final String CUE_SEP = " - ";

    private Container cp;
    private SimGraphPanel gp;
    private JPanel groupPanel, cuePanel;
    
    private JCheckBox groupButtons[], cueButtons[];
    // AF Aug-2012
    private TreeMap<String,JCheckBox[]> cueButtonsMap; // groupname,cues boxes

    private XYSeriesCollection xyseriescollection;
    
    private TreeMap<String,SimGroup> groupMap;
    private TreeSet<String>                          cueMap;     
    private TreeSet<String>          selectedGroups, selectedCues;
    Object                           groupNames[],   cueNames[];
    private int phase;

    // AF. August-2012. From TD
    /** Mapping from series name to colours & shapes. **/
    private static Map<String, SeriesImage> seriesImageMap = new HashMap<String, SeriesImage>();
    
    // AF. Aug-2012
    SimModel model;

    TreeMap<String,TreeSet<String>> selectedGroupsCues;


    /**
     * Class holding a shape-colour combination.
     * City University
     * BSc Computing with Artificial Intelligence
     * Project title: Building a TD Simulator for Real-Time Classical Conditioning
     * @supervisor Dr. Eduardo Alonso 
     * @author Jonathan Gray
    *
     */
    
    private class SeriesImage {
    	/** Colour of this combination. **/
    	private Paint colour;
    	/** Shape of the combination. **/
    	private Shape shape;
    	
    	public SeriesImage(Shape shape, Paint colour) {
    		this.colour = colour;
    		this.shape = shape;
    	}

		/**
		 * @return the shape
		 */
		public Shape getShape() {
			return shape;
		}

		/**
		 * @param shape the shape to set
		 */
		public void setShape(Shape shape) {
			this.shape = shape;
		}

		/**
		 * @return the colour
		 */
		public Paint getColour() {
			return colour;
		}

		/**
		 * @param colour the colour to set
		 */
		public void setColour(Paint colour) {
			this.colour = colour;
		}
		/**
		 * Check if this is the same as another combination (shape colour & shape)
		 */
		
		public boolean equals(Object other) {
			if(other instanceof SeriesImage) {
				SeriesImage tmp = (SeriesImage) other;
				if(tmp.getColour().equals(colour) && tmp.getShape().equals(shape)) {
					return true;
				}
			}
			return false;
		}
		
		public int hashCode() {
			return colour.hashCode();
		}
    }
    
    /**
     * Clear the colour mapping.
     */
    public static void clearDefaults() {
    	seriesImageMap.clear();
    }

    /**
     * SimGraph's Constructor method.
     * @param s frame's title.
     * @param m the experiment's model to use.
     * @param phase
     * @param compound
     * @param configuralCompoundsMapping
     */
    public SimGraph(String s, SimModel m, int phase, boolean compound) {
        super(s);
        
        this.phase = phase;
        groupMap = m.getGroups(); // to not lose the actual model and not update with a new one
        
        this.model = m; // AF. Aug-2012
        
        // selectedGroups = new TreeSet<String>(groupMap.keySet()); // AF Oct-2011
        selectedGroups = new TreeSet<String>();
        selectedCues   = new TreeSet<String>();
        
        selectedGroupsCues = new TreeMap<String,TreeSet<String>>(); // AF Aug-2012

        
        xyseriescollection = new XYSeriesCollection();
        cueMap = new TreeSet<String>(); // all the cues in all the groups in all the phases

        // AF. Dec 2012: iterate groups first, then cues
        Iterator itergroup = groupMap.values().iterator();
        while (itergroup.hasNext()) {
   	     	// for all groups
        	SimGroup group = (SimGroup)itergroup.next();

        	// Alberto Fernandez Nov-2011: iterate first cues, then groups, so as to keep the same colors along different groups
        
        	ArrayList<String> cues = m.getListAllCues();
        	// get the list sorted
        	Collections.sort(cues);
        	for (String cueName: cues) {

	        	
	        	// AF Oct-2012
//	        	if (!((SimPhase)group.getPhases().get(this.phase)).intialSequence().equals("0")) { // AF Oct-2011
	        		
	        		selectedGroups.add(group.getNameOfGroup()); // AF Oct-2011
//	        	}
	        	// Alberto Fernandez Nov-2011:
	        	// 
	        	// TreeMap<String,SimCue> cues = group.getCuesMap();

	        	//ArrayList<String> cues = m.getListAllCues();

	        	//Iterator itercues = cues.iterator();

	        	// Alberto Fernandez Nov-2011
	        	//Iterator itercues = cues.values().iterator();
	        	//while (itercues.hasNext()) {
	        	//for (String cueName: cues) {
	        	// for all cues of the group (to keep the colors similar for each group and cue in every phase)

	        	// Alberto Fernandez Nov-2011
	        	//SimCue tempCue = (SimCue)itercues.next();

	        	// Alberto Fernandez Nov-2011
	        	// Alberto Fernández August-2011
	        	//String cueName = tempCue.getSymbol();

	        	//if (!Character.isLowerCase(cueName.charAt(0))) { // don't draw configural cues 
	        	String interfaceName = m.cueName2InterfaceName(cueName);

	        	//String seriesName = group.getNameOfGroup() + CUE_SEP + tempCue.getSymbol() + "";
	        	String seriesName = group.getNameOfGroup() + CUE_SEP + interfaceName + "";

	        	XYSeries tempXYSeries = new XYSeries(seriesName);

	        	// Alberto Fernandez Nov-2011
	        	//if (((SimPhase)group.getPhases().get(this.phase)).isCueInStimuli(tempCue.getSymbol())) {                		
	        	if (((SimPhase)group.getPhases().get(this.phase)).isCueInStimuli(cueName)) {                		
	        		// looking for the data in results of the phase
	        		// Alberto Fernandez Nov-2011
	        		//SimCue alfa = (SimCue) ((SimPhase)group.getPhases().get(this.phase)).getResults().get(tempCue.getSymbol());
	        		SimCue alfa = (SimCue) ((SimPhase)group.getPhases().get(this.phase)).getResults().get(cueName);
	        		if (compound || (!compound && alfa.getSymbol().length()==1)){
	        			// Add values from the experiments results on the canvas in X, Y coordinates.
	        			for (int y = 1; y < alfa.getAssocValueSize(); y ++) { 
	        				tempXYSeries.add(y, alfa.getAssocValueAt(y).floatValue());	                        
	        			}
	        		}

	        	}

	        	// add probe stimuli. AF Sep-2012
//	        	XYSeries tempXYSeriesProbe = new XYSeries(seriesName+"^");
//	        	if (((SimPhase)group.getPhases().get(this.phase)).isCueInStimuli(cueName)) {                		
//	        		SimCue alfa = (SimCue) ((SimPhase)group.getPhases().get(this.phase)).getResultsProbe().get(cueName);
//	        		if (alfa != null) {
//	        			for (int y = 1; y < alfa.getAssocValueSize(); y ++) { 
//	        				tempXYSeriesProbe.add(y, alfa.getAssocValueAt(y).floatValue());	                        
//	        			}
//	        		}
//	        	}

	        	// AF Aug-2012. Adapted from TD
	        	// in jfreechart1.0.14 addSeries raises an exception if existing key name is added (c(null))
	        	SimPhase thisPhase = (SimPhase) group.getPhases().get(this.phase);
	        	if (thisPhase.isCueInStimuli(cueName)) {
	        		if (compound || (!compound && cueName.length()==1)) {
	        			xyseriescollection.addSeries(tempXYSeries);
	        		}
//	        		if (!tempXYSeriesProbe.isEmpty()) { // AF Sep-2012
//	        			xyseriescollection.addSeries(tempXYSeriesProbe);
//	        			//			            			cueMap.add(cueName+"^");
//	        		}

	        		// Alberto Fernandez Nov-2011
	        		//cueMap.add(tempCue.getSymbol()+"");
	        		cueMap.add(cueName+"");
	        	}
	        	//}



	        	//xyseriescollection.addSeries(tempXYSeries);
	        	//// Alberto Fernandez Nov-2011
	        	////cueMap.add(tempCue.getSymbol()+"");
	        	//cueMap.add(cueName+"");
	        	//}
	        	//}    
	        }
	        
	        
    	}

    	// add probe stimuli. AF Sep-2012
        
        for (SimGroup group: groupMap.values()) {
        	TreeMap<String,SimCue> resultsProbe = ((SimPhase) group.getPhases().get(this.phase)).getResultsProbe();
            Iterator<Map.Entry<String,SimCue>> iterCue = resultsProbe.entrySet().iterator();
            while (iterCue.hasNext()) {
            	Map.Entry<String,SimCue> pairCue = iterCue.next();
            	String probeCueName = pairCue.getKey();
                SimCue tempCue = (SimCue)pairCue.getValue();

                String seriesName = group.getNameOfGroup() + CUE_SEP + probeCueName;
                XYSeries tempXYSeriesProbe = new XYSeries(seriesName);

                for (int y = 1; y < tempCue.getAssocValueSize(); y ++) { 
                	tempXYSeriesProbe.add(y, tempCue.getAssocValueAt(y).floatValue());	                        
                }
                xyseriescollection.addSeries(tempXYSeriesProbe);
            }
        }

        // selectedCues only with the cues that are taking part in the phase
    	
        // AF April-2012: mapping cues in each group
    	// AF Aug-2012: moved to class attribute
        //TreeMap<String,TreeSet<String>> selectedGroupsCues = new TreeMap<String,TreeSet<String>>();

        Iterator itergr = groupMap.values().iterator();
        while (itergr.hasNext()) {
   	     	// for all groups
        	SimGroup group = (SimGroup)itergr.next();
        	SimPhase pha = (SimPhase)group.getPhases().get(this.phase);

    		TreeSet<String> selectedCuesInGroup = new TreeSet<String>(); // AF April-2012 

        	for (String cue: cueMap){
        		if (pha.isCueInStimuli(cue)) {
        		 	if (compound || (!compound && cue.length()==1)) {
                        //selectedCues.add(cue); 
        		 		selectedCues.add(m.cueName2InterfaceName(cue));
        		 		
        		 		selectedCuesInGroup.add(m.cueName2InterfaceName(cue)); // AF April-2012
        		 	}
                }
        	}	 		
         	// add probe cues. AF Sep-2012
        	Set<String> probeCues = pha.getResultsProbe().keySet();
        	for (String probeCueName: probeCues) {
        		selectedCuesInGroup.add(probeCueName);
        	}


            selectedGroupsCues.put(group.getNameOfGroup(),selectedCuesInGroup); // AF April-2012
        }


        // for buttons
        groupNames = selectedGroups.toArray();
        cueNames = selectedCues.toArray();
        
//        gp = new SimGraphPanel(xyseriescollection,selectedGroups,selectedCues, compound); // para que tenga todas en cuenta en el constructor
        gp = new SimGraphPanel(xyseriescollection,selectedGroupsCues, compound); // AF April-2012
        
        
        cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gp, BorderLayout.CENTER);
        
//        cp.add(createGroupPanel(), BorderLayout.WEST);
//        cp.add(createCuePanel(), BorderLayout.SOUTH);
        
        // Alberto Fernandez. Aug-2012.
        // cue selection: each group in a different row
        
        
        
        cp.add(createGroupCuePanel(), BorderLayout.SOUTH);
//        newCont.setLayout(new GridLayout(2,1));
//        newCont.add(createCuePanel());
//        newCont.add(createCuePanel());
//        cp.add(newCont, BorderLayout.SOUTH);
//        createCuePanel(cp);
//        createCuePanel(cp);

        
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL =  this.getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    
    /**
     * Creates JCheckbox on the left of the frame so the user will
     * have the flexibility to choose between the groups. The only thing 
     * that he has to do is to click on them. The graph will refresh 
     * automatically.
     * @return a JPanel.
     * @deprecated
     */
    public JPanel createGroupPanel() {
    	groupPanel = new JPanel(new GridLayout(0,1));  // rows,columns      
        groupButtons = new JCheckBox[groupNames.length];
        
        // Iterate through groups and create JRadioButtons.
        for(int i = 0; i < groupNames.length; i++) {
            groupButtons[i] = new JCheckBox((String)groupNames[i]);
            groupButtons[i].setActionCommand((String)groupNames[i]);
            groupButtons[i].addActionListener(this);
            groupButtons[i].setSelected(true);
            groupPanel.add(groupButtons[i]);
        }
        return groupPanel;
    }
    


    /**
     * Creates JCheckbox on the bottom of the frame so the user will
     * have the flexibility to choose between the cues. The only thing 
     * that he has to do is to click on them. The graph will refresh 
     * automatically.
     * @return a JPanel.
     * @deprecated
     */
    public JPanel createCuePanel() {
        cuePanel = new JPanel();      
        cueButtons = new JCheckBox[cueNames.length]; 
        
        // Iterate through groups and create JRadioButtons.
        for(int i = 0; i < cueNames.length; i++) {
            cueButtons[i] = new JCheckBox((String)cueNames[i]);
            cueButtons[i].setActionCommand((String)cueNames[i]);
            cueButtons[i].addActionListener(this);
            // Alberto Fernandez August-2011
            //if (cueNames[i].toString().charAt(0)=='(') {
            // AF. August-2012
            //if (cueNames[i].toString().contains("(") || Context.containsContext(cueNames[i].toString())) {
            if (cueNames[i].toString().contains("(") || Context.isContext(cueNames[i].toString())) {
                cueButtons[i].setSelected(false);            	
            }
            else {
                cueButtons[i].setSelected(true);
            }
            cuePanel.add(cueButtons[i],BorderLayout.CENTER);
        }
        return cuePanel;
    }
  
    // AF. August-2012


    /**
     * Creates a panel containing a matrix of groups and cues JCheckbox 
     * @return a JPanel.
     */
    public JPanel createGroupCuePanel() {
    	
    	// main panel contains two containers: groups and cues

    	groupPanel = new JPanel(new GridLayout(0,1));  // rows,columns      
    	
    	// container for each group's cues
    	JPanel cuePanelsContainer = new JPanel(new GridLayout(0,1));
  
        groupButtons = new JCheckBox[groupNames.length];
        cueButtonsMap = new TreeMap<String, JCheckBox[]>();
        
        // Iterate through groups and create JCheckBox.
        for(int i = 0; i < groupNames.length; i++) {
            groupButtons[i] = new JCheckBox((String)groupNames[i]);
            groupButtons[i].setActionCommand((String)groupNames[i]);
            groupButtons[i].addActionListener(this);
            groupButtons[i].setSelected(true);
            
            //groupButtons[i].setBorderPainted(true);
            //groupButtons[i].setBorder(BorderFactory.createEtchedBorder());

            //groupPanel.add(groupButtons[i]);
        	JPanel groupPanelInternal = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)); // to keep same margins as cues
        	groupPanelInternal.setBorder(BorderFactory.createEtchedBorder());
        	groupPanelInternal.add(groupButtons[i]);
            groupPanel.add(groupPanelInternal);
            
            // add cues
            
            SimGroup gr = groupMap.get((String)groupNames[i]);
            TreeMap<String,SimCue> cuesMap = gr.getCuesMap();
            Set<String> cueNamesInGroupSet = new TreeSet<String>();
            
            for (String cueInGroup: cuesMap.keySet()) {
            	// AF Sep-2012
            	//if (selectedCues.contains(model.cueName2InterfaceName(cueInGroup))) {
            	if (selectedGroupsCues.get(groupNames[i]).contains(model.cueName2InterfaceName(cueInGroup)))  {
            		cueNamesInGroupSet.add(cueInGroup);
            	}
            }
            
            // Add probe cues. AF Sep-2012
            for (String cueInGroup: ((SimPhase)gr.getPhases().get(phase)).getResultsProbe().keySet()) {
           		cueNamesInGroupSet.add(cueInGroup);
            }

            
            
            Object[] cueNamesInGroup = cueNamesInGroupSet.toArray();
           
       		cuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));  
       		cuePanel.setBorder(BorderFactory.createEtchedBorder());
       		
            // Iterate through groups and create JCheckBox.
       		JCheckBox[] cueButtons = new JCheckBox[cueNamesInGroup.length];
            for(int c = 0; c < cueNamesInGroup.length; c++) {
            	String cueName = model.cueName2InterfaceName((String)cueNamesInGroup[c]);
	            	cueButtons[c] = new JCheckBox(cueName);
	                //cueButtons[c].setBorderPainted(true);
	                //cueButtons[c].setBorder(BorderFactory.createEtchedBorder());

	                cueButtons[c].setActionCommand(cueName);
	                cueButtons[c].addActionListener(this);
	                if (cueName.contains("(") || Context.isContext(cueName)) {
	                    cueButtons[c].setSelected(false);            	
	                }
	                else {
	                    cueButtons[c].setSelected(true);
	                }
	                cuePanel.add(cueButtons[c]);
            }
            cueButtonsMap.put(groupNames[i].toString(), cueButtons);
            cuePanelsContainer.add(cuePanel);
        }

        // main panel
        JPanel cont = new JPanel();
    	//cont.setLayout(new GridLayout(0,2)); // two columns: groups and cues
    	cont.setLayout(new FlowLayout());
    	
    	//groupPanel.setBorder(BorderFactory.createEtchedBorder()); //
    	//cuePanelsContainer.setBorder(BorderFactory.createEtchedBorder()); //

    	
    	cont.add(groupPanel);
        cont.add(cuePanelsContainer);
        
        return cont;
    }

    /**
     * The ActionListeners assigned to every JRadioButton. 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
//    public void actionPerformed(ActionEvent actionevent) {
//    	selectedGroups = new TreeSet<String>();
//        selectedCues   = new TreeSet<String>();
//        
//    	for (JCheckBox button: groupButtons) {
//    		if (button.isSelected()) {
//    			selectedGroups.add(button.getActionCommand());
//    		}
//    	}
//    	
//    	for (JCheckBox cuebutton: cueButtons) {
//    		if (cuebutton.isSelected()) {
//    			selectedCues.add(cuebutton.getActionCommand());
//    		}
//    	}
//
//    	gp.modify(selectedGroups,selectedCues);
//        
//        this.doLayout();
//        this.validate();
//        this.repaint(); 
//    }
    
    public void actionPerformed(ActionEvent actionevent) {
    	selectedGroups = new TreeSet<String>();
        selectedCues   = new TreeSet<String>();
        
        selectedGroupsCues = new TreeMap<String,TreeSet<String>>(); // AF Aug-2012

        // if the event is a group CheckBox, select/deselect its cues
        if (cueButtonsMap.containsKey(actionevent.getActionCommand())) {
        	JCheckBox[] cueButtonsInGroup = cueButtonsMap.get(actionevent.getActionCommand());
        	JCheckBox groupClicked = (JCheckBox) actionevent.getSource();
        	for (JCheckBox jcb: cueButtonsInGroup) {
        		jcb.setSelected(groupClicked.isSelected());
        	}
        	
        }
        
        
        
//    	for (JCheckBox button: groupButtons) {
//    		if (button.isSelected()) {
//    			selectedGroups.add(button.getActionCommand());
//    		}
//    	}
    	
//        private TreeMap<String,JCheckBox[]> cueButtonsMap; // groupname,cues boxes
    	
        Iterator<String> itergr = cueButtonsMap.keySet().iterator();
        while (itergr.hasNext()) {
   	     	// for all groups
        	String group = itergr.next();
        	JCheckBox[] cueButtons = cueButtonsMap.get(group);
        	TreeSet<String> cuesInGroup = new TreeSet<String>();
        	
        	for (JCheckBox cuebutton: cueButtons) {
        		if (cuebutton.isSelected()) {
        			cuesInGroup.add(cuebutton.getActionCommand());
        		}
        	}
        	selectedGroupsCues.put(group, cuesInGroup);
        	
        }


    	//gp.modify(selectedGroups,selectedCues);
    	gp.modify(selectedGroupsCues);
        
        this.doLayout();
        this.validate();
        this.repaint(); 
    }
    
       
    
    
    
    
    
    
    
    
    
    
    
    /**
     * SimGraphPanel produces a line graph from a specific group and display it in
     * a JPanel. By default the graph contains all group's cues. The user is able to
     * select and deselect the ones that he needs by clicking the appropriate checkbox
     * on the bottom of the panel.
     */
    class SimGraphPanel extends JPanel implements ActionListener {
 
    	private static final int LINE_WIDTH = 2;    	
		private XYLineAndShapeRenderer renderer;
        private XYSeriesCollection xyseriescollection;

        /**
         * SimGraphPanel's Constructor method.
         * @param groups the groups that was selected to be displayed.
         */
//        public SimGraphPanel(XYSeriesCollection xy, TreeSet<String> groups, TreeSet<String> cues, boolean compound) {
        // AF April-2012: changed parameters 'groups' and 'cues' by 'selectedGroupsCues'
        public SimGraphPanel(XYSeriesCollection xy, TreeMap<String,TreeSet<String>> selectedGroupsCues, boolean compound) {
            super(new BorderLayout());
            xyseriescollection = xy;
            JFreeChart jfreechart = createChart(xyseriescollection);  
            
            // AF Aug-2012. From TD
            setColorShapeCombination(jfreechart);

            // X = domainsAxis
            XYPlot plot = jfreechart.getXYPlot();
            NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
            
            // AF. August-2012
            //domainAxis.setAutoRange(false);
            domainAxis.setAutoRange(true);
            
            //domainAxis.setTickUnit(new NumberTickUnit(1.0));
            domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // AF Sept-2011
            plot.setDomainAxis(domainAxis);
            
            plot.setBackgroundPaint(null);
            plot.setDomainGridlinePaint(Color.black);
            plot.setRangeGridlinePaint(Color.black);
            
            // AF Dec-2012
            // make legend moved to a method
            
            //makeLegend(plot, compound);

            //Making wider and markable
            BasicStroke stro = new BasicStroke(LINE_WIDTH);
            for (int i=0; i<xyseriescollection.getSeriesCount(); i++) {
            	renderer.setSeriesStroke(i, stro); 
            	renderer.setSeriesShapesVisible(i, true); 
            	
            	// Alberto Fernandez Sept-2011
            	//String seriesName = xyseriescollection.getSeries(i).getName();
            	//if (xyseriescollection.getSeries(i).getName().contains("(")) {
            	// AF. August-2012. Checked context
            	// series is context if the last character is a context symbol
            	String seriesName = (String)xyseriescollection.getSeries(i).getKey();
            	if ( seriesName.contains("(") || Context.isContext(seriesName.substring(seriesName.length()-1))) {
                	renderer.setSeriesVisible(i, false);             	
            	}
            	else {
                	renderer.setSeriesVisible(i, true);             	
            	}
            }                              

            ChartPanel chartpanel = new ChartPanel(jfreechart);                         
            add(chartpanel);
            chartpanel.setPreferredSize(new Dimension(500, 270));  
            
            chartpanel.setDefaultDirectoryForSaveAs(new File("."));
        }
           	
       	/**
       	 *  AF Dec-2012
       	 *  set the legends for the active series
       	 *  
       	 */
		public void makeLegend(XYPlot plot, boolean compound) {

			// Removing the series that are not taking part in the phase from the legend
			// With this solution, the Legend will not be reupdated according to the buttons
			LegendItemCollection lcol = plot.getLegendItems();
			LegendItemCollection newlcol = new LegendItemCollection();
			for (int l=0; l< lcol.getItemCount(); l++) {
				LegendItem litem = lcol.get(l);
				String label = litem.getLabel();
				String groupSerie = label.substring(0,label.indexOf(SimGraph.CUE_SEP));
				String cueSerie   = label.substring(label.indexOf(SimGraph.CUE_SEP)+SimGraph.CUE_SEP.length(),label.length());

				// AF April-2012
				// if (groups.contains(groupSerie) && cues.contains(cueSerie)) {
				if (selectedGroupsCues.containsKey(groupSerie) && 
						selectedGroupsCues.get(groupSerie).contains(cueSerie)) {

					// Alberto Fernandez August-2011
					//if (compound || (!compound && cueSerie.length()==1)){
					if (compound || (!compound && (cueSerie.length()==1 || cueSerie.contains("(")))) {
						// Alberto Fernandez Nov-2011
						//newlcol.add(litem);	
						// new constructor with the goal of setting shapeVisible=true
						LegendItem newLegend = new LegendItem(litem.getLabel(),
								litem.getDescription(),
								litem.getToolTipText(),
								litem.getURLText(),
								true, // shapeVisible
								litem.getShape(),
								litem.isShapeFilled(),
								litem.getFillPaint(),
								litem.isShapeOutlineVisible(),
								litem.getOutlinePaint(),
								litem.getOutlineStroke(),
								litem.isLineVisible(),
								litem.getLine(),
								litem.getLineStroke(),
								litem.getLinePaint());
						newlcol.add(newLegend);	
					}
				}
			}

			// AF Sep-2012: sort legend items
			newlcol = sortLegendItems(newlcol);

			plot.setFixedLegendItems(newlcol);
		}
		
       	/**
       	 *  AF Sep-2012
       	 *  Sort legends in ascending order. Adapted from:
       	 *  http://www.jfree.org/phpBB2/viewtopic.php?t=14514&highlight=order+legenditemcollection
       	 *  
       	 */
        private LegendItemCollection sortLegendItems(
        		LegendItemCollection originLeyendCollection) {

			LegendItemCollection cLegendItemList = originLeyendCollection; 
			LegendItemCollection cLegendItemResult = new LegendItemCollection();

			if (cLegendItemList.getItemCount() > 0) {
				ArrayList cLegendItemSort = new ArrayList();

				for (Iterator iterLegendItem = cLegendItemList.iterator(); iterLegendItem.hasNext();) {
					LegendItem cLegendItem = (LegendItem) iterLegendItem.next();

					if (cLegendItemSort.isEmpty()) {
						cLegendItemSort.add(cLegendItem);
					} else {
						for (Iterator iterItemSort = cLegendItemSort.iterator(); iterItemSort.hasNext();) {
							LegendItem cWorkItemSort = (LegendItem) iterItemSort.next();

							// LEGEND_ORDER_BY_ASCENDING
							// the value 0 if the argument string is equal to
							// this string;
							// a value less than 0 if this string is
							// lexicographically less than the string argument;
							if (cLegendItem.getLabel().compareTo(cWorkItemSort.getLabel()) <= 0) {
								cLegendItemSort.add(cLegendItemSort.indexOf(cWorkItemSort), cLegendItem);
								break; // exit for
							} else {
								// and a value greater than 0 if this string
								// is lexicographically greater than the
								// string argument
								if (!iterItemSort.hasNext()) {
									cLegendItemSort.add(cLegendItem);
									break; // exit for
								}
								// continue for
							}
						} // end for on cLegendItemSort.iterator()

					} // endif LegendItemSort.isEmpty

				} // end for for on cLegendItemList.iterator()

				// Now there are in cLegendItemSort the cLegendItem sorted
				// (Ascending).

				// begin overwrite legend source
				for (Iterator iterLegendItemSort = cLegendItemSort.iterator(); iterLegendItemSort.hasNext();) {
					LegendItem cWorkLegendItem = (LegendItem) iterLegendItemSort.next();
					cLegendItemResult.add(cWorkLegendItem);
				}
				// end overwrite legend source

			} // endif LegendItemList.getItemCount() > 0

			return cLegendItemResult;
		}

        /**
         * Modify the SimGraphPanel showing only the data of the selected groups and cues
         * @param groups that was selected to be displayed.
         * @param cues   that was selected to be displayed.
         * 
         */
        public void modify(TreeSet<String> groups, TreeSet<String> cues) {
        	
        	for (int i=0; i<xyseriescollection.getSeriesCount(); i++) {
            	//String serie = xyseriescollection.getSeries(i).getName();
            	String serie = (String)xyseriescollection.getSeries(i).getKey();
            	String groupSerie = serie.substring(0, serie.indexOf(SimGraph.CUE_SEP));
            	String cueSerie   = serie.substring(serie.indexOf(SimGraph.CUE_SEP)+SimGraph.CUE_SEP.length(),serie.length());
            	
            	// the group and the cue is selected
        		if (groups.contains(groupSerie) && cues.contains(cueSerie)) { 
        			renderer.setSeriesVisible(i,true);
        		}
        		else {
        			renderer.setSeriesVisible(i,false);
        		}
        	}
        }

        // AF. Aug-2012
        /**
         * Modify the SimGraphPanel showing only the data of the selected groups and cues
         * @param selectedGrCues that was selected to be displayed.
         * 
         */
        public void modify(TreeMap<String,TreeSet<String>> selectedGrCues) {
        	
        	for (int i=0; i<xyseriescollection.getSeriesCount(); i++) {
            	//String serie = xyseriescollection.getSeries(i).getName();
            	String serie = (String)xyseriescollection.getSeries(i).getKey();
            	String groupSerie = serie.substring(0, serie.indexOf(SimGraph.CUE_SEP));
            	String cueSerie   = serie.substring(serie.indexOf(SimGraph.CUE_SEP)+SimGraph.CUE_SEP.length(),serie.length());
            	
            	// the group and the cue is selected
            	// AF. Oct-2012
            	//if (selectedGrCues.get(groupSerie).contains(cueSerie)) {
               	if (selectedGrCues.containsKey(groupSerie)&& selectedGrCues.get(groupSerie).contains(cueSerie)) {
//        		if (groups.contains(groupSerie) && cues.contains(cueSerie)) { 
        			renderer.setSeriesVisible(i,true);
        		}
        		else {
        			renderer.setSeriesVisible(i,false);
        		}
        	}
        }
       
        /**
         * Creates a chart from the coordinates that has been given. When the data has 
         * been processed and put on the chart, it returns it.
         * @param xydataset the data with the coordinates.
         * @return the chart with the group's graph presentation.
         */
        private JFreeChart createChart(XYDataset xydataset) {
			// the parameter after orientation specifies whether or not the legend is shown
        	JFreeChart jfreechart = ChartFactory.createXYLineChart("", "Trial (n)", "Associative Strength (V)", 
        			xydataset, PlotOrientation.VERTICAL, true, true, false);
        	XYPlot xyplot = jfreechart.getXYPlot();
            renderer = (XYLineAndShapeRenderer) xyplot.getRenderer(); 
            return jfreechart;
        }
        
        /**
         * The ActionListeners assigned to every JCheckBox. 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent actionevent) {
        	
        }

    
    }
    
    
    // Alberto Fernandez. August-2012. From TD
    
    /**
     * Helper function to make sure that each series has a unique colour-shape combination across
     * graphs and forcibly darken very light yellow to something visible.
     * @param chart
     */
    
    private void setColorShapeCombination(JFreeChart chart) {
    	int count = chart.getXYPlot().getDataset().getSeriesCount();
    	String name;
    	for(int i = 0; i < count; i++) {
    		//Confiigural compounds & regular compounds should use the same colour-shape combo
    		name = chart.getXYPlot().getDataset().getSeriesKey(i).toString().replaceAll("\\[|\\]", "");
    		if (!seriesImageMap.containsKey(name)){
    			Color color;
    			Shape shape;
    			SeriesImage combo;
    			do {
    				color = (Color) chart.getXYPlot().getDrawingSupplier().getNextPaint();
    				shape = chart.getXYPlot().getDrawingSupplier().getNextShape();
    				if((int)Math.sqrt(
    					      color.getRed() * color.getRed() * .241 + 
    					      color.getGreen() * color.getGreen() * .691 + 
    					      color.getBlue() * color.getBlue() * .068) > 160) {
        				color = color.darker();
        			}
    				combo = new SeriesImage(shape, color);
    			} while(seriesImageMap.containsValue(combo));
    			seriesImageMap.put(name, combo);
    		}
    		chart.getXYPlot().getRenderer().setSeriesShape(i, seriesImageMap.get(name).getShape());
    		chart.getXYPlot().getRenderer().setSeriesPaint(i, seriesImageMap.get(name).getColour());
    	}
    }

}
