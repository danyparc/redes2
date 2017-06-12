import java.util.Scanner;
import java.net.*;
import java.io.*;

/**
 * Created by danyparc on 1/5/17.
 */
public class ssClient {
public static void main(String[] args) {

  try{
            InetAddress dir = InetAddress.getByName("127.0.0.1");
            SocketChannel cl = SocketChannel.open( );
            cl.configureBlocking(false);
            InetSocketAddress dst = new InetSocketAddress(dir,1234);
            Selector sel = Selector.open();
            cl.connect(dst);
            cl.register(sel,SelectionKey.OP_CONNECT);
            while(true){
                sel.select();

            }
            Iterator<SelectionKey>it = sel.selectedKeys( ).iterator( );
            while(it.hasNext( )){
                SelectionKey k = (SelectionKey)it.next( );
                it.remove( );
                if(k.isConnectable( )){
                    SocketChannel ch = (SocketChannel)k.channel( );
                    if(ch.isConnectionPending()){
                        try{
                            ch.finishConnect();
                            System.out.println("Conexión establecida");
                        }catch(Exception e){ . . .}
                    }//if
                    ch.register(sel,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                    continue;
                }//if
                if(k.isReadable()){
                    SocketChannel ch2 = (SocketChannel)k.channel( );
                    ByteBuffer b= ByteBuffer.allocate(100);
                    b.clear();
                    ch2.read(b);
                    continue;

                }else if(k.isWritable()){
                    SocketChannel ch2 = (SocketChannel)k.channel( );
                    String msj="un mensaje";
                    byte[ ] b = msj.getBytes();
                    ByteBuffer buf = ByteBuffer.wrap(b);
                    ch2.write(buf);
                    continue;
                }//if
            }//while
        }//for
    }catch(Exception e){
      System.out.println(e);
    }
}
}
