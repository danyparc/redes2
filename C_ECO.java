import java.net.*;
import java.io.*;

public class C_Eco{
  public static void main(String[] args) {
    try{
      InetAddress dir = null;
      String host ="";
      int pto=5000;

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      while(true){
        System.out.println("Escribe la dir. del servidor");
        host = br.readLine();
        try {
          dir=InetAddress.getByName(host);
        }catch (UnknownHostException u) {
          System.out.println("Dir. no válida");
          continue;
        }//catch
        break;
      }//while

      Socket cl = new Socket(dir,pto);
      System.out.println("Conexión establecida \n Escribe un mensaje, <enter> para enviar, 'salir' para terminar");
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
      BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
      String msj="";
      for (; ; ) {
        msj = br.readLine();
        pw.println(msj);
        pw.flush();

        if (msj.compareToIgnoreCase("salir") ==0) {
          System.out.println("Termina aplicación");
          br1.close();
          br.close();
          pw.close();
          cl.close();
          System.exit(0);
        }else {
          String eco = br1.readLine();
          System.out.println("Eco recibido:"+eco);
        }//else
      }//for

    }catch (Exception e) {
      e.printStackTrace();
    }//catch
  }
}
