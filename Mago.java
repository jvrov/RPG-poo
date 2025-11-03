public class Mago extends Personagem {

    public Mago() {
        this("Mago Padrão"); 
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