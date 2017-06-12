/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Processor;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Luis Aguilar
 */
public class VideoLlamadaCliente extends JOptionPane implements Runnable{
    private static Player player = null;
    private MediaLocator localizador = null;
    private Processor p;
    private CaptureDeviceInfo dispositivo = null;
    private static String source = "vfw:Microsoft WDM Image Capture (Win32):0";
    private Timer timer;
    private Buffer buffer;
    private BufferToImage buffer_image = null;    
 
    private String servidorChat="192.168.0.3";
    private ObjectOutputStream salida; 
    private Socket cliente;
    private ObjectInputStream entrada;
   // private Socket conexion;
    private mesadeservicios.Lienzo lienzo1;
    private JPanel panelVideo1;

    
    public VideoLlamadaCliente(mesadeservicios.Lienzo Lienzo1,JPanel PanelVideo1,String servidorChat){
         this.lienzo1=Lienzo1;
         this.panelVideo1=PanelVideo1;
         this.servidorChat = servidorChat;
    }
    
    
    public void run(){
        cargar2();
        ejecutarCliente();
    }

      
    public void cargar2(){
        dispositivo = CaptureDeviceManager.getDevice(source);
        localizador = dispositivo.getLocator();
        timer = new Timer (1, new ActionListener () {   //Cada 1 milisegundo capturará el frame de video
            public void actionPerformed(ActionEvent e) {
                FrameGrabbingControl fgc = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
                buffer = fgc.grabFrame();
                // Convert it to an image
                buffer_image = new BufferToImage((VideoFormat)buffer.getFormat());
                BufferedImage bufferedImage = (BufferedImage)buffer_image.createImage(buffer);
                ByteArrayOutputStream salidaImagen = new ByteArrayOutputStream();
                try {
                    ImageIO.write(bufferedImage, "jpg", salidaImagen);
                    byte[] bytesImagen = salidaImagen.toByteArray();
                    salida.writeObject( bytesImagen );                   
                    salida.flush();
                //                panelCaptura.setImage(img);
                } catch ( Exception excepcionEOF ) {
                    System.err.println( "El cliente termino la conexión" );
                }
            }
        });
    }
    
     private void iniciarCaptura() {
        try {
            player = Manager.createRealizedPlayer(localizador);
            player.start();
            if (player.getVisualComponent() != null) {
                panelVideo1.add(player.getVisualComponent(), BorderLayout.CENTER);
                panelVideo1.updateUI();
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
     
     
     public void getCerrarCam()    {
      if(player != null){
            player.close();
            player.deallocate();
            timer.stop();
            panelVideo1.removeAll();
            player=null;
      }
    }
    
     public void getActivarMiCam() {
        if(player!=null)return;
        iniciarCaptura();
        
          try {
              salida = new ObjectOutputStream( cliente.getOutputStream() );
              salida.flush();
              acceso2Frames();
          } catch (IOException ex) {
              JOptionPane.showMessageDialog(this, "ERROR EN EL ENVIO de imagen"+ex.getMessage());
          }
    }
     
     public void acceso2Frames() {
        timer.start();
    }
     
     private void ejecutarCliente() {
        try {  
            conectarAServidor(); // Paso 1: crear un socket para realizar la conexión
            entrada = new ObjectInputStream( cliente.getInputStream() );///////////////CONEXION
            JOptionPane.showMessageDialog(panelVideo1,"\nSe recibieron los flujos de entrada(VIDEO)\n");
            procesarConexion();
        } catch ( EOFException excepcionEOF ) {
            System.err.println( "El cliente termino la conexión" );
        } catch ( IOException excepcionES ) {
            excepcionES.printStackTrace();
        }
    } 
     
    private void procesarConexion() throws IOException {
        do { 
            
            try {
                
                byte[] bytesImagen = (byte[]) entrada.readObject();
                ByteArrayInputStream entradaImagen = new ByteArrayInputStream(bytesImagen);
                BufferedImage bufferedImage = ImageIO.read(entradaImagen);
                lienzo1.setImage(bufferedImage);
              
            }
            catch ( ClassNotFoundException excepcionClaseNoEncontrada ) {
                System.out.println( "\nSe recibió un tipo de objeto desconocido" );
            }
        } while ( true );
    }
     
     private void conectarAServidor() throws IOException {
        cliente = new Socket( InetAddress.getByName( servidorChat ), 12345 );
    }
}
