package Helpers.GUI;

import javax.swing.*;

/**
 * Created by Rokas on 23/04/2016.
 */
public class FrameCloser{
    private JFrame frame;

    public FrameCloser(JFrame frame) {
        this.frame = frame;
    }

    public void closeFrame(){
        frame.dispose();
        //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}