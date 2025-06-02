
package Auxiliar;

public enum TiposPiso {
    PISAVEL_SIMPLES,  // Desaparece após 1 pisada
    PISAVEL_DUPLO,   // Desaparece após 2 pisadas
    INVISIVEL,       // Aparencia de Buraco e funcionalidade de piso simples
    OBJETIVO,       // Local para terminar Fase
    BURACO           // Onde um piso desapareceu, intransponível
}