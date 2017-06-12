/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manejador.de.datos;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Juan Pablo
 */
public class Cliente {
    
     public static void main(String args[]) {
        try {
            //System.out.println(campo1.getText());
            System.out.println("Especifique la IP del servidor (localhost): ");
            Scanner reader = new Scanner(System.in);
            String host = reader.nextLine();
            if (host == "\n"){
                host = "localhost";
            }

            System.out.println("Especifique el puerto del servidor: ");
            //Scanner reader = new Scanner(System.in);
            int pto = reader.nextInt();
            if (pto == 0){
                pto = 5000;
            }

            //Socket cl = new Socket(host,pto);
             Socket cl = new Socket(host,5000);
            System.out.println("Host: " + host + " Puerto: "+pto);
            System.out.println("Conexion Establecida");
            System.out.print("> ");

            String cmd="";
            do {
            cmd = reader.nextLine();

            // Escribir
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            dos.writeUTF(cmd);
            //dos.close();

            // Leer
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            System.out.println(cl.isClosed());
            String respuesta = dis.readUTF();
            System.out.println(respuesta);
            //dis.close();
          }while(cmd.compareToIgnoreCase("exit")!=0);

            cl.close();



        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
    
    
}
