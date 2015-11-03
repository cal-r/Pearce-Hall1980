/**
 * SimCue.java
 * 
 /**
 * The Centre for Computational and Animal Learning Research (CAL-R)
* @title Rescorla & Wagner Model Simulator
* @author Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray

* Preliminary version 10-Mar-2005 Esther Mondragón, Eduardo Alonso & Dioysios Skordoulis.
* Modified October 2009  Esther Mondragón, Eduardo Alonso & Rocío García Durán.
* Modified July 2011 Esther Mondragón, Eduardo Alonso & Alberto Fernández.
* Modified October 2012 Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray.
 */
package simulator;

import java.util.ArrayList;

/**
 * Cue class represents a model for every cue in the simulator's stimulus.
 * It stores the symbol of the cue, the 'alpha' value and also an ArrayList with all
 * the associative strength values that are changed on every phase.
 */
public class SimCue {
	
	private String symbol;
	private Double alpha;
	private ArrayList assocValue;

	/**
	 * Cue's Constructor method.
	 * @param symbol is the character of the cue (e.g. A, B, .. ,Z).
	 * @param alpha a Double value for the alpha of the specified cue.
	 */
	public SimCue(String symbol, Double alpha) {
		
		assocValue = new ArrayList(50);
		setAssocValue(new Double(0));
		this.symbol = symbol;
		this.alpha = alpha;
	
	}
	
	/**
	 * Returns the cue's symbol. It should be a character from the alphabet.
	 * @return  a character which represents cue's symbol (A, B, .. ,Z).
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Changes the Double variable of cue's alpha value
	 * @param alpha a double value of the alpha for the specified cue.
	 */
	public void setAlpha(Double alpha) {
	    this.alpha = alpha;
	}
	
	/**
	 * Returns the cue's 'alpha' value, a Double type variable.
	 * @return a Double value of the alpha for the specified cue.
	 */
	public Double getAlpha() {
		return alpha;
	}
	
	/**
	 * Adds a Double value at the end of the ArrayList assocValue
	 * which represents the associative values of the specified cue.
	 * @param av a Double value of the associative strength.
	 */
	public void setAssocValue(Double av) {
		assocValue.add(av);
	}

	// Added Alberto Fern‡ndez July-2011
	/**
	 * Changes the the ArrayList assocValue
	 * which represents the associative values of the specified cue.
	 * @param av an ArrayList<Double> value of the associative strength.
	 */
	public void setAssocValueVector(ArrayList<Double> av) {
		//assocValue = (ArrayList<Double>) av.clone();
		assocValue = av;
	}

	// Added Alberto Fern‡ndez July-2011
	/**
	 * Returns the the ArrayList assocValue
	 * which represents the associative values of the specified cue.
	 * @return the ArrayList<Double> value of the associative strength.
	 */
	public ArrayList<Double> getAssocValueVector() {
		return (ArrayList<Double>)assocValue;
	}


	/**
	 * Returns the last value that was entered in the associative value ArrayList.
	 * This will be the last associative value that the experiment's object will have
	 * on the current phase.
	 * If it is the first one it will be 0 (zero).
	 * @return a Double value of the last associative strength.
	 */
	public Double getLastAssocValue() {
		return (Double) assocValue.get(assocValue.size() - 1);
	}
	
	/**
	 * Returns the current size of the associative value. This will tell us how many stages
	 * the associative value has gone through.
	 * @return the number of stages the associative value has gone through.
	 */
	public int getAssocValueSize() {
		return assocValue.size();
	}
	
	/**
	 * Returns the associative value that the experiment had on a specific
	 * phase. The method will get as an argument the number of the phase.
	 * @param phase the experiments phase.
	 * @return the Double value of the associative value on the requested phase.
	 */
	public Double getAssocValueAt(int phase) {
		return (Double) assocValue.get(phase);
	}
	
	/**
	 * Updates the associative value on the ArrayList. But do succeed such an update,
	 * we will first need to remove the value from the requested position and then we
	 * add the new one again on the requested position.
	 * @param n the requested position.
	 * @param av the new associative value.
	 */
	public void setAssocValueAt(int n, Double av) {
		assocValue.remove(n);
		assocValue.add(n, av);
	}
	
	/**
	 * Resets the object's variables into their initial values. This is useful if the user
	 * chooses to restart the experiment with new values.
	 */
	public void reset() {
	    assocValue.clear();
	    setAssocValue(new Double(0));
	    alpha = new Double(0);
	}
}