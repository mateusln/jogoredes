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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateus
 */
public class RecebeJogador extends Thread{
    private Socket client;
    static int  numeroDeJogadores =0;
    public RecebeJogador(Socket s){
        client=s;
    }
    
    @Override
    public void run(){
        incrementaNumeroDeJogadores();
        while(true){
            try{
                    //CRIA UM PACOTE DE ENTRADA PARA RECEBER MENSAGENS, ASSOCIADO a CONEXaO (p)
                ObjectInputStream sServIn = new ObjectInputStream(client.getInputStream());
                System.out.println(" -S- Recebendo mensagem...");
                Object msgIn = sServIn.readObject(); //ESPERA (BLOQUEADO) POR UM PACOTE
                System.out.println(" -S- Recebido: " + msgIn);
                System.out.println(" resultado: ");
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
}
