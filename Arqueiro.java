public class Arqueiro extends Personagem {

    public Arqueiro() {
        this("Arqueiro Padrão");
    }
    
    public Arqueiro(String nome) {
        super(nome, 100, 24, 6);
    }

    public Arqueiro(Arqueiro original) {
        super(original);
    }
    
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 15;
        this.ataque += 3;
        this.defesa += 1;
    }
}