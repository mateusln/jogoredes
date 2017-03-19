/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

/**
 *
 * @author mateus
 */
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class Jogo implements Runnable
{
    ServerSocket socktServ;
    public Jogo(int porta) throws Exception{
        int PortaServidor = 7000;
        System.out.println(" -S- Aguardando conexao (P:"+PortaServidor+")...");
	socktServ = new ServerSocket(PortaServidor);
        new Thread(this).start();
        
    }
    
    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try{
            while(true){
                //new RecebeJogador()
                new RecebeJogador(socktServ.accept()).start();
                System.out.println("jogador conectado");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public static void main(String args[]) {
        int PortaServidor = 7000;
        try {
            new Jogo(PortaServidor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
           

    
}
