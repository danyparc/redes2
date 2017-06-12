/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

/**
 *
 * @author Callmetorre
 */
public class ServerThread extends Thread{
    
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    
    public ServerThread() throws IOException {
	this("QuoteServerThread");
    }
    
    public ServerThread(String name) throws SocketException {
        socket = new DatagramSocket(4445);
    }
    
    public void run() {

        while (moreQuotes) {
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // figure out response
                String dString = "Perro";
                buf = dString.getBytes();

		// send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
		moreQuotes = false;
            }
        }
        socket.close();
    }
    
}
