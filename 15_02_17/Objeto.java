import java.io.Serializable;

public class Objeto implements Serializable{
  String nombre;
  String apaterno;
  String amaterno;
  int edad;
  int clave;
  /*transient int clave;
  Con esto no se envía esa propiedad en el serializado
  */

  public Objeto(String n, String p, String m, int e, int c){
    this.nombre = n;
    this.apaterno=p;
    this.amaterno=m;
    this.edad=e;
    this.clave=c;

  }
}
