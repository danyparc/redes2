import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
public class MoverCuadro extends MIDlet implements CommandListener {
    private Display d;
    private Command c; 
    private Canvas k; 
    public MoverCuadro(  ) {
	d = Display.getDisplay(this);
	k = new Canvas() {
	      private int x=0;
	      private int y=0;
	      private int bloquew;
	      private int bloqueh;
   	      public void paint (Graphics g){
		  g.setColor(255,255,255);
		  g.fillRect(0,0,getWidth(),getHeight());
		  bloquew=15; //getWidth()/10;
		  bloqueh=20; //getHeight()/10;
		  g.setColor(0,0,0);
		  g.setStrokeStyle(Graphics.SOLID);
		  g.drawRect(x, y, bloquew, bloqueh);
		  System.out.println("Pintando en: (" + x + ", " + y + "), Pantalla (" +  getWidth() + ", " + getHeight() + ")");
	      }
	      protected void keyPressed(int keyCode) {
		  int arriba=getKeyCode(UP);
		  int abajo=getKeyCode(DOWN);
		  int izq=getKeyCode(LEFT);
		  int dcha=getKeyCode(RIGHT);
		  if (keyCode == arriba)     { if(y>0) y-=1; repaint(); }
		  else if (keyCode == abajo) { if(y<getWidth()) y+=1; repaint();}
		  else if (keyCode == izq  ) { if(x>0) x-=1; repaint();}
		  else if (keyCode == dcha ) { if(x<getHeight()) x+=1; repaint();}
	      }
        };
	c=new Command("Salir",Command.EXIT, 3);
	k.addCommand(c);
	k.setCommandListener(this);
    }
    protected void startApp(  ) {
        d.setCurrent(k);
    }
    protected void pauseApp(  ) {    }
    protected void destroyApp(boolean incondicional) {    }
    public void commandAction(Command co, Displayable d) {
	if (co==c) {
	   destroyApp(true);
	   notifyDestroyed();
        }
   } 
}