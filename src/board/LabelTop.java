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
public class LabelTop extends Label{
    
    public LabelTop(){
    
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
        for(int i=0;i<=10;++i)g.drawLine(30*i, 0, 30*i, 20);
        g.drawLine(0, 0, 300, 0);
        g.drawLine(0, 20, 300, 20);
        for(int i=1;i<=10;++i)g.drawString(String.valueOf(i), -17+30*i-3*(i/10), 15);
    }
    
}
