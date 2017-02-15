import java.io.*;
import java.net.*;
public class Envia_o{
  public static void main(String[] args) {
    try{
      String host ="127.0.0.1";
      int pto=8888;
      Socket cl = new Socket(host,pto);
      System.out.println("Conexión establecida... \nenviando objeto...");
      ObjectOutputStream oos=new ObjectInputStream(cl.getInputStram());
      Objeto o= new Objeto("Pepe","Suarez","Gonzales",20,12345);
      oos.writeObject(o);
      oos.flush();
      System.out.println("Se envió objeto con los datos:\nNombre: "+o.nombre+
                          "\nApellido pat: "+o.apaterno+"\nApellido mat:"
                          +o.amaterno+"\nEdad:"+o.edad+"\nClave:"+o.clave);
      Objeto o1=(Objeto)ois.readObject();
      System.out.println("Objeto recibido con los datos:\nNombre: "+o1.nombre+
                          "\nApellido pat: "+o1.apaterno+"\nApellido mat:"
                          +o1.amaterno+"\nEdad:"+o1.edad+"\nClave:"+o1.clave);
      oos.close();
      ois.close();
      cl.close();
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
