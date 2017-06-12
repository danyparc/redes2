/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import Server.*;

/**
 *
 * @author Callmetorre
 */
public class ServerMain {
     public static void main(String[] args) throws java.io.IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        new Server().run();
    }
}
