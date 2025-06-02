
package Auxiliar;

import java.io.Serializable;
import java.util.Objects; // Import para Objects.hash

public class Posicao implements Serializable {
    private int linha;
    private int coluna;
    
    private int linhaAnterior;
    private int colunaAnterior;

    public Posicao(int linha, int coluna) {
        this.setPosicao(linha, coluna);
    }

    public boolean setPosicao(int linha, int coluna) {
        // Validação para garantir que a posição não saia dos limites do mundo
        if (linha < 0 || linha >= Auxiliar.Consts.MUNDO_ALTURA || 
            coluna < 0 || coluna >= Auxiliar.Consts.MUNDO_LARGURA) {
            // System.out.println("Debug: Tentativa de setPosicao fora dos limites: " + linha + "," + coluna);
            return false; // Posição inválida se fora dos limites
        }
        linhaAnterior = this.linha;
        this.linha = linha;
        colunaAnterior = this.coluna;
        this.coluna = coluna;
        return true;
    }

    public int getLinha() {
        return linha;
    }

    public boolean volta() {
        return this.setPosicao(linhaAnterior, colunaAnterior);
    }

    public int getColuna() {
        return coluna;
    }

    public boolean igual(Posicao posicao) {
        if (posicao == null) return false;
        return (linha == posicao.getLinha() && coluna == posicao.getColuna());
    }

    public boolean copia(Posicao posicao) {
        if (posicao == null) return false;
        return this.setPosicao(posicao.getLinha(), posicao.getColuna());
    }

    // ... (métodos moveUp, moveDown, etc. como antes) ...
    public boolean moveUp() { //
        return this.setPosicao(this.getLinha() - 1, this.getColuna()); //
    }

    public boolean moveDown() { //
        return this.setPosicao(this.getLinha() + 1, this.getColuna()); //
    }

    public boolean moveRight() { //
        return this.setPosicao(this.getLinha(), this.getColuna() + 1); //
    }

    public boolean moveLeft() { //
        return this.setPosicao(this.getLinha(), this.getColuna() - 1); //
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicao posicao = (Posicao) o;
        return linha == posicao.linha && coluna == posicao.coluna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(linha, coluna);
    }

    @Override
    public String toString() { // Útil para debugging
        return "Posicao{" + "linha=" + linha + ", coluna=" + coluna + '}';
    }
}