/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manejador.de.datos;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/**
 *
 * @author Juan Pablo
 */
public class CompiladorJ {
     public List parametros;
    public CompiladorJ(List parametros){
        this.parametros = parametros;
    }

    public CompiladorJ(){
    }

    public void crearClase(String nombreClase, String atributos, String constructor ) throws IOException{

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    StringWriter writer = new StringWriter();
    PrintWriter out = new PrintWriter(writer);
    String[] words = atributos.split("\\s+");
   
    //Creamos la clase con el nombre que deseamos
     out.println("import java.util.Stack;import java.io.Serializable;");
    out.println("public class " + nombreClase + " implements Serializable{");
        
    //Creamos las propiedades del objeto
    for (int i = 1; i < words.length; i += 2) {
        System.out.println("WORDS : " + words [i] + " " + words[i+1]+";");
        out.println("public "+ words[i] + " " + words[i+1]+";");
    }
    
    //Creamos un constructor publico
    out.println("public " + nombreClase + "(){}");
    
    //Creamos el constructor
    out.println("public " + nombreClase + "(" + constructor + "){");
    for (int y = 2; y < words.length; y += 2) {
         out.println("this." + words[y] + "=" + words[y]+";"); 
         System.out.println("this." + words[y] + "=" + words[y]+";"); 
    }
    out.println("} "); 
 
   
    //Creamos todos los sets para hacer nuestra instancia
    out.println("public void setValores(Stack valores) {");
    for (int b = 2; b < words.length; b += 2) {
        System.out.println(words[b] + " = ("+words[b-1]+")valores.pop();"); 
        out.println("this."+ words[b] + " = ("+words[b-1]+")valores.pop();");
        out.println("System.out.println(\"Esto del set \" + "+ words[b] +");");
    }
    out.println("}"); 
    
    //Creamos el metodo toString
      out.println("public String obtenerDatos() { return ");
        for (int z = 2; z < words.length; z += 2) {
             if(z==(words.length-1)){
                 System.out.println(words[z]);
                 out.println(words[z]);
                 break;
             }
            out.println(words[z] + " + \" \"+ ");
           
        }
        out.println(";");
        out.println("} "); 
        
    out.println("}");
    out.close();
    JavaFileObject file = new JavaSourceFromString(nombreClase, writer.toString());

    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
    JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

    boolean success = task.call();
    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
      System.out.println(diagnostic.getCode());
      System.out.println(diagnostic.getKind());
      System.out.println(diagnostic.getPosition());
      System.out.println(diagnostic.getStartPosition());
      System.out.println(diagnostic.getEndPosition());
      System.out.println(diagnostic.getSource());
      System.out.println(diagnostic.getMessage(null));

    }
    System.out.println("Success: " + success);

    if (success) {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
    }
}
class JavaSourceFromString extends SimpleJavaFileObject {
  final String code;

  JavaSourceFromString(String name, String code) {
    super(URI.create("string:///" + name.replace('.','/') + JavaFileObject.Kind.SOURCE.extension),JavaFileObject.Kind.SOURCE);
    this.code = code;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    return code;
  }
  
  
}
    
}
