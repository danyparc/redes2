/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manejador.de.datos;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Juan Pablo
 */
public class SerlializeDatabase {
    //Contructor publico
    public SerlializeDatabase(){}
    
    //Permite serializar nuestra base de datos
    public static void serialize(Object root) {
        ObjectOutputStream out = null;
        FileOutputStream out_file = null;

        try {
            out_file = new FileOutputStream("basesita");
            out = new ObjectOutputStream(out_file);

            out.writeObject(root);
            out.close();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    //Permite desealizar nuestra base de datos
    public static Object deserialize() throws ClassNotFoundException {
        ObjectInputStream in = null;
        FileInputStream in_file = null;

        Object o = null;

        try {
            in_file = new FileInputStream("basesita");
            in = new ObjectInputStream(in_file);

            o = in.readObject();
            in.close();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
            System.exit(3);
        }

        return o;
    }
    
}
