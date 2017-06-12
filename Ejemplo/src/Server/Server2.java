/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Data.Datos;
import Data.Datos2;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Callmetorre
 */
public class Server2 extends Thread{
    
    private long FIVE_SECONDS = 5000;
    protected DatagramSocket socket = null;
    protected boolean moreQuotes = true;
    String nombre = "";
    long tam = 0;
    String ruta = "";
    int buf_len = 0;
    
    public Server2() throws SocketException {
        super("Server");
        socket = new DatagramSocket(4444);
    }
    
  public void run() {
       while (moreQuotes) {
            try {
                File f = new File("/Users/Callmetorre/Documents/Extra Stuff/Curriculum.docx");
                nombre = f.getName();
                tam = f.length();
                ruta = f.getPath();
                    //Cambiar este numero por el numero de servidores que hay
                buf_len = (int) (tam/3);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                Datos d2= new Datos(nombre, tam);
                oos.writeObject(d2);
                oos.flush();
                byte[] b = baos.toByteArray();
                InetAddress group = InetAddress.getByName("230.0.0.1");
                DatagramPacket packet = new DatagramPacket(b, b.length, group, 4447);
                socket.send(packet);
                byte[] buf = new byte[buf_len];
                int n=0, i=0;
		long enviados = 0;
                System.out.println("Enviando...1");
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta));
                while(enviados < tam){
                    System.out.println("Enviando...");
                    n = bis.read(buf);
                    Datos2 d = new Datos2(i, buf, n);
                    if(d.sec == 1){                   
                    System.out.println("Enviando...");
                    System.out.println("Secuencia:"+ d.sec+"Tamaño"+d.n);
                    baos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(baos);
                    oos.writeObject(d);
                    oos.flush();
                    byte[] datos = baos.toByteArray();
                    DatagramPacket p2 = new DatagramPacket(datos, datos.length, group, 4447);
                    System.out.println("Enviando a "+p2.getAddress().getHostName()+":"+packet.getPort());
                    socket.send(p2);
                    System.out.println("se envio: "+ d.sec);
                    }
                    i+=1;
                    enviados+=n;
                    Thread.sleep(300);
                }
                System.out.println("Archivo enviado");
		//bis.close();
		//baos.close();
		//oos.close();
		//socket.close();
                
                
                
                
                // construct quote
                //String dString = "Popis";
                //buf = dString.getBytes();

		// send it
                //InetAddress group = InetAddress.getByName("230.0.0.1");
                //DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                //socket.send(packet);

		    // sleep for a while
		//try {
		 // sleep((long)(Math.random() * FIVE_SECONDS));
		//} catch (InterruptedException e) { }
            } catch (IOException e) {
                e.printStackTrace();
		//moreQuotes = false;
            } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
	//socket.close();
    }
}
