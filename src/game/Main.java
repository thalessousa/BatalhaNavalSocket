/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import board.Tabuleiro;
import form.FormPrincipal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author thale
 */
public class Main {
    
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Tabuleiro boardP1 = new Tabuleiro(1);
                    Tabuleiro boardP2 = new Tabuleiro(2);
                    
                    FormPrincipal view = new FormPrincipal(boardP1, boardP2);
                    
                    Game model1 = new Game();
                    Game model2 = new Game();
                    model2.setVisible(false);
                    
                    ImageBuffer ib1 = new ImageBuffer(model1);
                    ImageBuffer ib2 = new ImageBuffer(model2);
                    
                    view.getConnection().setModel1(model1);
                    view.getConnection().setModel2(model2);
                    
                    boardP1.setView(view);
                    boardP1.registerObserver(ib1);
                    
                    boardP2.setView(view);
                    boardP2.registerObserver(ib2);
                    
                    GameControllerP1 controller1 = new GameControllerP1();
                    GameControllerP2 controller2 = new GameControllerP2();
                    
                    view.getConnection().setController2(controller2);
                    
                    controller1.addView(view);
                    controller1.addModel(model1);
                    controller1.setIb(ib1);
                    
                    controller2.addView(view);
                    controller2.addModel(model2);
                    controller2.setIb(ib2);
                    
                    view.setController1(controller1);
                    view.setController2(controller2);
                    
                    view.addController(boardP1, controller1);
                    view.addController(boardP2, controller2);
                    
                    controller1.gameStart();
                    controller2.gameStart();
                    
                    view.setVisible(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao inicializar o programa!");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
