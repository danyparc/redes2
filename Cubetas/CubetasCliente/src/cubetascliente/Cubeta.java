/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubetascliente;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Cubeta extends Thread{
    private ArrayList<Integer> contenido;
    private int max; 
    private int min;
    private final String server = "127.0.0.1";
    private int pto;
    public ArrayList<Integer> getContenido() {
        return contenido;
    }
    
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
    
    public Cubeta(int min, int inter, int pto){
        this.min=min;
        max=min+inter-1;
        this.pto = pto;
        contenido=new ArrayList();
    }
    
    public void addItem(Integer i){
        contenido.add(i);
    }
    
    public void printContenido(){
        for(Integer i: contenido)
            System.out.print(i+" ");
        System.out.println("");
    }
    @Override
    public void run(){
        if(contenido.size()>0){
            Socket cl=null;
            try{            
                Process p = new ProcessBuilder("/Users/danyparc/Documents/redes2/Cubetas/serverQS", pto+"" ).start();
                InetAddress dir = InetAddress.getByName(server);
                cl = new Socket(dir, pto);
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream(3000*4);
                DataOutputStream dos2 = new DataOutputStream(baos);
                System.out.println("\nEnviando Batch ("+min+","+max+") a " + dir.toString() +":"+ pto );
                dos2.writeInt(contenido.size());
                for(int i=0; i<contenido.size();i++ ){
                    dos2.writeInt(contenido.get(i));
                }                
                dos.write(baos.toByteArray());
                dos.flush();
                for(int i=0; i<contenido.size();i++ ){
                    contenido.set(i, dis.readInt());              
                }
                System.out.println("\nBatch ("+min+","+max+") recibido");
                cl.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
