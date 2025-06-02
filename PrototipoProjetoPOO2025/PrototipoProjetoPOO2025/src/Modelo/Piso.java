
package Modelo;

import Auxiliar.TiposPiso;
import Auxiliar.Consts;
import java.io.Serializable;
import javax.swing.ImageIcon; //
import java.io.IOException; //
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Piso implements Serializable {
    private TiposPiso tipo;
    private TiposPiso tipoOriginal; // Para saber se era originalmente PISAVEL_DUPLO
    private int pisadasRestantes;
    private boolean ehObjetivo;
    private ImageIcon imagemAtual;

    public Piso(TiposPiso tipoInicial) {
        this.tipo = tipoInicial;
        this.tipoOriginal = tipoInicial; // Guarda o tipo original
        this.ehObjetivo = (tipoInicial == TiposPiso.OBJETIVO);

        switch (tipoInicial) {
            case PISAVEL_SIMPLES:
                this.pisadasRestantes = 1;
                carregarImagem("bloco.jpeg"); 
                break;
            case PISAVEL_DUPLO:
                this.pisadasRestantes = 2;
                carregarImagem("blocoDuplo.jpeg"); 
                break;
            case INVISIVEL:
                this.pisadasRestantes = -1; 
                carregarImagem("blackTile.png"); 
                break;
            case OBJETIVO:
                this.pisadasRestantes = -1; 
                carregarImagem("piso_objetivo.png"); // Colocar piso de finalizaçõ da fase
                break;
            case BURACO:
                this.pisadasRestantes = 0;
                carregarImagem("blackTile.png"); 
                break;
        }
    }
    
    public boolean aoSerPisado() {
        boolean virouBuraco = false;
        if (this.tipo == TiposPiso.PISAVEL_SIMPLES || this.tipo == TiposPiso.PISAVEL_DUPLO) {
            if (pisadasRestantes > 0) {
                pisadasRestantes--;
                
                if (pisadasRestantes == 0) {
                    this.tipo = TiposPiso.BURACO; 
                    carregarImagem("blackTile.png"); 
                    System.out.println("Piso (" + tipoOriginal + ") virou buraco!");
                    virouBuraco = true; 
                } else if (this.tipoOriginal == TiposPiso.PISAVEL_DUPLO && pisadasRestantes == 1) {
                    this.tipo = TiposPiso.PISAVEL_SIMPLES;
                    carregarImagem("bloco.jpeg");
                    System.out.println("Piso Duplo agora parece Simples (resta 1 pisada)");
                }
            }
        }
        return virouBuraco; 
    }

    public TiposPiso getTipo() {
        return tipo;
    }
        
    public boolean ehObjetivo() {
        return this.ehObjetivo;
    }

    public boolean ehTransponivel() {
        return tipo != TiposPiso.BURACO; 
    }

    public ImageIcon getImagemAtual() {
        return imagemAtual;
    }

    private void carregarImagem(String nomeImagem) {
        if (nomeImagem == null || nomeImagem.isEmpty()) {
            this.imagemAtual = null;
            return;
        }
        try {
            this.imagemAtual = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + nomeImagem);
            Image img = imagemAtual.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            imagemAtual = new ImageIcon(bi);
            g.dispose();
        } catch (java.io.IOException ex) { //
            System.out.println("Erro ao carregar imagem do piso: " + nomeImagem + " - " + ex.getMessage());
            this.imagemAtual = null;
        }
    }
}