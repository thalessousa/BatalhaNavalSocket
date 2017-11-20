/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import boats.Auxiliar;
import boats.Navios;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author thale
 */
public class Game implements Serializable{
    private Navios n1[][] = new Navios[10][10];
    private ArrayList<Point> atacados; 
    private Point mouseCoord;
    private boolean visible = true;
    
    public Game(){
        init();
    }
    
    public void changeInto(Game model){
        this.atacados = model.getAtacados();
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                n1[i][j] = model.findNavio(i, j);
            }
        }
        mouseCoord = new Point();
    }
    
    public void init(){
        this.atacados = new ArrayList<Point>();
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                n1[i][j] = null;
            }
        }
        mouseCoord = new Point();
    }
    
    public boolean addNavio(Navios n){
        if(verifySpaces(n)){
            for(int i=0;i<n.getTam();i++){
                Point p = n.getCoord(i);
                n1[p.x][p.y]=n;
            }
            return true;
        }else{
            return false;
        }
    }
    
    public void addMarker(int x, int y, String type, boolean rotacao){
        Auxiliar n = new Auxiliar(x, y, type, rotacao);
        for(int i=0;i<n.getTam();i++){
            Point p = n.getCoord(i);
            n1[p.x][p.y]=n;
        }
    }
    
    public void recebeAtaque(int x, int y) throws CoordAttackedException{
        Point aux = new Point();
        aux.x = x;
        aux.y = y;
        if(atacados.contains(aux)){
            throw new CoordAttackedException();
        }else atacados.add(aux);
    }
    
    public boolean verifySpaces(Navios n){
        for(int i=0;i<n.getTam();i++){
            Point p = n.getCoord(i);
            if(p.x+1<=9 && (n1[p.x+1][p.y])!=null){
                return false;
            }
            if(p.x-1>=0 && (n1[p.x-1][p.y])!=null){
                return false;
            }
            if(p.y+1<=9 && (n1[p.x][p.y+1])!=null){
                return false;
            }
            if(p.y-1>=0 && (n1[p.x][p.y-1])!=null){
                return false;
            }
        }
        return true;
    }
    
    public Point getMouseCoord() {
        return mouseCoord;
    }

    public void setMouseCoord(Point mouseCoord) {
        this.mouseCoord = mouseCoord;
    }

    public ArrayList<Point> getAtacados() {
        return atacados;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public Navios findNavio(int x, int y) {
        return n1[x][y];
    }
    
    public void drawAttackedCoord(Graphics2D g){
        for(Point p : atacados){
            Color c;
            if(n1[p.x][p.y]!=null){
                c = new Color(0,255,0,75);
            }else{
                c = new Color(255,0,0,75);
            }
            drawRect(g, c, p);
        }
    }
    
    public void drawRect(Graphics2D g, Color c, Point p){
        g.setColor(c);
        g.fillRect(30*p.x, 30*p.y, 30, 30);
        g.setColor(Color.black);
    }
    
    public boolean FimDeJogo(){
        int total=0;
        for(Point p: atacados){
            if(n1[p.x][p.y]!=null)total++;
        }
        return total==18;
    }
}
