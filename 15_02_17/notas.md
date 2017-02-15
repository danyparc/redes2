# Sockets de Datagrama bloqueantes

MSS(Maximum Segment Size)=576 bytes  
MTU(Maximum Transmission Unit)=1500 bytes

#### Clase DatagramPacket (java.net.DatagramPacket)  

  Contiene una instancia `InetAddress dir`
  `int pto`  
  `byte[]={datos}`


**Constructores:**

- `DatagramPacket(byte[] b, int tam)`
- `DatagramPacket(byte[] b, int tam, InetAddress dst, int pto)`

**Métodos**
- `InetAddress getAddress()`
- `byte [] getData()`
- `int getLength`
- `void setAddress(InetAddress d)`
- `void setData(byte[] b)`
- `void setLength(int t)`
- `void setPort(int p)`

```java
try{
  String msj = "un mensaje";
  byte[] b = msj.getBytes();
  int pto=5000;
  DatagramPacket p = new DatagramPacket(b,b.length,InetAddress.getByName(),"1.2.3.4",pto)
}
```

#### Clase DatagramSocket(java.net.DatagramSocket)  

**Constructores**
- `DatagramSocket()`
- `DatagramSocket(int pto)`
- `DatagramSocket(int pto,InetAddress local)`

**Metodos**
- `void bind(SocketAddress local)`
- `void close()`
- `void connect(InetAddress dst, int pto) `
- `void disconnect() `
- `boolean getBroadcast() `
- `DatagramChannel getChannel() `
- `InetAddress getInetAddress() `
- `int getPort() `
