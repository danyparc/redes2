/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Juan Pablo
 */
public class DatabaseQuerys {
    public DatabaseQuerys(){}
    
    //Permite Ver todas las bases de datos
    public static String verTodasBasesDatos(Map<String, HashMap<String, LinkedList>> base) {
        List<String> list = new ArrayList<String>(base.keySet());
        return Arrays.toString(list.toArray()); }
    
    //Permite obtener una base de datos
    public static HashMap usarBaseDatos(String query, Map<String, HashMap<String, LinkedList>> base){
        String regexNombreTabla = "USE ";
        String nombreBaseDatos = query.replaceAll(regexNombreTabla, "");
        System.out.print("USANDO LA BASE DE DATOS " + nombreBaseDatos);
        return base.get(nombreBaseDatos);
    }
    
    //Permite crear una base de datos
    public static void crearBaseDatos(String query, Map<String, HashMap<String, LinkedList>> base) { 
        HashMap<String, LinkedList> baseDeDatos = new HashMap<>();
        String regex = "CREATE DATABASE ";
        String nombre = query.replaceAll(regex, "");   
        base.put(nombre, baseDeDatos);
    }
    
    //Permite borrar la base de datos
    public static void borrarBaseDatos(String query, Map<String, HashMap<String, LinkedList>> base) {
        String regex = "DROP DATABASE ";
        String nombre = query.replaceAll(regex, "");
        base.remove(nombre);
    }
     
    //Permite obtener todas las tablas
    public static String verTodasTablas(HashMap baseDeDatos) {
        List<String> list = new ArrayList<String>(baseDeDatos.keySet());
        if(list != null && !list.isEmpty())
            return  Arrays.toString(list.toArray());
        return "No Tables"; }
    
          //Permite crear Tablas
    public static void crearTabla(String query, HashMap baseDatos) throws IOException {
        String regexNombreTabla = "CREATE TABLE | [(]([a-zA-Z]+ [a-zA-Z]+[,]* )+[)]";
        String regexTiposTabla = "CREATE TABLE [a-zA-Z]+|[(]|[)]";
       LinkedList linkedlist = new LinkedList<>();

        String nombreTabla = query.replaceAll(regexNombreTabla, "");
        String constructor = query.replaceAll(regexTiposTabla, "");
        String argumentos = constructor.replaceAll(",\\s", " ");
        crearClaseTabla(nombreTabla, argumentos, constructor);
        baseDatos.put(nombreTabla, linkedlist);
    }
    
        //Permite dropear la tabla
    public static void borrarTabla(String query,HashMap baseDatos) {
        String regex = "DROP TABLE ";
        String nombre = query.replaceAll(regex, "");
        baseDatos.remove(nombre);
    }
    
    //Permite insertar datos a la tabla
    public static void insertarDatos(String query, HashMap baseDatos) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        String regexNombreTabla = "INSERT INTO | VALUES[(]([a-zA-Z0-9]+[,]* )+[)]";
        String regexValores = "INSERT INTO [a-zA-Z]+ VALUES|[(]|[)]|[,]";
        String nombreTabla = query.replaceAll(regexNombreTabla, "");
        String valores = query.replaceAll(regexValores, "");
        String[] words = valores.split("\\s+");
        String[] rwords;
        Stack st = new Stack();
        String methodName = "setValores";

        //Hacemos reverse de nuestra pila para que se inserten bien los datos 
        
        for (int i = (words.length - 1); i >= 0; i--) {
            st.push(words[i]);
        }

        //Reflexion para poder crear una instancia del metodo
        Class<?> c = Class.forName(nombreTabla);
        Constructor<?> cons = c.getConstructors()[0];
        Object object = cons.newInstance();
        Method setNameMethod = object.getClass().getMethod(methodName, Stack.class);
        setNameMethod.invoke(object, st);

        //Obtenemos la lista ligad aprevia y agregamos el nuevo registro
        LinkedList linkedlist = (LinkedList) baseDatos.get(nombreTabla);
        linkedlist.add(object);

        //Permite agregar a nuestra tabla hash los nuevos valores
        baseDatos.put(nombreTabla, linkedlist);
    }
    
     // Permite borrar un registro de la base de datos
    public static String borrarRegistro(String query,HashMap baseDatos) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String methodNameDatos = "obtenerDatos";
       String[] sp = query.split(" ");
       String nombreTabla = sp[2];
       
       
       System.out.println("NOMBRE TABLA ES "+ nombreTabla);
       String parametros = sp[5];
        
       System.out.println("MI PARAMETRO ES "+ parametros);
       
        Class<?> c = Class.forName(nombreTabla);
        int i,j;
        LinkedList linkedlist = (LinkedList) baseDatos.get(nombreTabla);
        for (i = 0; i < linkedlist.size(); i++) {
            Object firstvar = linkedlist.get(i);
            Method getall = firstvar.getClass().getMethod(methodNameDatos);
            String todosDatos = (String) getall.invoke(firstvar);
            String[] datos = Utils.obtenerPalabras(todosDatos);
            for (j = 0; j < datos.length; j++) {
                if (parametros.equals(datos[j])) {
                    linkedlist.remove(i);
                    System.out.print("Dato ENCONTRADO WUUU:" + datos[j]);
                    return "Deleted Value";
                }
 
            }
        }
         return "Data not found";
 }
    
    public static String obtenerTodosLosDatos(String query, HashMap baseDatos) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String regexNombreTabla = "SELECT * FROM ";
        String nombreTabla = query.replace(regexNombreTabla, "");
        String methodNameDatos = "obtenerDatos";

        LinkedList linkedlist = (LinkedList) baseDatos.get(nombreTabla);
        List<String> list = new ArrayList<String>();
    
        for (int i = 0; i < linkedlist.size(); i++) {
            Object firstvar = linkedlist.get(i);
            Method getall = firstvar.getClass().getMethod(methodNameDatos);
            String datos = (String) getall.invoke(firstvar);
            list.add(datos);
        }
        
        if(list != null && !list.isEmpty())
            return Arrays.toString(list.toArray());
        return "No data ";
    }
    
    
      public static String obtenerRegistro(String query, HashMap baseDatos) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        String methodNameDatos = "obtenerDatos";
        String[] sp = query.split(" ");
        String nombreTabla = sp[3];
       String parametros = sp[6];
       
        
        Class<?> c = Class.forName(nombreTabla);
        Field[] fields = c.getFields();

        for (int i = 0; i < fields.length; i++) {
            System.out.println("ESTO ES DE FIELDS " + fields[i]);
        }
        int i;
        int j;

        LinkedList linkedlist = (LinkedList) baseDatos.get(nombreTabla);
        for (i = 0; i < linkedlist.size(); i++) {
            Object firstvar = linkedlist.get(i);
            Method getall = firstvar.getClass().getMethod(methodNameDatos);
            String todosDatos = (String) getall.invoke(firstvar);
            String[] datos = Utils.obtenerPalabras(todosDatos);
            for (j = 0; j < datos.length; j++) {
                if (parametros.equals(datos[j])) {
                    System.out.println("Elemento encontrado ");
                    return todosDatos;
                }
            }
        }
        
        return "No found value";
    }
      
      
   public static void actualizarRegistro(String query, HashMap baseDatos) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
        String methodNameDatos = "obtenerDatos";
        String[] sp = query.split(" ");
        String nombreTabla = sp[1];
        String nuevoValor = sp[5];
        String valorAntiguo = sp[9];
         int i,j,a,b;
         int LugarDondeSeEncontro=-1;
         Stack st = new Stack();
         Stack st2 = new Stack();
         String methodName = "setValores";
        
          System.out.println("Esto es mi tabla: "+ nombreTabla);
         System.out.println("Esto es mi nuevo valor: "+ nuevoValor);
         System.out.println("Esto es mi valor antiguo: "+ valorAntiguo);
         
        Class<?> c = Class.forName(nombreTabla);
        Field[] fields = c.getFields();
        
         LinkedList linkedlist = (LinkedList) baseDatos.get(nombreTabla);
        for (i = 0; i < linkedlist.size(); i++) {
            Object firstvar = linkedlist.get(i);
            Method getall = firstvar.getClass().getMethod(methodNameDatos);
            String todosDatos = (String) getall.invoke(firstvar);
            String[] datos = Utils.obtenerPalabras(todosDatos);
            
            st.clear();
            
            for (j = 0; j < datos.length; j++) {
                if (valorAntiguo.equals(datos[j])) {
                    System.out.println("SE ENCONTRO VALOR EN "+j);
                    LugarDondeSeEncontro =j;
                    break;
                }
            }
        }
        
        if(LugarDondeSeEncontro != -1){
        
             System.out.println("Entro para setear el valor  "+LugarDondeSeEncontro);
            Object firstvar = linkedlist.get(LugarDondeSeEncontro);
            Method getall = firstvar.getClass().getMethod(methodNameDatos);
            String todosDatos = (String) getall.invoke(firstvar);
            String[] datos = Utils.obtenerPalabras(todosDatos);            
            for (b = 0; b < datos.length; b++) {
                if (valorAntiguo.equals(datos[b])) {
                    System.out.println("Esto es valor nuevo "+ nuevoValor);
                    st.push(nuevoValor);
                }else{
                    st.push(datos[b]);
                }
            }

             for (int e = (datos.length - 1); e >= 0; e--) {
                st2.push(st.pop());
            }

        //Reflexion para poder crear una instancia del metodo
        Constructor<?> cons = c.getConstructors()[0];
        Object object = cons.newInstance();
        Method setNameMethod = object.getClass().getMethod(methodName, Stack.class);
        setNameMethod.invoke(object, st2);
        linkedlist.remove(LugarDondeSeEncontro);
        linkedlist.add(object);

        //Permite agregar a nuestra tabla hash los nuevos valores
        baseDatos.put(nombreTabla, linkedlist);
        
        
        }
        /*
        for (int i = (words.length - 1); i >= 0; i--) {
            st.push(words[i]);
        }*/
        
        
        
   }
           
    //Permite la creacion de la tabla con el compilador
    private static void crearClaseTabla(String nombre, String atributos, String constructor) throws IOException { 
        CompiladorJ instanciaTabla = new CompiladorJ();
        instanciaTabla.crearClase(nombre, atributos, constructor);
        Files.move(Paths.get("C:\\Users\\Gober\\Desktop\\jdbm\\HM\\"+ nombre +".class"), Paths.get("C:\\Users\\Gober\\Desktop\\jdbm\\HM\\build\\classes\\"+ nombre +".class"));
    }
    
    
}
