package _from_RW_simulator;
/**
 * The Centre for Computational and Animal Learning Research (CAL-R)
 * @title Rescorla & Wagner Model Simulator
 * @author Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray

 * Preliminary version 10-Mar-2005 Esther Mondragón, Eduardo Alonso & Dioysios Skordoulis.
 * Modified October 2009  Esther Mondragón, Eduardo Alonso & Rocío García Durán.
 * Modified July 2011 Esther Mondragón, Eduardo Alonso & Alberto Fernández.
 * Modified October 2012 Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray.
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class Messages {
    private static final String BUNDLE_NAME = "simulator.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
