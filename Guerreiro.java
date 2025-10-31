public class Guerreiro extends Personagem {

  
    public Guerreiro() {
        super(); // Chama o construtor padrão do Pai
        this.nome = "Guerreiro Padrão";
        this.pontosVidaMax = 120;
        this.pontosVida = 120;
        this.ataque = 12;
        this.defesa = 8;
    }
    
    public Guerreiro(String nome) {
        super(nome, 120, 12, 10);
    }


    public Guerreiro(Guerreiro original) {
        super(original); 
    }
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 20;
        this.ataque += 2;
        this.defesa += 2;
    }
}