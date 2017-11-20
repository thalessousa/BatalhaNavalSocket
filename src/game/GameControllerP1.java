/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import boats.Navios;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author thale
 */
public class GameControllerP1 extends GameController{
    
    private Navios n = null;

    public void setN(Navios n) {
        this.n = n;
    }
    
    public void rotacionarN(){
        if(n!=null){
            n.rotacionar();
        }
    }
    
    @Override
    public void drawMouseQuadrante(Graphics2D g){
        if(mouseInside){
            int width = view.getBoard1().getWidth()/10;
            int height = view.getBoard1().getHeight()/10;

            int qx = model.getMouseCoord().x/width;
            int qy = model.getMouseCoord().y/height;

            int squareWidth = g.getClip().getBounds().width / 10;
            int squareHeight = g.getClip().getBounds().height / 10;

            g.setColor(Color.red);
            g.drawRect(qx * squareWidth, qy * squareHeight, squareWidth, squareHeight);
            g.setColor(Color.black);
            
            if(n!=null){
                ib.drawToCoord(g,qx,qy,n);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //not needed
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //not needed
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(n!=null){
            if(model.addNavio(n)){
                String type = n.getType();
                n = null;
                if(type.equals("PortaAvioes")){
                    view.setEnabledPortaAvioes(false);
                }else if(type.equals("Destroyer")){
                    view.setEnabledDestroyer(false);
                }else if(type.equals("Cruzador")){
                    view.setEnabledCruzador(false);
                }else if(type.equals("Submarino")){
                    view.setEnabledSubmarino(false);
                }else if(type.equals("Patrulha")){
                    view.setEnabledPatrulha(false);
                }
            }
        }
        view.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //not needed
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //not needed
    }
}
