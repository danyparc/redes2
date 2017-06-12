/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dragdropmultiplesarchivos;

/**
 *
 * @author David
 */
import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String []args) throws IOException{
        // Definimos el servidor con el puerto al cual se conecta el cliente
        ServerSocket servidor = new ServerSocket(9001);
        try{
           for (;;) {
            // Creamos un socket que a traves del ServerSocket
            Socket server = servidor.accept();
            
            // Aceptamos el flujo de datos enviados. 
            DataInputStream archivo = new DataInputStream(server.getInputStream());

            // Guardamos nombre y tamaño en variables locales
            String nombre = archivo.readUTF();
            long tamaño = archivo.readLong();
            
            // Declaramos la ruta a donde mover el archivo 
            String path = System.getProperty("user.dir")+"/recibido/"+nombre;
            FileOutputStream destino=new FileOutputStream(path);
            //System.out.println("Ubicacion: " + path );

            byte[] buffer = new byte[1024]; 
            int len; 
            
            while((len=archivo.read(buffer))>0){ 
                    destino.write(buffer,0,len); 
            }
            
            System.out.println("\nArchivo: "+nombre+" recibido satisfactoriamente");
            System.out.println("Tamaño de Archivo:"+tamaño);
            
            server.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}