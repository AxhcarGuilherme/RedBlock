
package Controler;

import Modelo.Ball;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import Auxiliar.Consts; // Importar Consts
import Auxiliar.Desenho; // Importar Desenho
import Modelo.Piso; // Importar Piso
import Modelo.Personagem; // Importar Personagem


public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Fase faseAtualObj;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private int numeroDoMapaControlador = 1;

    // Adiciona uma flag para saber se é a primeira carga do jogo (mapa 1)
    private boolean primeiraCargaDoJogo = true;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        carregarFasePorNumeroControlador(true); // true para mostrar dica inicial
    }

    /**
     * Carrega ou recarrega a fase baseada no valor de numeroDoMapaControlador.
     * @param mostrarDicaInicial Se true, mostra a dica inicial da fase.
     */
    private void carregarFasePorNumeroControlador(boolean mostrarDicaInicial) {
        String nomeArquivo = "mapa" + this.numeroDoMapaControlador + ".json";
        System.out.println("Tela: Carregando fase do arquivo: " + nomeArquivo);

        int vidasParaManter = 5; // Valor padrão se não houver bola antiga (primeira carga)
        Ball bolaAntiga = null;
        if (this.faseAtualObj != null) { // Verifica se faseAtualObj existe antes de tentar pegar a bola
            bolaAntiga = this.faseAtualObj.getBall();
        }

        if (bolaAntiga != null && !primeiraCargaDoJogo) {
            // Se existe uma bola da fase anterior E não é a primeira carga do jogo,
            // mantém as vidas dela.
            vidasParaManter = bolaAntiga.getVidas();
        }
        // Se for a primeiraCargaDoJogo, vidasParaManter permanecerá 5 (ou o valor inicial da Ball).

        this.faseAtualObj = new Fase(nomeArquivo); // Cria a nova fase (e uma nova Ball com 5 vidas por padrão)

        Ball bolaNova = getBall(); // Pega a bola da fase recém-carregada
        if (bolaNova != null) {
            if (!primeiraCargaDoJogo) {
                // Se não for a primeira carga do jogo, restaura as vidas para o valor mantido.
                // Isso sobrescreve as 5 vidas padrão da nova Ball com as vidas da bola antiga.
                bolaNova.restaurarVidas(vidasParaManter);
            }
            // Se for a primeiraCargaDoJogo, a bolaNova já terá 5 vidas de seu construtor,
            // e vidasParaManter também será 5 (ou o valor do construtor da Ball se bolaAntiga for null).
            // Assim, a primeiraCargaDoJogo efetivamente usa as vidas padrão da Ball.

            this.setTitle("-> Cell: " + (bolaNova.getPosicao().getColuna()) + ", " + (bolaNova.getPosicao().getLinha()) + " - Vidas: " + bolaNova.getVidas());

            if (mostrarDicaInicial) {
                String dica = faseAtualObj.getDicaDaFase();
                if (dica != null && !dica.equals("Nenhuma dica disponível para esta fase.")) {
                    javax.swing.SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, dica, "Dica da Fase " + faseAtualObj.getIdDoMapaLidoDoJson(), JOptionPane.INFORMATION_MESSAGE)
                    );
                }
            }
        } else {
            this.setTitle("POO2023-1 - Skooter - Mapa " + this.numeroDoMapaControlador);
        }

        primeiraCargaDoJogo = false; // Após a primeira carga, esta flag se torna false.
        this.atualizaCamera();
        this.repaint();
    }

    public void avancarParaProximoMapa() {
        this.numeroDoMapaControlador++;
        System.out.println("Tela: Avançando para o mapa número " + this.numeroDoMapaControlador);
        carregarFasePorNumeroControlador(true); // Mostrar dica para a nova fase
    }

    public void reiniciarFaseAtual() {
        System.out.println("Tela: Reiniciando mapa " + this.numeroDoMapaControlador + ".");
        carregarFasePorNumeroControlador(true); // Mostrar dica ao reiniciar a fase
    }

    // ... (paint, keyPressed, e todos os outros métodos como estavam na sua última versão)
    // Garanta que o construtor de Ball (Ball.java) ainda inicializa `this.vidas = 5;`
    // Garanta que Ball.java tenha o método `public void restaurarVidas(int numeroDeVidas)`
    // O restante dos métodos da Tela (paint, keyPressed, getBall, etc.) permanece o mesmo
    // que na sua última versão funcional.

    @Override
    public void paint(Graphics gOld) {
        if (this.getBufferStrategy() == null) { 
            this.createBufferStrategy(2); 
            return;
        }
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        for (int i = 0; i < Auxiliar.Consts.RES; i++) {
            for (int j = 0; j < Auxiliar.Consts.RES; j++) {
                try {
                    Image newImage = Toolkit.getDefaultToolkit().getImage(
                            new java.io.File(".").getCanonicalPath() + Auxiliar.Consts.PATH + "blackTile.png");
                    g2.drawImage(newImage,
                            j * Auxiliar.Consts.CELL_SIDE, i * Auxiliar.Consts.CELL_SIDE,
                            Auxiliar.Consts.CELL_SIDE, Auxiliar.Consts.CELL_SIDE, null);
                } catch (IOException ex) {
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (this.faseAtualObj != null && this.faseAtualObj.getMapaDePisos() != null) {
            Piso[][] mapaPisos = this.faseAtualObj.getMapaDePisos();
            for (int i = 0; i < Auxiliar.Consts.RES; i++) { 
                for (int j = 0; j < Auxiliar.Consts.RES; j++) {
                    int mapaLinha = cameraLinha + i; 
                    int mapaColuna = cameraColuna + j; 

                    if (mapaLinha >= 0 && mapaLinha < Fase.TAMANHO_MAPA_LINHAS &&
                        mapaColuna >= 0 && mapaColuna < Fase.TAMANHO_MAPA_COLUNAS) {
                        Piso piso = mapaPisos[mapaLinha][mapaColuna];
                        if (piso != null && piso.getImagemAtual() != null) {
                            Desenho.desenhar(piso.getImagemAtual(), mapaColuna, mapaLinha);
                        }
                    }
                }
            }
        }

        if (this.faseAtualObj != null && this.faseAtualObj.getPersonagens() != null && !this.faseAtualObj.getPersonagens().isEmpty()) {
            this.cj.desenhaTudo(this.faseAtualObj.getPersonagens());
            this.cj.processaTudo(this.faseAtualObj.getPersonagens());
        }

        Ball bola = getBall();
        if (bola != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.YELLOW);
            g2.drawString("Vidas: " + bola.getVidas(), 10, 20); 
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        Ball currentBall = getBall();

        if (e.getKeyCode() == KeyEvent.VK_C) {
            System.out.println("Tela: Tecla 'C' pressionada, reiniciando fase atual (mapa " + this.numeroDoMapaControlador + ").");
            reiniciarFaseAtual(); 
            return; 
        } else if (e.getKeyCode() == KeyEvent.VK_P) { 
            if (currentBall != null && faseAtualObj != null) {
                if (currentBall.gastarVidaParaDica()) {
                    String dica = faseAtualObj.getDicaDaFase();
                    JOptionPane.showMessageDialog(this, dica, "Dica da Fase " + faseAtualObj.getIdDoMapaLidoDoJson() + " (custou 1 vida)", JOptionPane.INFORMATION_MESSAGE);
                    Ball bolaAposDica = getBall(); 
                    if (bolaAposDica != null) {
                         this.setTitle("-> Cell: " + (bolaAposDica.getPosicao().getColuna()) + ", " + (bolaAposDica.getPosicao().getLinha()) + " - Vidas: " + bolaAposDica.getVidas());
                    }
                    repaint(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Você não tem vidas suficientes para pedir uma dica!", "Sem Vidas", JOptionPane.WARNING_MESSAGE);
                }
            }
            return; 
        }

        if (currentBall == null) return;

        boolean moveu = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) moveu = currentBall.moveUp();
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) moveu = currentBall.moveDown();
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) moveu = currentBall.moveLeft();
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveu = currentBall.moveRight();

        if (moveu) { 
            this.atualizaCamera();
            Ball bolaAposMovimento = getBall();
             if (bolaAposMovimento != null) { 
                 this.setTitle("-> Cell: " + (bolaAposMovimento.getPosicao().getColuna()) + ", " + (bolaAposMovimento.getPosicao().getLinha()) + " - Vidas: " + bolaAposMovimento.getVidas());
             }
        }
    }
    
    public Ball getBall() { 
        if (this.faseAtualObj == null) return null;
        return this.faseAtualObj.getBall();
    }
    
    public int getCameraLinha() { return cameraLinha; } 
    public int getCameraColuna() { return cameraColuna; } 

    public boolean ehPosicaoValida(Auxiliar.Posicao p) { 
        if (this.faseAtualObj == null || p == null) return false; 
        return cj.ehPosicaoValida(this.faseAtualObj.getPersonagens(), p);
    }

    public void addPersonagem(Personagem umPersonagem) { 
        if (this.faseAtualObj == null) return; 
        this.faseAtualObj.addPersonagem(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) { 
        if (this.faseAtualObj == null) return; 
        this.faseAtualObj.removePersonagem(umPersonagem);
    }

    public Graphics getGraphicsBuffer() { return g2; } 

    private void atualizaCamera() { 
        Ball currentBall = getBall(); 
        if (currentBall != null && currentBall.getPosicao() != null) { 
            int linha = currentBall.getPosicao().getLinha(); 
            int coluna = currentBall.getPosicao().getColuna(); 

            cameraLinha = Math.max(0, Math.min(linha - Auxiliar.Consts.RES / 2, Auxiliar.Consts.MUNDO_ALTURA - Auxiliar.Consts.RES)); 
            cameraColuna = Math.max(0, Math.min(coluna - Auxiliar.Consts.RES / 2, Auxiliar.Consts.MUNDO_LARGURA - Auxiliar.Consts.RES)); 
        }
    }

    public void go() { 
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Auxiliar.Consts.PERIOD); 
    }

    public void mousePressed(MouseEvent e) { 
        Ball currentBall = getBall();
        if (currentBall == null) return;

        int x = e.getX() - getInsets().left;
        int y = e.getY() - getInsets().top;

        int colunaNoMundo = x / Auxiliar.Consts.CELL_SIDE + cameraColuna;
        int linhaNoMundo = y / Auxiliar.Consts.CELL_SIDE + cameraLinha;

        if (linhaNoMundo >= 0 && linhaNoMundo < Fase.TAMANHO_MAPA_LINHAS &&
            colunaNoMundo >= 0 && colunaNoMundo < Fase.TAMANHO_MAPA_COLUNAS) {
            currentBall.setPosicao(linhaNoMundo, colunaNoMundo); 
            Ball bolaAposClique = getBall();
            if (bolaAposClique != null) {
                 this.setTitle("-> Cell: " + (bolaAposClique.getPosicao().getColuna()) + ", " + (bolaAposClique.getPosicao().getLinha()) + " - Vidas: " + bolaAposClique.getVidas());
            }
        } else {
            System.out.println("Clique fora dos limites do mapa.");
        }
        this.repaint();
    }
    
    public Fase getFaseAtualObj() {
        return this.faseAtualObj;
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