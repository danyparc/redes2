package servidorsopa;

import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClienteSopa {

    public static final int SERVICE_PORT = 4000; // UDP port to which service is bound
    public static final int BUFSIZE = 937; // Max size of packet
    private static MulticastSocket s;
    private static InetAddress group;
    private static final String address = "230.1.1.1";
    private static ArrayList<String> usersConnected;

    public static void main(String args[]) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        InetAddress addr = null; // Get an InetAddress for the specified hostname
        /*if (args.length != 1){
         System.err.println ("Syntax - java EchoClient hostname");
         return;
         }*/
        Scanner screader = new Scanner(System.in);  // Reading from System.in
        //System.out.println("Ingresa la dirección del servidor: ");
        //String hostname = screader.next();

        //Try to connect with the server
        try {
            addr = InetAddress.getByName(address); // Resolve the hostname to an InetAddr
        } catch (UnknownHostException uhe) {
            System.err.println("Unable to resolve host");
            return;
        }

        try {

            //Try to connect to broadcast
            /*
            group = InetAddress.getByName(address);
            s = new MulticastSocket(SERVICE_PORT);
            s.joinGroup(group);*/
            // Bind to any free port
            MulticastSocket socket = new MulticastSocket();
            socket.joinGroup(addr);
            // Set a timeout value of two seconds
            socket.setSoTimeout(2 * 1000);
            System.out.println("Ingresa tu nombre de jugador: ");
            String playerName = screader.next();
            String message = "JOIN ?P:" + playerName + " \n";

            for (;;) {
                // Copy some data to our packet

                char[] cArray = message.toCharArray();
                byte[] sendbuf = new byte[cArray.length];
                for (int offset = 0; offset < cArray.length; offset++) {
                    sendbuf[offset] = (byte) cArray[offset];
                }
                // Create a packet to send to the UDP server
                DatagramPacket sendPacket = new DatagramPacket(sendbuf, cArray.length, addr, SERVICE_PORT);
                
                System.out.println("Sending packet to " + addr);
                // Send the packet
                socket.send(sendPacket);
                System.out.print("Waiting for packet.... ");
                // Create a small packet for receiving UDP packets
                byte[] recbuf = new byte[BUFSIZE];
                DatagramPacket receivePacket = new DatagramPacket(recbuf, BUFSIZE);
                // Declare a timeout flag
                boolean timeout = false;
                // Catch any InterruptedIOException that is thrown
                // while waiting to receive a UDP packet
                try {
                    
                    socket.receive(receivePacket);
                } catch (InterruptedIOException ioe) {
                    timeout = true;
                }
                if (!timeout) {
                    System.out.println("packet received!");
                    System.out.println("Details : " + receivePacket.getAddress());
                    // Obtain a byte input stream to read the
                    // UDP packet
                    ByteArrayInputStream bin = new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength());
                    // Connect a reader for easier access
                    BufferedReader reader = new BufferedReader(new InputStreamReader(bin));
                    // Loop indefinitely
                    for (;;) {
                        String line = reader.readLine();
                        String word = "";
                        // Check for end of data
                        if (line == null) {
                            break;
                        } else if (line.startsWith("Puzzle")) {
                            word = "";
                            System.out.println("\tSOPA DE LETRAS: ");
                            System.out.println(line);
                            String l = "";
                            while (l != null) {
                                l = reader.readLine();
                                System.out.println(l);
                            }
                            System.out.println("\n Escribe SALIR para terminar");
                            System.out.println("\n Encontraste algo? \n Escribe la palabra: ");
                            String inputopt = screader.next();
                            if (inputopt == "SALIR") {
                                message = "LEAVE ?P:" + playerName;
                                break;
                            }
                            word = "WORD ?W:" + inputopt + "?P:" + playerName;
                            System.out.println("\n En que coordenadas? x0-y0,x1-y1,x2-y2... ");
                            word = word + "?C:" + screader.next() + " \n";
                            System.out.println("ESTO ES LO QUE ENVIA DE MIS COORDENADAS " + word);
                            message = word;
                        } else if (line.startsWith("Correcto")) {
                            word = "";                            
                            System.out.println("\nACERTASTE.\n ");
                            System.out.println("\n Escribe SALIR para terminar");
                            System.out.println("\n Encontraste algo? \n Escribe la palabra: ");
                            String inputopt = screader.next();
                            if ("SALIR".equals(inputopt)) {
                                message = "LEAVE ?P:" + playerName;
                                break;
                            }
                            word = "WORD ?W:" + inputopt + "?P:" + playerName;
                            System.out.println("\n En que coordenadas? x0-y0,x1-y1,x2-y2... ");
                            word = word + "?C:" + screader.next() + " \n";
                            System.out.println("ESTO ES LO QUE ENVIA DE MIS COORDENADAS " + word);
                            message = word;
                            

                        }else if(line.startsWith("GAMEOVER")){
                            System.out.println("\n**** GAME OVER ****\n");
                            String l = "";
                            while (l != null) {
                                l = reader.readLine();
                                System.out.println(l);
                            }
                            System.out.println("\n**** GAME OVER ****\n");

                            return;
                        } else {
                            System.out.println(line);
                            System.out.println("no implementado");
                        }

                    }
                } else {
                    System.out.println("packet lost!");
                }
                // Sleep for a second, to allow user to see packet
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        } catch (IOException ioe) {
            System.err.println("Socket error " + ioe);
        }
    }
}
