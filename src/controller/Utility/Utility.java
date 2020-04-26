package controller.Utility;

/**
 * In questa classe c'è un metodo utile usato nel progetto
 */

public class Utility {
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}