public class Paladino extends Personagem {
    public Paladino() { this("Paladino Padrão"); }
    public Paladino(String nome) { super(nome, 150, 14, 15, 25); } // 25 MP
    public Paladino(Paladino original) { super(original); }
    
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 18; this.ataque += 5; this.defesa += 2; this.mpMax += 5;
    }

    @Override
    public boolean usarHabilidadeEspecial(Personagem alvo) {
        if (gastarMp(15)) {
            System.out.println(this.nome + " invoca [LUZ SAGRADA]!");
            int cura = 30 + (this.nivel * 2);
            int dano = this.ataque;
            System.out.println("Você se cura em " + cura + " HP e ataca!");
            this.curar(cura);
            alvo.receberDano(dano);
            return true;
        }
        return false;
    }
}