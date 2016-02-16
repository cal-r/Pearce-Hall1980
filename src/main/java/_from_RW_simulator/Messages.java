package _from_RW_simulator;
/**
hack
 */

public class Messages {
    public static String getString(String key) {
        switch (key) {
            case "ContextDialog.warningTitle":
                return "Warning";
            case "ContextDialog.DataFormatError":
                return "Warning\\! wrong value";
            default:
                return '!' + key + '!';
        }
    }
}
