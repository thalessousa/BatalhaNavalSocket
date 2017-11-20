package comunicacao;

import boats.Navios;
import form.FormPrincipal;
import game.CoordAttackedException;
import game.Game;
import game.GameControllerP2;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author thale
 */
public class Connection {
    static final int COLOR_BLACK = 0;
    static final int COLOR_RED = 1;
    static final int COLOR_BLUE = 2;
    static final int COLOR_GREEN = 3;
    static final int COLOR_ORANGE = 4;
    
    static final int LOCAL_PLAYER = 0;
    static final int REMOTE_PLAYER = 1;
    
    static final String INPUT_MSG = "a";
    static final String INPUT_END = "b";
    static final String INPUT_ATK = "c";
    static final String INPUT_LOST = "d";
    static final String INPUT_TURN = "e";
    static final String INPUT_READY = "f";
    static final String INPUT_SYNC_INIT = "g";
    static final String INPUT_SYNC_END = "h";
    static final String INPUT_LOAD_STATE_CONFIRM = "i";
    static final String INPUT_LOAD_YES = "j";
    static final String INPUT_LOAD_NO= "k";
    
    static final String MSG_YOU = "Você ";
    static final String MSG_WIN = "ganhou!\n";
    static final String MSG_LOST = "perdeu!\n";
    static final String MSG_YOUR_TURN = "Seu turno...\n";
    static final String MSG_REMOTE_TURN1 = "Turno de ";
    static final String MSG_REMOTE_TURN2 = "...\n";
    static final String MSG_READY = " está pronto\n";
    static final String MSG_CONN_ERROR = "Erro de conexão!\n";
    static final String MSG_EXITED = " saiu...\n";
    static final String MSG_REMOTE_CONNECTED = " conectou-se...\n";
    static final String MSG_YOU_CONNECTED = "Você se conectou ao servidor de ";
    static final String MSG_ATK_EXCEPTION = "Coordenada já atacada!\n";
    
    private Game[] state;
    private Socket cliente;
    private ServerSocket servidor;
    private BufferedReader buffer;
    private PrintStream ps;
    private ChatController chat;
    private String name, IP, remoteName;
    private Game model1 = null, model2 = null;
    private GameControllerP2 controller2 = null;
    private boolean localReady, remoteReady, isConnected;
    private int ataques, turn;
    private Thread timeCounter;
    private FormPrincipal view;
    
    public Connection(ChatController remoteChat, FormPrincipal form){
        cliente = null;
        servidor = null;
        buffer = null;
        chat = remoteChat;
        view = form;
        name = null;
        IP = null;
        remoteName = null;
        localReady = false;
        remoteReady = false;
        isConnected = false;
        turn = 0;
        ataques = 0;
        
        setThread();
    }
    
    public void setThread(){
        timeCounter = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isConnected){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
                    }
                }
            }
        });
    }
    
    public void setModel1(Game model) {
        this.model1 = model;
    }
    
    public void setModel2(Game model) {
        this.model2 = model;
    }
    
    public void setController2(GameControllerP2 controller) {
        this.controller2 = controller;
    }

    public void setView(FormPrincipal view) {
        this.view = view;
    }
    
    public void close(){
        try {
            ps = new PrintStream(cliente.getOutputStream());
            ps.println(INPUT_END);
            chat.insertString(MSG_YOU + MSG_EXITED, COLOR_RED);
            connectionEnded();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void disconnect(){
        if(remoteName!=null)chat.insertString(remoteName + MSG_EXITED, COLOR_RED);
        connectionEnded();
    }
    
    public void connectionEnded(){
            try {
                if(cliente != null)cliente.close();
                if(servidor != null)servidor.close();
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            reset();
            model1.init();
            model2.init();
            setThread();
    }
    
    public void reset(){
        cliente = null;
        servidor = null;
        buffer = null;
        name = null;
        IP = null;
        remoteName = null;
        localReady = false;
        remoteReady = false;
        isConnected = false;
        turn = 0;
    }
    
    public boolean host(String n, String i){
        PrintStream ps;
        name = n;
        if(name.equals("")){
            name = "Host";
        }
        IP = i;
        try {
            servidor = new ServerSocket(12345);
            cliente  = servidor.accept();
            ps = new PrintStream(cliente.getOutputStream());
            ps.println(name);
            
            buffer = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            remoteName = buffer.readLine();
            
            chat.insertString(remoteName
                    +" ("+cliente.getInetAddress().getHostAddress()+")"
                    + MSG_REMOTE_CONNECTED, COLOR_RED);
            isConnected = true;
            timeCounter.start();
            
            return true;
        } catch (IOException ex) {
            chat.insertString(MSG_CONN_ERROR, COLOR_RED);
            Logger.getLogger(FormPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean connect(String n, String i){
        PrintStream ps;
        name = n;
        if(name.equals("")){
            name = "Client";
        }
        IP = i;
        try {
            cliente = new Socket(IP, 12345);
            ps = new PrintStream(cliente.getOutputStream());
            ps.println(name);
            
            buffer = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            remoteName = buffer.readLine();
            chat.insertString(MSG_YOU_CONNECTED +remoteName + "!\n", COLOR_RED);
            isConnected = true;
            timeCounter.start();
            return true;
        } catch (IOException ex) {
            chat.insertString(MSG_CONN_ERROR, COLOR_RED);
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void listen(){
        try{
            buffer = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String aux;
            
            while(cliente!=null){
                aux = null;
                aux = buffer.readLine();
                if(aux!=null){
                    if(aux.equals(INPUT_MSG)){
                        
                        chat.insertMessage(remoteName, buffer.readLine(), COLOR_GREEN);
                        
                    }else if(aux.equals(INPUT_END)){
                        
                        disconnect();
                        
                    }else if(aux.equals(INPUT_ATK)){
                        
                        ataques--;
                        int x = Integer.parseInt(buffer.readLine());
                        int y = Integer.parseInt(buffer.readLine());
                        
                        try {
                            model1.recebeAtaque(x, y);
                        } catch (CoordAttackedException ex) {
                            exceptionMessage();
                        }

                        boolean acertou = false;
                        String type = "";

                        if(model1.findNavio(x, y)!=null){
                            acertou = true;
                            type = model1.findNavio(x, y).getType();
                        }

                        chat.attackMessage(remoteName, x, y, acertou, type, REMOTE_PLAYER);

                        if(model1.FimDeJogo()){
                            try {
                                ps = new PrintStream(cliente.getOutputStream());
                                ps.println(INPUT_LOST);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
                                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            chat.insertString(MSG_YOU + MSG_LOST, COLOR_RED);
                        }else if(ataques==0){
                            controller2.seuTurno();
                            chat.insertString(MSG_YOUR_TURN, COLOR_RED);
                            fimDeTurno();
                        }
                        
                    }else if(aux.equals(INPUT_READY)){
                        
                        chat.insertString(remoteName + MSG_READY, COLOR_RED);
                        remoteReady = true;
                        
                        if(isEveryoneReady() && servidor!=null){

                            fimDeTurno();
                            chat.insertString(MSG_YOUR_TURN, COLOR_RED);
                            controller2.seuTurno();
                        }
                        
                    }else if(aux.equals(INPUT_TURN)){
                        turn = 2;
                        ataques = 3;
                        chat.insertString(MSG_REMOTE_TURN1 + remoteName 
                                + MSG_REMOTE_TURN2, COLOR_RED);
                        
                    }else if(aux.equals(INPUT_LOST)){
                        
                        controller2.finalizaJogo();
                        chat.insertString(MSG_YOU + MSG_WIN, COLOR_RED);
                        
                    }else if(aux.equals(INPUT_SYNC_INIT)){
                        
                        synchronizeReceive();
                        
                    }else if(aux.equals(INPUT_SYNC_END)){
                         
                    }
                    
                }
            }
        }catch(IOException ex){
            
        }
    }
    
    public void send(String msg){
        try{
            ps = new PrintStream(cliente.getOutputStream());
            ps.println(INPUT_MSG);
            ps.println(msg);
            chat.insertMessage(name, msg, COLOR_BLUE);
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
            ex.printStackTrace();
        }
    }
    
    public void atk(int x, int y, int atkRestantes){
        try{
            ataques--;
            ps = new PrintStream(cliente.getOutputStream());
            ps.println(INPUT_ATK);
            ps.println(x);
            ps.println(y);
            String type = "";
            boolean acertou = false;
            Navios n = model2.findNavio(x, y);
            if(n!=null){
                acertou = true;
                type = n.getType();
            }
            chat.attackMessage(remoteName, x, y, acertou, type, LOCAL_PLAYER);
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
            ex.printStackTrace();
        }
    }
    
    public void synchronizeSend(){
        try{
            localReady = true;
            
            boolean sentPatrulha = false;
            boolean sentSubmarino = false;
            boolean sentCruzador = false;
            boolean sentDestroyer = false;
            boolean sentPortaAvioes = false;
            
            ps = new PrintStream(cliente.getOutputStream());
            ps.println(INPUT_READY);
            chat.insertString(MSG_YOU + MSG_READY, COLOR_RED);
            ps.println(INPUT_SYNC_INIT);
            for(int i=0; i<10; i++){
                for(int j=0; j<10; j++){
                    Navios aux = model1.findNavio(i, j);
                    if(aux!=null){
                        String type = aux.getType();
                        if(type.equals("Patrulha")){
                            if(!sentPatrulha){
                                sentPatrulha = true;
                                sendBoat(aux);
                            }
                        }else if(type.equals("Submarino")){
                            if(!sentSubmarino){
                                sentSubmarino = true;
                                sendBoat(aux);
                            }
                        }else if(type.equals("Cruzador")){
                            if(!sentCruzador){
                                sentCruzador = true;
                                sendBoat(aux);
                            }
                        }else if(type.equals("Destroyer")){
                            if(!sentDestroyer){
                                sentDestroyer = true;
                                sendBoat(aux);
                            }
                        }else if(type.equals("PortaAvioes")){
                            if(!sentPortaAvioes){
                                sentPortaAvioes = true;
                                sendBoat(aux);
                            }
                        }
                    }
                }
            }
            ps.println(INPUT_SYNC_END);
            if(isEveryoneReady() && servidor!=null){
                fimDeTurno();
                controller2.seuTurno();
                chat.insertString(MSG_YOUR_TURN, COLOR_RED);
            }
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
            ex.printStackTrace();
        }
    }
    
    public void sendBoat(Navios aux){
        Point p = aux.getCoord(0);
        ps.println(p.x);
        ps.println(p.y);
        ps.println(aux.getType());
        ps.println(aux.getRotacao());
    }
    
    public void synchronizeReceive(){
        try{
            String aux = buffer.readLine();
            while(!aux.equals(INPUT_SYNC_END)){
                int x = Integer.parseInt(aux);
                int y = Integer.parseInt(buffer.readLine());
                String type = buffer.readLine();
                boolean rotacao = Boolean.parseBoolean(buffer.readLine());

                model2.addMarker(x, y, type, rotacao);

                aux = buffer.readLine();
            }
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
            ex.printStackTrace();
        }
    }
    
    public void fimDeTurno(){
        try{
            ataques = 3;

            ps = new PrintStream(cliente.getOutputStream());
            ps.println(INPUT_TURN);
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
            ex.printStackTrace();
        }
        turn = 1;
    }
    
    public boolean isEveryoneReady(){
        boolean ready = (localReady && remoteReady);
        return ready;
    }
    
    public String getIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            return "Desconhecido";
        }
    }
    
    public Game[] getState(){
        state = new Game[2];
        state[0] = model1;
        state[1] = model2;
        
        return state;
    }
    
    public void setState(Game[] state){
        try {
            this.state = state;
            ps = new PrintStream(cliente.getOutputStream());
            ps.println(INPUT_LOAD_STATE_CONFIRM);
            chat.insertString("Aguardando confirmação do jogador remoto para carregar o estado.\n", COLOR_RED);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado ocorreu!");
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exceptionMessage() {
        chat.insertString(MSG_ATK_EXCEPTION, COLOR_RED);
    }
}
