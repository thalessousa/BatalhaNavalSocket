/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boats;

/**
 *
 * @author thale
 */
public class Auxiliar extends Navios{
    //Embarcação auxiliar para anotar as informações recebidas do player remoto
    
    public Auxiliar(int x, int y, String type, boolean rotacao){
        if(type.equals("Patrulha"))tam = 2;
        if(type.equals("Submarino"))tam = 3;
        if(type.equals("Cruzador"))tam = 4;
        if(type.equals("Destroyer"))tam = 4;
        if(type.equals("PortaAvioes"))tam = 5;
        
        this.type = type;
        this.rotacao = rotacao;
        initMatriz();
        
        for(int i=0; i<tam; i++){
            ocupado[i][1]=x;
            ocupado[i][0]=y;
            if(!rotacao)x++;
            else y++;
        }
    }
}
