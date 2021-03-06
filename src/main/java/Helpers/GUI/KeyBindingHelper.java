package Helpers.GUI;

import Constants.GuiStringConstants;
import Controllers.MenuController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Rokas on 23/04/2016.
 */
public class KeyBindingHelper {

    private JComponent component;
    private MenuController menuController;

    public KeyBindingHelper(JComponent component, MenuController menuController){
        this.component = component;
        this.menuController = menuController;
    }

    public void bind(JMenuItem menuItem, final String action){
        Integer secondKey = getSecondKeyForAction(action);
        if(secondKey != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(secondKey, InputEvent.ALT_MASK));
        }
    }

    private static Integer getSecondKeyForAction(String action){
        switch (action) {
            case GuiStringConstants.NEW:
                return KeyEvent.VK_N;
            case GuiStringConstants.EXIT:
                return KeyEvent.VK_Q;
            case GuiStringConstants.SAVE:
                return KeyEvent.VK_S;
            case GuiStringConstants.OPEN:
                return KeyEvent.VK_O;
            case GuiStringConstants.GUIDE:
                return KeyEvent.VK_G;
            case GuiStringConstants.ABOUT:
                return KeyEvent.VK_A;
        }
        return null;
    }
}
