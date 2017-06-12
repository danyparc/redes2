
package proyectoredes2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Lienzo extends javax.swing.JPanel {

    private Image imagen;
    
    public Lienzo() {
        initComponents();
    }
    
    public Lienzo(JPanel panel){
        panel.setVisible(false);
        this.setBounds(panel.getBounds());
    }
    
private Lienzo lienzo2;
    public void setImage(Image imagen) {
        this.imagen = imagen;
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 248, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 176, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void paint( Graphics g ) {
        super.paint( g );
        if( imagen != null ) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            g.setColor(Color.BLACK);
          
        } else {
             
               }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
