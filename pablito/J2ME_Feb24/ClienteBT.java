import java.io.*;
import java.util.*;
import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
public class ClienteBT extends MIDlet implements DiscoveryListener, Runnable, CommandListener {
    private Form    f;
    private Display d;
    private List    l;
    private TextField tf = new TextField("Mensaje:", "", 40, TextField.ANY);
    private Command ce = new Command("Enviar",      Command.CANCEL, 2);
    private Command cc = new Command("Conectar",    Command.SCREEN, 2);
    private Command cl = new Command("Borrar texto",Command.SCREEN, 2);
    private Command cs = new Command("Salir",       Command.SCREEN, 2);
    private Vector  vdi = new Vector();
    private Vector  vda = new Vector();
    private int     id [] = new int[20];
    private int []  attrSet;
    private UUID[]  uuid = {new UUID("F0E0D0C0B0A000908070605040302010", false)};
    private int     dis;
    private InputStream is;
    private OutputStream os;
    private Thread  t;
    public ClienteBT() {    }
    public void startApp() {
        try {
            d = Display.getDisplay(this);
            l = new List("Servidores:", Choice.EXCLUSIVE);
            l.addCommand(cc);
            l.addCommand(cs);
            l.setCommandListener(this);
            d.setCurrent(l);
            LocalDevice dispositivoLocal = LocalDevice.getLocalDevice();
            if (!dispositivoLocal.setDiscoverable(DiscoveryAgent.GIAC)) { }
            DiscoveryAgent agenteDeBusqueda = dispositivoLocal.getDiscoveryAgent();
            agenteDeBusqueda.startInquiry(DiscoveryAgent.GIAC, this);
            while (vdi.size() == 0) { }
            for (int i = 0; i < vdi.size(); i++) {
                try {
                    id[i] = agenteDeBusqueda.searchServices(attrSet, uuid, ((RemoteDevice) vdi.elementAt(i)), this);
                    l.append(((RemoteDevice) vdi.elementAt(i)).getFriendlyName(true), null);
                } catch (Exception e) { }
            }
            while (vdi.size()==0 || vda.size()==0) { }
            d.setCurrent(l);
        } catch (Exception e) { }
    }
    public void pauseApp() {    }
    public void destroyApp(boolean b) {
        notifyDestroyed();
    }
    public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
        vdi.addElement(rd);
    }
    public void inquiryCompleted(int ic) {
        this.dis = ic;
    }
    public void serviceSearchCompleted(int transID, int respCode) {    }
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (int i = 0; i < servRecord.length; i++)
            vda.addElement(servRecord[i]);
    }
    public void run() {
        try {
            f = new Form("Cliente: Conectado al servidor");
            f.append(tf);
            f.addCommand(ce);
            f.addCommand(cl);
            f.addCommand(cs);
            f.setCommandListener(this);
            d = Display.getDisplay(this);
            d.setCurrent(f);
            String direccion = ((ServiceRecord) vda.elementAt(l.getSelectedIndex())).getConnectionURL(((ServiceRecord) vda.elementAt(l.getSelectedIndex())).NOAUTHENTICATE_NOENCRYPT, false);
            StreamConnection conn = (StreamConnection) Connector.open(direccion);
            is = conn.openInputStream();
            os = conn.openOutputStream();
            while (conn != null) {
                byte buffer[] = new byte[40];
                is.read(buffer);
                f.insert(1, new StringItem("Servidor:", new String(buffer)));
                d = Display.getDisplay(this);
                d.setCurrent(f);
            }
        } catch (Exception e) {
            f.append("Error : " + e);
        }
    }
    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Conectar")) {
            t = new Thread(this);
            t.start();
        } else if (label.equals("Enviar")) {
            try {
                os.write(tf.getString().getBytes());
                f.insert(1, new StringItem("Cliente: ", tf.getString()));
            } catch (Exception e) {
                f.append("Error: " + e);
            }
        } else if (label.equals("Borrar texto")) {
            tf.setString("");
        } else if (label.equals("Salir")) {
            destroyApp(false);
        }
    }
}