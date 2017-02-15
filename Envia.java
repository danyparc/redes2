import javax.swing.JFileChooser;
import java.net.*;
public class Envia{
  public static void main(String[] args) {
    try{
      String host = "127.0.0.1";
      int pto = 8000, porcentaje=0;
      Socket cl = new Socket(host,pto);
      JFileChooser fc = new JFileChooser();
      int r = fc.showOpenDialog(null);
      if(r==JFileChooser.APPROVE_OPTION){
        File f = fc.getSelectedFile();
        String nombre = f.getName();
        String path = f.getAbsolutePath();
        long tam = f.length();
        DataOutputStrean dis=new DataOutputStream(cl.getOutputStream());
        dos.writeUTF(nombre);
        dos.flush();
        dos.writeLong(tam);
        dos.flush();
        DataInputStream dis = new DataInputStream(new FileInputStream(path));
        long enviados=0;
        while(enviados <tam){
          byte[] b = new byte[2000];
          int n=dis.read(b);
          enviados = enviados+n;
          dos.write(b,0,n);
          dos.flush();
          porcentaje=(int)((enviados*100)/tam);
          System.out.println("\r Enviado el "+porcentaje+"%");
          }//while
        System.out.println("\n Archivo enciado...");
        dis.close();
        dos.close();
        cl.close();
      }//if
    }catch (Exception e) {
      e.printStackTrace();
    }//catch
  }
}
