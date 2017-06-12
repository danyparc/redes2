import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class Envia{
	public static void main (String[]args){
		try{
			String host = "127.0.0.1";
                        int pto = 8000;
			int e = 0;
                        int Porcentaje = e;
			Socket cl= new Socket(host,pto);
			JFileChooser fc=new JFileChooser();
			fc.setMultiSelectionEnabled(true);
			int r=fc.showOpenDialog(null);
			if(r==JFileChooser.APPROVE_OPTION){
				File f=fc.getSelectedFile();
				String nombre=f.getName();
				String path= f.getAbsolutePath();
				long tam=f.length();
				
				DataOutputStream dos=new DataOutputStream(cl.getOutputStream());
				dos.writeUTF(nombre);
				dos.flush();
				dos.writeLong(tam);
				dos.flush();
				DataInputStream dis=new DataInputStream(new FileInputStream(path));
				long enviados=0;
				while(enviados<tam){
					byte[] b=new byte[2000];
					int n=dis.read(b);
					enviados=enviados + n;
					dos.write(b,0,n);
					dos.flush();
					Porcentaje=(int)((enviados*100)/tam);
					System.out.print("\r Enviado el " + Porcentaje + "%");
				}
				System.out.println("\n Archivo enviado...");
				dis.close();
				dos.close();
				cl.close();
			}
		} catch(Exception e){
			e.printStackTrace();
	}
    }
}