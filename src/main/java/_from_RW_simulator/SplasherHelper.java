package _from_RW_simulator;

import Helpers.ResourceHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Rokas on 23/04/2016.
 */
public class SplasherHelper {
    public static void showAboutLogo() {
        // Modified by E Mondragon July 29, 2011
        JFrame.setDefaultLookAndFeelDecorated(false);

        JFrame about = new JFrame();


        JPanel aboutPanel = new JPanel();
        aboutPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        aboutPanel.setBackground(Color.WHITE);

        //modifi1ed by E.Mondragon. July 29, 2011
        //ImageIcon icon = createImageIcon(path, "About");


        ImageIcon icon = ResourceHelper.getAboutImageIcon();

        aboutPanel.setBorder(new SimBackgroundBorder(icon.getImage(), true));
        about.getContentPane().add(aboutPanel);
        about.pack();
        about.setLocation(200, 200);
        //about.setSize(520,320);
        about.setSize(596,435);//EMP 11 Oct. 2012
        about.setVisible(true);//EMP 11 Oct. 2012




        Image icon2 = ResourceHelper.getTopleftImage();
        about.setIconImage(icon2);//E.Mondragon 30 Sept 2011


    }
}
