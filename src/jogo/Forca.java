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
public class Forca {
    private String palavra;
    //private char [] palavraIncompleta;
    StringBuilder palavraIncompleta;
    private int score=0;

    public Forca(String palavra) {
        this.palavra = palavra;
        this.palavraIncompleta=new StringBuilder(palavra);
        for(int i=0; i<palavra.length(); i++)
            this.palavraIncompleta.setCharAt(i, '?');
        
    }
    
    public boolean palpite(char letra){
        if(palavra.contains( ""+letra)){
            int posicao=palavra.indexOf(letra);
            palavraIncompleta.setCharAt(posicao, letra);
            //System.out.println(palavraIncompleta);
            score++;
            return true;
        }
        else
            return false;
        
    }
    
    public StringBuilder getPalavraIncompleta(){
        return palavraIncompleta;
    }
    
    public boolean finalizaJogo(){
        if(score>=palavra.length())
            return true;
        else
            return false;
    }
    
    
}