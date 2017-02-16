import java.net.*;
import java.io.*;
public class C_ECO_D{
  public static void main(String[] args) {
    try{
      String host = "127.0.0.1";
      int pto = 7000;
      DatagramSocket cl = new DatagramSocket();
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Escribe un mensaje <Enter> para enviar, escribe 'salir' para terminar");
      for(;;){
        String msj = br.readLine();
        byte[] b = msj.getBytes();
        if(b.length>1500){
          ByteArrayInputStream bais = new ByteArrayInputStream(b);
          int n = 0;
          byte[] tmp = new byte[1500];
          while((n=bais.read(tmp))!=-1){
            DatagramPacket p = new DatagramPacket(tmp,tmp.length,InetAddress.getByName(host),pto);
            cl.send(p);
            if(msj.indexOf("salir")>=0){
              System.out.println("Termina Aplicacion");
              bais.close();
              cl.close();
              System.exit(0);
            }
            DatagramPacket p1 = new DatagramPacket(new byte[1500],1500);
            cl.receive(p1);
            System.out.println("Eco recibido desde: "+p1.getAddress()+":"+p1.getPort()+"Con el eco: "+new String(p1.getData(),0,p1.getLength()));
          }
        }else{
          DatagramPacket p = new DatagramPacket(b,b.length,InetAddress.getByName(host),pto);
          cl.send(p);
          DatagramPacket p1 = new DatagramPacket(new byte[1500],1500);
          cl.receive(p1);
          System.out.println("Eco recibido desde: "+p1.getAddress()+":"+p1.getPort()+"con el eco: "+new String(p1.getData(),0,p1.getLength()));
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
