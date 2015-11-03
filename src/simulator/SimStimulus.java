/**
 * SimStimulus.java
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

/**
 * SimStimulus class represents a model for a stimulus (combined or not), although if 
 * 2 same stimulus are placed in the experiment but in different order, it will be counted on
 * the same objects, the system will add the extra trials. 
 */
public class SimStimulus {
	
    private int trials;
	private String fullName, cueNames;
	private boolean reinforced;
	
	/**
	 * Stimulus' Constructor method
	 * @param name is the full name of the Stimulus containing the number of trials, 
	 * the actual set of cues and the reinforced sign (e.g. "6AB+").
	 * @param tr is the initial number of times that the stimulus is introduced.
	 * @param cnames only the set of cues.
	 * @param reinf confirms if the stimulus is reinforced.
	 */
	public SimStimulus(String name, int tr, String cnames, boolean reinf) {
		
		fullName = name;
		trials = tr;
		cueNames = cnames;
		reinforced = reinf;
	}
	
	/**
	 * Returns the String presentation of the stimulus containing the number 
	 * of trials and the reinforced sign.
	 * @return a String value of the stimulus presentation
	 */
	public String getName(){
		
		return fullName;
	}
	
	/**
	 * Returns the String presentation of the containing cues in the stimulus.
	 * @return a String describing the containing cues.
	 */
	public String getCueNames(){
		
		return cueNames;
	}
	
	/**
	 * Returns the number of trials that the specific cue appears in the phase.
	 * @return a number specifying the times the stimulus appears.
	 */
	public int getTrials(){
	
		return trials;
	}
	
	/**
	 * Adds the extra number of trials on the existed ones, if there is an existed stimuli
	 * on the ordered sequence.
	 * @param n a number specifying the extra trials to be added.
	 */
	public void addTrials(int n){
	
		trials += n;
	}
	
	/**
	 * Returns true if the stimulus is been reinforced by an US and false if is not.
	 * @return determines if it is reinforced or not.
	 */
	public boolean isReinforced(){
		
		return reinforced;
	}
}
