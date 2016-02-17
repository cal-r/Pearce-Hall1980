package _from_RW_simulator;

import Constants.DefaultValuesConstants;

import java.io.Serializable;

/**
 * City University
 * BSc Computing with Artificial Intelligence
 * Project title: Building a TD Simulator for Real-Time Classical Conditioning
 * @supervisor Dr. Eduardo Alonso
 * @author Jonathan Gray
 */
public class ContextConfig implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7555945696185751652L;

    /**
     * Enum holding context symbols.
     * City University
     * BSc Computing with Artificial Intelligence
     * Project title: Building a TD Simulator for Real-Time Classical Conditioning
     * @supervisor Dr. Eduardo Alonso
     * @author Jonathan Gray
     *
     */

    public enum Context implements Serializable {
        EMPTY(""),PHI("\u03A6"), PSI("\u03A8"), THETA("\u0398"), XI("\u039E"),
        // AF July-2012
        OMEGA("\u03A9"), KAPPA("\u039A");
        private Context(String name) {
            this.name = name;
        }
        /** Symbol. **/
        private String name;

        public String toString() {
            return name;
        }

        /**
         * Check if a cue is a context.
         * @param in
         * @return
         */

        public static boolean isContext(final String in) {
            for(Context ctxt : Context.values()) {
                if(ctxt.name.equals(in)) {
                    return true;
                }
            }
            return false;
        }

        /**
         *
         * @return an array of all the contexts except the empty one.
         */

        public static Context[] getList() {
            Context[] list = new Context[] {PHI, PSI, THETA, XI, OMEGA, KAPPA};
            return list;
        }

        // Alberto Fernandez. August-2012

        /**
         * Check if a string contains a context.
         * @param in
         * @return
         */

        public static boolean containsContext(final String in) {
            for(Context ctxt : Context.getList()) {
                if(in.contains(ctxt.name)) {
                    return true;
                }
            }
            return false;
        }

    }

    /** Symbol this context is a config for. **/
    private Context context;
    /** Alpha for this context. **/
    private double alpha;

    public double getSe() {
        return se;
    }

    public void setSe(double se) {
        this.se = se;
    }

    public double getSi() {
        return si;
    }

    public void setSi(double si) {
        this.si = si;
    }

    private double se;
    private double si;
    /** Empty context. **/
    public static final ContextConfig EMPTY = new ContextConfig(Context.EMPTY, 0, 0, 0);
    /** Default alpha. **/
    private static double defaultAlpha = 0.15;
    /** Default context. **/
    private static Context defaultContext = Context.PHI;
    /** Whether default has been updated. **/
    private static boolean defaultAlphaUpdated = false;
    private static boolean defaultContextUpdated = false;

    public ContextConfig() {
        context = defaultContext;
        alpha = DefaultValuesConstants.INITIAL_ALPHA;
        se = DefaultValuesConstants.SALIENCE_EXCITATORY;
        si = DefaultValuesConstants.SALIENCE_INHIBATORY;
    }

    public ContextConfig(final Context context, final double alpha, final double se, final double si) {
        this.context = context;
        this.alpha = alpha;
        this.se = se;
        this.si = si;
    }

    /**
     * @return the context
     */
    public Context getContext() {
        return context;
    }
    /**
     * @param context the context to set
     */
    public void setContext(Context context) {
        this.context = context;
        if(!defaultContextUpdated) {
            defaultContext = context;
            defaultContextUpdated = true;
        }
    }
    /**
     * @return the alpha value
     */
    public Double getAlpha() {
        return alpha;
    }
    /**
     * @param alpha the alpha value to set
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
        if(!defaultAlphaUpdated) {
            defaultAlpha = alpha;
            defaultAlphaUpdated = true;
        }
    }

    public String getSymbol() {
        return context.toString();
    }

    public String toString() {
        return context + "("+getAlpha().floatValue()+";"+getSe()+";"+getSi()+")";
    }

    public static void clearDefault() {
        defaultAlpha = 0.15;
        defaultAlphaUpdated = false;
        defaultContext = Context.PHI;
        defaultContextUpdated = false;
    }
}
