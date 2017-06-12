/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoredes2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.Format;
import javax.imageio.*;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.cdm.CaptureDeviceManager;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.util.BufferToImage;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JPanel;

/**
 *
 * @author Cmop
 */
public class Dispositivos {

    private ChatRoom padre;
    private Player player;

    public Dispositivos(ChatRoom padre)
    {
        this.padre=padre;
    }

    public String verInfoDispositivos()
    {
      String rpta="";
      Vector listaDispositivos = null;
      //Buscamos los dispositivos instalados
      listaDispositivos = CaptureDeviceManager.getDeviceList();
      Iterator it = listaDispositivos.iterator();
      while (it.hasNext())
      {
        CaptureDeviceInfo cdi = (CaptureDeviceInfo)it.next();
        rpta+=cdi.getName()+"\n";
        //cdi.getName() --> Obtiene el nombre del Dispositivo Detectado
      }
      if(rpta.compareTo("")!=0)
          rpta="Dispositivos detectados:\n\n"+rpta;
      else
          rpta="Sin Dispositivos Detectados";
      
      return rpta;
    }

    public void detectarDispositivos(JMenu dispositivos)
    {
      Vector listaDispositivos = null;
      //Buscamos los dispositivos instalados
      listaDispositivos = CaptureDeviceManager.getDeviceList();
      Iterator it = listaDispositivos.iterator();

      String nombre="";
      while (it.hasNext())
      {
          CaptureDeviceInfo cdi = (CaptureDeviceInfo)it.next();
          nombre=cdi.getName(); //cdi.getName() --> Obtiene el nombre del Dispositivo Detectado
          
          if(nombre.indexOf("Image")!=-1)
          {
              JMenu menuFormato=new JMenu(nombre);
              JMenuFormato tamanios=null;
              CaptureDeviceInfo dev = CaptureDeviceManager.getDevice(nombre);
              Format[] cfmts = dev.getFormats();

              for(int i=0; i<cfmts.length;i++)
              {
                  if(cfmts[i].getEncoding().compareTo("yuv")==0)
                  {tamanios=new JMenuFormato(cfmts[i].getEncoding()+" "+
                          ((YUVFormat)cfmts[i]).getSize().width+"x"+
                          ((YUVFormat)cfmts[i]).getSize().height,
                          ((YUVFormat)cfmts[i]).getSize().width,
                          ((YUVFormat)cfmts[i]).getSize().height,
                          padre,
                          padre.jPWebCam1);
                  }
                  else if(cfmts[i].getEncoding().compareTo("rgb")==0)
                  {tamanios=new JMenuFormato(cfmts[i].getEncoding()+" "+
                          ((RGBFormat)cfmts[i]).getSize().width+"x"+
                          ((RGBFormat)cfmts[i]).getSize().height,
                          ((RGBFormat)cfmts[i]).getSize().width,
                          ((RGBFormat)cfmts[i]).getSize().height,
                          padre,
                          padre.jPWebCam1);
                  }
                  menuFormato.add(tamanios);
              }
              dispositivos.add(menuFormato);
          }
      }
    }

    public void MuestraWebCam(JPanel panelCam,String dispositivo)
    {
        if(player != null)
            return;
        
        CaptureDeviceInfo dev = CaptureDeviceManager.getDevice(dispositivo);
        //obtengo el locator del dispositivo
        MediaLocator loc = dev.getLocator();
            try {
                player = Manager.createRealizedPlayer(loc);
            } catch (IOException ex) {
                Logger.getLogger(ProyectoRedes2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoPlayerException ex) {
                Logger.getLogger(ProyectoRedes2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CannotRealizeException ex) {
                Logger.getLogger(ProyectoRedes2.class.getName()).log(Level.SEVERE, null, ex);
            }
        player.start();

    /*    try {
            // esto lo saqué del foro jmf de Sun, hay que "parar un poco la aplicacion"
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProyectoRedes2.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        Component comp;

        if ((comp = player.getVisualComponent())!= null)
        {
          // mostramos visualmente el reproductor          
          panelCam.add(comp,BorderLayout.CENTER);
          panelCam.updateUI();
        //  padre.pack();
          
        }
    }

    public void CapturaFoto()
    {
        Image img=null;
        FrameGrabbingControl fgc = (FrameGrabbingControl)
        player.getControl("javax.media.control.FrameGrabbingControl");
        Buffer buf = fgc.grabFrame();
        // creamos la imagen awt
        BufferToImage btoi = new BufferToImage((VideoFormat)buf.getFormat());
        img = btoi.createImage(buf);

        if (img != null)
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            int result = chooser.showSaveDialog(null);
            Integer i = new Integer(JFileChooser.APPROVE_OPTION);
            if (i != null){
                File archivo = chooser.getSelectedFile();
                String imagen = archivo.getAbsolutePath();
                System.out.println("imagen: "+imagen);
                if (imagen.lastIndexOf(".") > 0){
                    imagen = imagen.substring(0,imagen.lastIndexOf("."));
                }
                imagen = imagen+".JPG";
                System.out.println("imagen:"+imagen);
                File imagenArch = new File(imagen);
                String formato = "JPEG";
                 try{
                   ImageIO.write((RenderedImage) img,formato,imagenArch);
                }catch (IOException ioe){System.out.println("Error al guardar la imagen");}
            }
        }
        else
        {
            javax.swing.JOptionPane.showMessageDialog(padre, "A ocurrido un error!!");
        }
        img=null;
     }
}