
package jogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateus
 */
public class RecebeJogador extends Thread{
    private Socket client; //socket para o cliente
    static int  numeroDeJogadores =0;
    static Forca forca;
    static ArrayList<Integer> pontos=new ArrayList<>();
    static int rodada=0, proximoJogador=0;
   
    public RecebeJogador(Socket s){
        client=s;
        forca = new Forca("teste");
    }
    
    @Override
    public void run(){
        long tempoinicial=System.nanoTime();
        
        incrementaNumeroDeJogadores();
        int jogador=numeroDeJogadores;
        pontos.add(0);
        int i=0;
        while(i<5){
            try{
                System.out.println("Jogador "+jogador+" entrou ");
                //CRIA UM PACOTE DE ENTRADA PARA RECEBER MENSAGENS, ASSOCIADO a CONEXaO (p)
                ObjectInputStream sServIn = new ObjectInputStream(client.getInputStream());
                System.out.println(" -S- Esperando jogar");
                Object msgIn = sServIn.readObject(); //ESPERA (BLOQUEADO) POR UM PACOTE
                System.out.println(" -S- Palpite Letra recebida" + msgIn);
                
                //manda uma cadeia de string com as informacoes da rodada para o jogador
                String saida=Rodada(msgIn,jogador);
                
                
                //CRIA UM PACOTE DE SAiDA PARA ENVIAR MENSAGENS, ASSOCIANDO-O a CONEXaO (p)
                ObjectOutputStream sSerOut = new ObjectOutputStream(client.getOutputStream());
                sSerOut.writeObject(saida); //ESCREVE NO PACOTE
                System.out.println(" -S- Enviando mensagem resposta ao jogador...");
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
            i++;
            
        }//end while
        
        try {//fecha conexao com cliente apos 5 rodadas
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(RecebeJogador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        long tempofinal=System.nanoTime();
        System.out.println("tempo :"+TimeUnit.NANOSECONDS.toMillis(tempofinal-tempoinicial) + " ms" );
        
    }
    
    public static synchronized void incrementaNumeroDeJogadores()
    {
        //numeroDeJogadores;
        numeroDeJogadores++;
        System.out.println("Numero de jogadores: "+numeroDeJogadores);
    }
    
    public static synchronized String  Rodada(Object pacote, int jogador){ // parte sincronizada 
        String saida="\n";
        
        saida+="Vez do jogador "+ ((rodada%numeroDeJogadores)+(int)1)+"\n";
        if( (rodada%numeroDeJogadores)+1!=jogador ){
            saida+="NAO EH A SUA VEZ\n";
              
        }
        else{
            rodada++;
            proximoJogador=(rodada%numeroDeJogadores)+1;
            //System.out.println("prox a jogar   "+proximoJogador );
            if(forca.palpite(pacote.toString().charAt(0))){
                int ponto=pontos.get(jogador-1);
                ponto++;
                pontos.set(jogador-1, ponto);
                saida+="Voce acertou\n";
            }else
                saida+="Voce ERROU nao existe essa letra\n";

        }
        saida+="Advinhe a palavra: "+forca.palavraIncompleta+"\n";
    
        for(int i=0; i<pontos.size(); i++){
            saida+="Jogador "+(i+1)+" Pontos:  "+pontos.get(i)+"  | ";
        }
        saida+="\n";
        
        System.out.println(saida);
                
        return saida;
        
    }
}
