/**
 * Crie subclasses Guerreiro, Mago e Arqueiro.
 */
public class Mago extends Personagem {

    // Construtor Padrão
    public Mago() {
        super();
        this.nome = "Mago Padrão";
        this.pontosVidaMax = 80;
        this.pontosVida = 80;
        this.ataque = 15;
        this.defesa = 5;
    }

    // Construtor principal
    public Mago(String nome) {
        // super(nome, hp, atk, def)
        super(nome, 80, 15, 5);
    }

    // Construtor de Cópia
    public Mago(Mago original) {
        super(original);
    }
    @Override
    public void aplicarBonusDeNivel() {
        // Mago ganha pouco HP, mas muito ataque
        this.pontosVidaMax += 10;
        this.ataque += 3;
        this.defesa += 1;
    }
}