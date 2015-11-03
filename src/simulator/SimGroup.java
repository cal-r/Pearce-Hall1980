/**
 * SimGroup.java
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

import extra166y.ParallelArray;

import simulator.configurables.ContextConfig;
import simulator.configurables.ContextConfig.Context;

/**
 * SimGroup is the class which models a group from the experiment. It will process 
 * any new sequences of stimuli and adds them to its ArrayList of phases. It is an
 * intermediate between the Controller and the Phase itself. It contains all the necessary
 * variables need to run a simulation and it keeps a record of the results but in a global
 * view, meaning that the results will be an extract from all phases together.
 */
public class SimGroup implements Runnable{
    
    private ArrayList<SimPhase> phases;
    private TreeMap<String,SimCue> cues;      
    private String nameOfGroup;
    private int noOfPhases, noOfCombinations, count;

    // AF July-2012. From TD
    /** The model this group belongs to. **/
	private SimModel model;

	// AF Sep-2012 from TD
	/** Threaded array. **/
	private ParallelArray<SimPhase> phasePool;

    /**
     * Create a group
     * @param n name of the Group
     * @param np number of phases
     * @param rn number of combinations
     * @return true if the method completed without any errors and false otherwise.
     */    
    public SimGroup(String n, int np, int rn, SimModel model) {
        nameOfGroup = n;
        noOfPhases = np;
        noOfCombinations = rn;
        count = 1;        
        cues = new TreeMap<String,SimCue>();
        phases = new ArrayList<SimPhase>(noOfPhases);
        this.setModel(model);
    }
    
    // Alberto Fern‡ndez August-2011
    // Added boolean parameters isConfiguralCompounds and configuralCompoundsMapping. 
    /**
     * Adds a new phase in the group's arraylist. The stimuli sequence of the given 
     * is being processed mainly so it could be added as a new SimPhase object and
     * secondary it might produce new cues which weren't on previous phases. This 
     * new cues are added on the group's cue list as well. 
     * @param seqOfStimulus the stimuli sequence of the given phase.
     * @param boolean to know if the phase is going to be randomly executed
     * @param int the number of the current phase
     * @param boolean to know if the phase is going to use configural cues
     * @param mapping with configural compounds from their "virtual" name
     * @return true if the method completed without any errors and false otherwise.
     */
	public static String getKeyByValue(TreeMap<String, String> map, String value) {
	     String key = null;
	     for (Map.Entry<String,String> entry : map.entrySet()) {
	         if (entry.getValue().equals(value)) {
	             key = entry.getKey();
	         }
	     }
	     return key;
	}
    public boolean addPhase(String seqOfStimulus, boolean isRandom, int phaseNum, 
    		                boolean isConfiguralCompounds, TreeMap<String, String> configuralCompoundsMapping,
    		                ContextConfig context, int CS_ITI_ratio) {
        
    	// AF Sep-2012. Include Probe Stimuli
    	// orderProbe: similar to 'order' but include info about probe stimuli (e.g. ["A^B+",...])

    	seqOfStimulus = seqOfStimulus.toUpperCase(); // Sequence is always stored in upper case. Alberto Fern‡ndez August-2011
    	
        ArrayList<String> order = new ArrayList<String>(50);       
        ArrayList<String> orderProbe = new ArrayList<String>(50); // like 'order' but including probe stimuli info
        
        String sep = "/";
        String[] listedStimuli = seqOfStimulus.toUpperCase().split(sep);
        
        // AF July-2012. Adapted from TD
        if (!cues.containsKey(context.getSymbol()) && !context.getContext().equals(Context.EMPTY)) {
			cues.put(context.getSymbol(), new SimCue(context.getSymbol(),context.getAlpha()));
		}

        // Added by Alberto Fern‡ndez
        // Introduce "virtual" cues (lower case letters) in case of configural compounds. 
        
		int noStimuli = listedStimuli.length;
		HashMap stimuli = new HashMap();
		
		for (int i = 0; i < noStimuli; i++) {
		    String selStim = listedStimuli[i], repStim = "", cuesName = "", stimName = "";
		    boolean reinforced = false;
		    boolean oktrials = false, okcues = false, okreinforced = false;
		    
		    String compound = new String("");
		    String compoundProbe = new String(""); // AF Sep-2012
		    
		    // AF July-2012. From TD
		    if(model.isUseContext()) {
		    	cuesName = context.getSymbol();
			    compound += cuesName; // AF July-2012
			    compoundProbe += cuesName; // AF Sep-2012
		    }

		    int noStimRep = 1;
		    for (int n = 0; n < selStim.length(); n++) {
			    
		        char selChar = selStim.charAt(n);
		        
				if (Character.isDigit(selChar) && !oktrials) {
					repStim += selChar;
				}
				else if (Character.isLetter(selChar) && !okcues) {
					oktrials = true;
				    cuesName += selChar;
				    if (!cues.containsKey(selChar+"")) {
						cues.put(selChar+"", new SimCue(selChar+"", null));
					}
					compound += selChar;
					compoundProbe += selChar;
				}
				else if (selChar == '^') { // AF Sep-2012
					// probe stimuli
					compoundProbe += '^';
				}
				else if ((selChar == '-' || selChar == '+') && !okreinforced) {
					oktrials = true; 
					okcues = true;
					reinforced = (selChar == '+');
					okreinforced = true;
					
					// Added by Alberto Fern‡ndez August-2011
					
					// AF July-2012. From TD
					//if (compound.length() > 1 && isConfiguralCompounds) {
					
					// AF August-2012. Include context in the configural cue if the option is selected.
					if ((model.isUseContext() || compound.length() > 1) && isConfiguralCompounds) {
					
						
						// Add configural cue as a "virtual" cue (lower case letter)
						
						// AF July-2012. From TD
						//compound = model.isUseContext() ? context.getSymbol()+compound : compound;
						// context already beginning 'compound'

						// AF Aug-2012
//						String configuralCompound; // = compound;
//						if (!contextInConfiguralCue) {
//							// remove context from compound
//							configuralCompound = compound.substring(1);
//						}
//						else {
//							configuralCompound = compound;
//						}
						
						String virtualCueName = getKeyByValue(configuralCompoundsMapping,compound);
						if (virtualCueName == null) {
							if (configuralCompoundsMapping.isEmpty()) {
								virtualCueName = "a";
							}
							else {
								char c = configuralCompoundsMapping.lastKey().charAt(0);
								c = (char) ((int)c + 1);
								virtualCueName = ""+c;
							}
							configuralCompoundsMapping.put(virtualCueName, compound);
						}
					    
						cuesName += virtualCueName;
					    compound += virtualCueName;
					    compoundProbe += virtualCueName;
					    if (!cues.containsKey(virtualCueName+"")) {
							cues.put(virtualCueName+"", new SimCue(virtualCueName+"", null));
						}
					}
				}
				else return false;
		    }
		    // Add to the cues the compound of the stimuli
		    // Alberto Fernandez Oct-2011
		    //if (!cues.containsKey(compound)) {
		    if (!compound.equals("") && !cues.containsKey(compound)) {
			//if (!compound.equals("") && !cues.containsKey(compound) && configuralCue(compound)) { // this in TD
		    	cues.put(compound, new SimCue(compound, null));
		    }
			
		    stimName = cuesName + (reinforced ? '+' : '-');
			
			if (repStim.length() > 0) noStimRep = Integer.parseInt(repStim);
					
			// AF July-2012. Modified
			if (stimuli.containsKey(stimName)) {
				((SimStimulus) stimuli.get(stimName)).addTrials(noStimRep);
			}
			else {
				stimuli.put(stimName,new SimStimulus (stimName, noStimRep, cuesName, reinforced));
			}
					
			for (int or = 0; or < noStimRep; or++) {
				order.add(stimName);
				orderProbe.add(compoundProbe + (reinforced ? '+' : '-'));
			}
		}		
		// Alberto Fernandez. July-2012
		// include CS_ITI_ratio contexts- in ITI
		if (model.isUseContext()) {
			int size = order.size();
			int pos = 0; // index in which insert context
			for (int i=0; i <= size; i++){
				for (int j=0; j < CS_ITI_ratio; j++) {
					order.add(pos, context.getSymbol() + '-');
					orderProbe.add(pos, context.getSymbol() + '-');
					pos++;
				}
				pos++; // jump original cue
			}
			// add stimuli
			stimuli.put(context.getSymbol() + '-',new SimStimulus (context.getSymbol(), CS_ITI_ratio*size, context.getSymbol(), false));
		}


		// AF July-2012. Added context parameter
		//return phases.add(new SimPhase(seqOfStimulus, order, stimuli, this, isRandom));		
		
		// AF Sep-2012. Added probe stimuli
		//return phases.add(new SimPhase(seqOfStimulus, order, stimuli, this, isRandom, context));		
		return phases.add(new SimPhase(seqOfStimulus, order, stimuli, this, isRandom, context, orderProbe));		

	}
    
    /**
     * The Runnable's run method. This starts a new Thread. It 
     * actually runs every SimPhases.runSimulator() method which is the
     * method that uses the formula on the phases.
     */
    // AF Sep-2012 from TD. I don't see any advantage since 'phasepool' is not used
//    public void run() {
//        for (int i = 0; i < noOfPhases; i++) {
//            ((SimPhase)phases.get(i)).runSimulator();
//        }
//    }
    public void run() {
    	//Add to phasepool so we can still cancel them quickly if required
    	phasePool = ParallelArray.createEmpty(noOfPhases, SimPhase.class, Simulator.fjPool);
        phasePool.asList().addAll(phases);
        for (int i = 0; i < noOfPhases; i++) {
        	// AF Sep-2012. Removed this. Not sure if needed
//        	if(model.contextAcrossPhase()) {
//            	//Deal with different omega per phase
//            	for(Entry<String, CueList> entry : cues.entrySet()) {
//            		String realName = model.getConfigCuesNames().get(entry.getKey());
//            		realName = realName == null ? "" : realName;
//            	}
//            }
            phases.get(i).runSimulator();
        }
    }

    /**
     * Empties every phase's results. It iterates through the phases
     * and calls the SimPhase.emptyResults() method. This method
     * cleans up the results variable.
     *
     */
    public void clearResults() {
        for (int i = 0; i < noOfPhases; i++) {
            SimPhase sp = (SimPhase)phases.get(i);
            sp.emptyResults();
        }
        count = 1;
    }
    
    /**
     * Returns the results from the simulation. They are included into
     * a String object. The method iterates through each phase and returns
     * the phase's results.
     * @return the group's results represented in a string.
     */
    // AF Sep-2012. (from TD) Using StringBuffer instead String. StringBuffer is faster
    public String phasesOutput(boolean compound, TreeMap<String,String> configuralCompoundsMapping) {
        //String result = "";
        StringBuffer result = new StringBuffer();
     
        // For all phases
        for (int i = 0; i < noOfPhases; i++) {                       
            SimPhase sp = (SimPhase)phases.get(i);
            //result += "(Phase " + (i+1) + " , Seq: " + sp.intialSequence() + " Rand: " + sp.isRandom() +")" + "\n\n";
            result.append("(Phase ").append(i+1).append(" , Seq: ").append(sp.intialSequence())
            .append(" Rand: ").append(sp.isRandom()).append(")").append("\n\n");
            
            TreeMap<String, SimCue> results = sp.getResults();

        	// Alberto Fernández August-2011

            // Output  Cues
            result.append(cuesOutput(results));
            
            Iterator iterCue = results.entrySet().iterator();
//            while (iterCue.hasNext()) {
//                Map.Entry pairCue = (Map.Entry)iterCue.next();
//                SimCue tempCue = (SimCue)pairCue.getValue();
//            	String cueName = tempCue.getSymbol();
//                if (tempCue.getSymbol().length()==1 && Character.isUpperCase(cueName.charAt(cueName.length()-1))) {
//               		//result += "Cue : " + tempCue.getSymbol() + "\n\n";
//            		result.append("Cue : ").append(cueName).append("\n\n");
//	            	for (int y = 1; y < tempCue.getAssocValueSize(); y ++) { // RO: from y=1 instead of 0
//	            		//result += "V" + y + " = " + tempCue.getAssocValueAt(y).floatValue() + '\n';
//	            		result.append("V").append(y).append(" = ").append(tempCue.getAssocValueAt(y).floatValue()).append("\n");
//	            	}
//	            	//result += '\n';
//	            	result.append("\n");
//                }
//            }
            
            
            // Output Compounds
            iterCue = results.entrySet().iterator();
            while (iterCue.hasNext()) {
                Map.Entry pairCue = (Map.Entry)iterCue.next();
                SimCue tempCue = (SimCue)pairCue.getValue();
            	String cueName = tempCue.getSymbol();
                if (compound && cueName.length()>1 && tempCue.getAssocValueSize() > 1){
                	if (Character.isLowerCase(cueName.charAt(cueName.length()-1))) {
                		String compoundName, interfaceName;
            			// configural compound
                		compoundName = cueName.substring(0,cueName.length()-1);
						interfaceName = "[" + compoundName + "]";
						//result += "Cue : " + interfaceName + "\n\n";
            			result.append("Cue : ").append(interfaceName).append("\n\n");
                	}
                	else { // compound (not configural)
                		//result += "Cue : " + tempCue.getSymbol() + "\n\n";
            			result.append("Cue : ").append(tempCue.getSymbol()).append("\n\n");
                	}
                	for (int y = 1; y < tempCue.getAssocValueSize(); y ++) { // RO: from y=1 instead of 0
                		//result += "V" + y + " = " + tempCue.getAssocValueAt(y).floatValue() + '\n';
	            		result.append("V").append(y).append(" = ").append(tempCue.getAssocValueAt(y).floatValue()).append("\n");
                	}
                	//result += '\n';
                	result.append("\n");
                }
            }
            // Output configural cues
            iterCue = results.entrySet().iterator();
            while (iterCue.hasNext()) {
                Map.Entry pairCue = (Map.Entry)iterCue.next();
                SimCue tempCue = (SimCue)pairCue.getValue();
            	String cueName = tempCue.getSymbol();
                if (tempCue.getSymbol().length()==1 && Character.isLowerCase(cueName.charAt(cueName.length()-1))
                		&& tempCue.getAssocValueSize() > 1) {
            		String compoundName, interfaceName;
        			// configural cue
            		compoundName = configuralCompoundsMapping.get(cueName);
					interfaceName = "c(" + compoundName + ")";
					//result += "Cue : " + interfaceName + "\n\n";
        			result.append("Cue : ").append(interfaceName).append("\n\n");
                	for (int y = 1; y < tempCue.getAssocValueSize(); y ++) { // RO: from y=1 instead of 0
                		//result += "V" + y + " = " + tempCue.getAssocValueAt(y).floatValue() + '\n';
	            		result.append("V").append(y).append(" = ").append(tempCue.getAssocValueAt(y).floatValue()).append("\n");
                	}
                	//result += '\n';
                	result.append("\n");
                }
            }
            
            // Output Probe Stimuli Cues. AF Sep-2012
            if (!sp.getResultsProbe().isEmpty()) {
            	//result.append("^^^^^^^^ PROBE STIMULI ^^^^^^^^\n\n");
                result.append(cuesOutput(sp.getResultsProbe()));
            }

        }
        //return result;
        return result.toString();
    }
    
    private StringBuffer cuesOutput(TreeMap<String, SimCue> results) {
        StringBuffer result = new StringBuffer();
        Iterator iterCue = results.entrySet().iterator();
        while (iterCue.hasNext()) {
            Map.Entry pairCue = (Map.Entry)iterCue.next();
            SimCue tempCue = (SimCue)pairCue.getValue();
        	String cueName = tempCue.getSymbol();
            //if (tempCue.getSymbol().length()==1 && Character.isUpperCase(cueName.charAt(cueName.length()-1))) {
            if ((tempCue.getSymbol().length()==1 && Character.isUpperCase(cueName.charAt(cueName.length()-1)) 
            		&& tempCue.getAssocValueSize() > 1)
            	 || tempCue.getSymbol().contains("^")){
           		//result += "Cue : " + tempCue.getSymbol() + "\n\n";
        		result.append("Cue : ").append(cueName).append("\n\n");
            	for (int y = 1; y < tempCue.getAssocValueSize(); y ++) { // RO: from y=1 instead of 0
            		//result += "V" + y + " = " + tempCue.getAssocValueAt(y).floatValue() + '\n';
            		result.append("V").append(y).append(" = ").append(tempCue.getAssocValueAt(y).floatValue()).append("\n");
            	}
            	//result += '\n';
            	result.append("\n");
            }
        }
        return result;
    }

    /**
     * Returns the TreeMap which contains the cues and their values.
     * An important object on overall group result processing.
     * @return the group's cues.
     */
    public TreeMap<String,SimCue> getCuesMap() { 
        return cues;
    }
    
    /**
     * Returns the number of combinations that shall be run if the
     * user has chosen a random sequence.
     * @return the number of combinations.
     */
    public int noOfCombin() {
		return noOfCombinations;
	}
    
	/**
	 * Returns the ArrayList which contains the SimPhases, the phases that 
	 * run on this specific group.
	 * @return the group's phases.
	 */
	public ArrayList getPhases() {
	    return phases;
	}
	
	/**
	 * Returns the group's current name. By default shall be "Group n" where
	 * n is the position that has on the table.
	 * @return the name of the group.
	 */
	public String getNameOfGroup() {
	    return nameOfGroup;
	}
	
	/**
	 * Returns the number of trials that have been produced so far.
	 * @return the number of trials so far.
	 */
	public int getCount() {
	    return count;
	}
	
	/**
	 * Adds one more value to the count variable which represents
	 * the trials so far.
	 */
	public void nextCount() {
	    count++;
	}
	
	
	// Alberto Fernandez. July 2012
	// Adopted from TD
		
	/**
	 * 
	 * @return a map of context names to their configurations.
	 */
	
	public Map<String, ContextConfig> getContexts() {
		Map<String, ContextConfig> contexts = new HashMap<String, ContextConfig>();
		
		for(SimPhase phase : phases) {
			contexts.put(phase.getContextConfig().getSymbol(), phase.getContextConfig());
		}
		
		return contexts;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(SimModel model) {
		this.model = model;
	}
	
    /**
     * Checks if this is the name of a configural cue (i.e. contains lowercase characters)
     * @param cueName the cue to check
     * @return true if this is a configural cue
     */
    
	protected boolean configuralCue(String cueName) {
		if(cueName.length() == 1) { return false; }
		return !cueName.equals(cueName.toUpperCase());
	}

	/**
	 * @return the noOfPhases
	 */
	public int getNoOfPhases() {
		return noOfPhases;
	}

	// AF Aug-2012 from TD
	/**
	 * @param control the message passing object to use
	 */
	public void setControl(ModelControl control) {
		for(SimPhase phase : phases) {
			phase.setControl(control);
		}
	}

	/**
	 * 
	 * @return a count of how many phases are random in this group
	 */
	
	public int countRandom() {
		int count = 0;
		for(SimPhase phase : phases) {
//			if(phase.isRandom() || phase.getTimingConfig().hasVariableDurations()) {
			if(phase.isRandom() ) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @return the number of runs of the algorithm in this group
	 */
	public int numRandom() {
		int count = 0;
		for(SimPhase phase : phases) {
			int increment = phase.isRandom() ? model.getCombinationNo() : 0;
			//increment = phase.getTimingConfig().hasVariableDurations() ? Math.max(increment, 1)*model.getVariableCombinationNo() : increment;
			count += increment;
		}
		return count;
	}

	// AF Aug-2012
	public int getNumberOfValuesInResults() {
		int n = 0;
		
		for(SimPhase phase: phases) {
			n += phase.getNumberOfValuesInResults();
		}
        return n;
	}

}