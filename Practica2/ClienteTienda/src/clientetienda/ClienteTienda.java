/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientetienda;
import producto.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import com.google.gson.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author David
 */
public class ClienteTienda {
    public static DatagramSocket s;
    public static InetAddress dir;
    public static int pto;
    public static boolean sendSale(ArrayList<Pedido> pedidos){
        try{
            Gson gson = new Gson();
            String jsonPedidos = gson.toJson(pedidos);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(jsonPedidos);
            oos.flush();
            byte[] b = baos.toByteArray();
            DatagramPacket answer = new DatagramPacket(b, b.length,dir , pto);
            s.send(answer);
            baos.close();
            oos.close();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static void downloadImage(String name, long size){
        try{
            System.out.println("Descargando "+name+" de tamaño "+size);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(name);
            oos.flush();
            byte[] b = baos.toByteArray();
            System.out.println(dir.getCanonicalHostName());
            DatagramPacket request = new DatagramPacket(b, b.length, dir,pto);
            s.send(request);
            long received = 0;
            int maxSecuencia = 0;
            while(received < size){
                System.out.println("Faltan "+(size - received));
                DatagramPacket p = new DatagramPacket(new byte[20150], 20150);
                s.receive(p);
                ByteArrayInputStream bais = new ByteArrayInputStream(p.getData(), 0 , p.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Fragmento fr = (Fragmento)ois.readObject();
                System.out.println("Recibiendo fragmento "+ fr.secuencia + " de longitud "+fr.tam);
                received+=fr.tam;
                bais.close();
                ois.close();
                FileOutputStream fos = new FileOutputStream(new File("tmp/"+fr.secuencia + ".ser"));
                fos.write(fr.datos, 0, fr.tam);
                fos.close();
                if(fr.secuencia > maxSecuencia)
                    maxSecuencia = fr.secuencia;
                //fr=null;
            }
            baos.close();
            oos.close();
            FileOutputStream fos = new FileOutputStream(new File("imgs/"+name));
            byte[] buffer = new byte[20000];
            int n;
            for(int i = 0; i<= maxSecuencia; i++){
                File temp = new File("tmp/"+i+".ser");
                FileInputStream fis = new FileInputStream(temp);
                while((n = fis.read(buffer))!=-1)
                    fos.write(buffer, 0, n);
                fis.close();
                Files.delete(temp.toPath());
            }
            fos.close();
            System.out.println("Fin de la descarga de "+name);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            s = new DatagramSocket();
            dir = InetAddress.getByName("127.0.0.1");
            pto = 9090;
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(new String("Enviar catalogo"));
            oos.flush();
            byte[] b = baos.toByteArray();
            DatagramPacket start = new DatagramPacket(b, b.length, dir, pto);
            s.send(start);
            DatagramPacket p = new DatagramPacket(new byte[3000], 3000);
            s.receive(p);                
            ByteArrayInputStream bais = new ByteArrayInputStream(p.getData(), 0 , p.getLength());
            System.out.println(p.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            System.out.println("Recibido");
            Object o = ois.readObject();
            Producto[] productos;
            Gson gson = new Gson();
            String json = (String)o;
            productos = gson.fromJson(json, Producto[].class);
            int counter=0;
            for(Producto pro: productos){
                for(int i=0; i<pro.getImagenes().length; i++){
                    downloadImage(pro.getImagen(i), pro.getSize(i));
                    counter+=1;
                }
            }
            System.out.println("Se descargaron "+counter + "imagenes");
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                    
                    Tienda fs;
                    try {
                        fs = new Tienda(productos);
                        fs.setVisible(true);
                    } catch (IOException ex) {
                        Logger.getLogger(ClienteTienda.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
