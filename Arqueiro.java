
public class Arqueiro extends Personagem {


    public Arqueiro() {
        super();
        this.nome = "Arqueiro Padrão";
        this.pontosVidaMax = 100;
        this.pontosVida = 100;
        this.ataque = 14;
        this.defesa = 6;
    }
    
    public Arqueiro(String nome) {
        super(nome, 100, 24, 6);
    }

    // Ccopia
    public Arqueiro(Arqueiro original) {
        super(original);
    }
    @Override
    public void aplicarBonusDeNivel() {
        // up
        this.pontosVidaMax += 15;
        this.ataque += 3;
        this.defesa += 1;
    }
}