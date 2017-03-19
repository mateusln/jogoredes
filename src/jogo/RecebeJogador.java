/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateus
 */
public class RecebeJogador extends Thread{
    private Socket client;
    static int  numeroDeJogadores =0;
    static Forca forca;
    static ArrayList<Integer> pontos=new ArrayList<>();
    static int rodada=0;
    public RecebeJogador(Socket s){
        client=s;
        forca = new Forca("teste");
    }
    
    @Override
    public void run(){
        incrementaNumeroDeJogadores();
        int jogador=numeroDeJogadores;
        pontos.add(0);
        
        while(true){
            try{
                System.out.println("Jogador"+jogador);
                //CRIA UM PACOTE DE ENTRADA PARA RECEBER MENSAGENS, ASSOCIADO a CONEXaO (p)
                ObjectInputStream sServIn = new ObjectInputStream(client.getInputStream());
                System.out.println(" -S- Esperando jogar");
                Object msgIn = sServIn.readObject(); //ESPERA (BLOQUEADO) POR UM PACOTE
                System.out.println(" -S- Palpite Letra recebida" + msgIn);
                
                Rodada(msgIn,jogador);
                
                
                //CRIA UM PACOTE DE SAiDA PARA ENVIAR MENSAGENS, ASSOCIANDO-O a CONEXaO (p)
                ObjectOutputStream sSerOut = new ObjectOutputStream(client.getOutputStream());
                sSerOut.writeObject("RETORNO " + msgIn.toString() + " - TCP"); //ESCREVE NO PACOTE
                System.out.println(" -S- Enviando mensagem resposta...");
                sSerOut.flush(); //ENVIA O PACOTE



                //client.close();
            }catch (IOException | ClassNotFoundException e){

                e.printStackTrace();
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecebeJogador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public static synchronized void incrementaNumeroDeJogadores()
    {
        //numeroDeJogadores;
        numeroDeJogadores++;
        System.out.println("Numero de jogadores: "+numeroDeJogadores);
    }
    
    public static synchronized void Rodada(Object pacote, int jogador){
        String saida="\n";
        rodada++;
        System.out.println("vez do jogador "+ (rodada%numeroDeJogadores)+1);
        if( (rodada%numeroDeJogadores)+1!=jogador )
            System.out.println("NAO EH A SUA VEZ");
        else{                

            if(forca.palpite(pacote.toString().charAt(0))){
                int ponto=pontos.get(jogador-1);
                ponto++;
                pontos.set(jogador-1, ponto);
                saida+="Voce acertou\n";
            }else
                saida+="ERROU , nao existe essa letra\n";

        }
        saida+=forca.palavraIncompleta+"\n";
    
        //System.out.println("\nAdvinhe uma letra da palavra: \n ---------> "+forca.palavraIncompleta+"\n\n");
        for(int i=0; i<pontos.size(); i++){
            saida+="Jogador "+(i+1)+" Pontos:  "+pontos.get(i)+"  | ";
        }
        
        System.out.println(saida);
                
        
        
    }
}
