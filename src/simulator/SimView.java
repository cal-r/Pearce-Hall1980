/**
 * SimView.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import simulator.configurables.ContextConfig;
import simulator.configurables.ContextConfig.Context;
import simulator.editor.ContextEditor;
import simulator.util.ValuesTableModel;


/**
 * SimView class is the main graphical user interface of the Simulator's
 * application. The main purposes is the managing of viewing objects. It
 * contains a menu bar on top with File -> New, Open, Save, Export and Quit,
 * Settings -> Groups, Phases, Combinations and last the Help -> Guide.
 * Next, a phase table where the user adds the group names, the sequences
 * on every phase and if he prefers to run randomly. The value table where
 * the user adds the values for every parameter that is needed and the text
 * output which display the results. It also contains buttons for interaction.
 */
public class SimView extends JFrame {
	
	// Alberto Fern�ndez July-2011
	// Font styles
	private static final String TABLE_FONT_NAME = "Dialog";
	private static final int TABLE_FONT_STYLE = 1;
	private static final int TABLE_FONT_SIZE = 14;

	private static final int TABLE_ROW_HEIGHT = 18;
	
    private static final int INITIAL_WIDTH = 760;
	private static final int INITIAL_HEIGHT= 650;
	private static final float AligmentY = 0;
	
	private SimModel model;
	
	
	private Container cp;
	
	private JMenuBar menuBar;
	private JMenu menuFile, menuSettings, menuHelp;


	private JMenuItem menuItemNew, menuItemOpen, menuItemSave, menuItemExport, menuItemQuit, 
		menuItemComb, menuItemGuide,
		menuItemAbout; 
	private JCheckBoxMenuItem menuItemUSAcrossPhases, menuItemSetCompound, menuItemSetConfiguralCompounds; // menuItemSetConfiguralCompounds by Alberto Fern�ndez August-2011
	
	private JPanel mainPanel, bottomPanel; 
	private JButton setVariablesBut, clearBut, runBut, dispGraphBut,
		addPhaseBut, removePhaseBut, addGroupBut, removeGroupBut;
	
	private JScrollPane phasesScroll, CSValuesScroll, USValuesScroll, GlobalValuesScroll, outputScroll;
	private JTable phasesTable, CSValuesTable, USValuesTable, GlobalValuesTable;
	private PhasesTableModel phasesTableModel;
	private CSValuesTableModel CSValuesTableModel;
	private USValuesTableModel USValuesTableModel;
	private GlobalValuesTableModel GlobalValuesTableModel;
	private JTextArea outputArea;
	private JLabel bottomLabel;
	
	private boolean isUSAcrossPhases, isSetCompound, isSetConfiguralCompounds;

	// Alberto Fernandez. July-2012
	// Adapted from TD
	
	private JMenu menuContext;
	JMenuItem menuItemContext;
	private JMenuItem menuItemContextAcrossPhases;
//	JRadioButtonMenuItem menuItemSingleContext;

	/** Tracking for hidden context columns. **/
	private Map<TableColumn, Integer> hiddenColumns;
	public static final String GROUP_NAME = Messages.getString("SimView.groupName"); //$NON-NLS-1$
	public static final String RANDOM = Messages.getString("SimView.random"); //$NON-NLS-1$
	public static final String CONTEXT = Messages.getString("SimView.context"); //$NON-NLS-1$
	public static final String CONTEXT_ITI_CS_RATIO = Messages.getString("SimView.contextITICSRatio"); //$NON-NLS-1$

	/** Default context salience & salience for uniform contexts. **/
	private double contextAlpha;

//	public static final int DEFAULT_ITI_CS_RATIO = 5;
	public static final String DEFAULT_ITI_CS_RATIO = "" + 5; // String rather than int to avoid some error if cell is left empty

	public static final String DEFAULT_ALPHA = "0.2";
	public static final double DEFAULT_CONTEXT_ALPHA = 0.15;


	/**
	 * SimView's Constructor method.
	 * @param m - the SimModel Object, the model on the structure which 
	 * contains the groups with their phases and also some parameters that 
	 * are needed for the simulator.
	 */
	public SimView(SimModel m) {
		contextAlpha = DEFAULT_CONTEXT_ALPHA; // AF July-2012. Aug-2012
		model = m;
		isUSAcrossPhases = false;
		isSetCompound = false;
		isSetConfiguralCompounds = false;
		cp = this.getContentPane();
		
		hiddenColumns = new LinkedHashMap<TableColumn, Integer>(); // AF July-2012

		
		cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
		
		center();
		createMenu();
		createGUI2();
			
		//this.setTitle("RESCORLA & WAGNER SIMULATOR © ver. 4.01");
		this.setTitle(Messages.getString("SimView.title")); //$NON-NLS-1$
		// modified by Alberto Fernandez: 19 July 2011
//		ImageIcon icon = createImageIcon("..simulator/extras/R&W.png","");//R&W.png", ""); E.Mondragon 28 Sept 2011
//		ImageIcon icon = createImageIcon("/simulator/extras/RW4.png","");//R&W.png", ""); //E.Mondragon 10 OCt 2012
//		this.setIconImage(icon.getImage());//R&W.png", ""); //E.Mondragon 10 OCt 2012
		
		List<Image> icon = new ArrayList<Image>();//E.Mondragon 10 OCt 2012
		icon.add(createImageIcon("/simulator/extras/icon_16.png","").getImage());//E.Mondragon 10 OCt 2012
		icon.add(createImageIcon("/simulator/extras/icon_24.png","").getImage());//E.Mondragon 10 OCt 2012
		icon.add(createImageIcon("/simulator/extras/icon_32.png","").getImage());//E.Mondragon 10 OCt 2012
		icon.add(createImageIcon("/simulator/extras/icon_64.png","").getImage());//E.Mondragon 10 OCt 2012
		icon.add(createImageIcon("/simulator/extras/icon_256.png","").getImage());//E.Mondragon 10 OCt 2012
		icon.add(createImageIcon("/simulator/extras/icon_512.png","").getImage());//E.Mondragon 10 OCt 2012
		this.setIconImages(icon);//E.Mondragon 10 OCt 2012


		this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
	}
	
	public void updateModel(SimModel m){
		model = m;
	}
	
	/**
	 * Set if the cues of lambdas and betas are updatable across phases
	 * @param boolean
	 */
	public void setIsUSAcrossPhases(boolean b){
		isUSAcrossPhases = b;
	}
	
	/**
	 * Return if the cues of lambdas and betas are updatable across phases
	 * @return boolean
	 */
	public boolean getIsUSAcrossPhases(){
		return isUSAcrossPhases;
	}
	
	/**
	 * Set if the compounds values are going to be shown
	 * @param boolean
	 */
	public void setIsSetCompound(boolean b){
		isSetCompound = b;
	}
	
	/**
	 * Return if the compounds values are going to be shown
	 * @return boolean
	 */
	public boolean getIsSetCompound(){
		return isSetCompound;
	}

	/**
	 * Set if the configural compounds values are going to be used
	 * @param boolean
	 */
	public void setIsSetConfiguralCompounds(boolean b){
		isSetConfiguralCompounds = b;
	}
	
	/**
	 * Return if the compounds values are going to be used
	 * @return boolean
	 */
	public boolean getIsSetConfiguralCompounds(){
		return isSetConfiguralCompounds;
	}

	/**
	 * Positions the frame into the center of the screen. It uses the 
	 * Toolkit.getDefaultToolkit().getScreenSize() method to retrieve the 
	 * screens actual size and from the it calculates the center,
	 */
	private void center() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int x = (screenSize.width - INITIAL_WIDTH) / 2; 
		int y = (screenSize.height - INITIAL_HEIGHT) / 2; 
		this.setLocation(x, y); 
	}
	
	/**
	 * Creates and initializes every element that is necessary for
	 * the menu component. Sets mnemonics on them and actionCommands
	 * for the easier process during events.
	 */
	
	// Alberto Fernandez July-2012.
	// Adapted from TD Simulator
	private void createMenu() {
	    menuBar = new JMenuBar();
	    
	    menuFile = new JMenu(Messages.getString("SimView.file")); //$NON-NLS-1$
	    menuFile.setMnemonic(KeyEvent.VK_F);
	    
	    menuItemNew = new JMenuItem(Messages.getString("SimView.new"), KeyEvent.VK_N); //$NON-NLS-1$
	    menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
	    menuItemNew.setActionCommand("New"); //$NON-NLS-1$
	    menuFile.add(menuItemNew);
	    
	    menuItemOpen = new JMenuItem(Messages.getString("SimView.open"), KeyEvent.VK_O); //$NON-NLS-1$
	    menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
	    menuItemOpen.setActionCommand("Open"); //$NON-NLS-1$
	    menuFile.add(menuItemOpen);
	    
	    menuItemSave = new JMenuItem(Messages.getString("SimView.save"), KeyEvent.VK_S); //$NON-NLS-1$
	    menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
	    menuItemSave.setActionCommand("Save"); //$NON-NLS-1$
	    menuFile.add(menuItemSave);
	    
	    menuItemExport = new JMenuItem(Messages.getString("SimView.export"), KeyEvent.VK_E); //$NON-NLS-1$
	    menuItemExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
	    menuItemExport.setActionCommand("Export"); //$NON-NLS-1$
	    menuFile.add(menuItemExport);
	    
	    menuFile.addSeparator();
	    
	    menuItemQuit = new JMenuItem(Messages.getString("SimView.quit"), KeyEvent.VK_Q); //$NON-NLS-1$
	    menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
	    menuItemQuit.setActionCommand("Quit"); //$NON-NLS-1$
	    menuFile.add(menuItemQuit);
	    
	    menuBar.add(menuFile);
	    
//	    menuSettings = new JMenu(Messages.getString("SimView.designSettings")); //$NON-NLS-1$
	    menuSettings = new JMenu(Messages.getString("SimView.Settings")); //$NON-NLS-1$
//	    menuProcSettings = new JMenu(Messages.getString("SimView.procedureSettings")); //$NON-NLS-1$
	    menuSettings.setMnemonic(KeyEvent.VK_S);
	   
	    menuItemComb = new JMenuItem(Messages.getString("SimView.numCombinations"), KeyEvent.VK_R); //$NON-NLS-1$
	    menuItemComb.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
	    menuItemComb.setActionCommand("Combinations"); //$NON-NLS-1$
	    menuSettings.add(menuItemComb);
	    
	    
	    menuItemUSAcrossPhases = new JCheckBoxMenuItem(Messages.getString("SimView.usPerPhase"), false);  //$NON-NLS-1$
	    menuItemUSAcrossPhases.setActionCommand("SetUSAcrossPhases"); //$NON-NLS-1$
	    menuSettings.add(menuItemUSAcrossPhases);
	    
	    
	    menuItemSetCompound = new JCheckBoxMenuItem(Messages.getString("SimView.compounds"), false);  //$NON-NLS-1$
	    menuItemSetCompound.setActionCommand("SetCompound"); //$NON-NLS-1$
	    menuSettings.add(menuItemSetCompound);
	   
	    // Added by Alberto Fernandez August-2011
	    // Option Configural Compounds
	    menuItemSetConfiguralCompounds = new JCheckBoxMenuItem(Messages.getString("SimView.configurals"), false);  //$NON-NLS-1$
	    menuItemSetConfiguralCompounds.setActionCommand("SetConfiguralCompounds"); //$NON-NLS-1$
	    menuSettings.add(menuItemSetConfiguralCompounds);
	    

	    // July-2012
	    menuContext = new JMenu(Messages.getString("SimView.contextSim")); //$NON-NLS-1$
	    menuSettings.add(menuContext);
	    ButtonGroup contexts = new ButtonGroup();
	    menuItemContext = new JRadioButtonMenuItem(Messages.getString("SimView.contextNo"), true);  //$NON-NLS-1$
	    menuItemContext.setActionCommand("SetContextNo"); //$NON-NLS-1$
	    contexts.add(menuItemContext);
	    menuContext.add(menuItemContext); 
	    
	    // AF. August-2012. Remove single/different context --> Yes/No
	    
//	    menuItemSingleContext = new JRadioButtonMenuItem(Messages.getString("SimView.sameContext"), false); //$NON-NLS-1$
//	    menuItemSingleContext.setActionCommand("SingleContext"); //$NON-NLS-1$
//		contexts.add(menuItemSingleContext);
		menuItemContextAcrossPhases = new JRadioButtonMenuItem(Messages.getString("SimView.contextYes"), false); //$NON-NLS-1$
		//menuItemContextAcrossPhases.setActionCommand("SetContextAcrossPhases"); //$NON-NLS-1$
		menuItemContextAcrossPhases.setActionCommand("SetContextYes"); //$NON-NLS-1$
		contexts.add(menuItemContextAcrossPhases);
		menuContext.add(menuItemContextAcrossPhases);
//		menuContext.add(menuItemSingleContext);

//	    menuItemCsc = new JCheckBoxMenuItem(Messages.getString("SimView.cscMode"), true);  //$NON-NLS-1$
//	    menuItemCsc.setActionCommand("SetCsc"); //$NON-NLS-1$
//	    //menuSettings.add(menuItemCsc);
	   

	    menuBar.add(menuSettings);
	    
	    menuHelp = new JMenu(Messages.getString("SimView.help")); //$NON-NLS-1$
	    menuHelp.setMnemonic(KeyEvent.VK_H);
	    menuBar.add(menuHelp);
	    
	    menuItemGuide = new JMenuItem(Messages.getString("SimView.guide"), KeyEvent.VK_G); //$NON-NLS-1$
	    menuItemGuide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
	    menuItemGuide.setActionCommand("Guide"); //$NON-NLS-1$
	    menuHelp.add(menuItemGuide);
	    
	    menuItemAbout = new JMenuItem(Messages.getString("SimView.about"), KeyEvent.VK_A); //$NON-NLS-1$
	    menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
	    menuItemAbout.setActionCommand("About"); //$NON-NLS-1$
	    menuHelp.add(menuItemAbout);

	    
	    this.setJMenuBar(menuBar);
	}
	/**
	 * Creates, initializes and configures the view components..
	 * This view can not change its size
	 */
	private void createGUI2() {
		
    	
		mainPanel = new JPanel(new GridBagLayout());
	    mainPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    
	    GridBagConstraints c = new GridBagConstraints();
	    c.anchor = GridBagConstraints.CENTER;
	    c.insets = new Insets(5,5,5,5); 
	    
	    //Phases panel
	    JPanel phasePanel = new JPanel();
	    phasePanel.setLayout(new BorderLayout());
	    addPhaseTable();
	    phasePanel.add(phasesScroll,BorderLayout.CENTER);
	    
	    //Add/remove buttons to add or remove phases or groups

	    JPanel phasesButtonPanel = new JPanel();
	    JLabel phasesLabel = new JLabel(Messages.getString("SimView.phases")); //$NON-NLS-1$
   

	    addPhaseBut = new JButton("+");addPhaseBut.setFont(new Font( "Courier", Font.BOLD, 18 ));
	    
	    //addPhaseBut.setMargin(new Insets(-7,10,-6,10)); // Insets(int top, int left, int bottom, int right)
	    //addPhaseBut = new JButton("+"); E Mondragon July 30, 2011
	    addPhaseBut.setFocusPainted(false);//E.Mondragon August 1st, 2011
	    addPhaseBut.updateUI();//
	    addPhaseBut.setActionCommand("addPhase");	   

	    removePhaseBut = new JButton("-");removePhaseBut.setFont(new Font( "Courier", Font.BOLD, 18 ));
	    
	    //removePhaseBut.setMargin(new Insets(-7,10,-6,10));//E Mondragon July 30, 2011
	    //removePhaseBut = new JButton("-"); E Mondragon July 30, 2011
	    removePhaseBut.setFocusPainted(false);;//E.Mondragon August 1st, 2011
	    removePhaseBut.setActionCommand("removePhase");

	    phasesButtonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
	    phasesButtonPanel.add(phasesLabel);
	    phasesButtonPanel.add(removePhaseBut);
	    phasesButtonPanel.add(addPhaseBut);
	    phasePanel.add(phasesButtonPanel,BorderLayout.NORTH);

	    
	   	JPanel groupsButtonPanel = new JPanel();	 
        JLabel groupsLabel = new JLabel("Groups");


	    addGroupBut = new JButton("+");addGroupBut.setFont(new Font( "Courier", Font.BOLD, 18 ));
	    //addGroupBut.setMargin(new Insets(-7,9,-6,9));//E Mondragon July 30, 2011
	    // addGroupBut = new JButton("+"); E Mondragon July 30, 2011
	    addGroupBut.setFocusPainted(false);;//E.Mondragon August 1st, 2011
	    addGroupBut.setActionCommand("addGroup");
	    
	    removeGroupBut = new JButton("-");removeGroupBut.setFont(new Font( "Courier", Font.BOLD, 18 ));
	    //removeGroupBut.setMargin(new Insets(-7,9,-6,9));//E Mondragon July 30, 2011
	    //removeGroupBut = new JButton("-"); E Mondragon July 30, 2011
	    removeGroupBut.setFocusPainted(false);;//E.Mondragon August 1st, 2011
	    removeGroupBut.setActionCommand("removeGroup");
	    
	    //A. Fernandez modification E.Mondragon 10/10/2011
	    
	    String OS = System.getProperty("os.name");
	    System.out.println(OS);
	    if (OS.toUpperCase().contains("WINDOWS")) {
	     System.out.println("es windows");
	     addPhaseBut.setMargin(new Insets(-7,10,-6,10)); // Insets(int top, int left, int bottom, int right)
	     removePhaseBut.setMargin(new Insets(-7,10,-6,10));//E Mondragon July 30, 2011
	     addGroupBut.setMargin(new Insets(-7,9,-6,9));//E Mondragon July 30, 2011
	     removeGroupBut.setMargin(new Insets(-7,9,-6,9));//E Mondragon July 30, 2011
	    }
	    
	    // end
	    

	    groupsButtonPanel.setLayout(new BoxLayout(groupsButtonPanel, BoxLayout.Y_AXIS));
	    groupsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    addGroupBut.setAlignmentX(Component.CENTER_ALIGNMENT);
	    removeGroupBut.setAlignmentX(Component.CENTER_ALIGNMENT);
	    groupsButtonPanel.add(groupsLabel);    
	    groupsButtonPanel.add(removeGroupBut);
	    //groupsButtonPanel.add(addGroupBut);
	    JPanel jp = new JPanel(); // Alberto Fernandez Oct-2011
	    jp.add(addGroupBut);
	    groupsButtonPanel.add(jp);
	    
	    phasePanel.add(groupsButtonPanel,BorderLayout.WEST); 
	    
	    //Variables panel
	    JPanel variablePanel = new JPanel();
	    variablePanel.setLayout(new BorderLayout());
	    
	    setVariablesBut = new JButton(Messages.getString("SimView.setParams")); //$NON-NLS-1$
	    setVariablesBut.setActionCommand("setVariables");
	    JPanel varButPanel = new JPanel();
	    varButPanel.add(setVariablesBut);
		addCSValuesTable();
		addUSValuesTable();
		addGlobalValuesTable();
	    runBut = new JButton(Messages.getString("SimView.run")); //$NON-NLS-1$
	    runBut.setActionCommand("run");	 
	    JPanel runButPanel = new JPanel();
	    runButPanel.add(runBut);
	    
	    JPanel valuesPanel = new JPanel();
	    valuesPanel.setLayout(new GridLayout(2, 1));
	    valuesPanel.add(CSValuesScroll);
	    valuesPanel.add(USValuesScroll);
		valuesPanel.add(GlobalValuesScroll);
	    
	    variablePanel.add(varButPanel,BorderLayout.NORTH);
	    variablePanel.add(valuesPanel, BorderLayout.CENTER);
	    variablePanel.add(runButPanel,BorderLayout.SOUTH);
	    
	    //Result panel
	    JPanel resultPanel = new JPanel();
	    resultPanel.setLayout(new BorderLayout());
	    
	    clearBut = new JButton(Messages.getString("SimView.clear")); //$NON-NLS-1$
	    clearBut.setActionCommand("clearAll");
	    JPanel clearButPanel = new JPanel();
	    clearButPanel.add(clearBut);
	    outputArea = new JTextArea(10, 22);
		outputArea.setEditable(false);
		outputArea.setFont(new Font("Serif" , Font.PLAIN , 16));
		outputScroll = new JScrollPane(outputArea);
		dispGraphBut = new JButton(Messages.getString("SimView.graphs")); //$NON-NLS-1$
	    dispGraphBut.setActionCommand("dispGraph");
	    JPanel dispGraphButPanel = new JPanel();
	    dispGraphButPanel.add(dispGraphBut);

	    resultPanel.add(clearButPanel,BorderLayout.NORTH);
	    resultPanel.add(outputScroll, BorderLayout.CENTER);
	    resultPanel.add(dispGraphButPanel,BorderLayout.SOUTH);
	    
	    c.gridx = 0;
	    c.gridy = 0;
	    c.gridwidth = 2;
	    c.weighty = 0.4;
	    c.weightx = 1;
	    c.fill = GridBagConstraints.BOTH;
	    mainPanel.add(phasePanel, c);
	    
	    c.gridx = 0;
	    c.gridy = 1;
	    c.gridwidth = 1;
	    c.weighty = 0.6;
	    c.weightx = 0.5;
	    c.fill = GridBagConstraints.BOTH;
	    mainPanel.add(variablePanel,c);
	    
	    c.gridx = 1;
	    c.gridy = 1;
	    c.gridwidth = 1;
	    c.weighty = 0.6;
	    c.weightx = 0.5;
	    c.fill = GridBagConstraints.BOTH;

	    mainPanel.add(resultPanel,c);
	    
	    cp.add(mainPanel);
	    cp.add(createBottomPanel());
	    
	}
	
		
	/*
	 * Creates the bottom panel with the logo
	 */
	private JPanel createBottomPanel() {
	    JPanel aboutPanel = new JPanel();
	    aboutPanel.setMinimumSize(new Dimension(INITIAL_WIDTH, 58));

	    //aboutPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		aboutPanel.setBackground(Color.WHITE);
		// modified by Alberto Fernandez: 19 July 2011
		//ImageIcon icon = createImageIcon("/Extras/logo6-final.jpg", "");

		//ImageIcon icon = createImageIcon("../Extras/logo6-final.png", "");
		// AF July-2012
		//ImageIcon icon = createImageIcon("/simulator/extras/logo6-final.png", "");
		ImageIcon icon = createImageIcon("/simulator/extras/RW4-BOTTOM-LOGO.png", "");
		
		JLabel label = new JLabel(icon);
		aboutPanel.add(label);
		
		//aboutPanel.setBorder(new SimBackgroundBorder(icon.getImage(), true));
		return aboutPanel;
		
	    
	}
	

	/**
	 * check whether there are two or more consecutive stimuli in any phase
	 */
	private boolean containsConsecutiveStimuli() {
		
		AbstractTableModel tm = phasesTableModel;
	    
	    //First check that table contains legal values.
	    for (int row = 0; row < model.getGroupNo(); row ++) {
	        //Checking phases values
	        for (int col = 1; col < model.getPhaseNo() + 1; col++) {
	            String tmp = (String) tm.getValueAt(row , 4*col-3);
	            if (containsConsecutiveStimuli(tmp)) {
	            	return true;
	            }
	        }
	    }
		return false;
	}
	/**
	 * check whether there are two or more consecutive stimuli in 
	 * the given string, i.e. two consecutive letters
	 */
	private boolean containsConsecutiveStimuli(String sequence) {
		for (int i = 1; i < sequence.length(); i++) {
			if (Character.isLetter(sequence.charAt(i-1)) && Character.isLetter(sequence.charAt(i))) {
				return true;
			}
		}
		return false;
	}

		
	/**
	 * Creates, initializes, configures and adds a phase table into the
	 * frame. Columns and rows are depending on model's parameters.
	 */
	private void addPhaseTable() {
	    phasesTableModel = new PhasesTableModel();
	    phasesTable = new JTable(phasesTableModel) {
	    	
	    	// AF August-2012. From TD
	    	/**
	    	 * Overridden to autosize columns after an edit and update the timing
	    	 * configurations to reflect phase strings.
	    	 */
	    	public void editingStopped(ChangeEvent e) {
	    		super.editingStopped(e);
	    		updatePhasesColumnsWidth();
	    		
	    		// If there are two or more consecutive stimuli activate "SetCompound"
	    		if (containsConsecutiveStimuli()) {
					setIsSetCompound(true);
					setStatusComponent(getIsSetCompound(), "SetCompound");	    			
	    		}

	    	}
	    	
             public boolean editCellAt(int row, int column, EventObject e) {
                boolean b = super.editCellAt(row, column, e);
                //if ( b && column % 2 == 1) {
                if ( b && getColumnName(column).contains(Messages.getString("SimView.phase"))) { //$NON-NLS-1$
                    TableCellEditor tce = getCellEditor(row, column);
                    DefaultCellEditor dce = (DefaultCellEditor)tce;
                    Component c = dce.getComponent();
                    JTextComponent jtc = (JTextComponent)c;
                    jtc.setFont(new Font(TABLE_FONT_NAME, TABLE_FONT_STYLE, TABLE_FONT_SIZE)); // Alberto Fernandez July-2011
                    
                   jtc.requestFocus();
                    
                    // Alberto Fernandez July-2012
	                // Alberto Fernandez Oct-2011
	                //Removed this because it was leading to no enter being passed at all when
                    //forcing descendant cell editors to close on hitting enter
                    //JTables are a nightmare.
	                // This avoids to press a double enter 
                    // AF August-2012. Added again
                    InputMap inputMap = jtc.getInputMap();
                    KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
                    inputMap.put(key, "none"); //$NON-NLS-1$
                    	                
	                jtc.addFocusListener(new java.awt.event.FocusAdapter() {  
	                    public void focusLost(java.awt.event.FocusEvent evt)  {    
	                        int col = CSValuesTable.getColumnCount();   
	                        if(col>0) {      
	                            if (CSValuesTable.getCellEditor() != null) {       
	                                CSValuesTable.getCellEditor().stopCellEditing();
	                            }    
	                        }    
	                        //else;  //take care about the other rows if you need to.
	                    }
	                    public void focusGained(java.awt.event.FocusEvent evt)  {   
	    	                JTextComponent jtc = (JTextComponent)evt.getComponent();
	                        jtc.setCaretPosition(jtc.getCaretPosition());
	                    }
	                });  
	                // AF July-2012. Added from Jonathan's code
	                //Treat clicks on 'same context' as wanting to change the salience and act as
	                //though that menu item had been clicked.
                }
                // AF August-2012
                else if (b && getColumnName(column).contains(Messages.getString("SimView.contextITICSRatio"))) {
	                TableCellEditor tce = getCellEditor( row, column);
	                DefaultCellEditor dce = (DefaultCellEditor)tce;
	                Component c = dce.getComponent();
	                JTextComponent jtc = (JTextComponent)c;
	                
	                jtc.setText("");

	                jtc.requestFocus();
	                
                    InputMap inputMap = jtc.getInputMap();
                    KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
                    inputMap.put(key, "none"); //$NON-NLS-1$
                    	                
	                jtc.addFocusListener(new java.awt.event.FocusAdapter() {  
	                    public void focusLost(java.awt.event.FocusEvent evt)  {   
	                    	JTextComponent jtc = (JTextComponent)evt.getComponent();
	                    }
	                    public void focusGained(java.awt.event.FocusEvent evt)  {   
	    	                JTextComponent jtc = (JTextComponent)evt.getComponent();
	                        jtc.setCaretPosition(jtc.getCaretPosition());
	                    }
	                });        
               	
                }
                // AF. Aug-2012
//                else if (!b && getColumnName(column).contains(Messages.getString("SimView.context"))) {
//                	menuItemSingleContext.doClick();
//                }
                return b;
            }    
	    };      
	    
	    // AF July-2012
	    phasesTable.addMouseMotionListener(new MouseMotionAdapter(){
	    	   public void mouseMoved(MouseEvent e){
	    	        Point p = e.getPoint(); 
	    	        int columnId = phasesTable.columnAtPoint(p);
	    	        String column = phasesTable.getColumnName(columnId);
	    	        String tip = Messages.getString("SimView.temporalTip"); //$NON-NLS-1$
	    	        if(column.equals(GROUP_NAME)) {
	    	        	tip = Messages.getString("SimView.groupNameTip"); //$NON-NLS-1$
	    	        } else if (column.contains(Messages.getString("SimView.phase"))) { //$NON-NLS-1$
	    	        	tip = Messages.getString("SimView.trialStringTip"); //$NON-NLS-1$
	    	        } else if (column.equals(RANDOM)) {
	    	        	tip = Messages.getString("SimView.randomTip"); //$NON-NLS-1$
//	    	        } else if (column.equals(ITI)) {
//	    	        	tip = Messages.getString("SimView.ITITip"); //$NON-NLS-1$
	    	        } else if (column.equals(CONTEXT)) {
	    	        	tip = Messages.getString("SimView.contextTip"); //$NON-NLS-1$
	    	        } else if (column.equals(CONTEXT_ITI_CS_RATIO)) {
	    	        	tip = Messages.getString("SimView.contextITICSRatioTip"); //$NON-NLS-1$
	    	        }
	    	        phasesTable.setToolTipText(tip);
	    	    }//end MouseMoved
	    	}); // end MouseMotionAdapter
	    //Make single click start editing instead of needing double
	    DefaultCellEditor singleClickEditor = new DefaultCellEditor(new JTextField());
	    singleClickEditor.setClickCountToStart(1);
	    phasesTable.setDefaultEditor(Object.class, singleClickEditor);
	    
	    phasesTableModel.setPhasesTable();
	    phasesTable.setCellSelectionEnabled(false);
	    phasesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    // AF July-2012
	    // updatePhasesColumnsWidth();
	    
	    phasesTable.setFont(new Font(TABLE_FONT_NAME, TABLE_FONT_STYLE, TABLE_FONT_SIZE)); // Alberto Fern�ndez July-2011
	    phasesTable.setRowHeight(TABLE_ROW_HEIGHT);
	    
	    phasesTable.requestFocus();
	    	    
	    phasesScroll = new JScrollPane(phasesTable);
	    
	    phasesTable.setDefaultEditor(ContextConfig.class, new ContextEditor());

	}
	
	/**
	 * Set the width for the cells of the TableModel
	 */
//	public void updatePhasesColumnsWidth() {
//		phasesTable.getColumnModel().getColumn(0).setPreferredWidth(78); 
//	    for (int i=1;i<phasesTable.getColumnCount();i++) {
//	    	phasesTable.getColumnModel().getColumn(i).setPreferredWidth(i%2==0?53:239);//phasesTable.getColumnModel().getColumn(i).setPreferredWidth(i%2==0?50:200);E. Mondragon 28 Sept 2011 
//	    }
//	}
	// AF July-2012. From TD
	public void updatePhasesColumnsWidth() {		
	    for (int i=0;i<phasesTable.getColumnCount();i++) {
	    	final TableColumn tableColumn = phasesTable.getColumnModel().getColumn(i);
    	    final TableCellRenderer headRend = phasesTable.getTableHeader().getDefaultRenderer();
    	    tableColumn.setPreferredWidth(headRend.getTableCellRendererComponent(phasesTable,
            		tableColumn.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().width + 10);
    	    for(int j = 0; j < phasesTable.getRowCount(); j++) {
//    	    	int width = phasesTable.getCellRenderer(j, i).getTableCellRendererComponent(phasesTable,
//                		phasesTable.getValueAt(j, i), false, false, j, i).getPreferredSize().width;
    	    	
    	    	TableCellRenderer tcr = phasesTable.getCellRenderer(j, i);
    	    	Object val = phasesTable.getValueAt(j, i);
    	    	Component com =	tcr.getTableCellRendererComponent(phasesTable, val , false, false, j, i);
    	    	int width = com.getPreferredSize().width;
    	    		
    	    		
    	    	 

    	    	tableColumn.setPreferredWidth(Math.max(width+10,
    	            tableColumn.getPreferredWidth()));
    	    }
	    	
	    }
	}

	
	/**
	 * Creates, initializes, configures and adds a values table into the
	 * frame. Columns and rows are depending on model's parameters.
	 */
	private void addCSValuesTable() {
	    CSValuesTableModel = new CSValuesTableModel();
	    	    	    
	    CSValuesTable = new JTable(CSValuesTableModel) {
	        public boolean editCellAt(int row, int column, EventObject e) {
	            boolean b = super.editCellAt(row, column, e);
	            if ( b ) {
	                TableCellEditor tce = getCellEditor( row, column);
	                DefaultCellEditor dce = (DefaultCellEditor)tce;
	                Component c = dce.getComponent();
	                JTextComponent jtc = (JTextComponent)c;
	                
	                // AF August-2012. Delete previous content
	                jtc.setText("");

	                jtc.requestFocus();
	                
 	                // Alberto Fernandez Oct-2011
	                
	                // This avoids to press a double enter 
	                // AF July-2012
	                // AF Aug-2012 again
                    InputMap inputMap = jtc.getInputMap();
                    KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
                    inputMap.put(key, "none"); //$NON-NLS-1$
                                      
                    	                
	                jtc.addFocusListener(new java.awt.event.FocusAdapter() {  
	                    public void focusLost(java.awt.event.FocusEvent evt)  {    
	                        int col = CSValuesTable.getColumnCount();   
	                        if(col>0) {      
	                            if (CSValuesTable.getCellEditor() != null) {       
	                                CSValuesTable.getCellEditor().stopCellEditing();
	                            }    
	                        }    
	                        else;  //take care about the other rows if you need to.
	                        
	                    }
	                    public void focusGained(java.awt.event.FocusEvent evt)  {   
	    	                JTextComponent jtc = (JTextComponent)evt.getComponent();
	                        jtc.setCaretPosition(jtc.getCaretPosition());
	                    }
	                });        
	            }
	            return b;
	        }    
	    };      
	    CSValuesTable.setDefaultRenderer(String.class, new GreekRenderer());
	    CSValuesTableModel.setValuesTable(false);
	    CSValuesTable.setCellSelectionEnabled(false);
	    CSValuesTable.requestFocus();
	    
	    CSValuesScroll = new JScrollPane(CSValuesTable);
	}

	private void addGlobalValuesTable(){
		GlobalValuesTableModel = new GlobalValuesTableModel();
		GlobalValuesTable = new JTable(GlobalValuesTableModel){
			public boolean editCellAt(int row, int column, EventObject e) {
				boolean b = super.editCellAt(row, column, e);
				if ( b ) {
					TableCellEditor tce = getCellEditor( row, column);
					DefaultCellEditor dce = (DefaultCellEditor)tce;
					Component c = dce.getComponent();
					JTextComponent jtc = (JTextComponent)c;

					// AF August-2012. Delete previous content
					jtc.setText("");

					jtc.requestFocus();

					// Alberto Fernandez Oct-2011

					// This avoids to press a double enter
					// AF July-2012
					// AF Aug-2012 again
					InputMap inputMap = jtc.getInputMap();
					KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
					inputMap.put(key, "none"); //$NON-NLS-1$

					jtc.addFocusListener(new java.awt.event.FocusAdapter() {
						public void focusLost(java.awt.event.FocusEvent evt)  {
							int col = GlobalValuesTable.getColumnCount();
							if(col>0) {
								if (GlobalValuesTable.getCellEditor() != null) {
									GlobalValuesTable.getCellEditor().stopCellEditing();
								}
							}
							else;  //take care about the other rows if you need to.
						}
						public void focusGained(java.awt.event.FocusEvent evt)  {
							JTextComponent jtc = (JTextComponent)evt.getComponent();
							jtc.setCaretPosition(jtc.getCaretPosition());
						}
					});
				}
				return b;
			}
		};
		GlobalValuesTable.setDefaultRenderer(String.class, new GreekRenderer());
		GlobalValuesTable.setCellSelectionEnabled(false);
		GlobalValuesTableModel.setInitialValuesTable();
		GlobalValuesTable.requestFocus();
		GlobalValuesScroll = new JScrollPane(GlobalValuesTable);
	}

	/**
	 * Creates, initializes, configures and adds a values table into the
	 * frame. Columns and rows are depending on model's parameters.
	 */
	private void addUSValuesTable() {
	   
		USValuesTableModel = new USValuesTableModel(1);
	    	    	    
	    USValuesTable = new JTable(USValuesTableModel) {
	        public boolean editCellAt(int row, int column, EventObject e) {
	            boolean b = super.editCellAt(row, column, e);
	            if ( b ) {
	                TableCellEditor tce = getCellEditor( row, column);
	                DefaultCellEditor dce = (DefaultCellEditor)tce;
	                Component c = dce.getComponent();
	                JTextComponent jtc = (JTextComponent)c;
	                
	                // AF August-2012. Delete previous content
	                jtc.setText("");

	                jtc.requestFocus();

 	                // Alberto Fernandez Oct-2011

	                // This avoids to press a double enter 
	                // AF July-2012
	                // AF Aug-2012 again
                    InputMap inputMap = jtc.getInputMap();
                    KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
                    inputMap.put(key, "none"); //$NON-NLS-1$
                    	                
	                jtc.addFocusListener(new java.awt.event.FocusAdapter() {  
	                    public void focusLost(java.awt.event.FocusEvent evt)  {    
	                        int col = USValuesTable.getColumnCount();   
	                        if(col>0) {      
	                            if (USValuesTable.getCellEditor() != null) {       
	                            	USValuesTable.getCellEditor().stopCellEditing();      
	                            }    
	                        }    
	                        else;  //take care about the other rows if you need to.  
	                    }
	                    public void focusGained(java.awt.event.FocusEvent evt)  {   
	    	                JTextComponent jtc = (JTextComponent)evt.getComponent();
	                        jtc.setCaretPosition(jtc.getCaretPosition());
	                    }
	                });        
	            }
	            return b;
	        }    
	    };      
	    USValuesTable.setDefaultRenderer(String.class, new GreekRenderer());
	    USValuesTableModel.setInitialValuesTable();
	    USValuesTable.setCellSelectionEnabled(false);
	    USValuesTable.requestFocus();
	    
	    // AF July-2012. From TD
	    USValuesTable.addMouseMotionListener(new MouseMotionAdapter(){
	    	   public void mouseMoved(MouseEvent e){
	    	        Point p = e.getPoint(); 
	    	        int row = USValuesTable.rowAtPoint(p);
	    	        String tip;
	    	        switch(row) {
	    	        case 0:
	    	        	tip = Messages.getString("SimView.betaPlusTip"); //$NON-NLS-1$
	    	        	break;
	    	        case 1:
	    	        	tip = Messages.getString("SimView.betaMinusTip"); //$NON-NLS-1$
	    	        	break;
	    	        case 2:
	    	        	tip = Messages.getString("SimView.lambdaPlusTip"); //$NON-NLS-1$
	    	        	break;
	    	        case 3:
	    	        	tip = Messages.getString("SimView.lambdaMinusTip"); //$NON-NLS-1$
	    	        	break;
//	    	        case 4:
//	    	        	tip = "Decay factor for eligibility traces."; //$NON-NLS-1$
//	    	        	break;
//	    	        case 5:
//	    	        	tip = "Discount factor."; //$NON-NLS-1$
//	    	        	break;
//	    	        case 6:
//	    	        	tip = "Response threshold."; //$NON-NLS-1$
//	    	        	break;
//	    	        case 7:
//	    	        	tip = "Context stimulus salience."; //$NON-NLS-1$
//	    	        	break;
	    	        default:
	    	        	tip = ""; //$NON-NLS-1$
	    	        }
	    	        USValuesTable.setToolTipText(tip);
	    	    }//end MouseMoved
	    	}); // end MouseMotionAdapter
	    
	    USValuesScroll = new JScrollPane(USValuesTable);
	}

	/**
	 * Set the width for the cells of the TableModel
	 */
	public void updateUSValuesColumnsWidth() {
		USValuesTable.getColumnModel().getColumn(0).setPreferredWidth(70);
	    for (int i=1;i<USValuesTable.getColumnCount();i++) {
	    	USValuesTable.getColumnModel().getColumn(i).setPreferredWidth(70);
	    }
	}
	
	
	/**
     * Responds to the user when he presses a menu option.
     */
    public void addMenuListeners(ActionListener event) {
        menuItemNew.addActionListener(event);
        menuItemOpen.addActionListener(event);
        menuItemSave.addActionListener(event);
        menuItemExport.addActionListener(event);
        menuItemQuit.addActionListener(event);
        menuItemComb.addActionListener(event);
        menuItemUSAcrossPhases.addActionListener(event);
        menuItemSetCompound.addActionListener(event);
        menuItemSetConfiguralCompounds.addActionListener(event); // Alberto Fernandez August-2011
        menuItemGuide.addActionListener(event);
        menuItemAbout.addActionListener(event); 
        // Alberto Fernandez. July 2012
//        menuItemSingleContext.addActionListener(event);
        menuItemContextAcrossPhases.addActionListener(event);
        menuItemContext.addActionListener(event);
    }
    
    /**
     * Responds to the user when he presses a button.
     */
    public void addButtonListeners(ActionListener event) {
        setVariablesBut.addActionListener(event);
        clearBut.addActionListener(event);
        runBut.addActionListener(event);
        dispGraphBut.addActionListener(event);
        addPhaseBut.addActionListener(event);
        removePhaseBut.addActionListener(event);
        removeGroupBut.addActionListener(event);
        addGroupBut.addActionListener(event);
        
    }
    
    /**
     * Sets the status of a component, it could be a button or
     * a menu item. This helps the smooth procedure of the application.
     * It stops the user to choose an inappropriate action.
     * @param mode set the component accordingly to this boolean value. 
     * @param b the component that needs to be change. The string contains
     * the actionCommand.
     */
    public void setStatusComponent(boolean mode, String b) {
    	if (b == setVariablesBut.getActionCommand()) setVariablesBut.setEnabled(mode);
    	if (b == clearBut.getActionCommand()) clearBut.setEnabled(mode);
    	if (b == runBut.getActionCommand()) runBut.setEnabled(mode);
    	if (b == dispGraphBut.getActionCommand()) dispGraphBut.setEnabled(mode);
    	if (b == menuItemSave.getActionCommand()) menuItemSave.setEnabled(mode);
    	if (b == menuItemExport.getActionCommand()) menuItemExport.setEnabled(mode);
    	if (b == menuItemUSAcrossPhases.getActionCommand()) menuItemUSAcrossPhases.setState(mode);
    	if (b == menuItemSetCompound.getActionCommand()) menuItemSetCompound.setState(mode);
    	if (b == menuItemSetConfiguralCompounds.getActionCommand()) menuItemSetConfiguralCompounds.setState(mode);
    	if (b.equals(menuItemContext.getActionCommand())) menuItemContext.setSelected(mode);
    	if (b.equals(menuItemContextAcrossPhases.getActionCommand())) menuItemContextAcrossPhases.setSelected(mode);
//    	if (b.equals(menuItemSingleContext.getActionCommand())) menuItemSingleContext.setSelected(mode);
   }
    
    /**
     * Clear the Area of the results. 
     */
    public void clearOutputArea() {
        outputArea.setText("");
    }
    
    /**
     * It displays a JOptionPane.showInputDialog on top of the view's
     * frame asking an integer. The request message and current value
     * are provided as arguments.
     * @param s the message that is been displayed to the screen.
     * @param cur the current value that the variable, that will be change, has.
     * @return the new value of the variable.
     */
    public int getIntInput(String s, String cur) {
        String input = JOptionPane.showInputDialog(s, cur);
		if (input == null) return -1;
        else return Integer.parseInt(input);
	}
    
    /**
     * Returns the phases' table model. This helps to get the tables contents
     * or set some new ones.
     * @return the phases' table model.
     */
    public PhasesTableModel getPhasesTableModel() {
        return phasesTableModel;
    }
    
    /**
     * Returns the phases' table.
     * @return the phases' table.
     */
    public JTable getPhasesTable() {
        return phasesTable;
    }
    
    /**
     * Returns the values' table model. This helps to get the table's contents
     * or set some new ones.
     * @return the values' table model.
     */
    public CSValuesTableModel getCSValuesTableModel() {
       return CSValuesTableModel;
    }
   
    /**
     * Returns the values' table model. This helps to get the table's contents
     * or set some new ones.
     * @return the values' table model.
     */
    public USValuesTableModel getUSValuesTableModel() {
       return USValuesTableModel;
    }
   
    /**
     * Returns the values' table.
     * @return the values' table.
     */
    public JTable getCSValuesTable() {
       return CSValuesTable;
    }
    
    /**
     * Returns the values' table.
     * @return the values' table.
     */
    public JTable getUSValuesTable() {
       return USValuesTable;
    }
    
    /*
     * Add a new phase to the phasesTableModel
     */
    public void addPhase() {
    	phasesTableModel.addPhase();
    	updatePhasesColumnsWidth();
    }
    
    /*
     * Add a new group to the phasesTableModel
     */
    public void addGroup() {
    	phasesTableModel.addGroup();
    	updatePhasesColumnsWidth();
    }
    
    /*
     * Remove the last phase of the phasesTableModel
     */
    public void removePhase(){
    	phasesTableModel.removePhase();
    	updatePhasesColumnsWidth();
    }
    
    /*
     * Remove the last group of the phasesTableModel
     */
    public void removeGroup() {
    	phasesTableModel.removeGroup();
    	updatePhasesColumnsWidth();
    }
	
    /*
     * Add US across phases
     */
    public void addUSPhases() {
    	USValuesTableModel.addPhases();
    	USValuesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	updateUSValuesColumnsWidth();
    }
    
    /*
     * Remove US across phases 
     */
    public void removeUSPhases() {
    	USValuesTableModel.removePhases(1);
    	USValuesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
	
    
    /**
     * showError displays error message dialogs directly to the user.
     * @param errMessage - the String to be displayed.
     */
    public void showError(String errMessage) {
    	JOptionPane.showMessageDialog(this, errMessage, Messages.getString("SimView.errorTitle"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
    }
    
    /**
     * showAbout displays information dialogs directly to the user.
     * @param message - the String to be displayed.
     */
    public void showAbout(String message) {
    	JOptionPane.showMessageDialog(this, message, Messages.getString("SimView.aboutTitle"), JOptionPane.PLAIN_MESSAGE); //NO_OPTION //$NON-NLS-1$
    }
    
    /**
     * showAbout displays information dialogs directly to the user.
     * @param message - the String to be displayed.
     */
    //public void showAboutLogo(String path) {
    public void showAboutLogo() {
    	// Modified by E Mondragon July 29, 2011
    	JFrame.setDefaultLookAndFeelDecorated(false);

    	JFrame about = new JFrame();
    

	    JPanel aboutPanel = new JPanel();
	    aboutPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		aboutPanel.setBackground(Color.WHITE);
		
		//modified by E.Mondragon. July 29, 2011
		//ImageIcon icon = createImageIcon(path, "About");
		
		
		ImageIcon icon = createImageIcon("/simulator/extras/uc.png", "");
		
		aboutPanel.setBorder(new SimBackgroundBorder(icon.getImage(), true));
		about.getContentPane().add(aboutPanel);
		about.pack();
        about.setLocation(200, 200);
        //about.setSize(520,320);
        about.setSize(596,435);//EMP 11 Oct. 2012
        about.setVisible(true);//EMP 11 Oct. 2012
        
       
		    
	        
	    
//        about.setTitle("RESCORLA & WAGNER SIMULATOR © ver. 3.1");//E.Mondragon 30 Sept 2011
        about.setTitle(Messages.getString("SimView.title")); // A.Fernandez July-2012
        ImageIcon icon2 = createImageIcon("/simulator/extras/icon_16.png","");//icon_16.png", ""); E.Mondragon 30 Sept 2011
		about.setIconImage(icon2.getImage());//E.Mondragon 30 Sept 2011
        
        
    }

    /**
     * Sets the output for the JTextArea object. Adds the String that is been 
     * passed from the application and then it displays it.
     * @param msg
     */
    public void setOutput(String msg) {
        outputArea.setText(msg);
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL =  this.getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println(Messages.getString("SimView.404Error") + path); //$NON-NLS-1$
            return null;
        }
    }
    
    
	
	/*
     * Order a vector of cues by phases
     * @param vec Vector of records with cues to order
     * @param nlambdabeta number of lambda and beta cues in vec
     * @return Vector vector of cues ordered by phases
     */
    private Vector orderByPhase(Vector vec, int nlambdabeta) {   	
    	Vector v = new Vector();
    	
    	// alphas
    	for (int j=0; j<(vec.size()-nlambdabeta); j++) {
    		v.addElement((Object[])vec.get(j));
    	}
    	// lambdas and betas per phase
    	for (int i=1; i<=model.getPhaseNo(); i++){ 
    		for (int k=(vec.size()-nlambdabeta); k<vec.size(); k++){ // finding lambda+, lambda-, beta+, beta-
    			String cuename = (String)((Object[])vec.get(k))[0];
    			if ((""+cuename.charAt(cuename.length()-1)).equals(""+i)) v.addElement(vec.get(k));
    		}
    	}
    	return v;
    }


    /*
     * Overwrite the names alpha, lambda and beta to the corresponding greek characters
     * in the ValuesTable
     */
	private class GreekRenderer extends DefaultTableCellRenderer {
		@Override
		public void setText(String name) {
			String newValue = name;
			if (name.indexOf("lambda")!=-1) newValue = name.replace("lambda", "\u03BB");
    		if (name.indexOf("beta")!=-1) newValue = name.replace("beta", "\u03B2");
    		if (name.indexOf("alpha")!=-1) newValue = name.replace("alpha", "\u03B1");
    		super.setText(newValue);
		}
	}
    
    
	/******************************************************************************************************************/   
	/******************************************************************************************************************/   
	
	// Alberto Fernandez. July-2012
	
//	/** This class is the values' table model */
//	class ValuesTableModel extends AbstractTableModel {
	
	// Extracted to own class (as TD)
	
	/********************************************************/
	/********************************************************/
    
    
    
	/** This class is the phases' table model */
	class PhasesTableModel extends ValuesTableModel {
        
        /**
         * PhasesTableModel's Constructor method.
         */
        public PhasesTableModel() {
            super();        }
             
        /*
         * Add a new column to the vector data 
         */
        public void addPhase() {
        	//col = model.getPhaseNo()*2 + 1;
        	col = model.getPhaseNo()*4 + 1; // AF July-2012
        	columnNames = getColNames();
        	Vector newData = new Vector();
        	for (Iterator it = data.iterator();it.hasNext();) {
        		Object record[] = new Object[col];
        		Object[] oldRecord = (Object[]) it.next();
        		System.arraycopy(oldRecord, 0, record, 0, oldRecord.length);
        		record[record.length-4] = "";
        		record[record.length-3] = new ContextConfig();
        		record[record.length-2] = DEFAULT_ITI_CS_RATIO;
        		record[record.length-1] = new Boolean(false);
        		newData.add(record);
        	}
        	data = newData;
        	fireTableChanged(null); // notify everyone that we have a new table.
        	// AF July-2012
        	if(!useContext()) {
            	removeOmegaPhases();
            }
        }
        
        /*
         * Remove the last column of the vector data 
         */
        public void removePhase() {
        	if (col > 4) {
        		col = model.getPhaseNo()*4 + 1;
        		columnNames = getColNames();
            	Vector newData = new Vector();
            	for (Iterator it = data.iterator();it.hasNext();) {
            		Object record[] = new Object[col];
            		Object[] oldRecord = (Object[]) it.next();
            		System.arraycopy(oldRecord, 0, record, 0, record.length);
            		newData.add(record);
            	}
            	data = newData;
            	fireTableChanged(null); // notify everyone that we have a new table.
        	}
        	// AF July-2012
        	if(!useContext()) {
            	removeOmegaPhases();
            }
        }
        
        /*
         * Add a new row to the vector data 
         */
        // Modified AF July-2012
        public void addGroup() {
        	row = model.getGroupNo();
        	Object record[] = new Object[col];
            for (int c = 0; c < col; c++) {
            	if (c == 0) record[c] = Messages.getString("SimView.group") + row; //$NON-NLS-1$
            	else if (c % 4 == 2) record[c] = new ContextConfig();
            	else if (c % 4 == 3) record[c] = DEFAULT_ITI_CS_RATIO;
            	else if (c % 4 == 0) record[c] = new Boolean(false);
                	else record[c] = "";
            }
            data.addElement(record);
            fireTableChanged(null); // notify everyone that we have a new table.
        	// AF July-2012
        	if(!useContext()) {
            	removeOmegaPhases();
            }
        }
        
        /*
         * Remove last row of the vector data 
         */
        public void removeGroup() {
        	row = model.getGroupNo();
        	data.remove(data.size()-1);
        	fireTableChanged(null);
        	// AF July-2012
        	if(!useContext()) {
            	removeOmegaPhases();
            }
        }
        
        /*
         * Initializes and configures the table with some initial values.
         */
        public void setPhasesTable() {
        	clearHidden(); // AF July-2012
        	data = new Vector();
            try {
                col = model.getPhaseNo()*4 + 1;
                row = model.getGroupNo();
                columnNames = getColNames();
                
                for (int r = 0; r < row; r++) {
    	            Object record[] = new Object[col];
                    for (int c = 0; c < col; c++) {
                    	if (c == 0) record[c] = Messages.getString("SimView.group") + (r + 1); //$NON-NLS-1$
                    	else if (c % 4 == 2) record[c] = new ContextConfig();
                    	else if (c % 4 == 3) record[c] = DEFAULT_ITI_CS_RATIO;
                    	else if (c % 4 == 0) record[c] = new Boolean(false);
                        	else record[c] = "";
                    }
                    data.addElement(record);
                }
                fireTableChanged(null); // notify everyone that we have a new table.
            	// AF July-2012
            	if(!useContext()) {
                	removeOmegaPhases();
                }
            }
            catch(Exception e) {
                data = new Vector(); // blank it out and keep going. 
                e.printStackTrace();                
            }
        }
        
        /**
         * Return the names of the table's column names.
         * @return an array with the table's column names.
         */
	    private String[] getColNames() {
	        String[] s = new String[col];
	        for (int c = 0; c < col; c++) {
                if (c == 0) s[c] = GROUP_NAME;
            	else if (c % 4 == 2) s[c] = CONTEXT;
        		else if (c % 4 == 3) s[c] = CONTEXT_ITI_CS_RATIO;
            	else if (c % 4 == 0) s[c] = RANDOM; 
                else s[c] = Messages.getString("SimView.phaseSpace") + (c/4 + 1); //$NON-NLS-1$
            }
	        return s;
	    }
	    
	  
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
        	// AF July-2012
            if(getColumnName(col).equals(CONTEXT) && !model.contextAcrossPhase()) {
         	   return false;
            }
            return true;
         }
        
    	// Alberto Fernandez. July-2012
        /**
         * Returns CS/ITI ratio for the given group and phase
         * @param group the group
         * @param phase the phase
         * @return the CS/ITI ratio of the group and phase.
         */
        public int getCS_ITI_ratio(SimGroup group, int phase) {
        	int SC_ITI_ratio = 0;
        	for (int r=0; r < model.getGroupNo(); r++){
        		if (group.getNameOfGroup().equals(phasesTableModel.getValueAt(r,0))){
        			// Group found
        			//SC_ITI_ratio = (Integer) phasesTableModel.getValueAt(r, phase*4-1);
        			SC_ITI_ratio = Integer.parseInt(phasesTableModel.getValueAt(r, phase*4-1).toString());
        			break;
        		}
        	}
        	return SC_ITI_ratio;
     	}

    }

	
	
	/********************************************************/
	/********************************************************/
		
	
	
	
	/** This class is the values' table model */
	class CSValuesTableModel extends ValuesTableModel {
        
	    /**
         * ValuesTableModel's Constructor method.
         */
        public CSValuesTableModel() {
        	super();
        }
         
        /**
         * Return the names of the table's column names.
         * @return an array with the table's column names.
         */
	    private String[] getColNames() {
	        String[] s = new String[col];
	        for (int c = 0; c < col; c++) {
                if (c == 0) s[c] = "CS parameter"; //$NON-NLS-1$
                else if (col==2) s[c]=Messages.getString("SimView.value"); //$NON-NLS-1$
                else s[c] = Messages.getString("SimView.phaseSpace") + (c + 1); //$NON-NLS-1$
            }
	        return s;
	    }
	    
	  
        /*
         * Initializes and configures the table with some initial values.
         * @param iniValues. If true, we initialize all variables to "" or by default 
         * without taking into account last values of cues  
         */
        public void setValuesTable(boolean iniValues) {   
        	// if data had some values before, then reuse them
        	Vector data2 = new Vector((Vector)getData().clone());
        	Vector data1 = new Vector();        	          
        	data = new Vector();
        	
        	try {	
        		row = model.getNumberAlphaCues();
        		col = 2;
        		columnNames = getColNames();
        		
        		TreeMap<String,Double> tm = model.getAlphaCues();
        		Iterator<String> it = tm.keySet().iterator();
            	while (it.hasNext()) {
            		String pair = (String)it.next();	
            		//Disregard context cues, alphas for them are set elsewhere
            		if(!Context.isContext(pair)) {
            			
	            		if (pair.length()==1){
	            			Object record[] = new Object[col];
	            			int isInValues = -1;
	            			boolean configuralEmpty = false;
	        			
	            			for (int c = 0; c < col; c++) {
	            				// cue name
	            				if (c==0) {
	            					record[c] = pair;
	            					// Alberto Fernandez August-2011
	            					if (getIsSetConfiguralCompounds() && model.getConfigCuesNames().containsKey(pair)) {
	            						String compoundName = model.getConfigCuesNames().get(pair);
	            						//String interfaceName = "�(" + compoundName + ")";
	            						String interfaceName = "c(" + compoundName + ")";
	            						record[c] = interfaceName;
	            					}
	            					//isInValues = isInValuesTable(data2,pair);		
	            					isInValues = isInValuesTable(data2,(String)record[c]);		
	            				}
	            				// cue value
	            				// If it exists already --> last value from data2
	               				//else if (isInValues>-1 && !iniValues) {
	                 			else if (isInValues>-1 && !iniValues && 
	                 					 !(((String)record[0]).length()>1 && ((Object[])data2.get(isInValues))[c]=="")) {
	            					record[c]=((Object[])data2.get(isInValues))[c]; 
	            				}
	            				// Alberto Fernandez Sept-2011
	            				// If it didnt exist --> if c(AB) ==> A*B otherwise default value
	            				else if (((String)record[0]).length()>1) {
	            					
	            					// find record[0]= c(AB)
	            					Double val = 1.0;
	            					boolean found, missing = false;
	            					String compound = ((String)record[0]).substring(2, ((String)record[0]).length()-1);
	            					
	            					// AF July-2012. This in TD. Not sure should be here as well !!
	            					//if(compound.startsWith(Simulator.OMEGA+"")) { //$NON-NLS-1$
	            					//	compound = compound.substring(1);
	            					//}
	            					
	            					String cue;
	
	            					for (int i = 0; i < compound.length(); i++) {
	            						cue = compound.substring(i, i+1);
	            						// find cue in Vector data2
	            						found = false;
	            						missing = false; // there is no value for current cue
	            						if(Context.isContext(cue)) {
	            							val *= model.getContexts().get(cue).getAlpha();
	            							found = true;
	            						} else {
	            							for (int j = 0; j < data2.size(); j++) {
	            								Object cue_value_pair[] = (Object [])data2.get(j);
	            								if (cue.equals(cue_value_pair[0])) {
	            									String s = (String) cue_value_pair[1];
	            									if (s != "") { //$NON-NLS-1$
	            										val = val * Double.parseDouble(s);
	            										found = true;
	            										break;
	            									}
	            								}
	            							}
	            						}
	            						if (!found) {
	            							missing = true;
	            							break;
	            						}
	            					}
	            					if (!missing) { //found) {
	            						record[c] = val.toString();
	            					}
	            					else {
	            						record[c] = "";
	            					}
	            				}
	            				else {
	            					record[c]=DEFAULT_ALPHA; // AF. Aug-2012
	            				}
	            			}
	            			data1.add(record);
	            		}
            		}
            	}
            	setData(data1);
            	fireTableChanged(null); // notify everyone that we have a new table.
        	}
        	catch(Exception e) {
        		setData(new Vector()); // blank it out and keep going. 
        		e.printStackTrace();                
        	}
        }
	}
	

	
	/********************************************************/
	/********************************************************/
		
	
	class GlobalValuesTableModel extends ValuesTableModel {

		private static final String GAMMA = "gamma"; //$NON-NLS-1$

		private final String[] GlobalsNames = { GAMMA };
		private final String[] GlobalsValues = { "0.1" };

		private String[] getColNames() {
			String[] colNames = new String[2];
			colNames[0] = "parameter";
			colNames[1] = "value";
			return  colNames;
		}

		public void setInitialValuesTable() {
			Vector data1 = new Vector();
			col = 2;
			row = GlobalsNames.length;
			columnNames = getColNames();

				for (int r = 0; r < row; r++) { // row ser� 4 (betas y lambdas)
					Object record[] = new Object[col];
					record[0] = GlobalsNames [r];
					for (int c = 1; c < col; c++) {
						// AF July-2012
						record[c] = GlobalsValues[r];
//        			if (((String)record[0]).indexOf("lambda+")!=-1 && c==1) record[c]="1";
//        			else if (((String)record[0]).indexOf("lambda-")!=-1 && c==1) record[c]="0";
//        			else if (((String)record[0]).indexOf("beta+")!=-1 && c==1)   record[c]="0.5";
//        			else if (((String)record[0]).indexOf("beta-")!=-1 && c==1)   record[c]="0.45";
//        			else record[c]="";
					}
					data1.addElement(record);
				}
				setData(data1);
				fireTableChanged(null); // notify everyone that we have a new table.
		}

	}


	/** This class is the values' table model */
	class USValuesTableModel extends ValuesTableModel {
        
		/**
		 * US Names
		 */
		private static final String LAMBDA_MINUS = "lambda-"; //$NON-NLS-1$
		private static final String LAMBDA_PLUS = "lambda+"; //$NON-NLS-1$

		private final String[] USnames = {LAMBDA_PLUS, LAMBDA_MINUS};
		private final String[] USvalues= {"1","0"};

    	
		/**
         * ValuesTableModel's Constructor method.
         */
        public USValuesTableModel(int col) {
        	super();
        }
        
        /**
         * Return the names of the table's column names.
         * @return an array with the table's column names.
         */
	    private String[] getColNames() {
	        String[] s = new String[col];
	        for (int c = 0; c < col; c++) {
                if (c == 0) s[c] = Messages.getString("SimView.164"); //$NON-NLS-1$
                else if (col==2) s[c]=Messages.getString("SimView.value"); //$NON-NLS-1$
                else s[c] = Messages.getString("SimView.phaseSpace") + c; //$NON-NLS-1$
            }
	        return s;
	    }
	   
	    /*
         * Initializes and configures the table with some initial values.
         * @param iniValues. If true, we initialize all variables to "" or by default 
         * without taking into account last values of cues  
         */
        public void setInitialValuesTable() {   
        	Vector data1 = new Vector();   
        	col = 2;
        	row = USnames.length;
        	columnNames = getColNames();
        	
        	try{
        	for (int r = 0; r < row; r++) { // row ser� 4 (betas y lambdas)
        		Object record[] = new Object[col];
        		record[0] = USnames [r];
        		for (int c = 1; c < col; c++) {
        			// AF July-2012
        			record[c] = USvalues[r];
//        			if (((String)record[0]).indexOf("lambda+")!=-1 && c==1) record[c]="1";  
//        			else if (((String)record[0]).indexOf("lambda-")!=-1 && c==1) record[c]="0"; 
//        			else if (((String)record[0]).indexOf("beta+")!=-1 && c==1)   record[c]="0.5"; 
//        			else if (((String)record[0]).indexOf("beta-")!=-1 && c==1)   record[c]="0.45"; 
//        			else record[c]=""; 
        		}			
        		data1.addElement(record);       			
        	}
        	setData(data1);
        	fireTableChanged(null); // notify everyone that we have a new table.
        	}
        	catch(Exception e) {
        		setData(new Vector()); // blank it out and keep going. 
        		e.printStackTrace();                
        	}
        }
        
        /*
         * Initializes and configures the table with some initial values.
         * @param iniValues. If true, we initialize all variables to "" or by default 
         * without taking into account last values of cues  
         */
        public void setValuesTable(boolean iniValues, boolean allphases) {   
        	// if data had some values before, then reuse them
        	Vector data2 = new Vector((Vector)getData().clone());
        	if (allphases) {
        		Object record2[] = (Object[]) data2.firstElement();
        		if (record2.length<=(model.getPhaseNo()+1)) addUSPhases();
        		else removePhases(model.getPhaseNo());
        	}
        	else removeUSPhases();
        }
        
        /*
         * Add a new column to the vector data 
         */
        public void addPhases() {
        	col = model.getPhaseNo() + 1;
        	columnNames = getColNames();
        	Vector newData = new Vector();
        	for (Iterator it = data.iterator();it.hasNext();) {
        		Object record[] = new Object[col];
        		Object[] oldRecord = (Object[]) it.next();
        		System.arraycopy(oldRecord, 0, record, 0, oldRecord.length);
        		for (int i=oldRecord.length; i<col; i++) record[i]="";
        		newData.add(record);
        	}
        	data = newData;
        	fireTableChanged(null); // notify everyone that we have a new table.
        }
        
        /*
         * Remove the last column of the vector data 
         */
        public void removePhases(int phases) {
        	col = phases+1;
        	columnNames = getColNames();
        	Vector newData = new Vector();
        	for (Iterator it = data.iterator();it.hasNext();) {
        		Object record[] = new Object[col];
        		Object[] oldRecord = (Object[]) it.next();
        		System.arraycopy(oldRecord, 0, record, 0, record.length);
        		newData.add(record);
        	}
        	data = newData;
        	fireTableChanged(null); // notify everyone that we have a new table.
        }

	
        
	}

	// Alberto Fernandez. July 2012
	// Adopted from TD
	
    /**
     * 
     * @return a boolean indicating whether to use the context stimulus.
     */
    public boolean useContext() {
    	return !menuItemContext.isSelected();
    }
    
    
    /**
     * Switch the omega/context variable on or off.
     * @param on
     */
    
    public void toggleContext(boolean on) {
    	if(on ) {
    		addOmegaPhases();
    	} else {
    		removeOmegaPhases();
    	}
    }
 
    /*
     * Remove Omega across phases 
     */
    public void removeOmegaPhases() {
    	//otherTableModel.removePhases(1);
    	//otherValuesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	
    	ContextConfig.clearDefault();
    	// AF Aug-2012. If context was already not selected, then hiddenColumns is not empty
    	//hiddenColumns.clear();
    	Map<TableColumn, Integer> hiddenColumnsTMP = new LinkedHashMap<TableColumn, Integer>();
    	
    	for(TableColumn column : Collections.list(phasesTable.getColumnModel().getColumns())) {
    		// AF July-2012
    		//if(column.getHeaderValue().equals(CONTEXT)) {
        	if(column.getHeaderValue().equals(CONTEXT) || column.getHeaderValue().equals(CONTEXT_ITI_CS_RATIO)) {
    			hiddenColumnsTMP.put(column, column.getModelIndex());    			
    		}
    	}
    	// if context was already = NO, there are not hidden columns
    	if (!hiddenColumnsTMP.isEmpty()) {
    		hiddenColumns = hiddenColumnsTMP;
    	}
    	for(TableColumn column : Collections.list(phasesTable.getColumnModel().getColumns())) {
    		// AF July-2012
    		if(column.getHeaderValue().equals(CONTEXT) || column.getHeaderValue().equals(CONTEXT_ITI_CS_RATIO)) {
    			phasesTable.getColumnModel().removeColumn(column);
    		}
    	}
    }
    

    /*
     * Add Omega across phases
     */
    public void addOmegaPhases() {
    	/*otherTableModel.addPhases();
    	otherValuesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	updateOtherValuesColumnsWidth();*/
    	//int colCount = (phasesTableModel.getColumnCount()-1)/5;
    	//ContextConfig.clearDefault();
    	Iterator<Entry<TableColumn, Integer>> it = hiddenColumns.entrySet().iterator();
    	// AF July-2012
       //	for(int i = 0; i < (phasesTableModel.getColumnCount()-1)/4 && it.hasNext(); i++) {
    	for(int i = 0; it.hasNext(); i++) {
    		Entry<TableColumn, Integer> column = it.next();
    		int oldIndex = column.getValue();
    		phasesTable.getColumnModel().addColumn(column.getKey());
    		int currIndex = phasesTable.getColumnModel().getColumnCount()-1;
    		phasesTable.getColumnModel().moveColumn(currIndex, oldIndex);
    	}
    	hiddenColumns.clear();
    }

    /**
     * Bulk set the alpha value for all contexts in a table and make all contexts
     * phi.
     * @param alpha
     */
    
    public void setOmegaSalience(double alpha) {
    	ContextConfig.clearDefault();
    	contextAlpha = alpha;
    	for(TableColumn column : Collections.list(phasesTable.getColumnModel().getColumns())) {
    		if(column.getHeaderValue().equals(CONTEXT)) {
    			int col = column.getModelIndex();
    			for(int i = 0; i < phasesTableModel.getRowCount(); i++) {
    				((ContextConfig) phasesTableModel.getValueAt(i, col)).setAlpha(alpha);
    				((ContextConfig) phasesTableModel.getValueAt(i, col)).setContext(Context.PHI);
    				phasesTableModel.fireTableCellUpdated(i, col);
    			}
    		}
    	}
    }

    // AF Sep-2012
    /**
     * Bulk set the alpha and context values for all contexts in a table
     * @param alpha
     * @param Context
     */
    
    public void setOmegaSalience(Context context, double alpha) {
    	ContextConfig.clearDefault();
    	contextAlpha = alpha;
    	for(TableColumn column : Collections.list(phasesTable.getColumnModel().getColumns())) {
    		if(column.getHeaderValue().equals(CONTEXT)) {
    			int col = column.getModelIndex();
    			for(int i = 0; i < phasesTableModel.getRowCount(); i++) {
    				((ContextConfig) phasesTableModel.getValueAt(i, col)).setAlpha(alpha);
    				//((ContextConfig) phasesTableModel.getValueAt(i, col)).setContext(Context.PHI);
    				((ContextConfig) phasesTableModel.getValueAt(i, col)).setContext(context);
    				phasesTableModel.fireTableCellUpdated(i, col);
    			}
    		}
    	}
    }

	/**
	 * Clears the list of hidden context columns.
	 */
	public void clearHidden() {
		hiddenColumns.clear();
	}

	/**
	 * 
	 * @return a boolean indicating whether to use a different context stimulus per phase
	 */
	
	public boolean getIsOmegaAcrossPhases() {
		return menuItemContextAcrossPhases.isSelected();
	}

	public double getContextAlpha() {
		return contextAlpha;
	}

	public void setContextAlpha(double contextAlpha) {
		this.contextAlpha = contextAlpha;
	}

    /**
     * It displays a JOptionPane.showInputDialog on top of the view's
     * frame asking a double. The request message and current value
     * are provided as arguments.
     * @param s the message that is been displayed to the screen.
     * @param cur the current value that the variable, that will be change, has.
     * @return the new value of the variable.
     */
    public double getDoubleInput(String s, String cur) {
        String input = JOptionPane.showInputDialog(s, cur);
		if (input == null) return -1;
        else return Double.parseDouble(input);
	}
 

	/**
	 * Reset the menus, tables and output to starting state.
	 */
	
	public void reset() {
		//Context off
		menuItemContext.setSelected(true);
		//Phases table reeanbled
		getPhasesTable().setEnabled(true);
		//Export off
	    setStatusComponent(false, "Export"); //$NON-NLS-1$
	    //Save off
		setStatusComponent(false, "Save"); //$NON-NLS-1$
		//Set variables button on
	    setStatusComponent(true,  "setVariables"); //$NON-NLS-1$
	    //Run button off
	    setStatusComponent(false, "run"); //$NON-NLS-1$
	    //Figures button off
	    setStatusComponent(false, "dispGraph"); //$NON-NLS-1$
	    //Clear output
	    clearOutputArea();
	    //US per phase off
	    setIsUSAcrossPhases(false);
	    setStatusComponent(false, "SetUSAcrossPhases"); //$NON-NLS-1$
	    //Compounds off
	    setStatusComponent(false, "SetCompound"); //$NON-NLS-1$
	    // Added by Alberto Fernandez August-2011
	    setIsSetCompound(false);
	    //Configurals off
	    setStatusComponent(false, "SetConfiguralCompounds");  //$NON-NLS-1$
	    setIsSetConfiguralCompounds(false);
	    
	    clearOutputArea();
	    
	}


	// AF Aug-2012 from TD
	/**
	 * @param lock true to lock the UI
	 */
	public void setUILocked(boolean lock) {
		menuBar.setEnabled(!lock);
		addGroupBut.setEnabled(!lock);
		addGroupBut.setEnabled(!lock);
		setVariablesBut.setEnabled(!lock);
		clearBut.setEnabled(!lock);
		runBut.setEnabled(!lock);
		dispGraphBut.setEnabled(!lock);
		addPhaseBut.setEnabled(!lock);
		removePhaseBut.setEnabled(!lock);
	}



}