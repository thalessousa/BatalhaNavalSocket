/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import form.FormPrincipal;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thale
 */
public abstract class GameController implements MouseListener, MouseMotionListener, ActionListener{
    
    private static final int FRAMES_PER_SECOND = 30;
    private static final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
    
    boolean gameIsRunning = true, mouseInside = false;
    protected FormPrincipal view;
    protected Game model;
    protected ImageBuffer ib;

    public void setIb(ImageBuffer ib) {
        this.ib = ib;
    }
    
    public void addView(FormPrincipal view){
        this.view = view;
    }
    
    public void addModel(Game model){
        this.model = model;
    }
    
    public void loadModel(Game model){
        this.model.changeInto(model);
    }
    
    public void gameStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                
                long time = System.currentTimeMillis();
                long sleepTime = 0;
                
                while(gameIsRunning){
                    view.repaint();
                    
                    time += SKIP_TICKS;
                    sleepTime = time - System.currentTimeMillis();
                    try {
                        if(sleepTime > 0)Thread.sleep(sleepTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }
    
    public abstract void drawMouseQuadrante(Graphics2D g);

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseInside = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseInside = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        model.getMouseCoord().setLocation(e.getX(), e.getY());
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
    
    @Override
    public abstract void mouseDragged(MouseEvent e);
    
    @Override
    public abstract void mouseClicked(MouseEvent e);

    @Override
    public abstract void mousePressed(MouseEvent e);

    @Override
    public abstract void mouseReleased(MouseEvent e);

}
