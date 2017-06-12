package proyectoredes2;
import java.io.*;
import java.net.*;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


public class clsChatCliente1 extends JOptionPane implements Runnable {
    Socket Cliente=null;
    ObjectOutputStream salida;
    ObjectInputStream entrada;
    String ip="192.168.0.3"; int port=20000;
    JEditorPane pantalla;
    String msgS=null,msgE;
    String nombre;
    
   public void run()                            {
       getEjecutarCliente();
   } 
    public clsChatCliente1(JEditorPane JPantalla,String nombre,String ip) {
        pantalla=JPantalla;
        this.nombre = nombre;
        this.ip = ip;
    }  
    private void getEjecutarCliente()           {   
      try{
         do{
          getConectarse(); 
          getCanales();
          getRecibir();
         }while(true);
       }catch ( IOException ie) {
         JOptionPane.showMessageDialog(pantalla,"falla obtencion de Canales: "+ie.getMessage()+ie.toString()); }//
       finally { getDesconectar(); } 
    }
    private void getCanales() throws IOException{  
        salida = new ObjectOutputStream( Cliente.getOutputStream() );
        salida.flush();
        entrada = new ObjectInputStream( Cliente.getInputStream() );
        JOptionPane.showMessageDialog(pantalla,"FLUJOS CORRECTOS");
   }   
    private void getRecibir() throws IOException{       
        do{
          try {
             msgE = ( String ) entrada.readObject();
            }catch( ClassNotFoundException cnfe ) {
               JOptionPane.showMessageDialog(pantalla,"Problemas al RECIBIR datos(DESCONOCIDO)"+cnfe.getMessage());
            }
            System.out.println("Mensaje actual: "+pantalla.getText());
            pantalla.setText(pantalla.getText().replace("</body>",msgE+"<br></body>"));
          //pantalla.setText(msgE);
          //pantalla.setCaretPosition(pantalla.getText().length() );     
       }while( !msgE.equals( "SERVIDOR>>> CISC" ) );
   }    
    private void getConectarse()                 {
       try
       {  
          Cliente= new Socket(ip,20000);
          JOptionPane.showMessageDialog(pantalla,"Conectando...."); 
       }
       catch(IOException ie){JOptionPane.showMessageDialog(this,"Problemas de Conexion"); }
   }        
    public void getEnviar(String Msg)           {
         msgS=nombre+">>> "+Msg+"\n";
         try
         {                       
            salida.writeObject(msgS);
            salida.flush();                 
         }catch(IOException ie){JOptionPane.showMessageDialog(pantalla,"Problemas AL ENVIAR Datos(DESCONOCIDO)"+ie.getMessage()); }
            //pantalla.setText(pantalla.getText()+msgS);
            //pantalla.setCaretPosition(pantalla.getText().length() );
            pantalla.setText(pantalla.getText().replace("</body>",msgS+"<br></body>"));
    }   
     private void getDesconectar()              {
      try{
         salida.close();
         entrada.close();
         Cliente.close();
      }catch( IOException ie ) {
          JOptionPane.showMessageDialog(pantalla,"Error al salir "+ie.getMessage());
         
        }  
    }
}
