public class Paladino extends Personagem {

    public Paladino() {
        this("Paladino Padrão"); 
    }
    
    public Paladino(String nome) {
        super(nome, 150, 14, 15);
    }

    public Paladino(Paladino original) {
        super(original); 
    }
    
    //deixa quieto por hora
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