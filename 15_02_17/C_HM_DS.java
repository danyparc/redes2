import java.net.*;
import java.io.*;

public class C_HM_DS{
  public static void main(String[] args) {
    try{
      //BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
      int pto=9000;

      DatagramSocket s = new DatagramSocket(pto);
      System.out.println("Esperando mensaje...");
      for(;;){
      DatagramPacket p = new DatagramPacket(new byte[1500],1500);
      s.receive(p);
      System.out.println("Mensaje recibido desde:"+p.getInetAddress()+":"+p.getPort());
      String datos = new String(p.getData(),0,p.getLength());
      System.out.println("Datos: "+datos);
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
