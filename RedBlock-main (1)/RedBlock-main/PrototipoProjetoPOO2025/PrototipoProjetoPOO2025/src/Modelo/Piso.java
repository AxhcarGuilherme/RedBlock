
package Modelo;

import Auxiliar.TiposPiso; //
import Auxiliar.Consts;   //
import java.io.Serializable;
import javax.swing.ImageIcon; //
import java.io.IOException; //
import java.awt.Image; //
import java.awt.image.BufferedImage; //
import java.awt.Graphics; //

public class Piso implements Serializable { //
    private TiposPiso tipo; //
    private TiposPiso tipoOriginal; //
    private int pisadasRestantes; //
    private boolean ehObjetivo; //
    private ImageIcon imagemAtual; //
    private boolean prestesAQuebrar; // Novo estado

    public Piso(TiposPiso tipoInicial) { //
        this.tipo = tipoInicial; //
        this.tipoOriginal = tipoInicial; //
        this.ehObjetivo = (tipoInicial == TiposPiso.OBJETIVO); //
        this.prestesAQuebrar = false;

        switch (tipoInicial) { //
            case PISAVEL_SIMPLES: //
                this.pisadasRestantes = 1; //
                carregarImagem("bloco.jpeg"); //
                break;
            case PISAVEL_DUPLO: //
                this.pisadasRestantes = 2; //
                carregarImagem("blocoDuplo.jpeg"); //
                break;
            case INVISIVEL: //
                this.pisadasRestantes = -1; //
                carregarImagem("blackTile.png"); // Aparência de buraco/fundo
                break;
            case OBJETIVO: //
                this.pisadasRestantes = -1; //
                carregarImagem("blocoObjetivo.jpeg"); // Temporariamente usa a imagem do piso único
                break;
            case PORTAL: //
                this.pisadasRestantes = -1; //
                carregarImagem("portal.png"); //
                break;
            case PORTAL_SAIDA: //
                this.pisadasRestantes = -1; //
                carregarImagem("portal2.png"); //
                break;
            case PISO_SPAWN: //
            case DISPARADOR_UP: // Tipo de piso que marca local de personagem
                this.pisadasRestantes = -1; //
                carregarImagem("bloco.jpeg"); // Visual de piso normal
                break;
            case DISPARADOR_RIGHT: // Tipo de piso que marca local de personagem
                this.pisadasRestantes = -1; //
                carregarImagem("bloco.jpeg"); // Visual de piso normal
                break;
            case BURACO: // Já começa como buraco
                this.pisadasRestantes = 0; //
                this.prestesAQuebrar = false; 
                carregarImagem("blackTile.png"); //
                break;
        }
    }

    // Chamado quando a Ball PISA neste piso
    public void aoSerPisado() { //
        if (this.tipo == TiposPiso.BURACO || this.prestesAQuebrar) { //
            return;
        }

        if (this.tipoOriginal == TiposPiso.PISAVEL_DUPLO) { //
            if (pisadasRestantes == 2) { //
                pisadasRestantes--; // Agora é 1
                this.tipo = TiposPiso.PISAVEL_SIMPLES; // Muda o tipo para se comportar como simples
                carregarImagem("bloco.jpeg"); // Muda a imagem para a de um piso simples
                System.out.println("Piso Duplo agora é Simples (resta 1 pisada)"); //
            } else if (pisadasRestantes == 1) { // Era um PISAVEL_DUPLO, agora com 1 pisada restante (já tipo PISAVEL_SIMPLES)
                pisadasRestantes--; // Agora é 0
                this.prestesAQuebrar = true;
                // NÃO muda a imagem aqui, continua como "bloco.jpeg"
                System.out.println("Piso (originalmente Duplo) está PRESTES A VIRAR BURACO!");
            }
        } else if (this.tipo == TiposPiso.PISAVEL_SIMPLES) { //
            if (pisadasRestantes == 1) { //
                pisadasRestantes--; // Agora é 0
                this.prestesAQuebrar = true;
                // NÃO muda a imagem aqui, continua como "bloco.jpeg"
                System.out.println("Piso Simples está PRESTES A VIRAR BURACO!");
            }
        } else if (this.tipo == TiposPiso.PORTAL) { //
            System.out.println("Jogador pisou no PORTAL!"); //
        }
    }

    // Chamado quando a Ball SAI deste piso
    public void aoSairDoPiso() {
        if (this.prestesAQuebrar) {
            this.tipo = TiposPiso.BURACO; //
            carregarImagem("blackTile.png"); //
            this.prestesAQuebrar = false; 
            System.out.println("Piso virou BURACO ao sair!");
        }
    }

    public TiposPiso getTipo() { //
        return tipo;
    }

    public boolean ehObjetivo() { //
        return this.ehObjetivo;
    }

    public boolean ehTransponivel() { //
        return tipo != TiposPiso.BURACO; //
    }

    public ImageIcon getImagemAtual() { //
        return imagemAtual;
    }

    private void carregarImagem(String nomeImagem) { //
        if (nomeImagem == null || nomeImagem.isEmpty()) { //
            this.imagemAtual = null; //
            return; //
        }
        try {
            ImageIcon iconeOriginal = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + nomeImagem); //
            Image imgOriginal = iconeOriginal.getImage(); //
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB); //
            Graphics g = bi.createGraphics(); //
            g.drawImage(imgOriginal, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null); //
            g.dispose(); //
            this.imagemAtual = new ImageIcon(bi); //
        } catch (java.io.IOException ex) { //
            System.out.println("Erro ao carregar ou redimensionar imagem do piso: " + nomeImagem + " - " + ex.getMessage()); //
            this.imagemAtual = null; //
        }
    }
}