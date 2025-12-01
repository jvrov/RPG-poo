public class Guerreiro extends Personagem {
    public Guerreiro() { this("Guerreiro Padrão"); }
    public Guerreiro(String nome) { super(nome, 120, 12, 10, 20); } // 20 MP
    public Guerreiro(Guerreiro original) { super(original); }
    
    @Override
    public void aplicarBonusDeNivel() {
        this.pontosVidaMax += 20; this.ataque += 2; this.defesa += 3; this.mpMax += 5;
    }

    @Override
    public boolean usarHabilidadeEspecial(Personagem alvo) {
        if (gastarMp(10)) {
            System.out.println(this.nome + " usa [GOLPE DEVASTADOR]!");
            int danoExtra = this.ataque * 2; // Dano Dobrado
            System.out.println("O golpe ignora a defesa inimiga!");
            alvo.receberDano(danoExtra);
            return true;
        }
        return false;
    }
}