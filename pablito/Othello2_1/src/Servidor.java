import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Servidor {
    OthelloBoard b;
    
    public void showWinner(OthelloBoard board) {
		//inputEnabled = false;
		//active = false;
		if (board.counter[0] > board.counter[1])
		JOptionPane.showMessageDialog(null, "You win!","Othello",JOptionPane.INFORMATION_MESSAGE);
		else if (board.counter[0] < board.counter[1])
		   JOptionPane.showMessageDialog(null, "I win!","Othello",JOptionPane.INFORMATION_MESSAGE);
		   else JOptionPane.showMessageDialog(null, "Drawn!","Othello",JOptionPane.INFORMATION_MESSAGE);
	}
    public void computerMove(final OthelloBoard board) {
		if (board.gameEnd()) {
			showWinner(board);
			return;
		}
		Move move = new Move();
		if (board.findMove(TKind.white,3,move)) {
			board.move(move,TKind.white);
			//score_black.setText(Integer.toString(board.getCounter(TKind.black)));
			//score_white.setText(Integer.toString(board.getCounter(TKind.white)));
			//repaint();
			if (board.gameEnd()) showWinner(board);
			else if (!board.userCanMove(TKind.black)) {
				   JOptionPane.showMessageDialog(null, "You pass...","Othello",JOptionPane.INFORMATION_MESSAGE);
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							computerMove(board);
						}
					});
				}
		}
		else if (board.userCanMove(TKind.black))
		   JOptionPane.showMessageDialog(null, "I pass...","Othello",JOptionPane.INFORMATION_MESSAGE);
		   else showWinner(board);
	}
    public static void main(String[] args){
        try {
            String host = "127.0.0.1";
            int pto = 9000;
            boolean valido = false;
            
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(host, pto));
            server.register(selector, SelectionKey.OP_ACCEPT);
            
            System.out.println("Servidor iniciado...");
            
            while(true){
                selector.select();
                Iterator <SelectionKey> iterator = selector.selectedKeys().iterator();
                
                while(iterator.hasNext()){
                    SelectionKey key = (SelectionKey)iterator.next();
                    iterator.remove();
                    
                    if(key.isAcceptable()){
                        
                        SocketChannel client = server.accept();
                        System.out.println("Cliente conectado desde: "+client.socket().getInetAddress()+": "+client.socket().getPort());
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        continue;
                    }
                    if(key.isReadable()){
                        System.out.println("Listo para leer");
                        SocketChannel sc = (SocketChannel)key.channel();
                        
                        ByteBuffer bO = ByteBuffer.allocate(2000);
                        bO.clear();
                        sc.read(bO);
                        bO.flip();
                        int i = bO.getInt();
                        int j = bO.getInt();
                        System.out.println(i+", "+j);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bO.array(), bO.position(), bO.limit()));
                        OthelloBoard board = (OthelloBoard)ois.readObject();
                        System.out.println("Recibido");                        
                        if ((i < 8) && (j < 8) && (board.get(i,j) == TKind.nil) && (board.move(new Move(i,j),TKind.black) != 0)){
                            System.out.println("Verdadero");
                            valido = true;
                        }
                        else{
                            System.out.println("Falso");
                            valido = false;
                        }
                        
                        if(valido){
                            ByteBuffer bS = ByteBuffer.wrap("Verdadero".getBytes());
                            ByteBuffer bC = ByteBuffer.allocate(200);
                            sc.write(bS);
                        }
                        else{
                            ByteBuffer bS = ByteBuffer.wrap("Falso".getBytes());
                            sc.write(bS);
                        }
                    }
                    
//                    else if(key.isWritable()){
//                        System.out.println("Writable");
//                        SocketChannel sc = (SocketChannel)key.channel();
//                        if(valido){
//                            ByteBuffer bS = ByteBuffer.wrap("validoadero".getBytes());
//                            sc.write(bS);
//                        }
//                        else{
//                            ByteBuffer bS = ByteBuffer.wrap("falso".getBytes());
//                            sc.write(bS);
//                        }
//                    }
                }//while
            }//while
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
