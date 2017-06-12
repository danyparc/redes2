import java.io.Serializable;

public class Data implements Serializable{
long id;
String arch;
long total;
byte[] b;

  public Data(long id, String a,long t,byte[] b){
    this.id = id;
    this.arch = a;
    this.total = t;
    this.b = b;
  }
}
