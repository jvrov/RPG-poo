public class Guerreiro extends Personagem {

    public Guerreiro() {
        this("Guerreiro Padrão");
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
        this.defesa += 3;
    }
}