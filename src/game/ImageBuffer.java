/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import boats.Navios;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
*
* @author thale
 */
public class ImageBuffer implements Observer{
    private final static String backgroundPath = "src/img/";
    
    Game model = null;
    
    BufferedImage patH = null,
            patV = null,
            subH = null,
            subV = null,
            cruH = null,
            cruV = null,
            desH = null,
            desV = null,
            porH = null,
            porV = null;
    
    public ImageBuffer(Game model){
        this.model = model;
        
        try {
            patH = ImageIO.read(new File(backgroundPath+"patrulha.png"));
            patV = ImageIO.read(new File(backgroundPath+"patrulha - vertical.png"));
            subH = ImageIO.read(new File(backgroundPath+"submarino.png"));
            subV = ImageIO.read(new File(backgroundPath+"submarino - vertical.png"));
            cruH = ImageIO.read(new File(backgroundPath+"cruzador.png"));
            cruV = ImageIO.read(new File(backgroundPath+"cruzador - vertical.png"));
            desH = ImageIO.read(new File(backgroundPath+"destroyer.png"));
            desV = ImageIO.read(new File(backgroundPath+"destroyer - vertical.png"));
            porH = ImageIO.read(new File(backgroundPath+"portaavioes.png"));
            porV = ImageIO.read(new File(backgroundPath+"portaavioes - vertical.png"));
        } catch (IOException ex) {
        }
    }
    
    public void drawAllBoats(Graphics2D g){
        Navios aux = null;
        
        if(model.isVisible()){
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    aux = model.findNavio(i, j);
                    if(aux!=null)drawBoat(g, aux);
                }
            }
        }
        
        model.drawAttackedCoord(g);
    }
    
    public void drawToCoord(Graphics2D g, int x, int y, Navios n) {
        n.pos(y,x);
        drawBoat(g, n);
    }
    
    public void drawBoat(Graphics2D g, Navios n) {
        BufferedImage aux = getImage(n);
        
        int squareWidth = g.getClip().getBounds().width / 10;
        int squareHeight = g.getClip().getBounds().height / 10;
        
        Point p = n.getCoord(0);
        
        int x0 = p.x * squareWidth;
        int y0 = p.y * squareHeight;
        if(!n.getRotacao()){
            squareWidth *= n.getTam();
        }else{
            squareHeight *= n.getTam();
        }
        
        g.drawImage(aux, x0, y0, null);
    }
    
    public BufferedImage getImage(Navios n){
        
        if(n.getType().equals("Patrulha")){
            if(n.getRotacao()){
                return patV;
            }
            return patH;
        }
        
        if(n.getType().equals("Submarino")){
            if(n.getRotacao()){
                return subV;
            }
            return subH;
        }
        
        if(n.getType().equals("Cruzador")){
            if(n.getRotacao()){
                return cruV;
            }
            return cruH;
        }
        
        if(n.getType().equals("Destroyer")){
            if(n.getRotacao()){
                return desV;
            }
            return desH;
        }
        
        if(n.getType().equals("PortaAvioes")){
            if(n.getRotacao()){
                return porV;
            }
            return porH;
        }
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        drawAllBoats((Graphics2D) arg);
    }
}
