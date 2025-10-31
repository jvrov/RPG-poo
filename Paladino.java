public class Paladino extends Personagem {

    public Paladino() {
        super(); 
        this.nome = "Paladino Padrão";
        this.pontosVidaMax = 150;
        this.pontosVida = 150;
        this.ataque = 14;
        this.defesa = 10;
    }
    
    public Paladino(String nome) {
        super(nome, 150, 14, 15);
    }

    public Paladino(Paladino original) {
        super(original); 
    }
    
    public void curaDivina() {
        int cura = (int) (this.pontosVidaMax * 0.25); 
        System.out.println(this.nome + " usa [Cura Divina]!");
        this.curar(cura);
    }
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 18;
        this.ataque += 5;
        this.defesa += 2;
    }
}