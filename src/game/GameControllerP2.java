/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import comunicacao.Connection;

/**
 *
 * @author thale
 */
public class GameControllerP2 extends GameController{
    
    Connection conn = null;
    int ataques = -1;
    
    public void setConn(Connection conn){
        this.conn = conn;
    }
    
    public void seuTurno(){
        ataques = 3;
    }
    
    public boolean fimDeTurno(){
        return ataques == 0;
    }
    
    public void finalizaJogo(){
        ataques = -1;
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
        if(ataques>0){
            try {
                model.recebeAtaque(e.getX()/30, e.getY()/30);
                ataques = ataques -1;
                conn.atk(e.getX()/30, e.getY()/30, ataques);
                view.repaint();
            } catch (CoordAttackedException ex) {
                conn.exceptionMessage();
            }
        }
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
