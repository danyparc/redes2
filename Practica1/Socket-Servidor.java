/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dragdropmultiplesarchivos;

import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String []args) throws IOException{
        // Definimos el servidor con el puerto al cual se conecta el cliente
        ServerSocket servidor = new ServerSocket(9000);
        try{
           for (;;) {
            // Creamos un socket que atravez del ServerSocket
            Socket server = servidor.accept();
            
            // Aceptamos el flujo de datos enviados. 
            DataInputStream archivo = new DataInputStream(server.getInputStream());

            // Guardamos nombre y tamaño en variables locales
            String nombre = archivo.readUTF();
            long tamaño = archivo.readLong();
            
            // Declaramos la ruta a donde mover el archivo 
            String path = System.getProperty("user.dir")+nombre;
            FileOutputStream destino=new FileOutputStream(path); 

            byte[] buffer = new byte[1024]; 
            int len; 
            
            while((len=archivo.read(buffer))>0){ 
                    destino.write(buffer,0,len); 
            }
            
            System.out.println("Archivo: "+nombre+" enviado satisfactoriamente");
            System.out.println("Tamaño de Archivo:"+tamaño);
            
            server.close();
            }
        }
        catch(IOException e){
            
        }
    }
}