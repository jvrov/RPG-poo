
public class Mago extends Personagem {

    public Mago() {
        super();
        this.nome = "Mago Padrão";
        this.pontosVidaMax = 80;
        this.pontosVida = 80;
        this.ataque = 15;
        this.defesa = 5;
    }

    public Mago(String nome) {
        super(nome, 80, 15, 5);
    }

    public Mago(Mago original) {
        super(original);
    }
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 10;
        this.ataque += 3;
        this.defesa += 1;
    }
}