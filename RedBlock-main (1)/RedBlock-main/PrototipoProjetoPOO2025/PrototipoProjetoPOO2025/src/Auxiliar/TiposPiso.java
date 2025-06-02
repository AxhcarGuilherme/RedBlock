
package Auxiliar;

public enum TiposPiso {
    PISAVEL_SIMPLES,  // Desaparece após 1 pisada
    PISAVEL_DUPLO,   // Desaparece após 2 pisadas
    INVISIVEL,       // Aparência de Buraco e funcionalidade de piso simples
    OBJETIVO,        // Local para terminar Fase
    BURACO,          // Onde um piso desapareceu, intransponível
    PORTAL,
    PORTAL_SAIDA,         // Novo: Para portais
    PISO_SPAWN,      // Novo: Marca o local de nascimento do jogador (Ball)
    DISPARADOR_UP,       // Novo
    DISPARADOR_DOWN,     // Novo
    DISPARADOR_LEFT,     // Novo
    DISPARADOR_RIGHT       // Novo: Marca o local de um Disparador (o personagem)
}