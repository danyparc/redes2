/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manejador.de.datos;

/**
 *
 * @author Juan Pablo
 */
public class Utils {
    public Utils(){}
    
        //Permite hacer reverse a un arreglo de Strings
    public static String[] reverseString(String[] words) {
        String[] t = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            t[i] = new StringBuilder(words[i]).reverse().toString();
        }
        return t;
    }

    //Permite crear un arreglo Strings conteniendo todas las palbras
    public static String[] obtenerPalabras(String palabras) {
        return palabras.split("\\s+");
    }
    
}
