package Helpers;

import sun.misc.Launcher;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Rokas on 07/02/2016.
 */
public class ResourceHelper {
    public static ImageIcon getAboutImage(){
        return getImage("/images/About.png");
    }

    public static ImageIcon getFooterImage(){
        return getImage("/images/Foot.png");
    }

    public static Image getIconImage(){
        return getImage("/images/P-Hisizes/P&H-32.png").getImage();
    }

    public static ImageIcon getImage(String path){
        return new ImageIcon(Launcher.class.getResource(path));
    }
}
