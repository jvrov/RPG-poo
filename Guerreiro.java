/**
 * Crie subclasses Guerreiro, Mago e Arqueiro.
 */
public class Guerreiro extends Personagem {

    // Construtor Padrão
    public Guerreiro() {
        super(); // Chama o construtor padrão do Pai
        this.nome = "Guerreiro Padrão";
        this.pontosVidaMax = 120;
        this.pontosVida = 120;
        this.ataque = 12;
        this.defesa = 8;
    }
    
    // Construtor principal
    public Guerreiro(String nome) {
        // super(nome, hp, atk, def)
        super(nome, 120, 12, 8);
    }

    // Construtor de Cópia
    public Guerreiro(Guerreiro original) {
        super(original); // Chama o construtor de cópia do Pai
    }
    @Override
    public void aplicarBonusDeNivel() {
        // Guerreiro ganha muita vida e defesa
        this.pontosVidaMax += 20;
        this.ataque += 2;
        this.defesa += 2;
    }
}