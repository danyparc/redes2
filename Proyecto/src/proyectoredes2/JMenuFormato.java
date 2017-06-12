/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoredes2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Cmop
 */
public class JMenuFormato extends JMenuItem implements ActionListener{

    private int ancho;
    private int alto;
    private JPanel modificable;
    private ChatRoom padre;

    public JMenuFormato(String etiqueta,int ancho,int alto,ChatRoom Padre,JPanel modificable)
    {
        super(etiqueta);
        this.modificable=modificable;
        this.ancho=ancho;
        this.alto=alto;
        this.addActionListener(this);
        this.padre=Padre;
    }

    public void actionPerformed(ActionEvent e) {
       //modificable.setSize(ancho,alto);
       padre.setSize(ancho, alto+200);
    }
}