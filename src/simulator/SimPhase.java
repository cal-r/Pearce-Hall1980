
/**
 * SimPhase.java
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

import simulator.configurables.ContextConfig;

/**
 * SimPhases is the class which models and processes a phase from the 
 * experiment. It will process the sequence of stimuli and give results
 * as requested from the controller.
 */
public class SimPhase {
	
    private SimGroup group;
	private ArrayList<String> orderedSeq;
	private HashMap<String,SimStimulus> stimuli; 
	private TreeMap<String,SimCue> cues; 
	private TreeMap<String,SimCue> results; 
	private String initialSeq;
	private int trials;
	private boolean random;
	private Double lambdaPlus, lambdaMinus, betaPlus, betaMinus;
	
	// *********************!!!!!!!!!!!!!!!!!!************
	/** Context configuration. **/
	private ContextConfig contextCfg;

	// AF Aug-2012 from TD
	/** Message passing object to update progress in the GUI & fast-cancel the sim. **/
	private volatile ModelControl control;

	// AF Sep-2012
	private ArrayList<String> orderedSeqProbe;
	private TreeMap<String,SimCue> resultsProbe; 

	
	/**
	 * SimPhase's Constructor method
	 * @param seq the sequence as it has been given by the user.
	 * @param ord the same sequence processed into an ArrayList.
	 * @param sti the stimuli that the sequence contains.
	 * @param sg the parent group that this phase is belong to.
	 * @param random if the phase must be executed in a random way
	 * @param context the context to be used
	 * @param ordProbe the sequence 'ord' including probe stimuli information (^)
	 */
	//public SimPhase(String seq, ArrayList ord, HashMap sti, SimGroup sg, boolean random,
	//		        ContextConfig context) {
	public SimPhase(String seq, ArrayList<String> ord, HashMap<String,SimStimulus> sti, SimGroup sg, boolean random,
		        ContextConfig context, ArrayList<String> ordProbe) {
	    
	    results = new TreeMap<String,SimCue>();
	    initialSeq = seq;
	    stimuli = sti;
	    orderedSeq = ord;
	    orderedSeqProbe = ordProbe; // AF Sep-2012
	    group = sg;
	    this.random = random;
	    this.trials = orderedSeq.size();
	    this.cues = group.getCuesMap();
	    
	    lambdaPlus = 0.0;
	    betaPlus = 0.0;
	    lambdaMinus = 0.0;
	    betaMinus = 0.0;

	    // AF July-2012. From TD
	    //Added to make use of contexts per phase/group
	    contextCfg = context;

	    // AF Sep-2012
	    resultsProbe = new TreeMap<String,SimCue>();
	}
	
	
	/**
	 * This starts the execution of the algorithm. The method first checks
	 * if the sequence has to be executed in random order and then executes
	 * the same algorithm but in different execution style. If the sequence
	 * is random, creates a tempSequence from the original and runs a simple
	 * shuffle method. The shuffle methods use a random generator which 
	 * provides a number from 0 to the end of the sequences length. Then swaps
	 * position with the previous number - position. Finally it calls the 
	 * algorithm. The previous task is running iterative depending the
	 * number of combinations that the user has chosen. If the sequence
	 * is not supposed to run in random order it skips this step and goes
	 * straight to the algorithm.
	 *
	 */
	public void runSimulator() {
	    results = copyKeysMapToTreeMap(cues); 
	    
	    // Sequence is running randomly
	    if (isRandom()) {
        	// Alberto Fern‡ndez July-2011
        	TreeMap<String,SimCue> copyOriginalCues = (TreeMap<String,SimCue>) copyKeysMapToTreeMap(cues);  

        	TreeMap<String,Double> sumLastAssocValues = new TreeMap<String,Double>();

        	// end changes July-2011
        	
	        // Shuffle process
	        Random generator = new Random();
	        ArrayList<TreeMap<String,SimCue>> allResults = new ArrayList(group.noOfCombin());
	        ArrayList<TreeMap<String,SimCue>> allResultsProbe = new ArrayList(group.noOfCombin()); // AF Sep-2012
	        for (int i = 0; i < group.noOfCombin(); i++) {
	        	ArrayList<String> tempSeq = orderedSeq;
	        	ArrayList<String> tempSeqProbe = orderedSeqProbe;
	        	int n;
	        	for (int x = 0; x < trials && orderedSeq.size()>1; x++) {
	        		n = generator.nextInt(orderedSeq.size()-1);
	        		String swap = (String)tempSeq.get(x);
	        		String swapProbe = (String)tempSeqProbe.get(x);
	        		tempSeq.remove(x);
	        		tempSeq.add(n, swap);
	        		tempSeqProbe.remove(x);
	        		tempSeqProbe.add(n, swapProbe);
	        	}
	        	// Copies an exact copy of the result treemap and 
	        	// runs the algorithm using this temporarily copy.
	        	TreeMap<String,SimCue> tempRes = copyKeysMapToTreeMap(cues); //copyKeysMap(cues);
	        	
	        	
	        	// AF Aug-2012 from TD
//	    		long count = System.currentTimeMillis();
    		    //algorithm(tempSeq,tempRes, context); // Alberto Fernández July-2011
	        	
	    		// AF Sep-2012
	        	//algorithm(tempSeq, tempRes);
	        	TreeMap<String,SimCue> tempResProbe = new TreeMap<String,SimCue>();
	    		algorithm(tempSeq, tempRes, tempSeqProbe, tempResProbe);
	        	allResults.add(tempRes);
	        	allResultsProbe.add(tempResProbe);
	    		
	        	control.incrementProgress(1);
	        	
	        	// AF Sep-2012
	        	if (control.isCancelled()) {
	        		return;
	        	}
	        	
	        	// Alberto: store lastAssocValues, which are not in tempRes but in 'cues'
	        	// this is to keep the average last assoc values for the next phase
	        	
	        	for (String cueId: cues.keySet()) {
	        		if (cueId.length() == 1){ // not compounds
	        			double newValue;
	        			if (!sumLastAssocValues.containsKey(cueId)) {
	        				// first round
	        				newValue = cues.get(cueId).getLastAssocValue(); 
	        			}
	        			else {
	        				newValue = sumLastAssocValues.get(cueId) + cues.get(cueId).getLastAssocValue();
	        			}
	        			sumLastAssocValues.put(cueId, newValue);
	        		}
	        	}	        	
	        	// Alberto Fern‡ndez July-2011
	        	// restore original cues
	        	//cues = copyKeysMapToTreeMap(copyOriginalCues);
//	        	for (String cueId: cues.keySet()) {
//	        		ArrayList values = copyOriginalCues.get(cueId).getAssocValueVector();
//	        		cues.get(cueId).setAssocValueVector(values);
//	        	}
	        	for (String cueId: cues.keySet()) {
	    			SimCue currentCue = cues.get(cueId);
	    			Double alpha = currentCue.getAlpha();
	    			currentCue.reset();
	    			currentCue.setAlpha(alpha);
	        		if (copyOriginalCues.containsKey(cueId)) {
	        			currentCue.setAssocValueAt(0,copyOriginalCues.get(cueId).getLastAssocValue());
	        		}
	        	}	        	
	        }
	        
	        // Finally, after the algorithm finishes for each number
	        // of combination. It returns the average result into the
	        // group's cues and phases HashMaps       
	        Set keys = results.keySet(); // phase results
	        Iterator it = keys.iterator();
	        // for all the cues in the phase
	        while (it.hasNext()) {
	        	String element = (String)it.next(); // A
	        	TreeMap tm = allResults.get(0);	
	        	SimCue currentCue = (SimCue) tm.get(element);  // SimCue de A de la phase (una de las ncomb)
	        	SimCue cue = results.get(element);             // SimCue de A de la phase (aœn no tiene valores, falta calcular la media)
	        	SimCue globalCue = (SimCue) cues.get(element); // SimCue de A del grupo
	        	// for all the trial for this cue in the phase
	        	for (int i=1; i<currentCue.getAssocValueSize() ;i++){
	        		double total = 0;
	        		// for all the number of combinations
	        		for (int y = 0; y < allResults.size(); y++) {
	        			TreeMap<String,SimCue> m = (TreeMap<String,SimCue>) allResults.get(y);
	        			SimCue c = (SimCue) m.get(element);
	        			total += c.getAssocValueAt(i).doubleValue(); 
	        		}
	        		cue.setAssocValue(new Double(total/allResults.size()));      // Update SimCue A de la phase
		        	globalCue.setAssocValue(new Double(total/allResults.size()));// Update SimCue A del grupo    	
	        	}
	        }
	        
	        // Now, calculate average results for probe stimuli. AF Sep-2012
		    resultsProbe = copyKeysMapToTreeMap(allResultsProbe.get(0)); 

	        keys = allResultsProbe.get(0).keySet(); // phase results
	        it = keys.iterator();
	        // for all the cues in the phase
	        while (it.hasNext()) {
	        	String element = (String)it.next(); // A
	        	TreeMap<String, SimCue> tm = allResultsProbe.get(0);	
	        	SimCue currentCue = (SimCue) tm.get(element);  // SimCue de A de la phase (una de las ncomb)
	        	SimCue cue = resultsProbe.get(element);             // SimCue de A de la phase (aœn no tiene valores, falta calcular la media)
	        	//SimCue globalCue = (SimCue) cues.get(element); // SimCue de A del grupo
	        	// for all the trial for this cue in the phase
	        	for (int i=1; i<currentCue.getAssocValueSize() ;i++){
	        		double total = 0;
	        		// for all the number of combinations
	        		for (int y = 0; y < allResultsProbe.size(); y++) {
	        			TreeMap<String,SimCue> m = (TreeMap<String,SimCue>) allResultsProbe.get(y);
	        			SimCue c = (SimCue) m.get(element);
	        			total += c.getAssocValueAt(i).doubleValue(); 
	        		}
	        		cue.setAssocValue(new Double(total/allResultsProbe.size()));      // Update SimCue A de la phase
		        	//globalCue.setAssocValue(new Double(total/allResultsProbe.size()));// Update SimCue A del grupo    	
	        	}
	        }

	        // Update initial group.cues with the average last assoc value
        	for (String cueId: cues.keySet()) {
    			SimCue currentCue = cues.get(cueId);
    			Double alpha = currentCue.getAlpha();
    			currentCue.reset();
    			currentCue.setAlpha(alpha);
        		if (sumLastAssocValues.containsKey(cueId)) {
        			currentCue.setAssocValue(sumLastAssocValues.get(cueId)/group.noOfCombin());
        		}
        	}	        	
	        
	        
	    }
	    // A standard sequence
	    else {
        	// AF Aug-2012 from TD
    		//long count = System.currentTimeMillis();

	    	// AF Sep-2012
        	if (control.isCancelled()) {
        		return;
        	}

	        //algorithm(orderedSeq);
        	//AF Sep-2012. Probe Stimuli
	        //algorithm(orderedSeq,results); // Alberto Fernández July-2011
	        algorithm(orderedSeq,results,orderedSeqProbe,resultsProbe); // Alberto Fernández July-2011
    		
        	control.incrementProgress(1);
    		//control.setEstimatedCycleTime(System.currentTimeMillis()-count);

	    }
	}
	
	/**
	 * The basic Rescorla-Wagner formula shows how V (associative strength) changes
	 * during each trial. The formula is deltaV = alpha(beta)(lambda - V) where V is
	 * the previous stimulus associative value. If the stimuli is compound, the V is 
	 * equal with the addition of the last cues' associative strength values that is
	 * compound from. The method calculates the associative strength according to the
	 * @param sequence contains a list of stimulus.
	 * @param tempRes the results to be stored in.
	 * @param sequenceProbe list of stimulus including probe stimuli information
	 */
	// Modified by Alberto Fern‡ndez July-2011
	//private void algorithm(ArrayList sequence, TreeMap tempRes) {
	private void algorithm(ArrayList<String> sequence, TreeMap<String,SimCue> tempRes, 
			               ArrayList<String> sequenceProbe, TreeMap<String,SimCue> tempResProbe) {

		// This appears in TD
//		if(context) {
//			//Set the alpha on the context we're using here
//			tempRes.get(contextCfg.getContext().toString()).setAlpha(contextCfg.getAlpha());
//		}

	    // Iterate for every stimulus and act accordingly
	    for (int i = 0; i < sequence.size(); i++) {
//	    	System.out.println(i);
	        String curNameSt = (String) sequence.get(i);
			SimStimulus currentSt = (SimStimulus) stimuli.get(curNameSt);
			double lastAssoc = getTotalCueAssoc(currentSt, cues);
			// If the stimulus is compound needs to go through each cue value
			for (int y = 0; y < curNameSt.length() - 1; y ++) {
				Character curCue = new Character(curNameSt.charAt(y));
				SimCue currentCue = (SimCue) cues.get(curCue+"");
				SimCue resCue = (SimCue) tempRes.get(curCue+"");
								
				// The associative strength for the current trial.											
				double currentCueLastAssocVal = currentCue.getLastAssocValue().doubleValue();
				double currAssocVal = currentCue.getLastAssocValue().doubleValue() + 
					( currentCue.getAlpha().doubleValue() * 
							(currentSt.isReinforced() ? this.getBetaPlus().doubleValue() : this.getBetaMinus().doubleValue()) *
							( (currentSt.isReinforced() ? this.getLambdaPlus().doubleValue() : this.getLambdaMinus().doubleValue()) 
									- lastAssoc));
				currentCue.setAssocValue(new Double(currAssocVal));
				resCue.setAssocValue(new Double(currentCueLastAssocVal));
				
				// AF Sep-2012. Here store cue values if marked as probe stimuli
				
				String curNameStProbe = sequenceProbe.get(i);
				if (curNameStProbe.contains(curCue+"^")) {
					SimCue resCueProbe;
					//if (tempResProbe.containsKey(curCue.toString())) {
					if (tempResProbe.containsKey(probeIDfromSt(curCue,curNameStProbe))) {
						//resCueProbe = tempResProbe.get(curCue.toString());
						resCueProbe = tempResProbe.get(probeIDfromSt(curCue,curNameStProbe));
					}
					else {
						//resCueProbe = new SimCue(curCue.toString(),null);
						//tempResProbe.put(curCue.toString(), resCueProbe);
						resCueProbe = new SimCue(probeIDfromSt(curCue,curNameStProbe),null);
						tempResProbe.put(probeIDfromSt(curCue,curNameStProbe), resCueProbe);
					}
					resCueProbe.setAssocValue(new Double(currentCueLastAssocVal));
				}
				
			}
			
			// Compound:
			if (curNameSt.length()>2){ //"AB+"
//				SimCue tempCue = (SimCue) tempRes.get(curNameSt.subSequence(0,curNameSt.length()-1));
//				double compound = getTotalCueAssoc(currentSt,tempRes); 
//				tempCue.setAssocValue(compound);
				SimCue compCue = (SimCue) cues.get(curNameSt.subSequence(0,curNameSt.length()-1));
				SimCue compRes = (SimCue) tempRes.get(curNameSt.subSequence(0,curNameSt.length()-1));
				double compound = getTotalCueAssoc(currentSt,cues); 
				compCue.setAssocValue(compound);
				//compRes.setAssocValue(compound);
				compRes.setAssocValue(lastAssoc);
			}
			// Unused cues keep the previous values in these trials.
			// RO20090922
			//updateUnusedCues((i + 1), tempRes);
	    }
	}
	
	/**
	 * Returns the probe ID from a trial String. 
	 * The ID is composed as 
	 * Examples: A^B+ --> A^{AB+}
	 *           A, A^B^C- --> A^{ABC-}
	 *           B, A^B^C- --> B^{ABC-}
	 * @param cueName the cue for which the id
	 * @param nameSTProbe the string with the probe trial
	 * @return
	 */
	private String probeIDfromSt(Character cueName, String nameStProbe) {
		String s = "";
		for (int i=0; i < nameStProbe.length(); i++) {
			if ((nameStProbe.charAt(i)>='A' && nameStProbe.charAt(i)<='Z') ||
				  nameStProbe.charAt(i) == '+' || nameStProbe.charAt(i) == '-') {
				s += nameStProbe.charAt(i);
			}
		}
		//return cueName + "^{" + nameStProbe.replace("^", "") + "}";
		return cueName + "^{" + s + "}";
	}
	
	/**
	 * Returns an exact TreeMap copy from the TreeMap that is been given.
	 * It iterates through it's keys and puts their values into a new object.
	 * @param orig the original TreeMap object which to copy from.
	 * @return
	 */
	private TreeMap<String,SimCue> copyKeysMapToTreeMap(TreeMap<String,SimCue> orig) { 
	    TreeMap<String,SimCue> reqMap = new TreeMap<String,SimCue>(); 
	    
	    Set<String> keys = orig.keySet();
	    
	    //Iterating over the elements in the set
	    Iterator<String> it = keys.iterator();
	    while (it.hasNext()) {
	        // Get element
	        String element = (String)it.next();
	        // Alberto Fern‡ndez July-2011: removed if
	        //if (this.isCueInStimuli(element)){ // only the cues in the phase
	        	SimCue currentCue = (SimCue) orig.get(element);
	        	SimCue values = new SimCue(currentCue.getSymbol(), currentCue.getAlpha());
	        	values.setAssocValueAt(0, currentCue.getLastAssocValue());
	        	reqMap.put(element, values);
	        //}
	    }
	    return reqMap;
	}

	
	/**
	 * Updates the unused cues. This happens by providing the current position
	 * of the trial. If the size of the associative strength arraylist is smaller
	 * that it is supposed to be, it adds the last value.
	 * @param i the current trial number. It could be on this phase or in total so far
	 * from the group's first phase.
	 * @param curCues
	 */
	private void updateUnusedCues(int i, TreeMap curCues) {
	    
        Iterator iterCue = curCues.entrySet().iterator();
        while (iterCue.hasNext()) {
            Map.Entry pairCue = (Map.Entry)iterCue.next();
            SimCue tempCue = (SimCue)pairCue.getValue();
            if(tempCue.getAssocValueSize() == i) {
                tempCue.setAssocValue(tempCue.getLastAssocValue());
            }
        }
	}
	
	/**
	 * Updates the unused cues. This happens by providing the current position
	 * of the trial. If the size of the associative strength arraylist is smaller
	 * that it is supposed to be, it adds the last value.
	 * @param i the current trial number. It could be on this phase or in total so far
	 * from the group's first phase.
	 * @param curCues
	 */
	private void updateUnusedCues(int i, HashMap curCues) {
	    
        Iterator iterCue = curCues.entrySet().iterator();
        while (iterCue.hasNext()) {
            Map.Entry pairCue = (Map.Entry)iterCue.next();
            SimCue tempCue = (SimCue)pairCue.getValue();
            if(tempCue.getAssocValueSize() == i) {
                tempCue.setAssocValue(tempCue.getLastAssocValue());
            }
        }
	}

	/**
	 * Returns the cues last known V, associative strength value. If the stimulus
	 * is compound then adds the the last values from all cues that participate.
	 * @param currentSt the current stimulus
	 * @param cs the temporal results TreeMap
	 * @return the last associative strength value.
	 */
	private double getTotalCueAssoc(SimStimulus currentSt, TreeMap cs) { 
		double tca = 0;
	    		
		int cNo = currentSt.getCueNames().length();
		for (int i = 0; i < cNo; i++) {
			Character c = new Character(currentSt.getCueNames().charAt(i));
			SimCue currentCue = (SimCue) cs.get(c+"");
			tca += currentCue.getLastAssocValue().doubleValue();			
		}
		return tca;
	}



	
	/**
	 * Returns true if the cue is in the stimuli of the phase.
	 * (Returns true if the cue is taking part in the phase)
     * @param cue the cue looked for
 	 * @return if the cue is taking part in the current phase
	 */
	public boolean isCueInStimuli(String cue){
	 	   boolean is = false;
	 	   // looking for the cue in the stimuli
	 	   Set<String> keys = stimuli.keySet();
	 	   for (String key: keys) {
	 		   	if (cue.length()>1){ //cue is a compound --> check with the complete name
	 		   		if (stimuli.get(key).getCueNames().equals(cue)) {
	 		   			is = true; 
	 		   			break;
	 		   		}
	 		   	}else if (key.indexOf(cue)!=-1) { // cue is a simple one --> check if cue is inside the stimuli
	    				is = true; 
	    				break;
	    		}
	 	   }
	 	   return is;
	}
	
	/**
	 * Returns the results into a HashMap containing the cues that are
	 * participate in this phase or in the other group's phase's (their value
	 * remain the same) with their associative strengths.
	 * @return the results from the algorithms process.
	 */
	public TreeMap<String,SimCue> getResults() { 
	    return results;
	}
	
	/**
	 * Returns the results into a HashMap containing the stimuli that are
	 * participate in this phase or in the other group's phase's (their value
	 * remain the same) with their associative strengths.
	 * @return the stimuli of the phase.
	 */
	public HashMap<String,SimStimulus> getStimuli() {
	    return stimuli;
	}
	
	/**
	 * Returns the total number of trials that this phase contains.
	 * @return the number of trials.
	 */
	public int getNoTrials() {
	    return trials;
	}
	
	/**
	 * Returns the initial sequence that was entered by the user on the 
	 * phases table.
	 * @return the initial sequence.
	 */
	public String intialSequence() {
	    return initialSeq;
	}
	
	/**
	 * Empty the HashMap with the results inside. This happens in case that 
	 * the user chooses to keep the same information on the phase table but 
	 * wishes to update their value.
	 *
	 */
	public void emptyResults() {
	    results.clear();
	    resultsProbe.clear();
	}

	/**
	 * Return if the phase will be randomly executed
	 * @return
	 */
	public boolean isRandom() {
		return random;
	}

	/**
	 * Set the random attribute for this phase
	 * @param random
	 */
	public void setRandom(boolean random) {
		this.random = random;
	}
	
	/**
     * Returns the phase's 'lambda' value which represents the reinforced
     * stimuli.
     * @return a 'lambda' value for the reinforced stimuli.
     */
	public Double getLambdaPlus() {
		return lambdaPlus;
	}
	
	/**
     * Returns the phase's 'lambda' value which represents the non-reinforced
     * stimuli.
     * @return a 'lambda' value for the non-reinforced stimuli.
     */
	public Double getLambdaMinus() {
		return lambdaMinus;
	}

	/**
     * Returns the phase's 'beta' value which represents the reinforced
     * stimuli.
     * @return a 'beta' value for the reinforced stimuli.
     */
	public Double getBetaPlus() {
		return betaPlus;
	}
	
	/**
     * Returns the phase's 'beta' value which represents the non-reinforced
     * stimuli.
     * @return a 'beta' value for the non-reinforced stimuli.
     */
	public Double getBetaMinus() {
		return betaMinus;
	}

	/**
     * Sets the phase's 'lambda' value which represents the reinforced
     * stimuli.
     * @param l 'lambda' value for the reinforced stimuli.
     */
	public void setLambdaPlus(Double l) {
		lambdaPlus = l;
	}
	
	/**
     * Sets the phase's 'lambda' value which represents the non-reinforced
     * stimuli.
     * @param l 'lambda' value for the non-reinforced stimuli.
     */
	public void setLambdaMinus(Double l) {
		lambdaMinus = l;
	}
	
	/**
     * Sets the phase's 'beta' value which represents the reinforced
     * stimuli.
     * @param l 'beta' value for the reinforced stimuli.
     */
	public void setBetaPlus(Double l) {
		betaPlus = l;
	}
	
	/**
     * Sets the phase's 'beta' value which represents the non-reinforced
     * stimuli.
     * @param l 'beta' value for the non-reinforced stimuli.
     */
	public void setBetaMinus(Double l) {
		betaMinus = l;
	}
	
	// Alberto Fernandez. July 2012	
	
	/**
	 * @return the contextCfg
	 */
	public ContextConfig getContextConfig() {
		return contextCfg;
	}

	/**
	 * @param contextCfg the contextCfg to set
	 */
	public void setContextConfig(ContextConfig contextCfg) {
		this.contextCfg = contextCfg;
	}


	// AF Aug-2012 from TD
	/**
	 * @param control
	 */
	public void setControl(ModelControl control) {
		this.control =  control;
	}
	
	// AF Aug-2012
	public int getNumberOfValuesInResults() {
		int n = 0;
        Set<String> keys = results.keySet(); // phase results
        Iterator<String> it = keys.iterator();
        // for all the cues in the phase
        SimCue cue;
        while (it.hasNext()) {
        	String element = (String)it.next();
        	cue = results.get(element);
        	n += cue.getAssocValueSize();
		}
        return n;
	}

	/**
	 * Returns the stimuli probe results into a HashMap containing the cues that are
	 * participate in this phase or in the other group's phase's (their value
	 * remain the same) with their associative strengths.
	 * @return the results from the algorithms process.
	 */
	public TreeMap<String,SimCue> getResultsProbe() { 
	    return resultsProbe;
	}

	

}
