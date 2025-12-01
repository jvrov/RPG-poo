public class Mago extends Personagem {
    public Mago() { this("Mago Padrão"); }
    public Mago(String nome) { super(nome, 80, 15, 5, 50); } // 50 MP (Muito Mana)
    public Mago(Mago original) { super(original); }
    
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 10; this.ataque += 3; this.defesa += 1; this.mpMax += 15;
    }

    @Override
    public boolean usarHabilidadeEspecial(Personagem alvo) {
        if (gastarMp(20)) {
            System.out.println(this.nome + " conjura [BOLA DE FOGO]!");
            int danoMagico = 40 + (this.nivel * 5); // Dano alto fixo + scaling
            alvo.receberDano(danoMagico);
            return true;
        }
        return false;
    }
}