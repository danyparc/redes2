import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;


class Manejador extends Thread {
  Semaphore sem;
  Map mime_types;
  protected Socket socket;
  protected PrintWriter pw;
  protected BufferedOutputStream bos;
  protected BufferedReader br;
  protected String FileName;
  public Manejador(Socket _socket) throws Exception {
    this.socket=_socket;
    System.out.println("Intentando obtener acceso a seccion critica...");
    sem.acquire();
    System.out.println("Lo ha logrado. Quedan "+sem.availablePermits()+ " permisos");
  }

public void run() {
    try{
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bos=new BufferedOutputStream(socket.getOutputStream());
        pw=new PrintWriter(new OutputStreamWriter(bos));
        String line=br.readLine();
        //System.out.println(line);
        if(line==null){
                    pw.print("<html><head><title>Servidor WEB");
                    pw.print("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
                    pw.print("</body></html>");
                    socket.close();
                    return;
         }
        System.out.println("\nCliente Conectado desde: "+socket.getInetAddress());
        System.out.println("Por el puerto: "+socket.getPort());
        System.out.println("Datos: "+line+"\r\n\r\n");
        if(line.toUpperCase().startsWith("GET")){
            System.out.println("Peticion get");
            if(line.indexOf("?")>0){
                String data=line.substring(line.indexOf("?")+1);
                data = data.substring(0, data.indexOf(" "));
                sendHTMLResponse(data);
            }
            else{
                getArch(line, "GET");
            }
        }else if(line.toUpperCase().startsWith("POST")){
                    System.out.println("Peticion POST");
                    int bodyLength = 0;
                    do{
                        line=br.readLine();
                        System.out.println(line);
                        if(line.startsWith("Content-Length:"))
                            bodyLength = Integer.parseInt(line.substring(16));
                    }
                    while(line.length()!=0);
                    line = "";
                    if(bodyLength>0){
                        char body[] = new char[bodyLength];
                        br.read(body, 0, bodyLength);
                        line = new String(body);
                    }
                    System.out.println(line);
                    sendHTMLResponse(line);
        }
                 else if(line.toUpperCase().startsWith("HEAD")){
                    System.out.println("Peticion head");
                    getArch(line, "HEAD");
                }
                 else{
                    System.out.println("Operacion no implementada");
                    String sb = "";
                    sb = sb+"HTTP/1.0 501 Not Implemented\n";
                    sb = sb +"Server: JM Server/1.0 \n";
                    sb = sb +"Date: " + new Date()+" \n";
                    sb = sb +"Content-Length: 0 \n";
                    sb = sb +"\n";
                    pw.println(sb);
                 }
        pw.flush();
        bos.flush();
                socket.close();
    }
  catch(Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Liberando el semaforo");
            sem.release();
            System.out.println("Quedan "+sem.availablePermits() + " permisos" );
        }
}

public void getArch(String line, String method){
        int i = line.indexOf("/");
        int f =line.indexOf(" ",i);
        FileName=line.substring(i+1,f);
        System.out.println("Buscando "+FileName);
        if(!(new File(FileName).exists())){
            String body = "<!DOCTYPE html>";
            body+= "<head><title>Error 404</title></head>";
            body+= "<body>";
            body+= " <h1>Error 404</h1>";
            body+= "<h4>"+FileName+" no existe</h4>";
            body+= "</body>";
            String sb = "";
            sb = sb+"HTTP/1.0 404  Not found\n";
            sb = sb +"Server: JM Server/1.0 \n";
            sb = sb +"Date: " + new Date()+" \n";
            sb = sb +"Content-Length: "+body.length()+" \n";
            sb = sb +"Content-Type: text/html \n";
            sb = sb +"\n";
            if(method=="GET")
                pw.println(sb+body);
            else
                pw.println(sb);
            pw.flush();
        }
        else{
            SendA(FileName, method);
        }
    }
public void SendA(String arg, String method) {
        try{
            String extension = arg.split("\\.(?=[^\\.]+$)")[1];
            long tam_archivo = new File(arg).length();
            String sb = "";
sb = sb+"HTTP/1.0 200 ok\n";
sb = sb +"Server: JMServer/1.0 \n";
sb = sb +"Date: " + new Date()+" \n";
sb = sb +"Content-Type: "+mime_types.get(extension)+" \n";
sb = sb +"Content-Length: "+tam_archivo+" \n";
sb = sb +"\n";
bos.write(sb.getBytes());
bos.flush();
            if(method.equals("GET")){
                int b_leidos=0;
                BufferedInputStream bis2=new BufferedInputStream(new FileInputStream(arg));
                byte[] buf=new byte[1024];
                int tam_bloque=0;
                if(bis2.available()>=1024){
                    tam_bloque=1024;
                }
                else{
                    bis2.available();
                }
                while((b_leidos=bis2.read(buf,0,buf.length))!=-1){
                    bos.write(buf,0,b_leidos);
                   }
                bos.flush();
                bis2.close();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
        public void sendHTMLResponse(String args){
            System.out.println(args);
            String sb = "";
            sb = sb+"HTTP/1.0 200 ok\n";
            sb = sb +"Server: JM Server/1.0 \n";
            sb = sb +"Date: " + new Date()+" \n";
            sb = sb +"Content-Type: text/html \n";
            sb = sb +"\n";
            sb = sb + "<!DOCTYPE html>";
            sb = sb +"<html><head><title>SERVIDOR WEB</title></head>";
            if(args.length()>0){
                String[] fields = args.split("&");
                sb = sb + "</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>";
                for(int i=0; i< fields.length; i++){
                    System.out.println(fields[i]);
                    String[] keyValue = fields[i].split("=");
                    if(keyValue.length>1){
                        keyValue[1] = keyValue[1].replace('+',' ');
                        sb+="<h3>"+keyValue[0]+":"+keyValue[1]+"</h3>";
                    }
                    else
                        sb+="<h3>"+keyValue[0]+":</h3>";
                }
                sb += "</center></body></html>";
            }
            else{
                sb = sb + "<body bgcolor=\"#AACCFF\"><center><h1><br>No mandaste nada .__.</br></h1></center></body>";
            }
            pw.println(sb);
            pw.flush();
        }
}
