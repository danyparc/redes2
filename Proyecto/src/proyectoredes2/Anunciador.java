/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author Luis Aguilar
 */
public class Anunciador extends Thread {
    String nombre = "paco";
    
    public static void main(String[] args){
        Anunciador a = new Anunciador("Patricia");
        a.start();
    }
    
    Anunciador(String nombre){
        this.nombre = nombre;
    }
    
    public void run() {

        InetAddress gpo = null;
        int pto = 9999;
        System.out.println("Anunciador anunciando :v");
        try {
            MulticastSocket s = new MulticastSocket(pto);
            s.setReuseAddress(true);
            s.setTimeToLive(255);
            String msj = nombre;
            byte[] b = msj.getBytes();
            try {
                msj = ""+InetAddress.getLocalHost().getHostAddress();
                msj = msj.substring(1);
                gpo = InetAddress.getByName("228.1.1.1");
            } catch (UnknownHostException u) {
                System.err.println("Direccion no valida");
            }//catch
            s.joinGroup(gpo);
            for (;;) {
                DatagramPacket p = new DatagramPacket(b, b.length, gpo, pto);
                s.send(p);
                s.setTimeToLive(255);
                //System.out.println("Enviando mensaje "+msj+ " con un TTL= "+ s.getTimeToLive());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                }
            }//for
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }

}
