import java.util.Random;
import java.util.Scanner;

/**
 * A classe Jogo deve conter o loop principal do RPG.
 */
public class Jogo {
    
    private static Personagem jogador;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random dado = new Random(); 
    private static Personagem savePoint; 
    
    // --- MUDANÇAS DE HISTÓRIA E CONTROLE ---
    private static boolean jogoAtivo = true; // Controla o fim do jogo
    
    /**
     * Controla o progresso da história.
     * 0 = Acloreira
     * 1 = Trilha das Bestas
     * 2 = Ruínas Antigas
     * 3 = Coração da Floresta (Chefe 1)
     * 4 = Santuário Antigo (Chefe Final)
     */
    private static int localizacaoAtual = 0;
    private static String nomeLocalizacao = "Acloreira";

    public static void main(String[] args) {
        iniciarJogo();
        loopPrincipal();
        scanner.close();
    }

    /**
     * Cria o personagem e introduz a história.
     */
    private static void iniciarJogo() {
        System.out.println("=========================================");
        System.out.println(" REINO DAS BESTAS: O ECO DA FLORESTA PERDIDA ");
        System.out.println("=========================================");
        
        System.out.print("Digite o nome do seu aventureiro: ");
        String nome = scanner.nextLine();
        
        System.out.println("\nEscolha sua classe:");
        System.out.println("[1] Guerreiro"); 
        System.out.println("[2] Mago"); 
        System.out.println("[3] Arqueiro"); 
        System.out.println("[4] Paladino");
        
        jogador = switch (scanner.nextLine()) {
            case "1" -> new Guerreiro(nome);
            case "2" -> new Mago(nome);
            case "3" -> new Arqueiro(nome);
            case "4" -> new Paladino(nome);
            default -> {
                System.out.println("Escolha inválida. Você será um Guerreiro.");
                yield new Guerreiro(nome);
            }
        };
        
        jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 2));
        System.out.println("\nPersonagem criado!");
        System.out.println(jogador.toString());
        
        System.out.println("\n...Sua cabeça dói.");
        System.out.println("Após dias viajando com seu grupo de mercenários pela fronteira do Reino das Bestas, algo deu errado.");
        System.out.println("Uma tempestade sobrenatural. Relâmpagos verdes. Um rugido nas trevas.");
        System.out.println("No caos, você foi separado do grupo e caiu ribanceira abaixo...");
        System.out.println("\nAgora, sozinho e ferido, você acorda na Floresta de Ervaluna.");
        System.out.println("Sua missão: Sobreviver. Encontrar seu grupo. E sair vivo daqui.");
        
        criarSavePoint();
    }

    /**
     * O loop principal do jogo com as ações do jogador.
     */
    private static void loopPrincipal() {
        // ATUALIZADO: usa 'jogoAtivo'
        while (jogoAtivo && jogador.estaVivo()) {
            
            // Atualiza o nome do local
            switch(localizacaoAtual) {
                case 0: nomeLocalizacao = "Acloreira"; break;
                case 1: nomeLocalizacao = "Trilha das Bestas"; break;
                case 2: nomeLocalizacao = "Ruínas Antigas"; break;
                case 3: nomeLocalizacao = "Coração da Floresta"; break;
                case 4: nomeLocalizacao = "Santuário Antigo"; break; // NOVO LOCAL
            }
            
            System.out.println("\n--- O QUE VOCÊ FAZ? (Local: " + nomeLocalizacao + ") ---");
            System.out.println("[1] Explorar/Avançar"); 
            System.out.println("[2] Usar item do inventário"); 
            System.out.println("[3] Ver status do personagem");
            System.out.println("[4] Carregar último save point");
            System.out.println("[5] Sair do Jogo");
            System.out.print("Sua escolha: ");
            
            String escolha = scanner.nextLine();
            
            switch (escolha) {
                case "1":
                    explorar();
                    break;
                case "2":
                    usarItem();
                    break;
                case "3":
                    System.out.println(jogador.toString());
                    jogador.mostrarInventario();
                    break;
                case "4":
                    carregarSavePoint();
                    break;
                case "5":
                    System.out.println("Obrigado por jogar!");
                    jogoAtivo = false; // ATUALIZADO
                    break;
                default:
                    System.out.println("Comando inválido.");
                    break;
            }
        }
        
        if (!jogador.estaVivo()) {
            System.out.println("--- FIM DE JOGO ---");
        }
    }

    /**
     * Direciona a exploração para o método da localização atual.
     */
    private static void explorar() {
        switch(localizacaoAtual) {
            case 0:
                explorarAcloreira();
                break;
            case 1:
                explorarTrilha();
                break;
            case 2:
                explorarRuinas();
                break;
            case 3:
                explorarCoracaoDaFloresta(); // NOVO
                break;
            case 4:
                explorarSantuario(); // NOVO
                break;
        }
    }
    
    // --- MÉTODOS DE EXPLORAÇÃO ATUALIZADOS COM XP ---

    private static void explorarAcloreira() {
        System.out.println("\nVocê explora a Acloreira. As árvores sussurram...");
        int rolagem = rolarDado(100);
        Inimigo inimigo = null;

        if (rolagem <= 20) {
            System.out.println("Você pisa em uma placa de pressão... uma armadilha!");
            jogador.receberDano(10);
        } else if (rolagem <= 50) {
            System.out.println("Um Slime verde-ácido desliza em sua direção!");
            inimigo = new Inimigo("Slime", 20, 6, 2, 10); // XP Adicionado
        } else if (rolagem <= 80) {
             System.out.println("Você encontra um baú de madeira antigo!");
             jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 1));
        } else {
            System.out.println("Um Goblin Faminto, com olhos vermelhos, salta das sombras!");
            inimigo = new Inimigo("Goblin Faminto", 40, 9, 5, 25); // XP Adicionado
            inimigo.getInventario().adicionarItem(new Item("Pedaço de Tecido", "Tem o brasão do seu grupo de mercenários!", "clue", 1));
        }

        if (inimigo != null) {
            batalhar(inimigo);
        }
    }
    
    private static void explorarTrilha() {
        System.out.println("\nVocê segue a trilha de pegadas. As sombras parecem se mover...");
        int rolagem = rolarDado(100);
        Inimigo inimigo = null;

        if (rolagem <= 30) {
            System.out.println("Você encontra uma mochila abandonada. Parece que foi rasgada por garras.");
            jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 2));
        } else if (rolagem <= 70) {
            System.out.println("O chão treme! Um Ogro da Montanha bloqueia seu caminho!");
            inimigo = new Inimigo("Ogro da Montanha", 100, 14, 8, 100); // XP Adicionado
            inimigo.getInventario().adicionarItem(new Item("Clava de Ogro", "Um porrete gigante", "lixo", 1));
        } else {
            System.out.println("Um Troll da Caverna faminto, com pele verde e grossa, te fareja!");
            inimigo = new Inimigo("Troll da Caverna", 80, 12, 6, 75); // XP Adicionado
            inimigo.getInventario().adicionarItem(new Item("Fivela de Cinto", "É a fivela do Capitão do seu grupo! Ele esteve aqui.", "clue", 1));
        }
        
        if (inimigo != null) {
            batalhar(inimigo);
        }
    }
    
    private static void explorarRuinas() {
        System.out.println("\nA trilha termina em ruínas antigas, engolidas pela mata.");
        int rolagem = rolarDado(100);
        Inimigo inimigo = null;

        if (rolagem <= 3) { 
            System.out.println("!!! PERIGO !!!");
            System.out.println("Um enorme DRAGÃO VERMELHO JOVEM pousa nas ruínas, rugindo!");
            inimigo = new Inimigo("Dragão Vermelho Jovem", 160, 16, 12, 300); // XP Adicionado
            inimigo.getInventario().adicionarItem(new Item("Poção de Cura Grande", "Cura 50 HP", "cura_grande", 1));
            inimigo.getInventario().adicionarItem(new Item("Escama de Dragão", "Material raro de forja", "material", 1));
        
        } else if (rolagem <= 50) {
            System.out.println("Um Ogro da Montanha está guardando um totem antigo!");
            inimigo = new Inimigo("Ogro da Montanha", 100, 14, 8, 100); // XP Adicionado
            inimigo.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 1));

        } else {
            System.out.println("Um espírito da floresta aparece. Ele oferece ajuda...");
            System.out.println("'Em troca de uma poção de cura, eu o guiarei ao Coração da Floresta.'");
            System.out.println("Você aceita? [1] Sim [2] Não");
            
            Item pocao = jogador.getInventario().encontrarItem("Poção de Cura");
            
            if (scanner.nextLine().equals("1") && pocao != null && pocao.getQuantidade() > 0) {
                System.out.println("Você entrega a poção. O espírito aponta para uma passagem secreta.");
                jogador.getInventario().removerItem(pocao, 1);
                localizacaoAtual = 3; // Avança para o Coração
            } else {
                System.out.println("Você recusa ou não tem a poção. O espírito desaparece.");
            }
        }
        
        if (inimigo != null) {
            batalhar(inimigo);
        }
    }

    // --- NOVOS MÉTODOS DE HISTÓRIA ---

    /**
     * Eventos do Local 3: Coração da Floresta (Chefe 1)
     */
    private static void explorarCoracaoDaFloresta() {
        System.out.println("\nO Coração da Floresta pulsa com uma energia estranha e sombria.");
        System.out.println("As árvores se abrem para revelar uma clareira.");
        System.out.println("Você vê uma figura encapuzada... não é um monstro. É ELARA, a rastreadora do seu grupo!");
        System.out.println("'...corra...', ela sussurra, antes de seus olhos ficarem brancos.");
        System.out.println("Atrás dela, uma criatura feita de vinhas e sombras se ergue... O 'Espirito da Floresta'.");
        
        Inimigo chefe1 = new Inimigo("Espírito Corrompido da Floresta", 200, 18, 10, 500);
        chefe1.getInventario().adicionarItem(new Item("Amuleto de Ervaluna", "Um amuleto que Elara carregava. Pulsa fracamente.", "clue", 1));
        
        batalhar(chefe1);
    }
    
    /**
     * Eventos do Local 4: Santuário Antigo (Chefe Final)
     */
    private static void explorarSantuario() {
        System.out.println("\nVocê usa o amuleto para abrir caminho até o Santuário Antigo.");
        System.out.println("É um lugar de silêncio mortal. No centro, você vê os corpos dos seus companheiros mercenários.");
        System.out.println("Sobre eles, se alimentando da energia da tempestade... a criatura que os caçou.");
        System.out.println("Uma Besta Manticora, com relâmpagos verdes crepitando em suas asas.");
        System.out.println("'TOLO', ruge a besta. 'A floresta os marcou. Agora, ela marcará você!'");

        Inimigo finalBoss = new Inimigo("A Besta Manticora", 350, 20, 14, 1000);
        batalhar(finalBoss);
    }
    
    /**
     * O método de batalha pedido no trabalho.
     * ATUALIZADO: Concede XP e checa a progressão da história.
     */
    private static void batalhar(Inimigo inimigo) {
        System.out.println("--- BATALHA INICIADA: " + jogador.getNome() + " vs " + inimigo.getNome() + " ---");
        
        while (jogador.estaVivo() && inimigo.estaVivo() && jogoAtivo) { 
            // Turno do Jogador
            System.out.println(jogador.toString());
            System.out.println(inimigo.toString());
            System.out.println("\nTurno do Jogador: [1] Atacar [2] Usar Item [3] Tentar Fugir");
            String acao = scanner.nextLine();
            
            boolean turnoInimigo = true;
            
            if (acao.equals("1")) {
                int rolagem = rolarDado(20); 
                System.out.println("Você rolou um " + rolagem);
                int ataqueTotal = jogador.calcularAtaque(rolagem); 
                
                if (ataqueTotal > inimigo.getDefesa()) { 
                    System.out.println("Acerto!");
                    inimigo.receberDano(ataqueTotal);
                } else {
                    System.out.println("Você errou! O ataque ( " + ataqueTotal + " ) não superou a defesa ( " + inimigo.getDefesa() + " ).");
                }
                
            } else if (acao.equals("2")) {
                usarItem(); 
                turnoInimigo = false; 
                
            } else if (acao.equals("3")) {
                int rolagemFuga = rolarDado(10); 
                System.out.println("Você rolou " + rolagemFuga + " para fugir...");
                if (rolagemFuga > 5) { 
                    System.out.println("Você conseguiu fugir!");
                    break; 
                } else {
                    System.out.println("A fuga falhou!");
                }
            } else {
                System.out.println("Ação inválida, você perdeu o turno.");
            }
            
            // Turno do Inimigo
            if (inimigo.estaVivo() && turnoInimigo) {
                System.out.println("Turno do " + inimigo.getNome() + "...");
                int rolagem = rolarDado(20); 
                int ataqueTotal = inimigo.calcularAtaque(rolagem); 
                
                if (ataqueTotal > jogador.getDefesa()) { 
                    System.out.println("O inimigo acertou!");
                    jogador.receberDano(ataqueTotal);
                } else {
                    System.out.println("O inimigo errou!");
                }
            }
        }
        
        // Fim da batalha
        if (jogador.estaVivo() && !inimigo.estaVivo()) {
            System.out.println("Você venceu a batalha!");
            
            // --- LÓGICA DE XP ---
            int xpGanhos = inimigo.getXpRecompensa();
            jogador.ganharXp(xpGanhos);

            jogador.pegarLoot(inimigo);
            
            // --- LÓGICA DE PROGRESSÃO DA HISTÓRIA ---
            if (inimigo.getNome().equals("Goblin Faminto") && localizacaoAtual == 0) {
                System.out.println("\n[HISTÓRIA] Ao saquear o Goblin, você encontra um pedaço de tecido com o brasão do seu grupo!");
                System.out.println("[HISTÓRIA] Você agora vê uma trilha de pegadas que antes estava escondida!");
                localizacaoAtual = 1; // Avança para a Trilha
            }
            if (inimigo.getNome().equals("Troll da Caverna") && localizacaoAtual == 1) {
                System.out.println("\n[HISTÓRIA] Em meio aos tesouros nojentos do Troll, você acha a fivela do cinto do seu capitão!");
                System.out.println("[HISTÓRIA] A trilha leva a antigas ruínas.");
                localizacaoAtual = 2; // Avança para as Ruínas
            }
            if (inimigo.getNome().equals("Espírito Corrompido da Floresta") && localizacaoAtual == 3) {
                System.out.println("\n[HISTÓRIA] Com a derrota da criatura, Elara está livre, mas exausta.");
                System.out.println("[HISTÓRIA] 'O resto do grupo... eles foram para o Santuário Antigo... Cuidado. O rugido... o rugido que nos atacou... está lá.'");
                localizacaoAtual = 4; // Avança para o Santuário
            }
            if (inimigo.getNome().equals("A Besta Manticora")) {
                System.out.println("\n\n--- FIM DA JORNADA ---");
                System.out.println("Com a Manticora morta, um silêncio cai sobre Ervaluna.");
                System.out.println("Você sobreviveu. Você vingou seu grupo. Mas a floresta... ela ainda te observa.");
                System.out.println("...Obrigado por jogar...");
                jogoAtivo = false; // Termina o jogo
            }
            
            // Só salva se o jogo não tiver terminado
            if(jogoAtivo) {
                criarSavePoint(); 
            }

        } else if (!jogador.estaVivo()) {
            System.out.println("Você foi derrotado na batalha...");
        }
    }
    
    private static void usarItem() {
        if (jogador.getInventario().getItens().isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }
        
        System.out.println("Qual item você quer usar?");
        jogador.mostrarInventario(); // Agora mostra a lista numerada
        System.out.print("Digite o NÚMERO do item (ou 0 para cancelar): ");
        
        try {
            int numeroEscolhido = Integer.parseInt(scanner.nextLine());
            
            if (numeroEscolhido == 0) {
                System.out.println("...cancelado.");
                return;
            }
            
            Item item = jogador.getInventario().getItemPorIndice(numeroEscolhido - 1);
            
            if (item != null) {
                jogador.usarItem(item.getNome());
            } else {
                System.out.println("Número de item inválido.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um NÚMERO.");
        }
    }
    
    // --- Lógica de Save/Load com Construtor de Cópia ---
    
    private static void criarSavePoint() {
        // "Progresso salvo!" REMOVIDO
        if (jogador instanceof Guerreiro g) {
            savePoint = new Guerreiro(g);
        } else if (jogador instanceof Mago m) {
            savePoint = new Mago(m);
        } else if (jogador instanceof Arqueiro a) {
            savePoint = new Arqueiro(a);
        } else if (jogador instanceof Paladino p) { 
            savePoint = new Paladino(p);
        }
    }
    
    private static void carregarSavePoint() {
        if (savePoint != null) {
            System.out.println("...Carregando último save point...");
            if (savePoint instanceof Guerreiro g) {
                jogador = new Guerreiro(g);
            } else if (savePoint instanceof Mago m) {
                jogador = new Mago(m);
            } else if (savePoint instanceof Arqueiro a) {
                jogador = new Arqueiro(a);
            } else if (savePoint instanceof Paladino p) { 
                jogador = new Paladino(p);
            }
            System.out.println("Jogo carregado!");
            System.out.println(jogador.toString());
        } else {
            System.out.println("Nenhum save point encontrado.");
        }
    }
    
    // Rola um dado com o número de lados especificado
    private static int rolarDado(int lados) {
        return dado.nextInt(lados) + 1;
    }
}
    