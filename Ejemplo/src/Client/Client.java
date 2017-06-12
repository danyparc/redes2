package Client;
import Data.Datos;
import Data.Datos2;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    public static List<Datos2> datagram_list= new ArrayList<Datos2>();
    public static List<Datos2> temporal= new ArrayList<Datos2>();
    public static Datos objeto_t;
    
    
    
 private static class Conect implements Runnable {
     
        private int port;
        String name;
	long received, tam;
        int maxSec;
        
        public Conect(int port){
            this.port = port;
            this.name = "";
            long received = -1;
            long tam = 0;
            int maxSec = 0;
        }
        
        @Override
        public void run() {
            try {
                MulticastSocket socket = new MulticastSocket(port);
                InetAddress Iaddress = InetAddress.getByName("230.0.0.1");
                socket.joinGroup(Iaddress);
                //Cambiar este numero por (el numero de servidores -1
                while(datagram_list.size()<=2){

                    DatagramPacket packet =  new DatagramPacket(new byte[20250], 20250);
                    socket.receive(packet);
                    System.out.println("Recibiendo paquete...");
                    ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Object o = ois.readObject();
                    if(o instanceof Datos){
                        Datos d = (Datos)o;
			name = d.nombre;
			tam = d.tam;
                        received += 1;
                        objeto_t = new Datos(name,tam);
                        System.out.println("Recibiendo "+ name + " de tamaño "+tam);
                    }
                    else if(o instanceof Datos2){
                        Datos2 d = (Datos2)o;
			received+=d.n;
                        System.out.println("Llega paquete de "+ d.n + " bytes");
			FileOutputStream fos =new FileOutputStream("tmp/"+d.sec+".ser");
                        fos.write(d.b, 0, d.n);
			fos.close();
                        System.out.println("Secuencia:"+ d.sec+"Tamaño"+d.n);
                        datagram_list.add(d); 
                        /*if(port == 4446){
                            if(d.sec == 0){
                                    datagram_list.add(d); 
                            }
                        }
                        if(port == 4447){
                        if(d.sec == 1){
                                    
                                    datagram_list.add(d);                                                                   
                                
                            }
                        }
                        if(port == 4448){
                        if(d.sec == 2){
                                    datagram_list.add(d);                                                                   
                            }
                        }*/
                        System.out.println("/////////////////////////////");
                    }
                }
                socket.leaveGroup(Iaddress);
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("java.net.preferIPv4Stack", "true");    
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new Conect(4446));//Server 1
        executor.submit(new Conect(4447));//Server 2
        executor.submit(new Conect(4448));//Server 3
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        
        for (Datos2 dato: datagram_list){
            System.out.println(dato.sec);
        }
        
        
        int number1 = datagram_list.get(0).sec;
        int number2 = datagram_list.get(1).sec;
        int number3 = datagram_list.get(2).sec;
                
                
        if (number1 > number2 && number1 > number3) {
        if(number2 > number3){
                             temporal.add(0,datagram_list.get(2));
                             temporal.add(1,datagram_list.get(1));
                             temporal.add(2,datagram_list.get(0));
                         }
                         else{
                             temporal.add(0,datagram_list.get(1));
                             temporal.add(1,datagram_list.get(2));
                             temporal.add(2,datagram_list.get(0));
                         }
                     
                        }else 
                         if ((number2 > number1 && number2 > number3))
                        {
                            if(number1 > number3)
                            {
                             temporal.add(0,datagram_list.get(2));
                             temporal.add(1,datagram_list.get(0));
                             temporal.add(2,datagram_list.get(1));
                            }
                            else
                            {
                            temporal.add(0,datagram_list.get(0));
                            temporal.add(1,datagram_list.get(2));
                            temporal.add(2,datagram_list.get(1));
                          }
                        }
                        else if ((number3 > number1 && number3 > number2))
                        {
                        if(number1 > number2)
                        {
                        temporal.add(0,datagram_list.get(1));
                        temporal.add(1,datagram_list.get(0));
                        temporal.add(2,datagram_list.get(2));
                        }
                        else
                        temporal.add(0,datagram_list.get(0));
                        temporal.add(1,datagram_list.get(1));
                        temporal.add(2,datagram_list.get(2));
                        }
                     
                     for(Datos2 dato : temporal){
                        FileOutputStream fos =new FileOutputStream("tmp/"+dato.sec+".ser");
                        fos.write(dato.b, 0, dato.n);
                        fos.close();
                     }
                      
                
                     
                     
                FileOutputStream fos2 = new FileOutputStream(new File("files/"+objeto_t.nombre));
                byte[] buffer = new byte[20250];
                int n;
                for(int i = 0; i<= temporal.get(2).sec; i++){
                File temp = new File("tmp/"+i+".ser");
                FileInputStream fis = new FileInputStream(temp);
                while((n = fis.read(buffer))!=-1)
                    fos2.write(buffer, 0, n);
                fis.close();
                Files.delete(temp.toPath());
                }
               fos2.close();
    }
                

}