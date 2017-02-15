import java.net.*;
import java.io.*;
public class S_ECO{
  public static void main(String[] args){
    try {
      int pto = 5000;
      ServerSocket s = new ServerSocket(pto);
      s.setReuseAddress(true);
      System.out.println("Servicio iniciado... Esperando clientes...");
      for (; ; ) {
        Socket cl = s.accept();
        System.out.println("Cliente conectado desde->"+cl.getInetAddress()+":"+cl.getPort());
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
        string msj ="";
        for (; ; ) {
          msj= br.readLine();
          if (msj.compareToIgnoreCase("salir")==0) {
            System.out.println("El cliente terminó");
            br.close();
            pw.close();
            cl.close();
            break;
          }else {
            System.out.println("Mensaje recibido ");
            String eco = msj + "ECO";
            pw.println(eco);
            pw.flush();
            continue;
          }//if
        }//for
      }//for
    }catch(Exception e){
      e.printStockTrace();
    }
  }
}
