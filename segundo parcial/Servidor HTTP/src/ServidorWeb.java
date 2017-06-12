import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ServidorWeb
{
    public static final int PUERTO=8000;
    ServerSocket ss;
    Map mime_types;
    Semaphore sem;

	public ServidorWeb() throws Exception {
    System.out.println("Leyendo mime types...");
    mime_types = new HashMap();
    BufferedReader bf = new BufferedReader(new FileReader("mime_types.txt"));
    String line = "";
    while((line=bf.readLine())!=null){
      String[] splittedLine = line.split("\t");
      System.out.println(splittedLine[0]+":"+splittedLine[1]);
      mime_types.put(splittedLine[0], splittedLine[1]);
    }
    System.out.println("Iniciando Servidor.......");
    this.ss=new ServerSocket(PUERTO);
    System.out.println("Servidor iniciado:---OK");
    System.out.println("Esperando por Cliente....");
    sem = new Semaphore(7, true);
    System.out.println("Hay "+sem.availablePermits()+" permisos.");
    for(;;){
      Socket accept=ss.accept();
		      new Manejador(accept).start();
        }
	}
	public static void main(String[] args) throws Exception{
            ServidorWeb sWEB=new ServidorWeb();
	}
}
