public class Arqueiro extends Personagem {
    public Arqueiro() { this("Arqueiro Padrão"); }
    public Arqueiro(String nome) { super(nome, 100, 24, 6, 30); } // 30 MP
    public Arqueiro(Arqueiro original) { super(original); }
    
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 15; this.ataque += 3; this.defesa += 1; this.mpMax += 8;
    }

    @Override
    public boolean usarHabilidadeEspecial(Personagem alvo) {
        if (gastarMp(15)) {
            System.out.println(this.nome + " dispara [CHUVA DE FLECHAS]!");
            int dano = this.ataque + 15; 
            System.out.println("Uma flecha acerta o ponto fraco!");
            alvo.receberDano(dano);
            return true;
        }
        return false;
    }
}