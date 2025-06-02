// Na classe Controler.Fase
package Controler;

// ... (imports como antes, certifique-se que os de Auxiliar estão corretos)
import Modelo.Personagem;
import Modelo.Ball;
import Modelo.Disparador;
import Modelo.Piso;
import Auxiliar.TiposPiso;
import Auxiliar.Direction;
import Auxiliar.Consts;
import Auxiliar.Posicao; // Corrigido para Auxiliar.Posicao

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Fase {
    private ArrayList<Personagem> personagensDaFase;
    private Ball ball;
    private Piso[][] mapaDePisos;
    private Map<Posicao, Posicao> linksDePortais;
    private String dicaDaFase; // Novo atributo para a dica

    public static int TAMANHO_MAPA_LINHAS = 28;
    public static int TAMANHO_MAPA_COLUNAS = 28;

    private int idDoMapaLidoDoJson; // Armazena o 'mapa' ID lido do JSON

    public Fase(String nomeArquivoJson) {
        this.personagensDaFase = new ArrayList<>();
        this.mapaDePisos = new Piso[TAMANHO_MAPA_LINHAS][TAMANHO_MAPA_COLUNAS];
        this.linksDePortais = new HashMap<>();
        carregarFaseDeJson(nomeArquivoJson);
    }
    
     public String getDicaDaFase() {
        return this.dicaDaFase;
    }
    
    public int getIdDoMapaLidoDoJson() {
        return this.idDoMapaLidoDoJson;
    }

     private void carregarFaseDeJson(String nomeArquivoJson) {
        this.personagensDaFase.clear();
        this.linksDePortais.clear();
        this.ball = null;
        this.dicaDaFase = "Nenhuma dica disponível para esta fase."; // Padrão

        try {
            String caminhoCompleto = new java.io.File(".").getCanonicalPath() +
                                     Consts.PATH +
                                     nomeArquivoJson;
            
            String conteudoJson = new String(Files.readAllBytes(Paths.get(caminhoCompleto)));
            JSONObject jsonFase = new JSONObject(conteudoJson);

            // Lê o número do mapa DO ARQUIVO JSON
            this.idDoMapaLidoDoJson = jsonFase.optInt("mapa", 1); // Usando a variável de instância
            this.dicaDaFase = jsonFase.optString("dica", "Nenhuma dica disponível para esta fase."); // Carrega a dica


            // Lógica de carregamento de tiles, personagens, portais (como antes)
            // Certifique-se que a criação de Disparadores (UP, DOWN, etc.) e Portais está correta
            JSONObject tilesJson = jsonFase.getJSONObject("tiles");

            for (int i = 0; i < TAMANHO_MAPA_LINHAS; i++) {
                String chaveLinha = "linha_" + i;
                if (tilesJson.has(chaveLinha)) {
                    JSONArray jsonLinha = tilesJson.getJSONArray(chaveLinha);
                    for (int j = 0; j < Math.min(jsonLinha.length(), TAMANHO_MAPA_COLUNAS); j++) {
                        String tipoPisoStr = jsonLinha.getString(j);
                        TiposPiso tipoPisoEnum = TiposPiso.BURACO;
                        try {
                            tipoPisoEnum = TiposPiso.valueOf(tipoPisoStr.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.err.println("AVISO: Tipo de piso desconhecido '" + tipoPisoStr + "' no JSON em [" + i + "][" + j + "]. Usando BURACO.");
                        }
                        
                        mapaDePisos[i][j] = new Piso(tipoPisoEnum);

                        if (tipoPisoEnum == TiposPiso.PISO_SPAWN) {
                            if (this.ball != null) {
                                System.out.println("Aviso: Múltiplos PISO_SPAWN para Ball. Usando o último em (" + i + "," + j + ")");
                            }
                            this.ball = new Ball("bola.png");
                            this.ball.setPosicao(i, j);
                            this.addPersonagem(this.ball);
                        }
                        else if (tipoPisoEnum == TiposPiso.DISPARADOR_UP) {
                            Disparador d = new Disparador("caveira.png", Direction.UP);
                            d.setPosicao(i, j);
                            this.addPersonagem(d);
                        } else if (tipoPisoEnum == TiposPiso.DISPARADOR_DOWN) {
                            Disparador d = new Disparador("caveira.png", Direction.DOWN);
                            d.setPosicao(i, j);
                            this.addPersonagem(d);
                        } else if (tipoPisoEnum == TiposPiso.DISPARADOR_LEFT) {
                            Disparador d = new Disparador("caveira.png", Direction.LEFT);
                            d.setPosicao(i, j);
                            this.addPersonagem(d);
                        } else if (tipoPisoEnum == TiposPiso.DISPARADOR_RIGHT) {
                            Disparador d = new Disparador("caveira.png", Direction.RIGHT);
                            d.setPosicao(i, j);
                            this.addPersonagem(d);
                        }
                    }
                } else {
                    for (int j = 0; j < TAMANHO_MAPA_COLUNAS; j++) {
                        mapaDePisos[i][j] = new Piso(TiposPiso.BURACO);
                    }
                }
            }
            if (jsonFase.has("portais")) {
                JSONArray portaisArrayJson = jsonFase.getJSONArray("portais");
                for (int k = 0; k < portaisArrayJson.length(); k++) {
                    JSONObject portalLinkJson = portaisArrayJson.getJSONObject(k);
                    Posicao entrada = new Posicao(portalLinkJson.getInt("entradaLinha"), portalLinkJson.getInt("entradaColuna"));
                    Posicao saida = new Posicao(portalLinkJson.getInt("saidaLinha"), portalLinkJson.getInt("saidaColuna"));

                    if (mapaDePisos[entrada.getLinha()][entrada.getColuna()].getTipo() == TiposPiso.PORTAL &&
                        mapaDePisos[saida.getLinha()][saida.getColuna()].getTipo() == TiposPiso.PORTAL_SAIDA) {
                        this.linksDePortais.put(entrada, saida);
                    } else {
                        System.err.println("AVISO: Link de portal inválido no JSON.");
                    }
                }
            }

            if (this.ball == null) {
                System.err.println("AVISO CRÍTICO: Nenhum PISO_SPAWN encontrado no JSON: " + nomeArquivoJson + ". Adicionando Ball em (1,1).");
                mapaDePisos[1][1] = new Piso(TiposPiso.PISAVEL_SIMPLES);
                this.ball = new Ball("bola.png");
                this.ball.setPosicao(1, 1);
                this.addPersonagem(this.ball);
            }

        } catch (IOException | JSONException e) {
            System.err.println("ERRO CRÍTICO ao ler ou parsear JSON do nível: " + nomeArquivoJson);
            e.printStackTrace();
            preencherComFallback();
        }
    }

    private void preencherComFallback() {
        System.out.println("Preenchendo com mapa de fallback devido a erro.");
        for (int i = 0; i < TAMANHO_MAPA_LINHAS; i++) {
            for (int j = 0; j < TAMANHO_MAPA_COLUNAS; j++) {
                mapaDePisos[i][j] = new Piso(TiposPiso.BURACO);
            }
        }
        mapaDePisos[1][1] = new Piso(TiposPiso.PISAVEL_SIMPLES);
        mapaDePisos[TAMANHO_MAPA_LINHAS - 2][TAMANHO_MAPA_COLUNAS - 2] = new Piso(TiposPiso.OBJETIVO);

        if (this.ball == null) {
            this.ball = new Ball("bola.png");
            this.ball.setPosicao(1,1);
            this.addPersonagem(this.ball);
        }
    }
    
    // ... (getters e setters restantes: getPiso, getMapaDePisos, getPersonagens, getBall, addPersonagem, removePersonagem, getDestinoPortal)
    public Piso getPiso(int linha, int coluna) { 
        if (linha >= 0 && linha < TAMANHO_MAPA_LINHAS && coluna >= 0 && coluna < TAMANHO_MAPA_COLUNAS) { 
            if(mapaDePisos[linha][coluna] == null){  
                mapaDePisos[linha][coluna] = new Piso(TiposPiso.BURACO); 
            }
            return mapaDePisos[linha][coluna]; 
        }
        return new Piso(TiposPiso.BURACO); 
    }

    public Piso[][] getMapaDePisos() { 
        return mapaDePisos; 
    }

    public ArrayList<Personagem> getPersonagens() { 
        return this.personagensDaFase; 
    }

    public Ball getBall() { 
        return this.ball; 
    }

    public void addPersonagem(Personagem p) { 
        this.personagensDaFase.add(p); 
        if (p instanceof Ball && this.ball == null) { 
             this.ball = (Ball) p; 
        }
    }

    public void removePersonagem(Personagem p) { 
        this.personagensDaFase.remove(p); 
    }

     public Posicao getDestinoPortal(Posicao posicaoEntrada) {
        return this.linksDePortais.get(posicaoEntrada);
    }
}