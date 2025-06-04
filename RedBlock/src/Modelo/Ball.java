package Modelo;

import Controler.Fase;
import Controler.Tela;
import Auxiliar.Desenho;
import Auxiliar.TiposPiso;
import Auxiliar.Posicao; // Certifique-se que é Auxiliar.Posicao
import java.io.Serializable;

public class Ball extends Personagem implements Serializable {
    private int vidas = 5;

    public Ball(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    public int getVidas() {
        return this.vidas;
    }

    public boolean gastarVidaParaDica() {
        if (this.vidas > 0) {
            this.vidas--;
            return true;
        }
        return false;
    }

    public void restaurarVidas(int numeroDeVidas) {
        this.vidas = numeroDeVidas;
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }

    @Override
    public boolean setPosicao(int linha, int coluna){
        Posicao posicaoAntiga = new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna());

        Tela tela = Desenho.acessoATelaDoJogo();
        if (tela == null) return false;
        Fase faseAtual = tela.getFaseAtualObj();
        if (faseAtual == null) return false;

        if (linha < 0 || linha >= Fase.TAMANHO_MAPA_LINHAS ||
            coluna < 0 || coluna >= Fase.TAMANHO_MAPA_COLUNAS) {
            return false;
        }

        Piso pisoAlvo = faseAtual.getPiso(linha, coluna);
        if (pisoAlvo != null && pisoAlvo.getTipo() == TiposPiso.BURACO) {
            tela.reiniciarFaseAtual();
            return false;
        }

        boolean realmenteMoveu = (this.pPosicao.getLinha() != linha || this.pPosicao.getColuna() != coluna);

        if (!super.setPosicao(linha, coluna)) {
            return false;
        }

        if (!tela.ehPosicaoValida(this.getPosicao())) {
            this.pPosicao.copia(posicaoAntiga);
            return false;
        }

        if (realmenteMoveu) {
            Piso pisoDeixado = faseAtual.getPiso(posicaoAntiga.getLinha(), posicaoAntiga.getColuna());
            if (pisoDeixado != null) {
                pisoDeixado.aoSairDoPiso();
            }
        }

        Piso pisoAtual = pisoAlvo;
        if (pisoAtual != null) {
            pisoAtual.aoSerPisado();

            if (pisoAtual.getTipo() == TiposPiso.PORTAL) {
                Posicao destino = faseAtual.getDestinoPortal(this.pPosicao);
                if (destino != null) {
                    super.setPosicao(destino.getLinha(), destino.getColuna());
                    Piso pisoDestino = faseAtual.getPiso(this.pPosicao.getLinha(), this.pPosicao.getColuna());
                    if (pisoDestino != null) {
                        pisoDestino.aoSerPisado();
                    }
                    return true;
                }
            }
            else if (pisoAtual.ehObjetivo()) {
                Desenho.acessoATelaDoJogo().avancarParaProximoMapa();
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean moveUp() {
        return this.setPosicao(this.pPosicao.getLinha() - 1, this.pPosicao.getColuna());
    }

    @Override
    public boolean moveDown() {
        return this.setPosicao(this.pPosicao.getLinha() + 1, this.pPosicao.getColuna());
    }

    @Override
    public boolean moveRight() {
        return this.setPosicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() + 1);
    }

    @Override
    public boolean moveLeft() {
        return this.setPosicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() - 1);
    }
}