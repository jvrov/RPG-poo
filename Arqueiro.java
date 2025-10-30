/**
 * Crie subclasses Guerreiro, Mago e Arqueiro.
 */
public class Arqueiro extends Personagem {

    // Construtor Padrão
    public Arqueiro() {
        super();
        this.nome = "Arqueiro Padrão";
        this.pontosVidaMax = 100;
        this.pontosVida = 100;
        this.ataque = 14;
        this.defesa = 6;
    }
    
    // Construtor principal
    public Arqueiro(String nome) {
        // super(nome, hp, atk, def)
        super(nome, 100, 14, 6);
    }

    // Construtor de Cópia
    public Arqueiro(Arqueiro original) {
        super(original);
    }
    @Override
    public void aplicarBonusDeNivel() {
        // Arqueiro é balanceado, focado em ataque
        this.pontosVidaMax += 15;
        this.ataque += 3;
        this.defesa += 1;
    }
}