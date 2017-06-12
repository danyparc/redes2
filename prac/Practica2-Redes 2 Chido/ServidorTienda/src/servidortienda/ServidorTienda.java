/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidortienda;

import com.google.gson.Gson;
import producto.Fragmento;
import producto.Producto;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 *
 * @author jose
 */
public class ServidorTienda {

    /**
     * @param args the command line arguments
     */
    public static ArrayList<Producto> loadProds() throws FileNotFoundException, IOException{
            ArrayList<Producto> prods = new ArrayList();
            BufferedReader bf1 = new BufferedReader(new FileReader("files/catalogo.txt"));
            //BufferedReader bf2 = new BufferedReader(new InputStreamReader(System.in));
            String linea;
            int i;
            while((linea=bf1.readLine())!=null){
                String datos[] = linea.split(":");
                System.out.println(datos[2]);
                Producto temp = new Producto(datos[0], Integer.parseInt(datos[1]), Integer.parseInt(datos[3]), Float.parseFloat(datos[2]) , datos[4], datos.length-5);                
                for(i=5; i<datos.length; i++)
                    temp.setImagen(i-5, datos[i], new File("imgs/"+datos[i]).length());
                prods.add(temp);
            }
            return prods;
    }
    public static void main(String[] args) {
        try{
            ArrayList<Producto> productos = loadProds();
            Gson gson = new Gson();
            //System.out.println("Escriba el puerto: ");
            int pto = 9090;//Integer.parseInt(bf2.readLine());
            DatagramSocket s = new DatagramSocket(pto);
            System.out.println("Servidor esperando mensajes...");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[1500], 1500);
                s.receive(p);
                System.out.println("Llega cliente " + p.getAddress().getHostName().toString()+":"+p.getPort());
                try{
                    ByteArrayInputStream bais = new ByteArrayInputStream(p.getData(), 0 , p.getLength());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Object o = ois.readObject();
                    if(o instanceof String){
                        String op = (String)o;
                        if(op.equals("Enviar catalogo")){
                            String jsonProductos = gson.toJson(productos);
                            System.out.println(jsonProductos + " "+jsonProductos.length());
                            //Enviar json
                            System.out.println("Enviando el catalogo de productos");
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(jsonProductos);
                            oos.flush();
                            byte[] b = baos.toByteArray();
                            DatagramPacket answer = new DatagramPacket(b, b.length, p.getAddress(), p.getPort());
                            s.send(answer);
                            baos.close();
                            oos.close();
                        }
                        else if(op.charAt(0) =='[' && op.charAt(op.length()-1)==']'){
                            Pedido[] ped = gson.fromJson(op, Pedido[].class);
                            for(Pedido pedido:ped){
                                for (Producto prod : productos) {
                                    if(prod.getId()==pedido.id){
                                        prod.setExistencia(prod.getExistencia()-pedido.cantidad);
                                    }
                                }
                                System.out.println("Se pidieron "+pedido.cantidad + " de "+pedido.id);
                            }
                            PrintWriter pw=new PrintWriter("files/catalogo.txt","UTF-8");
                            for (Producto producto : productos) {
                                String prodDet=producto.getNombre()+"\t"+
                                            producto.getId()+"\t"+
                                            producto.getPrecio()+"\t"+
                                            producto.getExistencia()+"\t"+
                                            producto.getDescripcion()+"\t";
                                for (String imagene : producto.getImagenes()) {
                                    prodDet+=imagene+"\t";
                                }
                                pw.println(prodDet);
                            }
                            pw.close();
                            productos=loadProds();
                        }
                        else{
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream( new File("imgs/"+op)));                           
                            System.out.println("Se abre "+op+"...");
                            int id=0, n=0;
                            byte[] b2 = new byte[20000];
                            long enviados = 0;
                            long tam = new File("imgs/"+op).length();
                            while(enviados < tam){
                                n = bis.read(b2);  
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                Fragmento fr = new Fragmento(id, b2, n);
                                oos.writeObject(fr);
                                oos.flush();
                                baos.flush();
                                byte datos[] = baos.toByteArray();
                                DatagramPacket p2 = new DatagramPacket(datos, datos.length, p.getAddress(), p.getPort());
                                s.send(p2);
                                id+=1;
                                enviados+=n;  
                                baos.close();
                                oos.close();
                                System.out.println("Enviando fragmento "+id+" de longitud "+n);
                                Thread.sleep(10);
                            }
                            System.out.println(op + " fue enviado");
                            bis.close();
                           
                        }
                    }
                    bais.close();
                    ois.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch(IOException | NumberFormatException e){
            e.printStackTrace();
        }
        
    }
    
}


class Pedido{
    public int id;
    public int cantidad;
    Pedido(int id, int cantidad){
        this.id = id;
        this.cantidad = cantidad;
    }
}
