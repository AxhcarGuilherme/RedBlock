package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Direction;
import Controler.Tela;
import java.awt.Graphics;
import java.io.Serializable;

public class Disparador extends Personagem implements Serializable{
    private int iContaIntervalos;
    private Direction direcaoDisparo;
    
    public Disparador(String sNomeImagePNG, Direction direcaoDisparo) {
        super(sNomeImagePNG); 
        this.bTransponivel = false;
        this.bMortal = false;
        this.iContaIntervalos = 0;
        this.direcaoDisparo = direcaoDisparo; 
    }

    public void setDirecaoDeTiro(Direction novaDirecao) {
        this.direcaoDisparo = novaDirecao;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if (this.iContaIntervalos == Consts.TIMER) { //
            this.iContaIntervalos = 0;
            
            int linhaProjetil = pPosicao.getLinha();
            int colunaProjetil = pPosicao.getColuna();

            // Switch agora usa os valores do enum Direction
            switch (this.direcaoDisparo) {
                case UP:
                    linhaProjetil--;
                    break;
                case DOWN:
                    linhaProjetil++;
                    break;
                case LEFT:
                    colunaProjetil--;
                    break;
                case RIGHT:
                    colunaProjetil++;
                    break;
            }

            Projetil p = new Projetil("caveira.png", this.direcaoDisparo);
            p.setPosicao(linhaProjetil, colunaProjetil); 
            Desenho.acessoATelaDoJogo().addPersonagem(p); //
        }
    }    
}
