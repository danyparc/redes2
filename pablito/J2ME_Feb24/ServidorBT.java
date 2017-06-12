import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
public class ServidorBT extends MIDlet implements Runnable, CommandListener {
    private Form    f;
    private Display d;
    private TextField tf = new TextField("Mensaje:", "", 40, TextField.ANY);
    private Command ce = new Command("Enviar",      Command.CANCEL, 2);
    private Command cl = new Command("Borrar texto",Command.SCREEN, 2);
    private Command cc = new Command("Conectar",    Command.SCREEN, 2);
    private Command cs = new Command("Salir",       Command.SCREEN, 2);
    private InputStream     is;
    private OutputStream    os;
    private static final UUID id = new UUID("F0E0D0C0B0A000908070605040302010", false);
    private LocalDevice     ld;
    private StreamConnectionNotifier scn;
    private Thread          t;
    public ServidorBT() {
        super();
    }
    public void startApp() {
        f = new Form("Conectarse.");
        f.addCommand(cc);
        f.addCommand(cs);
        f.setCommandListener(this);
        d = Display.getDisplay(this);
        d.setCurrent(f);
    }
    public void run() {
        f = new Form("Servidor: Conectado al cliente.");
        f.append(tf);
        f.addCommand(ce);
        f.addCommand(cl);
        f.addCommand(cs);
        f.setCommandListener(this);
        try {
            ld = LocalDevice.getLocalDevice();
            if (!ld.setDiscoverable(DiscoveryAgent.GIAC)) { }
            scn = (StreamConnectionNotifier) Connector.open("btspp://localhost:" + id.toString() + ";name=tcg");
        } catch (Exception ex) {
            f.append("Error: " + ex);
        }
        d = Display.getDisplay(this);
        d.setCurrent(f);
        while (true) {
            StreamConnection conn = null;
            try {
                conn = scn.acceptAndOpen();
            } catch (Exception e) {
                f.append("Error: " + e);
                continue;
            }
            try {
                os = conn.openOutputStream();
                is = conn.openInputStream();
                while (conn != null) {
                    byte buffer[] = new byte[40];
                    is.read(buffer);
                    f.insert(1, new StringItem("Cliente:", new String(buffer)));
                    d = Display.getDisplay(this);
                    d.setCurrent(f);
                }
                os.flush();
            } catch (IOException e) {
                f.append("Error: " + e);
            }
        }
    }
    public void pauseApp() {
        d = Display.getDisplay(this);
        d.setCurrent(f);
    }
    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }
    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Conectar")) {
            t = new Thread(this);
            t.start();
        } else if (label.equals("Enviar")) {
            try {
                os.write(tf.getString().getBytes());
                f.insert(1, new StringItem("Servidor :", tf.getString()));
            } catch (Exception e) {
                f.append("Error : " + e);
            }
        } else if (label.equals("Borrar texto")) {
            tf.setString("");
        } else if (label.equals("Salir")) {
            destroyApp(false);
        }
    }
}
