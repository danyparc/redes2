import java.net.*;
import java.io.*;
public class S_ECO_D{
  public static void main(String[] args) {
    try{
      int pto = 7000;
      DatagramSocket s = new DatagramSocket(pto);
      for (; ; ) {
        DatagramPacket p= new DatagramPacket(new byte[1500],1500);
        s.receive(p);
        System.out.println("Msj recibido desde:"+p.getAddress()+":"+p.getPort());
        String msj=new String(p.getData(),0,p.getLength());
        if (msj.indexOf("salir")>=0) {
          System.out.println("Termina aplicación");
        }
        System.out.println(msj);
        DatagramPacket pback = new DatagramPacket(p.getData(),p.getLength(),p.getAddress(),p.getPort());
        s.send(pback);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
