package Helpers;

import Constants.GuiStringConstants;

import java.net.URI;

/**
 * Created by Rokas on 23/04/2016.
 */
public class BrowserHelper {

    public static void GoToGuide(){
        try {
            java.awt.Desktop.getDesktop().browse(new URI(GuiStringConstants.GUIDE_URL));
        }catch (Exception ex){

        }
    }

}
