package simulator;

/**
 * SimSimulator.java
 * 
 * The Centre for Computational and Animal Learning Research (CAL-R)
* @title Rescorla & Wagner Model Simulator
* @author Esther Mondrag�n, Eduardo Alonso, Alberto Fern�ndez & Jonathan Gray

* Preliminary version 10-Mar-2005 Esther Mondrag�n, Eduardo Alonso & Dioysios Skordoulis.
* Modified October 2009  Esther Mondrag�n, Eduardo Alonso & Roc�o Garc�a Dur�n.
* Modified July 2011 Esther Mondrag�n, Eduardo Alonso & Alberto Fern�ndez.
* Modified October 2012 Esther Mondrag�n, Eduardo Alonso, Alberto Fern�ndez & Jonathan Gray.
 *
 */
import jsr166y.ForkJoinPool;
import simulator.*;

/**
 * Centre for Computational and Animal Learning Research
 * @title Rescorla & Wagner Model Simulator
 * @author Alberto Fernandez, Jonathan Gray, Esther Mondrag�n & Eduardo Alonso.
 * @e-mail e.mondragon@cal-r.org
 * @info  Simulator creates phase (model), view and controller. They are created
 * once here and passed to the other parts that need them so there
 * is only on copy of each. The MVC structure.
 */
public class Simulator {
	private SimModel model;
	private SimView view;
	private SimController controller;
	
	// AF July-2012
	public static final char OMEGA = '\u03A9';
	public static final double VERSION = 4.01; // AF July-2012

	// AF Sep-2012 from TD
	public static ForkJoinPool fjPool = new ForkJoinPool();

		
	/**
	 * Simulator's Constructor method
	 */
	public Simulator() {
		model = new SimModel();
		view = new SimView(model);
		controller = new SimController(model, view);
		 
		view.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		view.setVisible(true);
		 
	}
	/**
	 * The main method of the application. It creates a new Simulator object.
	 * This is the method that is needed to start up the program.
	 * @param args by default this parameter is needed if any arguments inserted
	 * from the command prompt.
	 */	
	// Modified by E. Mondragon. July 29, 2011 
	//public static void main(String[] args) {
	//javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
	//new Simulator();
	public static void main(String[] args) {
		javax.swing.JFrame.setDefaultLookAndFeelDecorated(false);
		new Simulator();
	}
}
