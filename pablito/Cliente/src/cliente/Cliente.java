/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;
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

            System.out.println("Especifique el puerto del servidor (8080): ");
            //Scanner reader = new Scanner(System.in);
            int pto = reader.nextInt();
            if (pto == 0){
                pto = 5000;
            }

            //Socket cl = new Socket(host,pto);
             Socket cl = new Socket(host,5000);
            System.out.println("Host: " + host + " Puerto: "+pto);
            System.out.println("Conexion Establecida\n Bienvenido a JDBM 1.0\n---------");
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

            String respuesta = dis.readUTF();
            System.out.println(respuesta);
            //dis.close();
          }while(cmd.compareToIgnoreCase("quit")!=0);

            cl.close();



        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
    
    
}
