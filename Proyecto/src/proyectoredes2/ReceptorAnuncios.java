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
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author Luis Aguilar
 */
public class ReceptorAnuncios extends Thread{
    JList<String> jList1;
    DefaultListModel<String> model;
    
    public ReceptorAnuncios(JList<String> jList1){
        this.jList1 = jList1;
    }
    
    public void run(){
        InetAddress gpo=null;
        int pto =9999;
        model = new DefaultListModel<>();
        jList1.setModel(model);
        try{
            MulticastSocket cl= new MulticastSocket(pto);
            System.out.println("Cliente escuchando puerto "+ cl.getLocalPort());
            cl.setReuseAddress(true);
            try{
                gpo = InetAddress.getByName("228.1.1.1");
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }//catch
            cl.joinGroup(gpo);
            System.out.println("Unido al grupo");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[10],10);
                cl.receive(p);
                //System.out.println("Datagrama recibido..");
                String msj = new String(p.getData());
                System.out.println("Servidor descubierto: "+p.getAddress());
                String ad = ""+p.getAddress();
                ad = ad.substring(1);
                System.out.println(ad+" Nombre: "+msj);
                String fin = ad+" Nombre: "+msj;
                if(model.contains(fin)){
                    System.out.println("Ya esta contenido");
                }else{
                    model.addElement(fin);
                }
                
            }//for
            
        }catch(Exception e){
            
        }//catch
    }
    
}
