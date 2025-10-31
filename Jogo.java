import java.util.Random;
import java.util.Scanner;

public class Jogo {
    
    private static Personagem jogador;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random dado = new Random(); 
    private static Personagem savePoint; 
    
    private static boolean jogoAtivo = true; 
    
    private static int localizacaoAtual = 0;
    private static String nomeLocalizacao = "Clareira Inicial";
    
    private static int monstrosDerrotadosNaArea = 0;
    
    private static int serCaoticoDerrotas = 0;

    public static void main(String[] args) {
        iniciarJogo();
        loopPrincipal();
        scanner.close();
    }

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
        jogador.getInventario().adicionarItem(new Item("Mapa da Floresta", "Um mapa parcial de Ervaluna.", "mapa", 1)); 
        
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

    private static void loopPrincipal() {
        while (jogoAtivo && jogador.estaVivo()) {
            
            switch(localizacaoAtual) {
                case 0: nomeLocalizacao = "Clareira Inicial"; break;
                case 1: nomeLocalizacao = "Trilha das Bestas"; break;
                case 2: nomeLocalizacao = "Ruínas Antigas"; break;
                case 3: nomeLocalizacao = "Coração da Floresta"; break;
                case 4: nomeLocalizacao = "Santuário Antigo"; break; 
                case 5: nomeLocalizacao = "Floresta Interminável"; break; 
            }
            
            System.out.println("\n--- O QUE VOCÊ FAZ? (Local: " + nomeLocalizacao + ") ---");
            System.out.println("[1] Explorar/Avançar"); 
            System.out.println("[2] Usar item do inventário"); 
            System.out.println("[3] Ver status do personagem");
            System.out.println("[4] Sair do Jogo");
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
                    break;
                case "4": 
                    System.out.println("Obrigado por jogar!");
                    jogoAtivo = false; 
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

    private static void explorar() {
        switch(localizacaoAtual) {
            case 0:
                explorarClareira();
                break;
            case 1:
                explorarTrilha();
                break;
            case 2:
                explorarRuinas();
                break;
            case 3:
                explorarCoracaoDaFloresta(); 
                break;
            case 4:
                explorarSantuario(); 
                break;
            case 5:
                explorarFlorestaInterminavel(); 
                break;
        }
    }
    
    private static void explorarClareira() {
        System.out.println("\nVocê explora a Clareira. As árvores sussurram...");
        
        if (monstrosDerrotadosNaArea >= 2) {
            System.out.println("Você encontra um rastro! Um Goblin Faminto está guardando uma passagem estreita!");
            Inimigo chefe = Inimigo.criarInimigo("Goblin Faminto");
            batalhar(chefe);
            return; 
        }
        
        int rolagem = rolarDado(100);
        Inimigo inimigo = null;

        if (rolagem <= 30) {
            System.out.println("Você pisa em uma placa de pressão... uma armadilha!");
            jogador.receberDano(10);
        } else if (rolagem <= 60) {
            System.out.println("Um Slime verde-ácido desliza em sua direção!");
            inimigo = Inimigo.criarInimigo("Slime"); 
        } else if (rolagem <= 90) {
             System.out.println("Um Cogumelo Venenoso Vivo balança, soltando esporos!");
             inimigo = Inimigo.criarInimigo("Cogumelo Venenoso Vivo");
        } else {
             System.out.println("Você encontra um baú de madeira antigo!");
             jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 1));
        }

        if (inimigo != null) {
            batalhar(inimigo);
        }
    }
    
    private static void explorarTrilha() {
        System.out.println("\nVocê segue a trilha de pegadas. As sombras parecem se mover...");
        
        if (monstrosDerrotadosNaArea >= 3) {
            System.out.println("Você chega a uma caverna fétida. Um Troll da Caverna rosna, protegendo seu tesouro!");
            Inimigo chefe = Inimigo.criarInimigo("Troll da Caverna");
            batalhar(chefe);
            return;
        }

        int rolagem = rolarDado(100);
        Inimigo inimigo = null;

        if (rolagem <= 20) {
            System.out.println("Você encontra uma mochila abandonada. Parece que foi rasgada por garras.");
            jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 2));
        } else if (rolagem <= 45) {
            System.out.println("Uma matilha de Lobos das Sombras te cerca!");
            inimigo = Inimigo.criarInimigo("Lobo das Sombras");
        } else if (rolagem <= 70) {
            System.out.println("Uma enorme Serpente de Ervaluna desce de uma árvore!");
            inimigo = Inimigo.criarInimigo("Serpente de Ervaluna");
        } else {
            System.out.println("O chão treme! Um Ogro da Montanha bloqueia seu caminho!");
            inimigo = Inimigo.criarInimigo("Ogro da Montanha"); 
        }
        
        if (inimigo != null) {
            batalhar(inimigo);
        }
    }
    
    private static void explorarRuinas() {
        System.out.println("\nA trilha termina em ruínas antigas, engolidas pela mata.");

        if (monstrosDerrotadosNaArea >= 3) {
            System.out.println("Você chega ao centro das ruínas. Um Guardião de Pedra se levanta!");
            System.out.println("Seus olhos de runa brilham, bloqueando a passagem adiante.");
            Inimigo chefe = Inimigo.criarInimigo("Guardião de Pedra");
            batalhar(chefe);
            return;
        }
        
        int rolagem = rolarDado(100);
        Inimigo inimigo = null;

        if (rolagem <= 1) { 
            if (serCaoticoDerrotas > 0) {
                System.out.println("\n!!! NÃO VAI ME GANHAR DESSA VEZ, " + jogador.getNome().toUpperCase() + " !!!");
            } else {
                System.out.println("\n!!! UMA PRESENÇA ATERRORIZANTE !!!");
            }
            System.out.println("O ar se dobra e a realidade se parte. Um Ser Caótico de Ervaluna se materializa!");
            inimigo = Inimigo.criarInimigo("Ser Caótico de Ervaluna");
            
            if (serCaoticoDerrotas > 0) {
                double escala = Math.pow(1.5, serCaoticoDerrotas);
                inimigo.pontosVidaMax = (int) (inimigo.pontosVidaMax * escala);
                inimigo.pontosVida = inimigo.pontosVidaMax;
                inimigo.ataque = (int) (inimigo.ataque * escala);
                inimigo.defesa = (int) (inimigo.defesa * escala);
                inimigo.setXpRecompensa((int) (inimigo.getXpRecompensa() * escala));
                System.out.println("(O Ser Caótico parece " + escala + "x mais forte!)");
            }
        }
        else if (rolagem <= 4) { 
            System.out.println("!!! PERIGO !!!");
            System.out.println("Um enorme DRAGÃO VERMELHO JOVEM pousa nas ruínas, rugindo!");
            inimigo = Inimigo.criarInimigo("Dragão Vermelho Jovem");
        
        } else if (rolagem <= 30) {
            System.out.println("Um Espectro Errante atravessa uma parede e flutua em sua direção!");
            inimigo = Inimigo.criarInimigo("Espectro Errante");
        } else if (rolagem <= 60) {
            System.out.println("Um Besouro Blindado emerge dos escombros!");
            inimigo = Inimigo.criarInimigo("Besouro Blindado");
        } else {
            System.out.println("Um Olho do Bosque flutua silenciosamente, te observando...");
            inimigo = Inimigo.criarInimigo("Olho do Bosque");
        }
        
        if (inimigo != null) {
            batalhar(inimigo);
        }
    }

    private static void explorarCoracaoDaFloresta() {
        System.out.println("\nO Coração da Floresta pulsa com uma energia estranha e sombria.");
        
        int rolagem = rolarDado(100);
        if (rolagem <= 6) { 
            System.out.println("!!! PERIGO !!!");
            System.out.println("Atraído pela energia, um DRAGÃO VERMELHO JOVEM desce dos céus!");
            Inimigo dragao = Inimigo.criarInimigo("Dragão Vermelho Jovem");
            batalhar(dragao);
            if (!jogador.estaVivo()) return; 
        }
        
        System.out.println("As árvores se abrem para revelar o centro da clareira.");
        System.out.println("Você vê uma figura encapuzada... não é um monstro. É ELARA, a rastreadora do seu grupo!");
        System.out.println("'...corra...', ela sussurra, antes de seus olhos ficarem brancos.");
        System.out.println("Atrás dela, uma criatura feita de vinhas e sombras se ergue... O 'Espirito da Floresta'.");
        
        Inimigo chefe1 = Inimigo.criarInimigo("Espírito Corrompido da Floresta");
        batalhar(chefe1);
    }
    
    private static void explorarSantuario() {
        System.out.println("\nVocê usa o amuleto para abrir caminho até o Santuário Antigo.");
        
        int rolagem = rolarDado(100);
        if (rolagem <= 6) { 
            System.out.println("!!! PERIGO !!!");
            System.out.println("Um DRAGÃO VERMELHO JOVEM está guardando a entrada do santuário!");
            Inimigo dragao = Inimigo.criarInimigo("Dragão Vermelho Jovem");
            batalhar(dragao);
            if (!jogador.estaVivo()) return;
        } else if (rolagem <= 30) {
             System.out.println("Um poderoso Alfa Lupino protege o covil de seu mestre!");
             Inimigo miniChefe = Inimigo.criarInimigo("Alfa Lupino");
             batalhar(miniChefe);
             if (!jogador.estaVivo()) return;
        } else if (rolagem <= 50) {
            System.out.println("Uma Fênix Crepuscular renasce das cinzas de um antigo ritual!");
            Inimigo miniChefe = Inimigo.criarInimigo("Fênix Crepuscular");
            batalhar(miniChefe);
            if (!jogador.estaVivo()) return;
        }

        System.out.println("Você avança... É um lugar de silêncio mortal. No centro, você vê os corpos dos seus companheiros mercenários.");
        System.out.println("Sobre eles, se alimentando da energia da tempestade... a criatura que os caçou.");
        System.out.println("Uma Besta Manticora, com relâmpagos verdes crepitando em suas asas.");
        System.out.println("'TOLO', ruge a besta. 'A floresta os marcou. Agora, ela marcará você!'");

        Inimigo finalBoss = Inimigo.criarInimigo("A Besta Manticora");
        batalhar(finalBoss);
    }
    
    private static void explorarFlorestaInterminavel() {
        System.out.println("\nVocê se aventura de volta pela Floresta Interminável, agora mais perigosa...");
        int rolagem = rolarDado(100);
        Inimigo inimigo = null;
        
        if (rolagem <= 1) { 
            if (serCaoticoDerrotas > 0) {
                System.out.println("\n!!! NÃO VAI ME GANHAR DESSA VEZ, " + jogador.getNome().toUpperCase() + " !!!");
            } else {
                System.out.println("\n!!! UMA PRESENÇA ATERRORIZANTE !!!");
            }
            System.out.println("O ar se dobra e a realidade se parte. Um Ser Caótico de Ervaluna se materializa!");
            
            inimigo = Inimigo.criarInimigo("Ser Caótico de Ervaluna");
            
            if (serCaoticoDerrotas > 0) {
                double escala = Math.pow(1.5, serCaoticoDerrotas);
                inimigo.pontosVidaMax = (int) (inimigo.pontosVidaMax * escala);
                inimigo.pontosVida = inimigo.pontosVidaMax;
                inimigo.ataque = (int) (inimigo.ataque * escala);
                inimigo.defesa = (int) (inimigo.defesa * escala);
                inimigo.setXpRecompensa((int) (inimigo.getXpRecompensa() * escala));
                System.out.println("(O Ser Caótico parece " + String.format("%.1f", escala) + "x mais forte!)");
            }
        }
        else if (rolagem <= 7) { 
            System.out.println("!!! PERIGO !!!");
            System.out.println("Um enorme DRAGÃO VERMELHO JOVEM te caça!");
            inimigo = Inimigo.criarInimigo("Dragão Vermelho Jovem");
        
        } else if (rolagem <= 20) {
            System.out.println("Uma Fênix Crepuscular renasce das cinzas de um antigo ritual!");
            inimigo = Inimigo.criarInimigo("Fênix Crepuscular");
        } else if (rolagem <= 35) {
            System.out.println("Um poderoso Alfa Lupino te vê como uma ameaça ao seu território!");
            inimigo = Inimigo.criarInimigo("Alfa Lupino");
        } else if (rolagem <= 50) {
            System.out.println("Um Guardião de Pedra se reergue das ruínas!");
            inimigo = Inimigo.criarInimigo("Guardião de Pedra");
        } else if (rolagem <= 65) {
            System.out.println("Um Ogro da Montanha furioso ataca!");
            inimigo = Inimigo.criarInimigo("Ogro da Montanha");
        } else if (rolagem <= 80) {
            System.out.println("Um Espectro Errante te embosca, com ódio nos olhos!");
            inimigo = Inimigo.criarInimigo("Espectro Errante");
        } else {
            System.out.println("Uma Serpente de Ervaluna gigante desce de uma árvore!");
            inimigo = Inimigo.criarInimigo("Serpente de Ervaluna");
        }
        
        if (inimigo != null) {
            batalhar(inimigo);
        }
    }
    
    private static void batalhar(Inimigo inimigo) {
        System.out.println("--- BATALHA INICIADA: " + jogador.getNome() + " vs " + inimigo.getNome() + " ---");
        
        while (jogador.estaVivo() && inimigo.estaVivo() && jogoAtivo) { 
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
                if(inimigo.getXpRecompensa() >= 120) { 
                    System.out.println("Não há como fugir de um inimigo tão poderoso!");
                } else {
                    int rolagemFuga = rolarDado(10); 
                    System.out.println("Você rolou " + rolagemFuga + " para fugir...");
                    if (rolagemFuga > 5) { 
                        System.out.println("Você conseguiu fugir!");
                        break; 
                    } else {
                        System.out.println("A fuga falhou!");
                    }
                }
            } else {
                System.out.println("Ação inválida, você perdeu o turno.");
            }
            
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
        
        if (jogador.estaVivo() && !inimigo.estaVivo()) {
            System.out.println("Você venceu a batalha!");
            
            int xpGanhos = inimigo.getXpRecompensa();
            jogador.ganharXp(xpGanhos);
            monstrosDerrotadosNaArea++;

            jogador.pegarLoot(inimigo);
            
            if (inimigo.getNome().equals("Goblin Faminto") && localizacaoAtual == 0) {
                System.out.println("\n[HISTÓRIA] Ao saquear o Goblin, você encontra um pedaço de tecido com o brasão do seu grupo!");
                System.out.println("[HISTÓRIA] Você agora vê uma trilha de pegadas que antes estava escondida!");
                localizacaoAtual = 1; 
                monstrosDerrotadosNaArea = 0;
            }
            if (inimigo.getNome().equals("Troll da Caverna") && localizacaoAtual == 1) {
                System.out.println("\n[HISTÓRIA] Em meio aos tesouros nojentos do Troll, você acha a fivela do cinto do seu capitão!");
                System.out.println("[HISTÓRIA] A trilha leva a antigas ruínas.");
                localizacaoAtual = 2; 
                monstrosDerrotadosNaArea = 0;
            }
            if (inimigo.getNome().equals("Guardião de Pedra") && localizacaoAtual == 2) {
                System.out.println("\n[HISTÓRIA] Com a derrota do Guardião, a runa em sua mão brilha intensamente.");
                System.out.println("[HISTÓRIA] Ela aponta para o Coração da Floresta... você sente uma presença familiar e sombria lá.");
                localizacaoAtual = 3; 
                monstrosDerrotadosNaArea = 0;
            }
            if (inimigo.getNome().equals("Espírito Corrompido da Floresta") && localizacaoAtual == 3) {
                System.out.println("\n[HISTÓRIA] Com a derrota da criatura, Elara está livre, mas exausta.");
                System.out.println("[HISTÓRIA] 'O resto do grupo... eles foram para o Santuário Antigo... Cuidado. O rugido... o rugido que nos atacou... está lá.'");
                localizacaoAtual = 4; 
                monstrosDerrotadosNaArea = 0;
            }
            
            if (inimigo.getNome().equals("A Besta Manticora")) {
                System.out.println("\n\n--- FIM DA HISTÓRIA PRINCIPAL ---");
                System.out.println("Com a Manticora morta, um silêncio cai sobre Ervaluna.");
                System.out.println("Você sobreviveu. Você vingou seu grupo.");
                
                System.out.println("\nO que você deseja fazer?");
                System.out.println("[1] Encerrar a jornada (Fim de Jogo)");
                System.out.println("[2] Continuar explorando a Floresta Interminável");
                System.out.print("Sua escolha: ");
                String escolhaFinal = scanner.nextLine();
                
                if (escolhaFinal.equals("1")) {
                    System.out.println("...Obrigado por jogar...");
                    jogoAtivo = false; 
                } else {
                    System.out.println("\nA floresta ainda te chama... A exploração agora é infinita.");
                    localizacaoAtual = 5; 
                    monstrosDerrotadosNaArea = 0;
                }

            } 
            else if (inimigo.getNome().equals("Ser Caótico de Ervaluna")) {
                System.out.println("\n[VITÓRIA ÉPICA] Você baniu o Ser Caótico... por enquanto.");
                serCaoticoDerrotas++; 
            }
            
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
        jogador.mostrarInventario(); 
        System.out.print("Digite o NÚMERO do item (ou 0 para cancelar): ");
        
        try {
            int numeroEscolhido = Integer.parseInt(scanner.nextLine());
            
            if (numeroEscolhido == 0) {
                System.out.println("...cancelado.");
                return;
            }
            
            Item item = jogador.getInventario().getItemPorIndice(numeroEscolhido - 1);
            
            if (item != null) {
                String efeito = jogador.usarItem(item.getNome());
                
                if (efeito.equals("mapa")) {
                    mostrarMiniMapa(localizacaoAtual);
                }
                
            } else {
                System.out.println("Número de item inválido.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um NÚMERO.");
        }
    }
    
    private static void criarSavePoint() {
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
    
    private static int rolarDado(int lados) {
        return dado.nextInt(lados) + 1;
    }
    
    
    private static void mostrarMiniMapa(int localizacao) {
        String loc0, loc1, loc2, loc3, loc4;
        loc0 = loc1 = loc2 = loc3 = loc4 = "[?]"; 

        switch (localizacao) {
            case 0: 
                loc0 = "[P]";
                loc1 = "[ ]"; 
                break;
            case 1: 
                loc0 = "[X]";
                loc1 = "[P]";
                loc2 = "[ ]"; 
                break;
            case 2: 
                loc0 = "[X]";
                loc1 = "[X]";
                loc2 = "[P]";
                loc3 = "[ ]"; 
                break;
            case 3: 
                loc0 = "[X]";
                loc1 = "[X]";
                loc2 = "[X]";
                loc3 = "[P]";
                loc4 = "[ ]"; 
                break;
            case 4: 
                loc0 = "[X]";
                loc1 = "[X]";
                loc2 = "[X]";
                loc3 = "[X]";
                loc4 = "[P]";
                break;
            case 5: 
                loc0 = "[X]";
                loc1 = "[X]";
                loc2 = "[X]";
                loc3 = "[X]";
                loc4 = "[X]";
                break;
        }


        System.out.println("\n==================== MAPA DA FLORESTA DE ERVALUNA ====================");

        System.out.println("^^^^^^^^^^^^^^^^^^^^ MONTANHAS DO NORTE (INACESSÍVEIS) ^^^^^^^^^^^^^^^^^^^");

        System.out.println("T T & ^ ^ ^ ^           ^ ^ ^ ^           ^ ^ & T T & ^T T T T T T");

        System.out.println("   & T ^         ^     " + loc4 + "  Santuário Antigo (Chefe Final)^ & T");

        System.out.println("T & ^ ^                /         \\            ^ ^   & T & T T T T");

        System.out.println("T T & ^               /           \\           ^ T T T T T T T T T");

        System.out.println("& T ^           " + loc3 + "  Coração da Floresta (Chefe 1)   ^ ^ T T");

        System.out.println("T & ^ ^            (Energia Mística) \\            ^ T T T T T T T");

        System.out.println("& & T ^                               \\            ^ T T T T T T T");

        System.out.println("T T ^     " + loc2 + "  Ruínas Antigas (Desafio)          T T ^ & T &");

        System.out.println("& T ^            /                      \\            ^ T T T T T T");

        System.out.println("T ^           /                          \\         ^ & T T T T T T");	

        System.out.println("& ^    " + loc1 + "  Trilha das Bestas (Selvagem)  \\           ^ T T &");

        System.out.println("T ^         /                              \\        ^ & T T T T T ");

        System.out.println("& ^        /                                \\           ^ T & T T T");

        System.out.println("T &       /                                  \\             & T T T");

        System.out.println("& T      /    " + loc0 + "  Clareira Inicial (Partida)   & T & TT T T T");

        System.out.println("T & T & T & & T T T & & T & T T T & T & & T T & & T T T & T T TT T");

        System.out.println("===========================================================================");

        System.out.println("[P] = Você   [X] = Concluído   [ ] = Revelado   [?] = Oculto");

        System.out.println("T = Árvore | ^ = Montanha | & = Rocha | \\ = Caminhos\n");

    }
}