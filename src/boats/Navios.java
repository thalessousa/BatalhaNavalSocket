/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boats;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author thale
 */

public abstract class Navios implements Serializable{
    protected int tam;
    protected String type;
    protected int ocupado[][] = null;
    protected boolean rotacao = false;
    protected int x, y;

    public void initMatriz(){
        ocupado = new int[tam][2];
        for(int i=0;i<tam;i++){
            for(int j=0;j<2;j++){
                ocupado[i][j]=0;
            }
        }
    }

    public void setQuadrante(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Point getCoord(int i){
        Point p = new Point();
        p.x = ocupado[i][1];
        p.y = ocupado[i][0];
        return p;
    }

    public int getTam() {
        return tam;
    }

    public String getType() {
        return type;
    }
    
    public boolean getRotacao(){
        return rotacao;
    }
    
    public void rotacionar(){
        rotacao = !rotacao;
    }
    
    public void pos(int vertIni, int horIni){
        for(int i=0;i<tam;i++){
            if(!rotacao){
                ocupado[i][0] = vertIni;
                ocupado[i][1] = horIni+i;
            }
            else{
                ocupado[i][0] = vertIni+i;
                ocupado[i][1] = horIni;
            }
        }
        while(ocupado[tam-1][1]>9){
            for(int j=0;j<tam;j++){
                ocupado[j][1] = ocupado[j][1] - 1;
            }
        }
        while(ocupado[tam-1][0]>9){
            for(int j=0;j<tam;j++){
                ocupado[j][0] = ocupado[j][0] - 1;
            }
        }
        setQuadrante(ocupado[0][1],ocupado[0][0]);
    }
}