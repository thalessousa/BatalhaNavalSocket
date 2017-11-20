/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacao;

import board.LabelLeft;
import form.FormPrincipal;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author thale
 */
public class ChatController {
    static final int COLOR_BLACK = 0;
    static final int COLOR_RED = 1;
    static final int COLOR_BLUE = 2;
    static final int COLOR_GREEN = 3;
    static final int COLOR_ORANGE = 4;
    
    
    private StyledDocument doc = null;
    private Style red = null, black = null, blue = null, green = null, orange = null;
    
    public ChatController(StyledDocument remoteDoc){
        doc = remoteDoc;
        
        StyleContext sc = new StyleContext();
        
        black = sc.addStyle("Black", null);
        black.addAttribute(StyleConstants.Foreground, Color.black);
        
        red = sc.addStyle("Red", null);
        red.addAttribute(StyleConstants.Foreground, Color.red);
        
        blue = sc.addStyle("Blue", null);
        blue.addAttribute(StyleConstants.Foreground, Color.blue);
        
        green = sc.addStyle("Green", null);
        green.addAttribute(StyleConstants.Foreground, Color.green.darker());
        
        orange = sc.addStyle("Orange", null);
        orange.addAttribute(StyleConstants.Foreground, Color.orange.darker());
    }
    
    public void insertString(String msg, int color){
        try {
            doc.insertString(doc.getLength(), msg, selectColor(color));
        } catch (BadLocationException ex) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
            Logger.getLogger(FormPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertMessage(String name, String msg, int nameColor){
        insertString( name + ": ", nameColor);
        insertString( msg+"\n", 0);
    }
    
    // 0 = Player Local, 1 = Player Remoto
    public void attackMessage(String remoteName, int x, int y, boolean acertou, String type, int player){
        if(player==0)remoteName = "Você";
        else remoteName += " te";
        
        insertString(remoteName + " atacou na Coordenada "
                + new LabelLeft().coordToString(y+1) + (x+1) +"! ", COLOR_ORANGE);
        if(acertou){
            insertString("Acertou um(a) " + type+"\n", COLOR_ORANGE);
        }else{
            insertString("Não acertou nada!\n", COLOR_ORANGE);
        }
    }
    
    public Style selectColor(int color){
        switch (color) {
            case COLOR_RED:
                return red;
            case COLOR_BLUE:
                return blue;
            case COLOR_GREEN:
                return green;
            case COLOR_ORANGE:
                return orange;
            default:
                return black;
        }
    }
}
