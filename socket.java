
try{
  ServerSocket s = new ServerSocket(1234);
  // s= new Socket();
  //s.bind(new InetSocketAddress(1234));
  System.out.println("Servicio iniciado... esperando clientes...");
  for (; ; ) {
    Socket cl= s.accept();
    System.out.println("Cliente conectado desde->"+cl.getInetAddress()+":"+cl.getPort());
    String msj = "Hola mundo";
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
    pw.println(msj);
    pw.flush();//vacía el buffer de escritura y garantiza el envío
    BufferedOutputStream bus= new BufferedOutputStream(cl.getOutputStream())
    byte[] b = msj.getBytes();
    bus.write(b);
    bus.flush();
  }
}
