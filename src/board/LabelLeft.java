/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author thale
 */
public class LabelLeft extends Label{
    
    public LabelLeft() {
       
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        drawLabel(g2);
    }

    @Override
    void drawLabel(Graphics2D g) {
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(2));
        for(int i=0;i<=10;++i)g.drawLine(0, 30*i, 20, 30*i);
        g.drawLine(0, 0, 0, 300);
        g.drawLine(20, 0, 20, 300);
        for(int i=1;i<=10;++i)g.drawString(coordToString(i), 8, -10+30*i);
    }
    
    public String coordToString(int i){
        switch(i){
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            case 4:
                return "D";
            case 5:
                return "E";
            case 6:
                return "F";
            case 7:
                return "G";
            case 8:
                return "H";
            case 9:
                return "I";
            case 10:
                return "J";
        }
        return "";
    }
}
    
