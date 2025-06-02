
package Controler;

import Modelo.Personagem;
import Modelo.Ball;
import Modelo.Disparador;
import Auxiliar.TiposPiso;
import Auxiliar.Direction;

import java.util.ArrayList;

public class Fase {
    private ArrayList<Personagem> personagensDaFase;
    private Ball ball; // Alterado de Hero para Ball: referência direta à bola do jogador

    public Fase() {
        this.personagensDaFase = new ArrayList<>();
        carregarFase(); 
    }

    private void carregarFase() {
        this.personagensDaFase.clear();
        
        //exemplo de carregamento
//        for (int i = 0; i < Consts.MUNDO_ALTURA; i++) { //
//            for (int j = 0; j < Consts.MUNDO_LARGURA; j++) { //
//                if (i == 3 && j > 2 && j < 7) { 
//                    mapaDePisos[i][j] = new Piso(TiposPiso.INVISIVEL); // CORRIGIDO
//                } else {
//                    mapaDePisos[i][j] = new Piso(TiposPiso.PISAVEL_SIMPLES); // CORRIGIDO
//                }
//            }
//        }
//        if (Consts.MUNDO_ALTURA > 5 && Consts.MUNDO_LARGURA > 5) { //
//             mapaDePisos[5][5] = new Piso(TiposPiso.OBJETIVO); // CORRIGIDO
//        }
        ball = new Ball("bola.png"); // Usando a classe Ball e a imagem "bola.png"
        ball.setPosicao(0, 7); // Posição inicial da bola
        this.addPersonagem(ball);

        Disparador spawner = new Disparador("fogo.png", Direction.RIGHT); 
        spawner.setPosicao(9, 1);
        this.addPersonagem(spawner);    
   
    }

    public ArrayList<Personagem> getPersonagens() {
        return this.personagensDaFase;
    }

    public Ball getBall() { // Alterado de getHero para getBall
        return this.ball;
    }

    public void addPersonagem(Personagem p) {
        this.personagensDaFase.add(p);
    }

    public void removePersonagem(Personagem p) {
        this.personagensDaFase.remove(p);
    }
}