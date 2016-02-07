package Helpers;

import sun.misc.Launcher;

import javax.swing.*;

/**
 * Created by Rokas on 07/02/2016.
 */
public class ResourceHelper {
    public static ImageIcon getFooterImage(){
        return getImage("/images/Foot.png");
    }

    private static ImageIcon getImage(String path){
        return new ImageIcon(Launcher.class.getResource(path));
    }
}
