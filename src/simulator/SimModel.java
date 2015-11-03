/**
 * SimModel.java
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

import java.util.*;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import extra166y.Ops;
import extra166y.ParallelArray;

import simulator.configurables.ContextConfig;
import simulator.configurables.ContextConfig.Context;

/**
 * SimModel is the main object model of the inputed data. It holds the users values
 * about the number of groups, phases and combinations. Updates the groups with new 
 * values from the value table and concentrates on the right creation of the final
 * cue list. Every time that a new experiment starts, a new experiment starts the
 * old model object is disposed and anew one is created, all other objects that link
 * with this model are getting refreshed as well. A disposal of the model means that
 * all data stored is getting lost but this is normal as a new experiment starts.
 */
//public class SimModel {
public class SimModel implements Runnable{
    
	
	private int groupsNo, phasesNo, combinationNo;
    private TreeMap<String,SimGroup> groups; 
    private SortedMap<String,Double> values;
    // Alberto Fern‡ndez August-2011
    // Mapping for configural cues. <K,V> K=virtual name (lower case letter), V=compound
    private TreeMap<String,String> configCuesNames; // e.g. <a,AB>
    
    // Alberto Fernandez Nov-2011
    ArrayList<String> listAllCues; // This is a sorted list of all existing cues 
    
    // Alberto Fernandez July-2012
	private boolean isContextAcrossPhase;
	/** Whether to simulate contextual stimulus. **/
	private boolean useContext;

	private double contextAlpha;

	private ModelControl control;
	
	// AF Sep 2012, from TD
    //SimGroup parallel pool
    private ParallelArray<SimGroup> groupPool;

    /** Procedure to run a group **/
    final Ops.Op<SimGroup, SimGroup> update = new Ops.Op<SimGroup, SimGroup>() {
  		public SimGroup op(final SimGroup current) {
  	    	try {
  	    		current.run();;
  	    	}
  	    	catch (java.lang.OutOfMemoryError e) {
  	        	//.err.println(e);
  	        	control.setCancelled(true);
  	        	JOptionPane.showMessageDialog(null, Messages.getString("SimModel.OutOfMemoryError"), Messages.getString("SimView.aboutTitle"), JOptionPane.PLAIN_MESSAGE); //NO_OPTION //$NON-NLS-1$
  	    	}

  			//current.run();
  			addCueNames(current.getCuesMap());
  		return current;
  	}};

    
    /**
     * SimModel's Constructor method.
     */
    public SimModel() {
        values = new TreeMap();
        groups = new TreeMap(); 
        //Initial values of groups, phases and combinations.
        groupsNo = phasesNo = 1;
        combinationNo = 1000; // 20; modified Alberto Fern‡ndez July-2011
        // Alberto Fern‡ndez August-2011
        configCuesNames = new TreeMap<String,String>();
        // Alberto Fernandez Nov-2011
        listAllCues = new ArrayList<String>();
        // Alberto Fernandez July-2012
        isContextAcrossPhase = false;
	    useContext = false;


    }
    
    // Alberto Fernández August-2011
    /**
     * Returns the external name of a configural cue or configural compound 
     * e.g. configural cues: a --> c(AB), configural compounds: ABb --> [AB] 
     */
    public String cueName2InterfaceName(String cueName) {
    	String interfaceName;
    	if (Character.isLowerCase(cueName.charAt(cueName.length()-1))) {
    		String compoundName;
    		if (cueName.length() == 1) {
    			// configural cue
    			// retrieve compound name
        		compoundName = configCuesNames.get(cueName);
				//interfaceName = "¢(" + compoundName + ")";
				interfaceName = "c(" + compoundName + ")";
    		}
    		else {
    			// configural compound
        		compoundName = cueName.substring(0,cueName.length()-1);
				//interfaceName = "[" + compoundName + "¢]";
				interfaceName = "[" + compoundName + "]";
    		}
    	}
    	else {
    		interfaceName = cueName;
    	}
    	return interfaceName;
    }
  
    private String getKey(TreeMap<String,String> map, String value) {
        Set<String> keys = map.keySet();
        boolean found = false;
        String key = null;
        Iterator<String> it = keys.iterator();
        while (!found && it.hasNext()) {
        	key = (String)it.next();
        	if (map.get(key) == value){
        		found = true;
        	}
        }
        return key;
    }
    /**
     * Returns the internal name of a configural cue or configural compound 
     * e.g. configural cues: c(AB) --> a, configural compounds: [AB] --> ABb  
     */
    public String interfaceName2cueName(String interfaceName) {
    	String cueName;
    	if (interfaceName.contains("(")) { 	// is configural cue, e.g. c(AB)
    		cueName = getKey(configCuesNames, interfaceName.substring(2,interfaceName.length()-2));
    	}
    	else if (interfaceName.contains("[")){ // configural compound
    		cueName = interfaceName.substring(1,interfaceName.length()-2) + 
    		          getKey(configCuesNames, interfaceName.substring(1,interfaceName.length()-2));
    	}
    	else {
    		cueName = interfaceName;
    	}
    	return cueName;
    }
    /**
     * Initializes the values and groups of the SimModel 
     */
    public void reinitialize() {
        values = new TreeMap();
        groups = new TreeMap(); 
        
        // AF Sep-2012 from TD
        listAllCues.clear();
        groupPool = ParallelArray.createEmpty(groupsNo, SimGroup.class, Simulator.fjPool);

        // Alberto Fern‡ndez August-2011
        configCuesNames = new TreeMap<String,String>();
       // Alberto Fernandez Nov-2011
       // listAllCues = new ArrayList<String>();
    }

    /**
     * Adds a new group into the experiment. Adds the group into the groups HashMap 
     * and adds the groups new cues in the cue list if it doesn't already exist. The
     * cue list is variable for the value table that is created for the user to input
     * the 'alpha', 'beta' and 'lambda' values.
     * @param name the name of the group. By default the name is 'Group #', where # an
     * ascending integer starting from 1.
     * @param group the SimGroup object which contains the phases and all other necessary
     * variables to accomplish an experiment.
     */
    public void addGroupIntoMap(String name, SimGroup group) {
        groups.put(name, group);
        addCueNames(group.getCuesMap());        
    }

    /**
     * Add the 'lambda' values for each phase in the values of the model
     */
    public void addValuesIntoMap() {
        // The initial values of the 'lambda' are null a value indicating that they 
        // haven't been assigned with any double values yet.
    	for (int p=1; p<=phasesNo; p++){    
    		
    		boolean atLeastOneGroupPlus = false;
    		boolean atLeastOneGroupMinus = false;
    		
    		Iterator iterGroup = groups.entrySet().iterator();
            while (iterGroup.hasNext()) {
                Map.Entry pairGroup = (Map.Entry)iterGroup.next();
            	SimGroup group = (SimGroup)pairGroup.getValue();
                if (((SimPhase)group.getPhases().get(p-1)).getLambdaPlus() != null)
                	atLeastOneGroupPlus = true;
                if (((SimPhase)group.getPhases().get(p-1)).getLambdaMinus() != null)
                	atLeastOneGroupMinus = true;
                if (atLeastOneGroupPlus && atLeastOneGroupMinus) break;
            }    		
    	    if (atLeastOneGroupPlus) {
    	    	values.put("lambda+ p"+p, null);
    	    	values.put("beta+ p"+p, null);
    	    }
            if (atLeastOneGroupMinus) {
            	values.put("lambda- p"+p, null);
            	values.put("beta- p"+p, null);
            }
        }
    }

    /**
     * Returns the total number of groups. This number can always be changed by the user.
     * @return the number of groups of the experiment.
     */
    public int getGroupNo() {
        return groupsNo;
    }
    
    /**
     * Sets an new number of groups that taking place on the experiment.
     * @param g a number indicating the new number of groups.
     */
    public void setGroupNo(int g) {
        groupsNo = g;
    }
    
    /**
     * Returns the number of phases that every group has. The phases are the same
     * for every group as it is unwise to run an experiment with different number
     * of phases.
     * @return the number of phases that every group has.
     */
    public int getPhaseNo() {
        return phasesNo;
    }
    
    /**
     * Sets an new number of phases that taking place on the experiment.
     * @param p a number indicating the new number of phases.
     */
    public void setPhaseNo(int p) {
        phasesNo = p;
    }
    
    /**
     * Returns the number of different combinations that need to be processed on
     * the sequence stimuli order in order to have a random stimuli execution.
     * The number is set to a default of 20 but can rise into a 3 digit number 
     * as well. Although bigger the number is, longer the time to process.
     * @return the number of combinations for the random stimuli sequence.
     * Modified E. Mondragon April 2012.
     */
    public int getCombinationNo() {
        return combinationNo;
    }
    
    /**
     * Sets an new number of random combinations that take on the experiment when the user
     * chooses random on the groups.
     * @param r a number indicating the combinations. This is being used on a iterating 
     * function that produces random stimuli position following the given sequence.
     */
    public void setCombinationNo(int r) {
        combinationNo = r;
    }
    
    /**
     * Returns the keySet of the cues HashMap. In other words it returns the symbol that 
     * each cue from the experiment has. This is been used on the acomplise of the value 
     * table.
     * @return the keySet of the cues HashMap.
     */
    public Set getCueNames() {
        return values.keySet();
    }
    
    
    /**
     * Returns the number of alpha cues in the model
     * @return number of alpha cues in the model
     */
    public int getNumberAlphaCues(){
    	int cont = 0;
    	Iterator it = values.keySet().iterator();
    	while (it.hasNext()) {
    		String pair = (String)it.next();
    		if ( pair.indexOf("lambda")!=-1 || pair.indexOf("beta")!=-1 ) cont++;	
    		} 				
    	return (values.size()-cont);
    }
    
    /**
     * Returns the number of alpha cues in the model
     * @return number of alpha cues in the model
     */
    public TreeMap<String,Double> getAlphaCues(){
    	TreeMap<String,Double> tm = new TreeMap<String,Double>();
    	Iterator<String> it = values.keySet().iterator();
    	while (it.hasNext()) {
    		String pair = (String)it.next();
    		if ( pair.indexOf("lambda")==-1 && pair.indexOf("beta")==-1 ) tm.put(pair, values.get(pair));	
    	} 				
    	return tm;
    }
    
    // Added by Alberto Fern‡ndez July-2011
    /**
     * Returns the values of parameters in the model
     * @return values of parameters in the model
     */
    public SortedMap<String, Double> getValues() {
		return values;
	}

	public TreeMap<String, String> getConfigCuesNames() {
		return configCuesNames;
	}

    /**
     * Returns the HashMap of the groups. The mapping is described from a key value which 
     * is the the name of the groups and the actual values are SimGroup objects.
     * @return the group objects in a HashMap.
     */
    public TreeMap<String,SimGroup> getGroups() { 
        return groups;
    }
    
    /**
     * Adds the new cues from every phases into the SortedMap. The new cues will be sorted
     * accordingly depending on the symbol character. Their initial Double value is null 
     * because it is only on the 1st stage of the experiment.
     * @param newCueNames the cues HashMaps deriving from every new group.
     */
    public void addCueNames(TreeMap newCueNames) { 
        Iterator it = newCueNames.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            SimCue tempCue = (SimCue)pair.getValue();
            values.put(tempCue.getSymbol() + "", null);
            
            // Alberto Fernandez Nov-2011
            if (!listAllCues.contains(tempCue.getSymbol())) {
            	listAllCues.add(tempCue.getSymbol());
            }
        }
    }
    
    // Alberto Fernandez Nov-2011
    public ArrayList<String> getListAllCues() {
    	return listAllCues;
    }
    
    /**
     * Updates the values from the SortedList to the values in the model 
     * @param name the key of the SortedList. In other words the name or symbol of the cue or
     * 'lambda' or 'beta'.
     * @param phase from which is going to update the value 
     * @param value the value that is been given from the user.
     */ 
    public void updateValues(String name, int phase, String value) {
    	if (value.equals("")) {
    		// beta+ p1, lambda+ p1 y lambda- p1 are never empty after checkValuesTable()
    		if (name.indexOf("beta+")!=-1)        values.put(name+" p"+phase, values.get("beta+ p1"));
    		else if (name.indexOf("beta-")!=-1)   values.put(name+" p"+phase, values.get("beta+ p1")); // if beta- is empty, take beta+
    		else if (name.indexOf("lambda+")!=-1) values.put(name+" p"+phase, values.get("lambda+ p1"));
    		else if (name.indexOf("lambda-")!=-1) values.put(name+" p"+phase, values.get("lambda- p1"));
    	}
    	else if (name.indexOf("beta+")!=-1 || name.indexOf("beta-")!=-1 || name.indexOf("lambda+")!=-1 || name.indexOf("lambda-")!=-1)
    		values.put(name+" p"+phase, new Double(value));
    	else {
    		// Alberto Fern‡ndez August-2011
    		// here, the "virtual" name have to be considered: 
    		//values.put(name, new Double(value));
    		if (name.length()>1) {
    			String compoundName = name.substring(2, name.length()-1); // ex. c(AB) --> AB
    			String virtualName = SimGroup.getKeyByValue(configCuesNames,compoundName);
    			values.put(virtualName, new Double(value));
    		}
    		else {
    			values.put(name, new Double(value));
    		}
    	}
    }
    
    /**
     * Updates every group with the new values that derived from the value table. It iterates
     * through every group and clears them from any values that they possible have and then
     * continues with a new iteration through their cue HashMap and updates as necessary.
     * The same with 'lambda' and 'beta' values.
     */
    public void updateValuesOnGroups() {
    	
        Iterator iterGroup = groups.entrySet().iterator();
        while (iterGroup.hasNext()) {
            Map.Entry pairGroup = (Map.Entry)iterGroup.next();
            SimGroup tempGroup = (SimGroup)pairGroup.getValue();
            tempGroup.clearResults();
            Iterator iterCue = tempGroup.getCuesMap().entrySet().iterator();
            while (iterCue.hasNext()) {
                Map.Entry pairCue = (Map.Entry)iterCue.next();
                SimCue tempCue = (SimCue)pairCue.getValue();
                
                // AF July-2012
                //tempCue.reset();
                // keep alpha after reset 
    			Double alpha = tempCue.getAlpha();
    			tempCue.reset();
    			tempCue.setAlpha(alpha);

                // Alberto Fernandez. July-2012
                // If cue is a context
                if(Context.isContext(tempCue.getSymbol())) {
                    tempCue.setAlpha(tempCue.getAlpha());
                }
                else {
                    tempCue.setAlpha((Double)values.get(tempCue.getSymbol() + ""));
                }
            }
            // Update the values of the lambdas and betas per phase in the current group
            for (int p=1; p<=phasesNo; p++){
            	if (values.containsKey("lambda+ p"+p)) {
                	((SimPhase)tempGroup.getPhases().get(p-1)).setLambdaPlus((Double)values.get("lambda+ p"+p));
            	}
            	if (values.containsKey("lambda- p"+p)) {
                	((SimPhase)tempGroup.getPhases().get(p-1)).setLambdaMinus((Double)values.get("lambda- p"+p));
            	}
            	if (values.containsKey("beta+ p"+p)) {
                	((SimPhase)tempGroup.getPhases().get(p-1)).setBetaPlus((Double)values.get("beta+ p"+p));
            	}
            	if (values.containsKey("beta- p"+p)) {
                	((SimPhase)tempGroup.getPhases().get(p-1)).setBetaMinus((Double)values.get("beta- p"+p));
            	}
            }       
        }
    }
    
    /**
     * Starts the process of the calculation. It executes the run() method of every group
     * which implements the Runnable for multi-thread calculations. This will speed up the
     * processes because they are running in concurrent dimension.
     */
    public void startCalculations2() {
        Iterator iterGroup = groups.entrySet().iterator();
        while (iterGroup.hasNext()) {
            Map.Entry pairGroup = (Map.Entry)iterGroup.next();
            SimGroup tempGroup = (SimGroup)pairGroup.getValue();
            tempGroup.run();
        }
    }
    // AF Sep-2012. From TD
    public void startCalculations() {
        /*Iterator<Entry<String, SimGroup>> iterGroup = groups.entrySet().iterator();
        while (iterGroup.hasNext()) {
            Entry<String, SimGroup> pairGroup = iterGroup.next();
            pairGroup.getValue().run();
        }*/
    	
    	//J Gray - 2012: This section wasn't actually running concurrent, but now does
    	//long timer = System.currentTimeMillis();
    	//System.out.println("Starting simulation..");
    	listAllCues.clear();
        groupPool = ParallelArray.createEmpty(groupsNo, SimGroup.class, Simulator.fjPool);
        groupPool.asList().addAll(groups.values());
        groupPool.withMapping(update).all();
//        control.incrementProgress(1); AF Sep-2012
        control.setComplete(true);
        /*timer = System.currentTimeMillis() - timer;
        System.out.println("Ran "+groups.size() +" groups, "+phasesNo+" phases, "+ totalNumPhases()+" total.");
        System.out.println(control.getEstimatedCycleTime()/1000+"s avg per iteration.");
        System.out.println("Done in " + timer/1000 + "s");*/
    }

    
    /**
     * It produces a text output of all the group results. It adds the headers of the
     * groups and then retrieves the text output of every group and adds it to one string
     * which at the end it will be presented on the main view. 
     * @return the final output of the results.
     */
    // AF Sep-2012. From TD, StringBuffer instead of String
//    public String textOutput(boolean compound) {
//        String result = "";
//        String sep = "------------------------\n";
//        Iterator iterGroup = groups.entrySet().iterator();
//        while (iterGroup.hasNext()) {
//            Map.Entry pairGroup = (Map.Entry)iterGroup.next();
//            SimGroup tempGroup = (SimGroup)pairGroup.getValue();
//            result += sep + tempGroup.getNameOfGroup() + '\n' + sep;
//            result += tempGroup.phasesOutput(compound, configCuesNames);
//        }
//        return result;
//    }
    public String textOutput(boolean compound) {
        StringBuffer result = new StringBuffer();
        String sep = "------------------------\n";
        Iterator<Entry<String, SimGroup>> iterGroup = groups.entrySet().iterator();
        while (iterGroup.hasNext()) {
            Entry<String, SimGroup> pairGroup = iterGroup.next();
            SimGroup tempGroup = pairGroup.getValue();
            result.append(sep).append(tempGroup.getNameOfGroup()).append('\n').append(sep);
            result.append(tempGroup.phasesOutput(compound, configCuesNames));
        }
        return result.toString();
    }
    
    
    // AF July-2012
    // adopted from TD simulator
    
	/**
	 * @param useContext whether context is used
	 */
	public void setUseContext(boolean on) {
		this.useContext = on;
		if(useContext) {
			if(!listAllCues.contains(Simulator.OMEGA+"")) {
				listAllCues.add(Simulator.OMEGA+"");
			}
		} else {
			listAllCues.remove(Simulator.OMEGA+"");
		}
	}

	public boolean contextAcrossPhase() {
		return isContextAcrossPhase;
	}
	public void setContextAcrossPhase(boolean on) {
		isContextAcrossPhase = on;
	}

    /**
	 * @return the a boolean indicated that the context should be used
	 */
	public boolean isUseContext() {
		return useContext;
	}
    
    /**
	 * @param contexts
	 */
	public Map<String, ContextConfig> getContexts() {
		Map<String, ContextConfig> contexts = new HashMap<String, ContextConfig>();
		for(SimGroup group : groups.values()) {
			contexts.putAll(group.getContexts());
		}
		return contexts;
	}

	/**
	 * 
	 * @return the default context alpha this model uses.
	 */
	
	public double getContextAlpha() {
		return contextAlpha;
	}

	/**
	 * 
	 * @param alpha the default context alpha this model uses.
	 */
	
	public void setContextAlpha(final double alpha) {
		contextAlpha = alpha;
	}

	public void setControl(ModelControl control) {
		this.control = control;
		for(SimGroup group : groups.values()) {
			group.setControl(control);
		}
	}

	// AF Aug-2012 from TD
	@Override
	public void run() {
		//startCalculations();
    	try {
    		startCalculations();
    	}
    	catch (java.lang.OutOfMemoryError e) {
        	System.err.println(e);
        	JOptionPane.showMessageDialog(null, Messages.getString("SimModel.OutOfMemoryError"), Messages.getString("SimView.aboutTitle"), JOptionPane.PLAIN_MESSAGE); //NO_OPTION //$NON-NLS-1$
    	}
	}

	public int totalNumPhases() {
		int total = phasesNo*groupsNo;
		for(SimGroup group : groups.values()) {
			total -= group.countRandom();
			total += group.numRandom();
		}
		return total;
	}



}