/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manejador.de.datos;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 *
 * @author Juan Pablo
 */
public class servidor {
    
     // -FALTA hacer el update Wups
    //-Falta validar tipos de datos

    //Estructura general de la base de datos
    public static Map<String, HashMap<String, LinkedList>> baseGeneral = new HashMap<String, HashMap<String, LinkedList>>();
    public static HashMap mibasesita;
    //Puerto para poder hacer la conexion con el cliente
    final static int SERVER_PORT = 5000;

    //[Main] Inicio del programa
    public static void main(String args[]) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        //Primero deserializamos la base de datos

        try{
          System.out.println("Cargando Archivo...");
          baseGeneral = deserializar();
          System.out.println("Base de datos inicializada correctamente");

        }catch (Exception e) {
          e.printStackTrace();
        }

        System.out.println("<------------------------------------------>");
        //String usar = "USE perritos";
        //operaciones(usar);

        //String crearTablaQuery = "CREATE TABLE Personas (String nombre, String propietario, String edad )";
       // String insertarQuery = "INSERT INTO Personas VALUES(Firulais, Armando, 25 )";
       //  String insertarQuery2 = "INSERT INTO Personas VALUES(PelusaTot, Gober, 21 )";
        // String borra = "DELETE FROM Personas WHERE(nombre = Armando )";*/
      //  String update = "UPDATE Personas SET Nombre = NuevoNombre WHERE Nombre = Gober";
      //  String obtenerTodosQuery = "SELECT * FROM Personas";

      //  operaciones(crearTablaQuery);
      //  operaciones(insertarQuery);
     //   operaciones(insertarQuery2);
    //    operaciones(update);
   //     operaciones(obtenerTodosQuery);
        /*
         String obtenerTodosQuery = "SELECT * FROM Personas";
         String obtenerDato = "SELECT * FROM Presonas WHERE(nombre = Algo )";
         operaciones(crearTablaQuery);
         operaciones(insertarQuery);
         operaciones(insertarQuery2);
         operaciones(obtenerTodosQuery);*/

        //Conexion con el cliente/*
        try {
            ServerSocket s = new ServerSocket(SERVER_PORT);
            for (;;) {
                Socket cl = s.accept();
                System.out.println("\nCliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
                DataInputStream clientinp = new DataInputStream(cl.getInputStream());
                DataOutputStream clientout = new DataOutputStream(cl.getOutputStream());
                for (;;) {
                    System.out.println("\nRecibiendo datos...");
                    String sentence = clientinp.readUTF();
                    if (sentence.equals("exit")) {
                      System.out.println("Sesión Finalizada");
                      clientout.writeUTF(String.format("> %s", "bye"));  
                      break;
                    }
                    System.out.printf("\nRecibido: %s desde host\n", sentence);
                    clientout.writeUTF(String.format("> %s", operaciones(sentence)));
                  }
                  cl.close();
                  }

                }
        catch (Exception e) {
            e.printStackTrace();}
        finally{
            serializar(baseGeneral);
        }

        /*
         String createQuery = "CREATE DATABASE perritos";
         String crearTablaQuery = "CREATE TABLE Personas (String nombre, String propietario, String edad )";
         String insertarQuery = "INSERT INTO Personas VALUES(Firulais, Armando, 25 )";
         String insertarQuery2 = "INSERT INTO Personas VALUES(PelusaTot, Gober, 21 )";
         String obtenerTodosQuery = "SELECT * FROM Personas";
        */

    }

        //Permite saber si el query es valido, TODO: Falta use
    public static String operaciones(String query) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
        if (query.matches("(SHOW DATABASES;)")) {
            return DatabaseQuerys.verTodasBasesDatos(baseGeneral);
         } else if (query.matches("(USE [a-zA-Z0-9]+)")) {
            mibasesita = DatabaseQuerys.usarBaseDatos(query, baseGeneral);
            return "Using Database";
        } else if (query.matches("(CREATE DATABASE [a-zA-Z0-9]+)")) {
            DatabaseQuerys.crearBaseDatos(query, baseGeneral);
            return "Database Created";
        } else if (query.matches("(DROP DATABASE [a-zA-Z0-9]+)")) {
            DatabaseQuerys.borrarBaseDatos(query, baseGeneral);
            return "Database Dropped";
        } else if (query.matches("(CREATE TABLE [a-zA-Z]+ [(]([a-zA-Z0-9]+ [a-zA-Z0-9]+[,]* )+[)])")) {
            DatabaseQuerys.crearTabla(query, mibasesita);
            return "Table Created";
        } else if (query.matches("(SHOW TABLES;)")) {
            return DatabaseQuerys.verTodasTablas(mibasesita);
         } else if (query.matches("(DELETE FROM [a-zA-Z0-9]+ WHERE[(]([a-zA-Z0-9]+ [=]* [a-zA-Z0-9]+)+ [)])")) {
             return DatabaseQuerys.borrarRegistro(query, mibasesita);
        } else if (query.matches("(SELECT [*] FROM [a-zA-Z0-9]+ WHERE[(]([a-zA-Z0-9]+ [=]* [a-zA-Z0-9]+)+ [)])")) {
            return DatabaseQuerys.obtenerRegistro(query, mibasesita);
         } else if (query.matches("(SELECT [*] FROM [a-zA-Z0-9]+)")) {
            return  DatabaseQuerys.obtenerTodosLosDatos(query, mibasesita);
        } else if (query.matches("(INSERT INTO [a-zA-Z0-9]+ VALUES[(]([a-zA-Z0-9]+[,]* )+[)])")) {
            DatabaseQuerys.insertarDatos(query, mibasesita);
            return "Register Inserted";
        }else if (query.matches("(UPDATE [a-zA-Z0-9]+ SET [a-zA-Z0-9]+ [=] [a-zA-Z0-9]+ WHERE [a-zA-Z0-9]+ [=] [a-zA-Z0-9]+)")) {
            DatabaseQuerys.actualizarRegistro(query, mibasesita);
            return "Updated";
        } else if (query.matches("(DROP TABLE [a-zA-Z0-9]+)")) {
            DatabaseQuerys.borrarTabla(query, mibasesita);
            return "Table Dropped";
        } else {
            return "COMANDO INVALIDO";
        }
    }

    //Permite des serealizar la base
    public static HashMap deserializar() throws ClassNotFoundException{
         HashMap objetoDesserealizada = (HashMap)SerlializeDatabase.deserialize();
         return objetoDesserealizada;
    }

    //Permite serealizar la base de datos
    public static void serializar(Map<String, HashMap<String, LinkedList>> baseDeDatos){
        SerlializeDatabase.serialize(baseDeDatos);
    }
}


