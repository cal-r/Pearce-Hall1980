package Helpers;

import sun.misc.Launcher;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Rokas on 07/02/2016.
 */
public class ResourceHelper {

    public static ImageIcon getRandomImageIcon(){ return getImageIcon("/images/Random.png"); }

    public static ImageIcon getForbiddenImageIcon(){ return getImageIcon("/images/forbidden.jpg"); }

    public static ImageIcon getAboutImageIcon(){
        return getImageIcon("/images/About.png");
    }

    public static ImageIcon getFooterImageIcon(){
        return getImageIcon("/images/Foot.png");
    }

    public static Image getTopleftImage(){
        return getImageIcon("/images/P-Hisizes/P&H-32.png").getImage();
    }

    public static ImageIcon getImageIcon(String path){
        return new ImageIcon(Launcher.class.getResource(path));
    }
}
