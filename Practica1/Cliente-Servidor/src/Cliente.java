/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dragdropmultiplesarchivos;
/**
 *
 * @author David
 */

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*; // Nesesario para usar ActionListener
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*; // Para los Socket
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.StringTokenizer;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(300,300,780,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel implements DropTargetListener {
	
        JTextArea textArea;
        DropTarget dt;
        JFileChooser jfc = new JFileChooser();
        

	public LaminaMarcoCliente(){
	
		JLabel texto=new JLabel("Arrastre y suelte un archivo");
		
		add(texto);
                
                textArea = new JTextArea(10, 60);
                JScrollPane scrollPane = new JScrollPane(textArea); 
                textArea.setEditable(false);
                add(textArea);
                
                // Asociamos el textarea como campo de accion del DropTarget
                dt = new DropTarget(textArea, this);
                setVisible(true);                
	
		miboton=new JButton("Seleccionar Archivo");
                botonenviar = new JButton("Enviar Archivo");
                
                SeleccionarArchivo mievento = new SeleccionarArchivo();
                miboton.addActionListener(mievento);
                
                EnviaDoc eventoenviar = new EnviaDoc();
                botonenviar.addActionListener(eventoenviar);
                
                
                add(miboton);	
		add(botonenviar);	
		
	}
        
        public void drop(DropTargetDropEvent dtde) {
            try {
                
                Transferable tranferido = dtde.getTransferable();
                // Tipo de dato a transferir
                DataFlavor[] flavors = tranferido.getTransferDataFlavors();
                
                // Desde cero hasta que ya no quede nada
                for (int i=0;i<flavors.length; i++) {
                    if(flavors[i].isFlavorJavaFileListType()) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        java.util.List list = (java.util.List) tranferido.getTransferData(flavors[i]);
                        for (int j = 0; j < list.size(); j++) {
                            textArea.append(list.get(j) + "\n");
                        }
                        dtde.dropComplete(true);
                        return;
                    }
                    else if(flavors[i].isFlavorSerializedObjectType()) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        Object o = tranferido.getTransferData(flavors[i]);
                        textArea.append("Object: " + o);
                        dtde.dropComplete(true);
                        return;
                    }
                    else if (flavors[i].isRepresentationClassInputStream()) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        textArea.setText("");
                        textArea.read(new InputStreamReader((InputStream) tranferido.getTransferData(flavors[i])),"");
                        dtde.dropComplete(true);
                    return;
                  }
                }
                
                
                dtde.rejectDrop();
                
            }catch (Exception e) {
                e.printStackTrace();
                dtde.rejectDrop();
            }
        }
  
  // Metodos de Drag      
  public void dragEnter(DropTargetDragEvent dtde) {}
  public void dragExit(DropTargetEvent dte) {}
  public void dragOver(DropTargetDragEvent dtde) {}
  public void dropActionChanged(DropTargetDragEvent dtde) {}
        
        
	private class SeleccionarArchivo implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
                jfc.setMultiSelectionEnabled(true);
                int r = jfc.showOpenDialog(null);
                File f[]=jfc.getSelectedFiles();
                for(int i = 0; i <= (f.length)-1; i++){
                    textArea.append((f[i].getAbsolutePath())+"\n");
                }
                
           
            }
        }
        
                     
	private class EnviaDoc implements ActionListener{
        @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //System.out.println(campo1.getText());
                    int pto = 9001;
                    String host = "localhost";
                    System.out.println("Conexion Establecida");
                    
                        String ruta = textArea.getText().trim();
                        StringTokenizer st = new StringTokenizer(ruta,"\n");
                        int n = st.countTokens();
                        
                        for(int i=0; i<n;i++){
                            Socket cl = new Socket(host,pto);

                            File f2 = new File(st.nextToken());
                            
                            String nombre = f2.getName();
                            long tam = f2.length();
                            String path = f2.getAbsolutePath();
                            
                            // Escribir
                            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                            // Leer
                            DataInputStream dis = new DataInputStream(new FileInputStream(path));
                            
                            dos.writeUTF(nombre);
                            dos.flush();
                            dos.writeLong(tam);
                            dos.flush();
                            
                            byte[] buf = new byte [1500];
                            long enviados = 0;
                            int num=0, leidos=0, porcentaje=0;
                            
                            while(enviados < tam){
                            num = dis.read(buf);
                            dos.write(buf,0,num);
                            enviados = enviados + num;
                            porcentaje = (int)((enviados*100)/tam);
                            System.out.println("\rTransmitido el:"+porcentaje+"%");
                            }
                            
                            System.out.println("\n Archivo "+nombre+" enviado");
                            dos.close();
                            dis.close();
                            cl.close();
                        }
                    
                } catch (IOException ex) {
                    Logger.getLogger(LaminaMarcoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }	
		
		
	private JTextField campo1;
	private JButton miboton;
	private JButton botonenviar;

}