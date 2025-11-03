import java.util.Objects;

public abstract class Personagem {
    
    protected String nome;
    protected int pontosVida;
    protected int pontosVidaMax; 
    protected int ataque;
    protected int defesa;
    protected int nivel;
    protected Inventario inventario;
    
    protected int xp;
    protected int xpParaProximoNivel;

    public Personagem() {
        this("Ninguém", 10, 1, 1); 
    }

    public Personagem(String nome, int hp, int atk, int def) {
        this.nome = nome;
        this.pontosVidaMax = hp;
        this.pontosVida = hp;
        this.ataque = atk;
        this.defesa = def;
        this.nivel = 1;
        this.inventario = new Inventario();
        this.xp = 0;
        this.xpParaProximoNivel = 100;
    }

    public Personagem(Personagem original) {
        this.nome = original.nome;
        this.pontosVidaMax = original.pontosVidaMax;
        this.pontosVida = original.pontosVida;
        this.ataque = original.ataque;
        this.defesa = original.defesa;
        this.nivel = original.nivel;
        this.inventario = original.inventario.clone(); 
        this.xp = original.xp;
        this.xpParaProximoNivel = original.xpParaProximoNivel;
    }

    public void ganharXp(int xpGanhos) {
        this.xp += xpGanhos;
        System.out.println(this.nome + " ganhou " + xpGanhos + " de XP! (Total: " + this.xp + "/" + this.xpParaProximoNivel + ")");
        
        while (this.xp >= this.xpParaProximoNivel) {
            subirDeNivel();
        }
    }

    private void subirDeNivel() {
        System.out.println("----------------------------------------");
        System.out.println("    ¡¡¡ LEVEL UP !!!");
        System.out.println(this.nome + " alcançou o Nível " + (this.nivel + 1) + "!");
        
        int xpExcedente = this.xp - this.xpParaProximoNivel;
        this.nivel++;
        this.xp = xpExcedente;
        this.xpParaProximoNivel = (int) (this.xpParaProximoNivel * 1.5);

        aplicarBonusDeNivel();
        
        this.pontosVida = this.pontosVidaMax;

        System.out.println("Novos Atributos:");
        System.out.println(this.toString());
        System.out.println("XP para o próximo nível: " + this.xpParaProximoNivel);
        System.out.println("----------------------------------------");
    }

    public abstract void aplicarBonusDeNivel();

    public int calcularAtaque(int rolagemDado) {
        return this.ataque + rolagemDado;
    }

    public void receberDano(int dano) {
        int danoRecebido = Math.max(0, dano - this.defesa);
        this.pontosVida -= danoRecebido;
        System.out.printf("%s recebeu %d de dano! (HP: %d/%d)\n", 
            this.nome, danoRecebido, this.pontosVida, this.pontosVidaMax);

        if (!this.estaVivo()) {
            System.out.printf("%s foi derrotado!\n", this.nome);
        }
    }
    
    public void curar(int valorCura) {
        this.pontosVida += valorCura;
        if (this.pontosVida > this.pontosVidaMax) {
            this.pontosVida = this.pontosVidaMax;
        }
        System.out.printf("%s se curou! (HP: %d/%d)\n", 
            this.nome, this.pontosVida, this.pontosVidaMax);
    }

    public boolean estaVivo() {
        return this.pontosVida > 0;
    }

    public String usarItem(String nomeDoItem) {
        Item item = this.inventario.encontrarItem(nomeDoItem);
        
        if (item == null) {
            return "falha_nao_tem";
        }
        
        if (item.getQuantidade() <= 0) {
            return "falha_sem_qtd";
        }

        String efeito = item.getEfeito();

        switch (efeito) {
            case "cura":
                curar(20);
                item.usar(); 
                return "cura";
            case "cura_grande":
                curar(50);
                item.usar(); 
                return "cura_grande";
            
            case "mapa":
                System.out.println("Você consulta o " + item.getNome() + "...");
                return "mapa";
            case "clue":
            case "material":
            case "equipamento":
            case "artefato":
            case "lixo":
                System.out.println("\n[INSPECIONAR ITEM]");
                System.out.println(item.getNome() + ": " + item.getDescricao());
                System.out.println("(Não é possível 'usar' este item agora.)");
                return "clue"; 
            
            default:
                System.out.println("Não é possível usar '" + item.getNome() + "' agora.");
                return "falha_usar";
        }
    }
    
    public void mostrarInventario() {
        this.inventario.listarItens();
    }
    
    public void pegarLoot(Personagem inimigo) {
        System.out.println("\nVocê saqueia o " + inimigo.getNome() + "...");
        Inventario loot = inimigo.getInventario().clone(); 

        if (loot.getItens().isEmpty()) {
            System.out.println("...não havia nada de valor.");
            return;
        }
        
        for (Item itemDoLoot : loot.getItens()) {
            System.out.println("Você encontrou: " + itemDoLoot.toString());
            this.inventario.adicionarItem(itemDoLoot);
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] (Nvl %d) HP: %d/%d, ATK: %d, DEF: %d, XP: %d/%d",
            this.nome, this.nivel, this.pontosVida, this.pontosVidaMax, this.ataque, this.defesa, this.xp, this.xpParaProximoNivel);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Personagem that = (Personagem) obj;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
    
    public String getNome() { return nome; }
    public int getDefesa() { return defesa; }
    public Inventario getInventario() { return inventario; }
}