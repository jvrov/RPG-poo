/**
 * Crie uma classe Inimigo, também derivada de Personagem.
 */
public class Inimigo extends Personagem {

    // --- NOVO ATRIBUTO ---
    private int xpRecompensa;

    // Construtor Padrão
    public Inimigo() {
        super();
        this.nome = "Rato";
        this.pontosVidaMax = 20;
        this.pontosVida = 20;
        this.ataque = 5;
        this.defesa = 2;
        this.xpRecompensa = 5; // XP Padrão
    }
    
    // Construtor principal ATUALIZADO
    public Inimigo(String nome, int hp, int atk, int def, int xp) {
        super(nome, hp, atk, def);
        this.xpRecompensa = xp;
    }

    // Construtor de Cópia ATUALIZADO
    public Inimigo(Inimigo original) {
        super(original);
        this.xpRecompensa = original.xpRecompensa;
    }

    // --- NOVO MÉTODO ---
    public int getXpRecompensa() {
        return this.xpRecompensa;
    }
    
    // Método abstrato obrigatório (Inimigos não sobem de nível)
    @Override
    public void aplicarBonusDeNivel() {
        // Inimigos não sobem de nível, então este método fica vazio.
    }
}