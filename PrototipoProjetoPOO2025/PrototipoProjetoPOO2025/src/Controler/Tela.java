
package Controler;

import Auxiliar.Consts; //
import Auxiliar.Desenho; //
import Modelo.Personagem;
import Modelo.Ball;      // 
import auxiliar.Posicao; //
import java.awt.FlowLayout; //
import java.awt.Graphics; //
import java.awt.Image; //
import java.awt.Toolkit; //
import java.awt.event.KeyEvent; //
import java.awt.event.KeyListener; //
import java.awt.event.MouseEvent; //
import java.awt.event.MouseListener; //
import java.io.File; //
import java.io.IOException; //
import java.util.ArrayList; //
import java.util.Timer; //
import java.util.TimerTask; //
import java.util.logging.Level; //
import java.util.logging.Logger; //

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Fase faseAtualObj; 
    private ControleDeJogo cj = new ControleDeJogo(); //
    private Graphics g2;
    private int cameraLinha = 0; //
    private int cameraColuna = 0; //

    public Tela() {
        Desenho.setCenario(this); //
        initComponents();
        this.addMouseListener(this); //
        this.addKeyListener(this);   //
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom); //

        this.faseAtualObj = new Fase(); 
        this.atualizaCamera(); 
    }

    // Método para dar acesso à bola para a câmera e input
    public Ball getBall() { 
        if (this.faseAtualObj == null) return null;
        return this.faseAtualObj.getBall();
    }
    
    public int getCameraLinha() { return cameraLinha; } //
    public int getCameraColuna() { return cameraColuna; } //

    public boolean ehPosicaoValida(Posicao p) { //
        if (this.faseAtualObj == null) return false; // Adiciona verificação
        return cj.ehPosicaoValida(this.faseAtualObj.getPersonagens(), p);
    }

    public void addPersonagem(Personagem umPersonagem) { //
        if (this.faseAtualObj == null) return; // Adiciona verificação
        this.faseAtualObj.addPersonagem(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) { //
        if (this.faseAtualObj == null) return; // Adiciona verificação
        this.faseAtualObj.removePersonagem(umPersonagem);
    }

    public Graphics getGraphicsBuffer() { return g2; } //

    public void paint(Graphics gOld) { //
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        
        // Desenha cenário de fundo (igual a antes)
        for (int i = 0; i < Consts.RES; i++) { //
            for (int j = 0; j < Consts.RES; j++) { //
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) { //
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "blackTile.png"); //
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE, //
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null); //
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex); //
                    }
                }
            }
        }
        

        if (this.faseAtualObj != null && !this.faseAtualObj.getPersonagens().isEmpty()) {
            this.cj.desenhaTudo(this.faseAtualObj.getPersonagens()); //
            this.cj.processaTudo(this.faseAtualObj.getPersonagens()); //
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    private void atualizaCamera() { //
        Ball currentBall = getBall(); // Usa o novo getter
        if (currentBall != null) { 
            int linha = currentBall.getPosicao().getLinha(); //
            int coluna = currentBall.getPosicao().getColuna(); //

            cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES)); //
            cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES)); //
        }
    }

    public void go() { //
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD); //
    }

    public void keyPressed(KeyEvent e) { //
        Ball currentBall = getBall();
        if (currentBall == null) return; 

        if (e.getKeyCode() == KeyEvent.VK_C) { //
            if (this.faseAtualObj != null) {
                 this.faseAtualObj = new Fase(); // Recarrega a fase padrão
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) { //
            currentBall.moveUp(); 
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) { //
            currentBall.moveDown(); 
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) { //
            currentBall.moveLeft(); 
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { //
            currentBall.moveRight(); 
        }
        this.atualizaCamera();
        this.setTitle("-> Cell: " + (currentBall.getPosicao().getColuna()) + ", " //
                + (currentBall.getPosicao().getLinha())); //
    }

    public void mousePressed(MouseEvent e) { //
        Ball currentBall = getBall();
        if (currentBall == null) return;

        int x = e.getX(); //
        int y = e.getY(); //

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE)); //

        int novaLinha = y / Consts.CELL_SIDE;
        int novaColuna = x / Consts.CELL_SIDE;
        currentBall.setPosicao(novaLinha, novaColuna); 

        repaint(); //
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}