/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author alumno
 */
public class Midlet extends MIDlet implements CommandListener {
    
    Display d;
    Form Opciones = new Form("Seleccione las opciones");
    Form Dibujos = new Form("Dibujo cuadro");
    
    Command c = new Command("Salir", Command.EXIT,1);
   Command c2 = new Command("OK", Command.SCREEN,1);
   Command c3 = new Command("Dibujar", Command.SCREEN,1);
   
   TextField tfTiempo = new TextField("Tiempo ", "", 30, TextField.DECIMAL);
   TextField tfIteraciones = new TextField("Iteraciones ", "", 30, TextField.DECIMAL);
   ChoiceGroup cgColor = new ChoiceGroup("Elije Color", Choice.EXCLUSIVE); 
   
   String color;
   
    
    

    public void startApp() {
        
        d = Display.getDisplay(this);
         
        cgColor.append("Rojo", null);
        cgColor.append("Verde", null);
        cgColor.append("Azul", null);
        
        Opciones.addCommand(c);
        Opciones.addCommand(c2);
        Opciones.append(cgColor);
        Opciones.append(tfTiempo);        
        Opciones.append(tfIteraciones);
        Opciones.setCommandListener(this);
        d.setCurrent(Opciones);
        
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void commandAction(Command command, Displayable displayable) {    
    
     if (command == c) {
      destroyApp(false);
      notifyDestroyed();
    }
     
     else if(command == c2)
     {
        Dibujos.addCommand(c);
        Dibujos.addCommand(c3);
        Dibujos.setCommandListener(this);
        d.setCurrent(Dibujos);
         
         
     }
     
     else if(command == c3)
     {
         for(int i=0; i<3; i++)
      {
          if(cgColor.isSelected(i))
          {
              color = cgColor.getString(i);
          }
      }
         int iteraciones = Integer.parseInt(tfIteraciones.getString()); 
         int itiempo =Integer.parseInt(tfTiempo.getString());
         Canvas canvas = new Cuadro(color, iteraciones, itiempo);
         d.setCurrent(canvas);
     }
 }
}

class Cuadro extends Canvas {
    String color;
    int cIteraciones;
    int cTiempo;
    
    
    Cuadro(String col, int itera, int tiempo) {
    //num = rnum;
    color = col;
    cIteraciones = itera;
    cTiempo = tiempo;
    
    
  }
  public void paint(Graphics g) {
      
      
      
    //Opciones
    if(color == "Rojo")
    {
        g.setColor(255, 0,0);
    }
    else if(color == "Azul")
    {
        g.setColor(0, 0,255);
    }    
    else if(color == "Verde")
    {
        g.setColor(0, 255,0);
    }
      
    //Dibuja cruadro
    int width = getWidth();
    int height = getHeight();
    
    
    //g.setStrokeStyle(Graphics.SOLID);
    g.drawRect(0, 0, width, height);
    
    //Dibuja circulo    
    g.drawArc(0, 0, width, height, 0, 360);
    
    
    for(int i=1; i<=cIteraciones; i++)
    {
        g.drawRect((36*i), ((36*i)), width-(80*i), height-(80*i));
        g.drawArc((36*i), ((36*i)), width-(80*i), height-(80*i), 0, 360);
    }
    
  }
}


/*g.drawRect(5, 18, width-20, height-20);
    
    //Dibuja circulo    
    g.drawArc(5, 18, width-20, height-20, 0, 360);
    
    
    for(int i=1; i<=cIteraciones; i++)
    {
        g.drawRect((5*(i+7)), (18*(i+2)), width-(30*(i+2)), height-(30*(i+2)));
        g.drawArc((5*(i+7)), (18*(i+2)), width-(30*(i+2)), height-(30*(i+2)), 0, 360);
    }*/

