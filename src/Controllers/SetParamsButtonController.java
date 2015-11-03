package Controllers;

import StringConstants.GuiConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Rokas on 03/11/2015.
 */
public class SetParamsButtonController implements ActionListener {

    private JButton button;

    public SetParamsButtonController(JButton button) {
        this.button = button;
        button.addActionListener(this);
        button.setText(GuiConstants.SET_PARAMETERS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        button.setText("sfgsdfdfds");
    }
}
