/**
 * Subclasse Paladino (mais forte), conforme solicitado.
 */
public class Paladino extends Personagem {

    // Construtor Padrão
    public Paladino() {
        super(); 
        this.nome = "Paladino Padrão";
        this.pontosVidaMax = 150;
        this.pontosVida = 150;
        this.ataque = 14;
        this.defesa = 10;
    }
    
    // Construtor principal
    public Paladino(String nome) {
        // super(nome, hp, atk, def)
        super(nome, 150, 14, 10);
    }

    // Construtor de Cópia
    public Paladino(Paladino original) {
        super(original); 
    }
    
    // Habilidade especial (exemplo)
    public void curaDivina() {
        int cura = (int) (this.pontosVidaMax * 0.25); // Cura 25% da vida max
        System.out.println(this.nome + " usa [Cura Divina]!");
        this.curar(cura);
    }
    @Override
    public void aplicarBonusDeNivel() {
        // Paladino é um tanque híbrido
        this.pontosVidaMax += 18;
        this.ataque += 5;
        this.defesa += 2;
    }
}