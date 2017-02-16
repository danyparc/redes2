import java.net.*;
import java.io.*;

public class C_HM_D{
  public static void main(String[] args) {
    try{
      BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
      int pto=9000;
      System.out.println("Escribe la dirección del servidor:");
      String host = br.readLine();
      System.out.println("Escribe un mensaje: ");
      String msj = br.readLine();
      byte[] b= msj.getBytes();
      /*Si el tamaño de byte > 65535 hay que manejar los envíos de alguna forma
      * Enviar en pedacitos
      */
      DatagramSocket cl = new DatagramSocket();
      System.out.println("Enviando mensaje...");
      DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName(host), pto);
      cl.send(p);
      System.out.println("Mensaje enviado");
      cl.close();
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
