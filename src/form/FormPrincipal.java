package form;

import board.Label;
import board.LabelLeft;
import board.LabelTop;
import game.GameController;
import board.Tabuleiro;
import boats.Cruzador;
import boats.Destroyer;
import game.GameControllerP1;
import game.GameControllerP2;
import boats.Patrulha;
import boats.PortaAvioes;
import boats.Submarino;
import game.Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;
import comunicacao.ChatController;
import comunicacao.Connection;

/**
 *
 * @author Gabriel
 */
public class FormPrincipal extends javax.swing.JFrame{
    
    private Tabuleiro boardP1 = null, boardP2 = null;
    private LabelLeft labelL1 = null, labelL2 = null;
    private LabelTop labelT1 = null, labelT2 = null;
    private ChatController chat = null;
    private GameControllerP1 controller1 = null;
    private GameControllerP2 controller2 = null;
    private Connection connection = null;
    boolean isConnected = false;
    
    public FormPrincipal(Tabuleiro boardP1, Tabuleiro boardP2) throws IOException {
        setIconImage(ImageIO.read(new File("src/img/icon32.png")));
        
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
        } catch (UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
        }
        
        initComponents();
        
        this.boardP1 = boardP1;
        this.boardP2 = boardP2;
        
        initBoardLabels();
        
        initBoards(jPJogo1, boardP1);
        initBoards(jPJogo2, boardP2);
        
        setLocationRelativeTo(null);
        statusCheck();
        
        DefaultCaret caret = (DefaultCaret)jTPChat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        chat = new ChatController(jTPChat.getStyledDocument());
        connection = new Connection(chat, this);
        
        
        atualizaIP();
    }
    
    public void updateTime(String time, String time1, String time2){
        jLTime.setText(time);
        jLTime1.setText(time1);
        jLTime2.setText(time2);
    }
    
    public Connection getConnection(){
        return connection;
    }
    
    private void initBoards(JPanel panel, Tabuleiro board){
      
        Dimension area = new Dimension(panel.getWidth(), panel.getHeight());

        board.setPreferredSize(area);
        board.setBackground(Color.white);
        panel.setLayout(new GridLayout(1, 1));
        
        panel.add(board);  
    }
    
    private void initLabel(JPanel panel, Label board){
      
        Dimension area = new Dimension(panel.getWidth(), panel.getHeight());

        board.setPreferredSize(area);
        board.setBackground(Color.white);
        panel.setLayout(new GridLayout(1, 1));
        
        panel.add(board);  
    }
    
    private void initBoardLabels(){
        labelL1 = new LabelLeft();
        initLabel(jPLeft1, labelL1);
        labelL2 = new LabelLeft();
        initLabel(jPLeft2, labelL2);
        
        labelT1 = new LabelTop();
        initLabel(jPTop1, labelT1);
        labelT2 = new LabelTop();
        initLabel(jPTop2, labelT2);
    }
    
    public void setController1(GameControllerP1 controller){
        this.controller1 = controller;
    }
    
    public void setController2(GameControllerP2 controller){
        this.controller2 = controller;
        controller2.setConn(connection);
    }
    
    public void addController(Tabuleiro board, GameController controller){
        board.addMouseListener(controller);
        board.addMouseMotionListener(controller);
    }
    
    public void statusCheck(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                jBDesconectar.setEnabled(isConnected);
                jBConectar.setEnabled(!isConnected);
                jBIniciarServer.setEnabled(!isConnected);
                jTFIP.setEnabled(!isConnected);
                jTFNome.setEnabled(!isConnected);
            } 
        }).start();
    }
    
    public void isConnecting(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                jBConectar.setEnabled(false);
                jBIniciarServer.setEnabled(false);
                jTFIP.setEnabled(false);
                jTFNome.setEnabled(false);
            } 
        }).start();
    }
    
    public void atualizaIP(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                jLLocalIP2.setText(connection.getIP());
            }
        }).start();
    }
    
    public JPanel getBoard1() {
        return jPJogo1;
    }
    
    public JPanel getBoard2() {
        return jPJogo2;
    }
    
    public void setEnabledPortaAvioes(boolean b){
        jBPortaAvioes.setEnabled(b);
        allowIniciar();
    }
    
    public void setEnabledDestroyer(boolean b){
        jBDestroyer.setEnabled(b);
        allowIniciar();
    }
    
    public void setEnabledCruzador(boolean b){
        jBCruzador.setEnabled(b);
        allowIniciar();
    }
    
    public void setEnabledSubmarino(boolean b){
        jBSubmarino.setEnabled(b);
        allowIniciar();
    }
    
    public void setEnabledPatrulha(boolean b){
        jBPatrulha.setEnabled(b);
        allowIniciar();
    }
    
    public void setEnabledIniciar(boolean b){
        jBIniciarJogo.setEnabled(b);
    }
    
    public void allowIniciar(){
        if(!jBPatrulha.isEnabled()
                && !jBSubmarino.isEnabled()
                && !jBCruzador.isEnabled()
                && !jBDestroyer.isEnabled()
                && !jBPortaAvioes.isEnabled()
                && isConnected){
            jBIniciarJogo.setEnabled(true);
        }
    }
    
    public void enableBoats(boolean b){
        if(!b)controller1.setN(null);
        jBIniciarJogo.setEnabled(!b);
        jBPatrulha.setEnabled(b);
        jBSubmarino.setEnabled(b);
        jBCruzador.setEnabled(b);
        jBDestroyer.setEnabled(b);
        jBPortaAvioes.setEnabled(b);
        jBRotacionar.setEnabled(b);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jBDesconectar = new javax.swing.JButton();
        jBIniciarServer = new javax.swing.JButton();
        jLNome = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField();
        jLPorta = new javax.swing.JLabel();
        jTFIP = new javax.swing.JTextField();
        jBConectar = new javax.swing.JButton();
        jLLocalIP2 = new javax.swing.JLabel();
        jLLocalIP1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTPChat = new javax.swing.JTextPane();
        jPJogo2 = new javax.swing.JPanel();
        jPJogo1 = new javax.swing.JPanel();
        jPLeft1 = new javax.swing.JPanel();
        jPTop1 = new javax.swing.JPanel();
        jPLeft2 = new javax.swing.JPanel();
        jPTop2 = new javax.swing.JPanel();
        jBRotacionar = new javax.swing.JButton();
        jLNomePortaAvioes = new javax.swing.JLabel();
        jLNomeDestroyer = new javax.swing.JLabel();
        jLNomeCruzador = new javax.swing.JLabel();
        jLNomeSubmarino = new javax.swing.JLabel();
        jLNomePatrulha = new javax.swing.JLabel();
        jBPortaAvioes = new javax.swing.JButton();
        jBDestroyer = new javax.swing.JButton();
        jBCruzador = new javax.swing.JButton();
        jBSubmarino = new javax.swing.JButton();
        jBPatrulha = new javax.swing.JButton();
        jBIniciarJogo = new javax.swing.JButton();
        jLTime = new javax.swing.JLabel();
        jLTime1 = new javax.swing.JLabel();
        jLTime2 = new javax.swing.JLabel();

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Batalha Naval");
        setMinimumSize(new java.awt.Dimension(970, 585));

        jBDesconectar.setText("Desconectar");
        jBDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDesconectarActionPerformed(evt);
            }
        });

        jBIniciarServer.setText("Hostear");
        jBIniciarServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBIniciarServerActionPerformed(evt);
            }
        });

        jLNome.setText("Nick:");

        jTFNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNomeActionPerformed(evt);
            }
        });

        jLPorta.setText("IP:");

        jTFIP.setText("127.0.0.1");
        jTFIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFIPActionPerformed(evt);
            }
        });

        jBConectar.setText("Conectar");
        jBConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBConectarActionPerformed(evt);
            }
        });

        jLLocalIP2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLLocalIP2.setForeground(new java.awt.Color(0, 0, 255));
        jLLocalIP2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLLocalIP2.setText("127.0.0.1");
        jLLocalIP2.setToolTipText("IP desta máquina, clique para atualizar.");
        jLLocalIP2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLLocalIP2MouseClicked(evt);
            }
        });

        jLLocalIP1.setText("Seu IP:");

        jTPChat.setEditable(false);
        jTPChat.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jTPChat.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTPChatCaretUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(jTPChat);

        jPJogo2.setBackground(new java.awt.Color(255, 255, 255));
        jPJogo2.setMaximumSize(new java.awt.Dimension(300, 300));
        jPJogo2.setMinimumSize(new java.awt.Dimension(300, 300));
        jPJogo2.setPreferredSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout jPJogo2Layout = new javax.swing.GroupLayout(jPJogo2);
        jPJogo2.setLayout(jPJogo2Layout);
        jPJogo2Layout.setHorizontalGroup(
            jPJogo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPJogo2Layout.setVerticalGroup(
            jPJogo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPJogo1.setBackground(new java.awt.Color(255, 255, 255));
        jPJogo1.setMaximumSize(new java.awt.Dimension(300, 300));
        jPJogo1.setMinimumSize(new java.awt.Dimension(300, 300));
        jPJogo1.setPreferredSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout jPJogo1Layout = new javax.swing.GroupLayout(jPJogo1);
        jPJogo1.setLayout(jPJogo1Layout);
        jPJogo1Layout.setHorizontalGroup(
            jPJogo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPJogo1Layout.setVerticalGroup(
            jPJogo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPLeft1.setBackground(new java.awt.Color(255, 255, 255));
        jPLeft1.setMaximumSize(new java.awt.Dimension(20, 300));
        jPLeft1.setMinimumSize(new java.awt.Dimension(20, 300));
        jPLeft1.setPreferredSize(new java.awt.Dimension(20, 300));

        javax.swing.GroupLayout jPLeft1Layout = new javax.swing.GroupLayout(jPLeft1);
        jPLeft1.setLayout(jPLeft1Layout);
        jPLeft1Layout.setHorizontalGroup(
            jPLeft1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPLeft1Layout.setVerticalGroup(
            jPLeft1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPTop1.setBackground(new java.awt.Color(255, 255, 255));
        jPTop1.setMaximumSize(new java.awt.Dimension(300, 20));
        jPTop1.setMinimumSize(new java.awt.Dimension(300, 20));
        jPTop1.setPreferredSize(new java.awt.Dimension(300, 20));

        javax.swing.GroupLayout jPTop1Layout = new javax.swing.GroupLayout(jPTop1);
        jPTop1.setLayout(jPTop1Layout);
        jPTop1Layout.setHorizontalGroup(
            jPTop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPTop1Layout.setVerticalGroup(
            jPTop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPLeft2.setBackground(new java.awt.Color(255, 255, 255));
        jPLeft2.setMaximumSize(new java.awt.Dimension(20, 300));
        jPLeft2.setMinimumSize(new java.awt.Dimension(20, 300));
        jPLeft2.setPreferredSize(new java.awt.Dimension(20, 300));

        javax.swing.GroupLayout jPLeft2Layout = new javax.swing.GroupLayout(jPLeft2);
        jPLeft2.setLayout(jPLeft2Layout);
        jPLeft2Layout.setHorizontalGroup(
            jPLeft2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPLeft2Layout.setVerticalGroup(
            jPLeft2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPTop2.setBackground(new java.awt.Color(255, 255, 255));
        jPTop2.setPreferredSize(new java.awt.Dimension(300, 20));

        javax.swing.GroupLayout jPTop2Layout = new javax.swing.GroupLayout(jPTop2);
        jPTop2.setLayout(jPTop2Layout);
        jPTop2Layout.setHorizontalGroup(
            jPTop2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPTop2Layout.setVerticalGroup(
            jPTop2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jBRotacionar.setText("Rotacionar Embarcação");
        jBRotacionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBRotacionarActionPerformed(evt);
            }
        });

        jLNomePortaAvioes.setText("Porta-aviões");

        jLNomeDestroyer.setText("Destroyer");

        jLNomeCruzador.setText("Cruzador");

        jLNomeSubmarino.setText("Submarino");

        jLNomePatrulha.setText("Patrulha");

        jBPortaAvioes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/portaavioes.png"))); // NOI18N
        jBPortaAvioes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPortaAvioesActionPerformed(evt);
            }
        });

        jBDestroyer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/destroyer.png"))); // NOI18N
        jBDestroyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDestroyerActionPerformed(evt);
            }
        });

        jBCruzador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cruzador.png"))); // NOI18N
        jBCruzador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCruzadorActionPerformed(evt);
            }
        });

        jBSubmarino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/submarino.png"))); // NOI18N
        jBSubmarino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSubmarinoActionPerformed(evt);
            }
        });

        jBPatrulha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/patrulha.png"))); // NOI18N
        jBPatrulha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPatrulhaActionPerformed(evt);
            }
        });

        jBIniciarJogo.setText("Iniciar Jogo");
        jBIniciarJogo.setEnabled(false);
        jBIniciarJogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBIniciarJogoActionPerformed(evt);
            }
        });

        jLTime.setForeground(new java.awt.Color(255, 0, 0));
        jLTime.setText("   ");

        jLTime1.setForeground(new java.awt.Color(255, 0, 0));
        jLTime1.setText("   ");

        jLTime2.setForeground(new java.awt.Color(255, 0, 0));
        jLTime2.setText("   ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jBPortaAvioes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jBDestroyer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jBCruzador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jBSubmarino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jBPatrulha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLNomeDestroyer, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLNomePortaAvioes, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLNomeCruzador, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLNomeSubmarino, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLNomePatrulha, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jBRotacionar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBIniciarJogo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPLeft1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPTop1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPJogo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLPorta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFIP, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLNome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLLocalIP1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLLocalIP2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 172, Short.MAX_VALUE)
                                .addComponent(jBConectar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBIniciarServer))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPLeft2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPTop2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPJogo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBDesconectar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jLTime)
                        .addGap(183, 183, 183)
                        .addComponent(jLTime1)
                        .addGap(196, 196, 196)
                        .addComponent(jLTime2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPTop2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPLeft2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPJogo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPTop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPLeft1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPJogo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLNomePortaAvioes, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jBPortaAvioes))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLNomeDestroyer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jBDestroyer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLNomeCruzador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jBCruzador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLNomeSubmarino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jBSubmarino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLNomePatrulha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jBPatrulha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jBRotacionar)
                                    .addComponent(jBIniciarJogo))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLPorta)
                    .addComponent(jLNome)
                    .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLLocalIP1)
                    .addComponent(jLLocalIP2)
                    .addComponent(jBConectar)
                    .addComponent(jBIniciarServer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBDesconectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLTime)
                    .addComponent(jLTime1)
                    .addComponent(jLTime2))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDesconectarActionPerformed
        connection.close();
        isConnected = false;
        enableBoats(true);
        statusCheck();
    }//GEN-LAST:event_jBDesconectarActionPerformed

    private void jBIniciarServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBIniciarServerActionPerformed
        jBConectar.setEnabled(false);
        jBIniciarServer.setEnabled(false);
        chat.insertString("Aguardando Conexão...\n", 1);
        isConnecting();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isConnected = connection.host(jTFNome.getText(),jTFIP.getText());
                allowIniciar();
                statusCheck();
                connection.listen();
                connection.disconnect();
                isConnected = false;
                enableBoats(true);
                statusCheck();
            }
        }).start();
    }//GEN-LAST:event_jBIniciarServerActionPerformed

    private void jTFNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNomeActionPerformed

    private void jTFIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFIPActionPerformed

    private void jBConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBConectarActionPerformed
        jBConectar.setEnabled(false);
        jBIniciarServer.setEnabled(false);
        isConnecting();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isConnected = connection.connect(jTFNome.getText(), jTFIP.getText());
                statusCheck();
                allowIniciar();
                if(isConnected){
                    connection.listen();
                    connection.disconnect();
                }
                isConnected = false;
                statusCheck();
                enableBoats(true);
            }
        }).start();
    }//GEN-LAST:event_jBConectarActionPerformed

    private void jLLocalIP2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLLocalIP2MouseClicked
        atualizaIP();
    }//GEN-LAST:event_jLLocalIP2MouseClicked

    private void jTPChatCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTPChatCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_jTPChatCaretUpdate

    private void jBRotacionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBRotacionarActionPerformed
        controller1.rotacionarN();
    }//GEN-LAST:event_jBRotacionarActionPerformed

    private void jBPortaAvioesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPortaAvioesActionPerformed
        controller1.setN(new PortaAvioes());
    }//GEN-LAST:event_jBPortaAvioesActionPerformed

    private void jBDestroyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDestroyerActionPerformed
        controller1.setN(new Destroyer());
    }//GEN-LAST:event_jBDestroyerActionPerformed

    private void jBCruzadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCruzadorActionPerformed
        controller1.setN(new Cruzador());
    }//GEN-LAST:event_jBCruzadorActionPerformed

    private void jBSubmarinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSubmarinoActionPerformed
        controller1.setN(new Submarino());
    }//GEN-LAST:event_jBSubmarinoActionPerformed

    private void jBPatrulhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPatrulhaActionPerformed
        controller1.setN(new Patrulha());
    }//GEN-LAST:event_jBPatrulhaActionPerformed

    private void jBIniciarJogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBIniciarJogoActionPerformed
        jBIniciarJogo.setEnabled(false);
        jBRotacionar.setEnabled(false);
        connection.synchronizeSend();
    }//GEN-LAST:event_jBIniciarJogoActionPerformed
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBConectar;
    private javax.swing.JButton jBCruzador;
    private javax.swing.JButton jBDesconectar;
    private javax.swing.JButton jBDestroyer;
    private javax.swing.JButton jBIniciarJogo;
    private javax.swing.JButton jBIniciarServer;
    private javax.swing.JButton jBPatrulha;
    private javax.swing.JButton jBPortaAvioes;
    private javax.swing.JButton jBRotacionar;
    private javax.swing.JButton jBSubmarino;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLLocalIP1;
    private javax.swing.JLabel jLLocalIP2;
    private javax.swing.JLabel jLNome;
    private javax.swing.JLabel jLNomeCruzador;
    private javax.swing.JLabel jLNomeDestroyer;
    private javax.swing.JLabel jLNomePatrulha;
    private javax.swing.JLabel jLNomePortaAvioes;
    private javax.swing.JLabel jLNomeSubmarino;
    private javax.swing.JLabel jLPorta;
    private javax.swing.JLabel jLTime;
    private javax.swing.JLabel jLTime1;
    private javax.swing.JLabel jLTime2;
    private javax.swing.JPanel jPJogo1;
    private javax.swing.JPanel jPJogo2;
    private javax.swing.JPanel jPLeft1;
    private javax.swing.JPanel jPLeft2;
    private javax.swing.JPanel jPTop1;
    private javax.swing.JPanel jPTop2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFIP;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextPane jTPChat;
    // End of variables declaration//GEN-END:variables
    
    public void update(int i, Graphics g){
        if(i==1){
            controller1.drawMouseQuadrante((Graphics2D) g);
        }
        if(i==2){
            controller2.drawMouseQuadrante((Graphics2D) g);
        }
    }
}
