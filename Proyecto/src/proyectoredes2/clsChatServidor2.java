package proyectoredes2;

import java.io.*;
import java.net.*;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class clsChatServidor2 extends JOptionPane  implements Runnable{
    
    ServerSocket Servidor=null;
    Socket Cliente=null;
    ObjectInputStream entrada=null;
    ObjectOutputStream salida=null;
    JEditorPane pantalla;
    int port=20000;
    String var=null,msgS,msgE; 
       
    public void run()                {
         this.getEjecutarTecnico();
      }    
    private void getEjecutarTecnico(){
        getConectar();
        do{
               getConectarlo();
               getCanales();
               getRecibir();
               getDesconectar();  
        }while(true);  
    //
    }
    private void getConectarlo()     {   try 
        {
             Cliente= Servidor.accept();
             //pantalla.setCaretPosition(pantalla.getText().length() );
        }catch (IOException ex) {
             JOptionPane.showMessageDialog(pantalla,"No se puede conectar con el Cliente: "+ex.getMessage());                    
        }
        JOptionPane.showMessageDialog(pantalla,"Mi Cliente Conectando @@@...");     
    }   
    public void getCanales()         {
        try{
            salida= new ObjectOutputStream(Cliente.getOutputStream());
            salida.flush();////////////////
            entrada= new ObjectInputStream(Cliente.getInputStream());
         } catch(IOException ex){JOptionPane.showMessageDialog(pantalla,"FALLA al obtener ls FLUJOS:  "+ex.getMessage());}
        JOptionPane.showMessageDialog(pantalla,"FLUJOS CORRECTOS");                         
      }   
    public void getRecibir()         {
       do{        
          try {
              msgE= ( String ) entrada.readObject();
           }catch ( ClassNotFoundException cnfe ) {
                JOptionPane.showMessageDialog(pantalla,"Problemas al RECIBIR datos(DESCONOCIDO)"+cnfe.getMessage()); }
            catch(IOException ex){JOptionPane.showMessageDialog(pantalla,"ERROR PROCESAR CONEXION"+ex.getMessage());}
         //pantalla.append(msgE);
         //pantalla.setCaretPosition(pantalla.getText().length() );
            pantalla.setText(pantalla.getText().replace("</body>",msgE+"<br></body>"));
      }while ( !msgE.equals( "CLIENTE>>> CISC" ) );
    }
String nombre;
    public void getEnviar(String Msg)          {
         msgS=nombre+">>> "+Msg+"\n";
         try
         {                      
            salida.writeObject(msgS);                 
         }catch(IOException ie){JOptionPane.showMessageDialog(pantalla,"Problemas AL ENVIAR Datos(DESCONOCIDO)"+ie.getMessage()); } 
         //pantalla.setText(pantalla.getText()+msgS);
         //pantalla.setCaretPosition(pantalla.getText().length() );
            pantalla.setText(pantalla.getText().replace("</body>",msgS+"<br></body>"));
    }        
    public clsChatServidor2(JEditorPane Pantalla,String nombre){
        this.pantalla=Pantalla;
        this.nombre = nombre;
    }  
    public void getConectar()                  {
       try
       {
          Servidor= new ServerSocket(port);
          JOptionPane.showMessageDialog(pantalla,"Conectandome  @@@.... ");
          //pantalla.append("\t\t\tEsperando una Conexion\n");
       }
       catch(IOException ie){JOptionPane.showMessageDialog(this,"Problemas de Conexion"); }
   }    
    public void getDesconectar()               {
        try{
            salida.close();
            entrada.close();
            Cliente.close();
        }catch(IOException ie){JOptionPane.showMessageDialog(this,ie.getMessage());
        }
    
    }       
}
 

