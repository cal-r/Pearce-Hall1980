package Helpers;

import sun.misc.Launcher;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Rokas on 07/02/2016.
 */
public class ResourceHelper {
    public static ImageIcon getFooterImage(){
        return getImage("/images/Foot.png");
    }

    public static Image getIconImage(){
        return getImage("/images/P-Hisizes/P&H-16.png").getImage();
    }

    private static ImageIcon getImage(String path){
        return new ImageIcon(Launcher.class.getResource(path));
    }
}
