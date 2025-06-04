package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Direction;
import Controler.Tela;
import java.awt.Graphics;
import java.io.Serializable;

public class Projetil extends Personagem implements Serializable{
    
    private Direction direcaoDisparo;
    
    public Projetil(String sNomeImagePNG, Direction direcao) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.direcaoDisparo = direcao;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho(); // Desenha o projétil na posição atual

        boolean conseguiuMover = false;
        // Switch agora usa os valores do enum Direction
        switch (this.direcaoDisparo) {
            case UP:
                conseguiuMover = this.moveUp(); //
                break;
            case DOWN:
                conseguiuMover = this.moveDown(); //
                break;
            case LEFT:
                conseguiuMover = this.moveLeft(); //
                break;
            case RIGHT:
                conseguiuMover = this.moveRight(); //
                break;
        }

        if (!conseguiuMover) {
            Desenho.acessoATelaDoJogo().removePersonagem(this); //
        }
    }
}




