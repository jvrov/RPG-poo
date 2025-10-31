import java.util.Objects;

/**
 * Classe Inimigo
 * Derivada de Personagem e funciona como uma "fábrica" de monstros.
 * Cada inimigo é criado via método estático criarInimigo().
 */
public class Inimigo extends Personagem {

    private int xpRecompensa;

    // Construtor Padrão (usado como fallback)
    private Inimigo() {
        super();
        this.nome = "Rato";
        this.pontosVidaMax = 20;
        this.pontosVida = 20;
        this.ataque = 5;
        this.defesa = 2;
        this.xpRecompensa = 5;
    }

    // Construtor principal
    private Inimigo(String nome, int hp, int atk, int def, int xp) {
        super(nome, hp, atk, def);
        this.xpRecompensa = xp;
    }

    // Construtor de cópia (para save/load)
    private Inimigo(Inimigo original) {
        super(original);
        this.xpRecompensa = original.xpRecompensa;
    }

    // ==========================================================
    // MÉTODO FÁBRICA DE INIMIGOS
    // ==========================================================
    public static Inimigo criarInimigo(String nomeDoInimigo) {
        Inimigo inimigo;

        switch (nomeDoInimigo) {

            // --------------------------------------------------
            // 🌿 INIMIGOS BÁSICOS DA FLORESTA
            // --------------------------------------------------
            case "Slime":
                inimigo = new Inimigo("Slime", 20, 6, 2, 10);
                inimigo.getInventario().adicionarItem(new Item("Gosma de Slime", "Material alquímico", "material", 3));
                break;

            case "Goblin Faminto":
                inimigo = new Inimigo("Goblin Faminto", 40, 9, 5, 25);
                inimigo.getInventario().adicionarItem(new Item("Pedaço de Tecido", "Tem o brasão do seu grupo de mercenários!", "clue", 1));
                break;

            case "Lobo das Sombras":
                inimigo = new Inimigo("Lobo das Sombras", 35, 10, 4, 20);
                inimigo.getInventario().adicionarItem(new Item("Presa Sombria", "Brilha sob a luz da lua. Pode ser usada em rituais.", "material", 1));
                break;

            case "Cogumelo Venenoso Vivo":
                inimigo = new Inimigo("Cogumelo Venenoso Vivo", 25, 8, 2, 15);
                inimigo.getInventario().adicionarItem(new Item("Esporo Letal", "Ingrediente de poções venenosas.", "material", 2));
                break;

            case "Serpente de Ervaluna":
                inimigo = new Inimigo("Serpente de Ervaluna", 70, 15, 5, 60);
                inimigo.getInventario().adicionarItem(new Item("Veneno de Serpente", "Usado em armas envenenadas.", "material", 1));
                break;

            // --------------------------------------------------
            // 🪨 INIMIGOS DAS RUÍNAS E CAVERNAS
            // --------------------------------------------------
            case "Ogro da Montanha":
                inimigo = new Inimigo("Ogro da Montanha", 100, 14, 8, 100);
                inimigo.getInventario().adicionarItem(new Item("Clava de Ogro", "Um porrete gigante", "lixo", 1));
                break;

            case "Troll da Caverna":
                inimigo = new Inimigo("Troll da Caverna", 80, 12, 6, 75);
                inimigo.getInventario().adicionarItem(new Item("Fivela de Cinto", "É a fivela do Capitão do seu grupo! Ele esteve aqui.", "clue", 1));
                break;

            case "Besouro Blindado":
                inimigo = new Inimigo("Besouro Blindado", 60, 11, 10, 35);
                inimigo.getInventario().adicionarItem(new Item("Casco Metálico", "Carapaça dura usada em forjas mágicas.", "material", 1));
                break;

            case "Guardião de Pedra":
                inimigo = new Inimigo("Guardião de Pedra", 120, 10, 18, 120);
                inimigo.getInventario().adicionarItem(new Item("Runa do Guardião", "A runa brilha com uma luz que aponta para o Coração da Floresta.", "clue", 1));
                break;

            // --------------------------------------------------
            // 💀 INIMIGOS ESPIRITUAIS / CORROMPIDOS
            // --------------------------------------------------
            case "Espectro Errante":
                inimigo = new Inimigo("Espectro Errante", 45, 13, 3, 40);
                inimigo.getInventario().adicionarItem(new Item("Essência Espiritual", "Poder mágico condensado.", "material", 1));
                break;

            case "Espírito Corrompido da Floresta":
                inimigo = new Inimigo("Espírito Corrompido da Floresta", 200, 18, 10, 500);
                inimigo.getInventario().adicionarItem(new Item("Amuleto de Ervaluna", "Um amuleto que Elara carregava. Pulsa fracamente.", "clue", 1));
                break;

            case "Olho do Bosque":
                inimigo = new Inimigo("Olho do Bosque", 55, 14, 4, 50);
                inimigo.getInventario().adicionarItem(new Item("Iris Cristalina", "Brilha como uma joia viva.", "material", 1));
                break;

            // --------------------------------------------------
            // 🐺 CHEFES INTERMEDIÁRIOS E AVANÇADOS
            // --------------------------------------------------
            case "Alfa Lupino":
                inimigo = new Inimigo("Alfa Lupino", 120, 17, 10, 180);
                inimigo.getInventario().adicionarItem(new Item("Pele do Alfa", "Item raro para armaduras leves.", "material", 1));
                inimigo.getInventario().adicionarItem(new Item("Amuleto de Presas", "Aumenta o ataque em 2 quando equipado.", "equipamento", 1));
                break;

            case "Fênix Crepuscular":
                inimigo = new Inimigo("Fênix Crepuscular", 150, 18, 12, 250);
                inimigo.getInventario().adicionarItem(new Item("Cinza Etérea", "Reage a fogo e cura 20 HP ao ser queimada.", "cura", 1));
                break;

            // --------------------------------------------------
            // 🐉 CHEFES FINAIS / SECRETOS
            // --------------------------------------------------
            case "Dragão Vermelho Jovem":
                inimigo = new Inimigo("Dragão Vermelho Jovem", 160, 16, 12, 300);
                inimigo.getInventario().adicionarItem(new Item("Poção de Cura Grande", "Cura 50 HP", "cura_grande", 1));
                inimigo.getInventario().adicionarItem(new Item("Escama de Dragão", "Material raro de forja", "material", 1));
                break;

            case "Ser Caótico de Ervaluna":
                inimigo = new Inimigo("Ser Caótico de Ervaluna", 500, 22, 18, 1500);
                inimigo.getInventario().adicionarItem(new Item("Coração Vivo da Floresta", "Fonte do poder ancestral. Irradia energia.", "artefato", 1));
                break;

            case "A Besta Manticora":
                inimigo = new Inimigo("A Besta Manticora", 350, 20, 14, 1000);
                break;

            default:
                inimigo = new Inimigo(); // Usa o inimigo padrão "Rato"
                break;
        }

        return inimigo;
    }

    // ==========================================================
    // MÉTODOS AUXILIARES
    // ==========================================================
    public int getXpRecompensa() {
        return this.xpRecompensa;
    }
    
    // --- NOVO MÉTODO SETTER ---
    /**
     * Permite que a classe Jogo altere a recompensa de XP (para escalar o chefe secreto).
     */
    public void setXpRecompensa(int xp) {
        this.xpRecompensa = xp;
    }

    @Override
    public void aplicarBonusDeNivel() {
        // Inimigos não sobem de nível
    }
}