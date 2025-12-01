import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.border.LineBorder;
import java.util.List;

/**
 * InterfaceJogoModernizado
 *
 * Versão visual "Híbrido Dark + Anime" (estilo O), tamanho alvo 1600x900.
 * - Mantém toda a lógica do seu jogo (Personagem, Inimigo, Item, etc).
 * - Substitui e organiza a interface para um visual mais profissional.
 *
 * Observações:
 * - Este arquivo depende das classes Personagem, Inimigo, Item presentes no seu projeto.
 * - Compile e rode como antes (java InterfaceJogoModernizado).
 */
public class InterfaceJogoModernizado extends JFrame {

    // --- Componentes UI principais ---
    private JTextPane areaTexto;         // Área de narrativa / log
    private CustomBar barraHP;          // Barra HP customizada com brilho
    private CustomBar barraMP;          // Barra MP customizada com brilho
    private CustomBar barraXP;          // Barra XP customizada com brilho
    private JLabel lblNome, lblClasse, lblNivel; // Labels de identificação
    private JPanel painelAcoes;         // Painel com botões de ação
    private StyledButton btnExplorar, btnAtacar, btnHabilidade, btnInventario, btnFugir;
    private JTextArea painelStatsCurto; // Painel lateral com atributos
    private JLabel retratoLabel;        // Retrato do personagem

    // --- Lógica do jogo (mantida) ---
    private Personagem jogador;
    private Inimigo inimigoAtual;
    private int localizacaoAtual = 0;
    private int monstrosDerrotadosNaArea = 0;
    private int serCaoticoDerrotas = 0;
    private final Random dado = new Random();
    private boolean emCombate = false;

    // --- Constantes visuais / dimensões ---
    private static final int WINDOW_W = 1600;
    private static final int WINDOW_H = 900;

    public static void main(String[] args) {
        // Ajuste do look-and-feel apenas para consistência visual (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // Janela de diálogo padronizada
            UIManager.put("OptionPane.background", new Color(40, 40, 40));
            UIManager.put("Panel.background", new Color(40, 40, 40));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
        } catch (Exception e) {
            // ignorar e usar padrão
        }

        SwingUtilities.invokeLater(() -> new InterfaceJogoModernizado());
    }

    public InterfaceJogoModernizado() {
        super("Reino das Bestas — Edição Premium");
        initUI();
        // Pequeno delay para abrir a criação de personagem depois que a janela aparece
        Timer timer = new Timer(250, e -> iniciarJogo());
        timer.setRepeats(false);
        timer.start();
    }

    // -----------------------
    // Inicialização da interface
    // -----------------------
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_W, WINDOW_H);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);

        // Conteúdo principal com background customizado (gradiente sutil)
        setContentPane(new BackgroundPanel());
        getContentPane().setLayout(new BorderLayout(12, 12));

        // --- TOP HUD (título compacto + pequenas informações) ---
        JPanel topHUD = new JPanel(new BorderLayout());
        topHUD.setOpaque(false);
        topHUD.setBorder(new EmptyBorder(12, 18, 8, 18));
        getContentPane().add(topHUD, BorderLayout.NORTH);

        JLabel titulo = new JLabel("REINO DAS BESTAS");
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(new Color(230, 220, 200));
        topHUD.add(titulo, BorderLayout.WEST);

        // Barra superior direita com Status breve (nível + área)
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        topRight.setOpaque(false);
        lblNivel = new JLabel("Nível: -");
        lblNivel.setForeground(new Color(210, 210, 210));
        topRight.add(lblNivel);
        getContentPane().add(topHUD, BorderLayout.NORTH);
        topHUD.add(topRight, BorderLayout.EAST);

        // --- CENTER: painel principal dividido (esquerda: retrato+atributos, centro: narrativa) ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.22); // 22% esquerda / 78% centro
        mainSplit.setDividerSize(6);
        mainSplit.setOpaque(false);
        mainSplit.setBorder(null);
        getContentPane().add(mainSplit, BorderLayout.CENTER);

        // Esquerda: retrato + barras + atributos
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BorderLayout(10, 10));
        leftPanel.setBorder(new EmptyBorder(12, 12, 12, 6));
        mainSplit.setLeftComponent(leftPanel);

        // Painel do retrato (com aro dourado)
        JPanel portraitCard = new RoundedPanel(new Color(18, 18, 20), new Color(60, 50, 40), 12);
        portraitCard.setLayout(new BorderLayout());
        portraitCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        portraitCard.setPreferredSize(new Dimension(320, 300));
        leftPanel.add(portraitCard, BorderLayout.NORTH);

        retratoLabel = new JLabel("<html><center>RETRATO</center></html>", SwingConstants.CENTER);
        retratoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        retratoLabel.setForeground(new Color(190, 180, 170));
        retratoLabel.setOpaque(false);
        retratoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portraitCard.add(retratoLabel, BorderLayout.CENTER);

        // Painel de barras (HP/MP/XP) abaixo do retrato
        JPanel barsPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        barsPanel.setOpaque(false);
        barsPanel.setBorder(new EmptyBorder(12, 0, 12, 0));
        leftPanel.add(barsPanel, BorderLayout.CENTER);

        // Criamos barras custom: cor base + cor de brilho (degradê)
        barraHP = new CustomBar(new Color(190, 30, 40), new Color(255, 80, 80), "HP");
        barraMP = new CustomBar(new Color(30, 80, 180), new Color(90, 170, 255), "MP");
        barraXP = new CustomBar(new Color(110, 40, 150), new Color(200, 120, 255), "XP");
        barraHP.setPreferredSize(new Dimension(200, 40));
        barraMP.setPreferredSize(new Dimension(200, 40));
        barraXP.setPreferredSize(new Dimension(200, 36));

        barsPanel.add(barraHP);
        barsPanel.add(barraMP);
        barsPanel.add(barraXP);

        // Painel de atributos (scroll)
        painelStatsCurto = new JTextArea();
        painelStatsCurto.setEditable(false);
        painelStatsCurto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        painelStatsCurto.setBackground(new Color(16, 16, 18));
        painelStatsCurto.setForeground(new Color(215, 215, 200));
        painelStatsCurto.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 70, 60)), "Atributos"));
        painelStatsCurto.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scrollStats = new JScrollPane(painelStatsCurto);
        scrollStats.setBorder(null);
        leftPanel.add(scrollStats, BorderLayout.SOUTH);

        // Centro: área de texto narrativa (com moldura elegante)
        JPanel centerCard = new RoundedPanel(new Color(10, 10, 12), new Color(22, 22, 24), 10);
        centerCard.setLayout(new BorderLayout(10, 10));
        centerCard.setBorder(new EmptyBorder(14, 14, 14, 14));
        mainSplit.setRightComponent(centerCard);

        areaTexto = new JTextPane();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Consolas", Font.PLAIN, 15));
        areaTexto.setBackground(new Color(6, 6, 8));
        areaTexto.setForeground(new Color(200, 230, 200));
        areaTexto.setBorder(null);
        areaTexto.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        scrollTexto.setBorder(null);
        centerCard.add(scrollTexto, BorderLayout.CENTER);

        // Topo do centro: nome & classe (maior destaque)
        JPanel nomeCard = new JPanel(new BorderLayout());
        nomeCard.setOpaque(false);
        lblNome = new JLabel("Nome: ---");
        lblNome.setForeground(new Color(240, 230, 220));
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblClasse = new JLabel("Classe: ---");
        lblClasse.setForeground(new Color(210, 210, 200));
        lblClasse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nomeCard.add(lblNome, BorderLayout.WEST);
        nomeCard.add(lblClasse, BorderLayout.EAST);
        centerCard.add(nomeCard, BorderLayout.NORTH);

        // --- SOUTH: painel de ações (botões grandes e espaçados) ---
        painelAcoes = new JPanel(new GridBagLayout());
        painelAcoes.setOpaque(false);
        painelAcoes.setBorder(new EmptyBorder(12, 18, 18, 18));
        getContentPane().add(painelAcoes, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        btnExplorar = new StyledButton("Explorar");
        btnAtacar = new StyledButton("Atacar");
        btnHabilidade = new StyledButton("Habilidade");
        btnInventario = new StyledButton("Inventário");
        btnFugir = new StyledButton("Fugir");

        // Cores mais fortes para ações importantes
        btnAtacar.setMode(StyledButton.Mode.ATTACK);      // vermelho leve
        btnHabilidade.setMode(StyledButton.Mode.EXPLORE); // azul (modo exploratório combina melhor com habilidades)
        btnExplorar.setMode(StyledButton.Mode.EXPLORE);   // neutro/azul
        btnInventario.setMode(StyledButton.Mode.NEUTRAL); // dourado suave substituirá o neutro
        btnFugir.setMode(StyledButton.Mode.NEUTRAL);      // neutro escuro

        // Adicionar botões com espaçamento e tamanho responsivo
        gbc.gridx = 0; painelAcoes.add(btnExplorar, gbc);
        gbc.gridx = 1; painelAcoes.add(btnAtacar, gbc);
        gbc.gridx = 2; painelAcoes.add(btnHabilidade, gbc);
        gbc.gridx = 3; painelAcoes.add(btnInventario, gbc);
        gbc.gridx = 4; painelAcoes.add(btnFugir, gbc);

        // Listeners das ações (mantendo a lógica existente)
        btnExplorar.addActionListener(e -> acaoExplorar());
        btnAtacar.addActionListener(e -> acaoAtacar());
        btnHabilidade.addActionListener(e -> acaoHabilidade());
        btnFugir.addActionListener(e -> acaoFugir());
        btnInventario.addActionListener(e -> abrirInventarioModal());

        // Atalhos globais
        setupAtalhos();

        // Atualiza estados iniciais
        atualizarBotoes(false);

        setVisible(true);
    }

    // -----------------------
    // Atalhos (E, A, I)
    // -----------------------
    private void setupAtalhos() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "explorar");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "atacar");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "inventario");

        am.put("explorar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { if (btnExplorar.isEnabled()) acaoExplorar(); }
        });
        am.put("atacar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { if (btnAtacar.isEnabled()) acaoAtacar(); }
        });
        am.put("inventario", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { abrirInventarioModal(); }
        });
    }

    // -----------------------
    // Início do jogo / criação de personagem (mantido)
    // -----------------------
    private void iniciarJogo() {
        mostrarTelaCriacaoPersonagem();

        // Itens iniciais
        jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 2));
        jogador.getInventario().adicionarItem(new Item("Poção de Mana", "Recupera 20 MP", "mana", 1));
        jogador.getInventario().adicionarItem(new Item("Mapa da Floresta", "Mapa parcial.", "mapa", 1));

        escreverComEstilo("=== REINO DAS BESTAS ===", new Color(160, 200, 255), true);
        escreverComEstilo("Bem-vindo, " + jogador.getNome() + "!", new Color(170, 255, 190), false);
        escreverComEstilo("Você acorda na Floresta de Ervaluna. Encontre seu caminho!\n", new Color(200, 200, 200), false);

        atualizarInterface(false);
    }

    // Diálogo de criação (melhor visual, mantendo lógica)
    private void mostrarTelaCriacaoPersonagem() {
        JDialog dialog = new JDialog(this, "Novo Jogo — Criação", true);
        dialog.setSize(720, 520);
        dialog.setLocationRelativeTo(this);

        // painel principal do dialog com fundo escuro
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(new Color(36, 36, 36));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        dialog.setContentPane(root);

        JLabel titulo = new JLabel("Crie seu Herói", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(230, 230, 220));
        root.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNomeDlg = new JLabel("Nome:");
        lblNomeDlg.setForeground(Color.LIGHT_GRAY);
        lblNomeDlg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        centro.add(lblNomeDlg, gbc);

        JTextField txtNome = new JTextField("Viajante");
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNome.setBackground(new Color(60, 60, 60));
        txtNome.setForeground(Color.WHITE);
        txtNome.setCaretColor(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        centro.add(txtNome, gbc);

        JLabel lblClasseDlg = new JLabel("Classe:");
        lblClasseDlg.setForeground(Color.LIGHT_GRAY);
        lblClasseDlg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        centro.add(lblClasseDlg, gbc);

        JPanel painelClasses = new JPanel(new GridLayout(1, 4, 10, 0));
        painelClasses.setOpaque(false);
        JToggleButton btnGuerreiro = new JToggleButton("Guerreiro");
        JToggleButton btnMago = new JToggleButton("Mago");
        JToggleButton btnArqueiro = new JToggleButton("Arqueiro");
        JToggleButton btnPaladino = new JToggleButton("Paladino");
        JToggleButton[] toggles = {btnGuerreiro, btnMago, btnArqueiro, btnPaladino};
        for (JToggleButton t : toggles) {
            t.setFont(new Font("Segoe UI", Font.BOLD, 12));
            t.setBackground(new Color(70, 70, 70));
            t.setForeground(Color.WHITE);
            t.setFocusPainted(false);
        }
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(btnGuerreiro); grupo.add(btnMago); grupo.add(btnArqueiro); grupo.add(btnPaladino);
        btnGuerreiro.setSelected(true);
        painelClasses.add(btnGuerreiro); painelClasses.add(btnMago); painelClasses.add(btnArqueiro); painelClasses.add(btnPaladino);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        centro.add(painelClasses, gbc);

        // Descrição da classe
        JTextArea txtDescricao = new JTextArea(6, 40);
        txtDescricao.setEditable(false);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setBackground(new Color(28, 28, 28));
        txtDescricao.setForeground(new Color(220, 210, 170));
        txtDescricao.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(8, 8, 8, 8)));
        String descG = "GUERREIRO\nAlta vida e defesa. Habilidade: Golpe Devastador.";
        String descM = "MAGO\nAlto dano mágico. Habilidade: Bola de Fogo.";
        String descA = "ARQUEIRO\nAtaque à distância, crítico. Habilidade: Chuva de Flechas.";
        String descP = "PALADINO\nDefesa e cura. Habilidade: Luz Sagrada.";
        txtDescricao.setText(descG);

        btnGuerreiro.addActionListener(e -> txtDescricao.setText(descG));
        btnMago.addActionListener(e -> txtDescricao.setText(descM));
        btnArqueiro.addActionListener(e -> txtDescricao.setText(descA));
        btnPaladino.addActionListener(e -> txtDescricao.setText(descP));

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        centro.add(txtDescricao, gbc);

        root.add(centro, BorderLayout.CENTER);

        JButton btnIniciar = new JButton("INICIAR AVENTURA");
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnIniciar.setBackground(new Color(0, 120, 50));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setPreferredSize(new Dimension(0, 56));
        btnIniciar.setFocusPainted(false);
        btnIniciar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) nome = "Viajante";
            if (btnGuerreiro.isSelected()) jogador = new Guerreiro(nome);
            else if (btnMago.isSelected()) jogador = new Mago(nome);
            else if (btnArqueiro.isSelected()) jogador = new Arqueiro(nome);
            else jogador = new Paladino(nome);
            dialog.dispose();
        });
        root.add(btnIniciar, BorderLayout.SOUTH);

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
    }

    // -----------------------
    // Ações do jogo (mantidas e integradas)
    // -----------------------
    private void acaoExplorar() {
        if (emCombate) return;

        escreverComEstilo("\n--- Explorando área " + localizacaoAtual + "... ---", new Color(255, 200, 120), true);

        if (checarChefeDeAndar()) return;

        int rolagem = dado.nextInt(100) + 1;
        if (rolagem <= 30) {
            escreverComEstilo("(!) Armadilha! Você pisou em espinhos.", new Color(255, 100, 100), false);
            jogador.receberDano(10);
            atualizarInterface(false);
            if (!jogador.estaVivo()) gameOver();
        } else if (rolagem <= 60) {
            gerarInimigoAleatorio();
        } else if (rolagem <= 80) {
            escreverComEstilo("(!) Você encontrou um baú!", new Color(255, 230, 140), false);
            jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Cura 20 HP", "cura", 1));
            escreverComEstilo("+1 Poção de Cura", new Color(140, 255, 140), false);
            atualizarInterface(false);
        } else {
            escreverComEstilo("A floresta está estranhamente quieta...", new Color(190, 190, 190), false);
        }
    }

    private void gerarInimigoAleatorio() {
        String[] pool = {"Slime", "Goblin Faminto", "Lobo das Sombras"};
        if (localizacaoAtual >= 2) pool = new String[]{"Espectro Errante", "Besouro Blindado", "Ogro da Montanha"};
        String nomeIni = pool[dado.nextInt(pool.length)];
        iniciarCombate(Inimigo.criarInimigo(nomeIni));
    }

    private boolean checarChefeDeAndar() {
        if (monstrosDerrotadosNaArea >= 2) {
            String nomeChefe = switch (localizacaoAtual) {
                case 0 -> "Goblin Faminto";
                case 1 -> "Troll da Caverna";
                case 2 -> "Guardião de Pedra";
                case 3 -> "Espírito Corrompido da Floresta";
                default -> "A Besta Manticora";
            };
            escreverComEstilo("\n!!! CHEFE ENCONTRADO !!!", new Color(255, 70, 70), true);
            iniciarCombate(Inimigo.criarInimigo(nomeChefe));
            return true;
        }
        return false;
    }

    private void iniciarCombate(Inimigo inimigo) {
        inimigoAtual = inimigo;
        if ("Ser Caótico de Ervaluna".equals(inimigo.getNome()) && serCaoticoDerrotas > 0) {
            inimigo.setXpRecompensa(inimigo.getXpRecompensa() * 2);
            escreverComEstilo("!!! O Ser Caótico voltou mais forte !!!", new Color(200, 120, 255), true);
        }
        escreverComEstilo("COMBATE: " + inimigo.getNome() + " (HP: " + inimigo.pontosVidaMax + ")", new Color(255, 80, 80), true);
        atualizarInterface(true);
    }

    private void acaoAtacar() {
        if (inimigoAtual == null) return;
        escreverComEstilo("\n> Você ataca!", Color.WHITE, false);
        int rolagem = dado.nextInt(20) + 1;
        int acerto = jogador.calcularAtaque(rolagem);
        if (acerto > inimigoAtual.getDefesa()) {
            int dano = jogador.calcularDano(dado);
            inimigoAtual.receberDano(dano);
            escreverComEstilo("ACERTOU! Dano: " + dano, new Color(100, 255, 100), false);
        } else {
            escreverComEstilo("ERROU! Inimigo defendeu.", new Color(170, 170, 170), false);
        }
        verificarFimTurno();
    }

    private void acaoHabilidade() {
        if (inimigoAtual == null) return;
        escreverComEstilo("\n> Usando Habilidade...", new Color(100, 150, 255), false);
        if (jogador.usarHabilidadeEspecial(inimigoAtual)) {
            escreverComEstilo("Habilidade usada com sucesso!", new Color(160, 220, 255), false);
            verificarFimTurno();
        } else {
            escreverComEstilo("Falha (Sem Mana?)", new Color(160, 160, 160), false);
            atualizarInterface(true);
        }
    }

    private void acaoFugir() {
        if (inimigoAtual == null) return;
        if (inimigoAtual.getXpRecompensa() >= 100) {
            escreverComEstilo("Você não pode fugir deste chefe!", new Color(255, 80, 80), false);
            turnoInimigo();
            return;
        }
        int rolagem = dado.nextInt(20) + 1;
        if (rolagem > 10) {
            escreverComEstilo("Fugiu com sucesso!", new Color(140, 255, 160), false);
            inimigoAtual = null;
            atualizarInterface(false);
        } else {
            escreverComEstilo("Falha na fuga!", new Color(255, 100, 100), false);
            turnoInimigo();
        }
    }

    private void verificarFimTurno() {
        if (inimigoAtual == null) return;
        if (!inimigoAtual.estaVivo()) {
            escreverComEstilo("\n*** VITÓRIA! ***", new Color(180, 255, 140), true);
            escreverComEstilo("Derrotou " + inimigoAtual.getNome(), new Color(210, 210, 140), false);
            int xp = inimigoAtual.getXpRecompensa();
            jogador.ganharXp(xp);
            escreverComEstilo("Ganhou " + xp + " XP.", new Color(140, 220, 255), false);
            jogador.pegarLoot(inimigoAtual);
            monstrosDerrotadosNaArea++;
            jogador.recuperarMp(5);

            String nome = inimigoAtual.getNome();
            if (nome.equals("Goblin Faminto") && localizacaoAtual == 0) avancarArea();
            else if (nome.equals("Troll da Caverna") && localizacaoAtual == 1) avancarArea();
            else if (nome.equals("Guardião de Pedra") && localizacaoAtual == 2) avancarArea();
            else if (nome.equals("Espírito Corrompido da Floresta") && localizacaoAtual == 3) avancarArea();
            else if (nome.equals("A Besta Manticora")) {
                JOptionPane.showMessageDialog(this, "PARABÉNS! Você zerou a história!");
                localizacaoAtual = 5;
                escreverComEstilo("=== ENDGAME: FLORESTA INTERMINÁVEL ===", new Color(200, 100, 230), true);
            } else if (nome.equals("Ser Caótico de Ervaluna")) {
                serCaoticoDerrotas++;
                escreverComEstilo("O Ser Caótico foi banido... por enquanto.", new Color(200, 100, 230), true);
            }

            inimigoAtual = null;
            atualizarInterface(false);
        } else {
            turnoInimigo();
        }
    }

    private void avancarArea() {
        localizacaoAtual++;
        monstrosDerrotadosNaArea = 0;
        escreverComEstilo("\n>>> ÁREA CONCLUÍDA! AVANÇANDO... <<<", new Color(230, 140, 220), true);
    }

    private void turnoInimigo() {
        if (inimigoAtual != null && inimigoAtual.estaVivo()) {
            escreverComEstilo("\n> " + inimigoAtual.getNome() + " ataca!", new Color(255, 100, 100), false);
            int rolagem = dado.nextInt(20) + 1;
            int acerto = inimigoAtual.calcularAtaque(rolagem);
            if (acerto > jogador.getDefesa()) {
                int dano = inimigoAtual.calcularDano(dado);
                jogador.receberDano(dano);
                escreverComEstilo("VOCÊ FOI ATINGIDO! Dano: " + dano, new Color(255, 160, 80), false);
            } else {
                escreverComEstilo("O inimigo errou!", new Color(170, 170, 170), false);
            }
            if (!jogador.estaVivo()) gameOver();
        }
        atualizarInterface(true);
    }

    private void gameOver() {
        escreverComEstilo("\n=== GAME OVER ===", new Color(255, 80, 80), true);
        JOptionPane.showMessageDialog(this, "Sua jornada acabou.", "Fim", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    // -----------------------
    // Atualizações visuais da interface com base no estado do jogador / combate
    // -----------------------
    private void atualizarInterface(boolean emCombate) {
        if (inimigoAtual == null) emCombate = false;
        this.emCombate = emCombate;

        if (jogador != null) {
            lblNome.setText(jogador.getNome());
            lblClasse.setText(jogador.getClass().getSimpleName());
            lblNivel.setText("Nível: " + jogador.nivel);

            // Atualiza barras custom (os CustomBar cuidam do desenho)
            barraHP.setMax(jogador.pontosVidaMax);
            barraHP.setValue(jogador.pontosVida);
            barraMP.setMax(jogador.mpMax);
            barraMP.setValue(jogador.mp);
            barraXP.setMax(Math.max(1, jogador.xpParaProximoNivel));
            barraXP.setValue(jogador.xp);

            // Atualiza retrato (placeholder textual — você pode definir um ImageIcon aqui)
            retratoLabel.setText("<html><center>" + jogador.getNome() + "<br/><small>" + jogador.getClass().getSimpleName() + "</small></center></html>");

            // Atualiza painel de atributos
            StringBuilder sb = new StringBuilder();
            sb.append("--- STATUS ---\n\n");
            sb.append("Ataque: ").append(jogador.getAtaque()).append("\n");
            sb.append("Defesa: ").append(jogador.getDefesa()).append("\n");
            sb.append("Área: ").append(localizacaoAtual).append("\n");
            sb.append("Kills: ").append(monstrosDerrotadosNaArea).append("\n\n");
            sb.append("--- INIMIGO ---\n");
            if (inimigoAtual != null) {
                sb.append(inimigoAtual.getNome()).append("\n");
                sb.append("HP Max: ").append(inimigoAtual.pontosVidaMax).append("\n");
                sb.append("Dano Rec: ").append(inimigoAtual.pontosVidaMax - inimigoAtual.pontosVida).append("\n");
            } else {
                sb.append("Nenhum\n");
            }
            painelStatsCurto.setText(sb.toString());
        }

        atualizarBotoes(emCombate);
    }

    private void atualizarBotoes(boolean combate) {
        if (inimigoAtual == null) combate = false;
        btnExplorar.setEnabled(!combate);
        btnAtacar.setEnabled(combate);
        btnHabilidade.setEnabled(combate);
        btnFugir.setEnabled(combate);
        btnInventario.setEnabled(true);
    }

    // -----------------------
    // Escrita estilizada na área de texto (cores e bold)
    // -----------------------
    private void escreverComEstilo(String texto, Color cor, boolean bold) {
        StyledDocument doc = areaTexto.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, cor);
        StyleConstants.setBold(style, bold);
        StyleConstants.setFontFamily(style, "Consolas");
        try {
            doc.insertString(doc.getLength(), texto + "\n", style);
            areaTexto.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // -----------------------
    // Inventário modal (mantido)
    // -----------------------
    private void abrirInventarioModal() {
        List<Item> itens = jogador.getInventario().getItens();
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mochila vazia.");
            return;
        }

        JDialog dialog = new JDialog(this, "Inventário", true);
        dialog.setSize(640, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

        JTable tabela = new JTable(new InventarioTableModel(itens));
        tabela.setRowHeight(26);
        tabela.setBackground(new Color(30, 30, 30));
        tabela.setForeground(Color.WHITE);
        tabela.setGridColor(new Color(60,60,60));
        tabela.setSelectionBackground(new Color(100, 130, 150));
        tabela.setSelectionForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(tabela);
        sp.getViewport().setBackground(new Color(30, 30, 30));
        dialog.add(sp, BorderLayout.CENTER);

        JButton btnUsar = new JButton("USAR ITEM SELECIONADO");
        btnUsar.setBackground(new Color(80, 120, 80));
        btnUsar.setForeground(Color.WHITE);
        btnUsar.setFocusPainted(false);
        btnUsar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row >= 0) {
                Item item = itens.get(row);
                String res = jogador.usarItem(item.getNome());
                escreverComEstilo("Usou item: " + item.getNome(), new Color(140, 220, 255), false);
                if ("mapa".equals(res)) mostrarMapaGrafico();
                if (emCombate && !res.startsWith("falha") && !res.equals("mapa") && !res.equals("clue")) {
                    dialog.dispose();
                    turnoInimigo();
                } else {
                    dialog.dispose();
                    atualizarInterface(emCombate);
                }
            }
        });
        dialog.add(btnUsar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // -----------------------
    // Mapa ASCII (mantido)
    // -----------------------
    private void mostrarMapaGrafico() {
        JTextArea mapa = new JTextArea(18, 80);
        mapa.setFont(new Font("Monospaced", Font.PLAIN, 12));
        mapa.setEditable(false);
        mapa.setBackground(new Color(12, 12, 12));
        mapa.setForeground(new Color(86, 255, 120));

        String loc0="[?]", loc1="[?]", loc2="[?]", loc3="[?]", loc4="[?]", loc5="[?]";
        switch (localizacaoAtual) {
            case 0 -> { loc0="[P]"; loc1="[ ]"; }
            case 1 -> { loc0="[X]"; loc1="[P]"; loc2="[ ]"; }
            case 2 -> { loc0="[X]"; loc1="[X]"; loc2="[P]"; loc3="[ ]"; }
            case 3 -> { loc0="[X]"; loc1="[X]"; loc2="[X]"; loc3="[P]"; loc4="[ ]"; }
            case 4 -> { loc0="[X]"; loc1="[X]"; loc2="[X]"; loc3="[X]"; loc4="[P]"; }
            case 5 -> { loc0="[X]"; loc1="[X]"; loc2="[X]"; loc3="[X]"; loc4="[X]"; loc5="[P]"; }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("==================== MAPA DA FLORESTA DE ERVALUNA ====================\n");
        sb.append("... (mapa reduzido para brevidade) ...\n");
        sb.append(String.format("Clareira Inicial: %s   Trilha: %s   Ruínas: %s\n", loc0, loc1, loc2));
        sb.append("===========================================================================\n");
        if(localizacaoAtual == 5) sb.append("[P] = Você está na FLORESTA INTERMINÁVEL (Endgame)\n");
        else sb.append("[P] = Você   [X] = Concluído   [ ] = Revelado   [?] = Oculto\n");

        mapa.setText(sb.toString());

        JDialog mapaDialog = new JDialog(this, "Mapa de Ervaluna", false);
        mapaDialog.setSize(820, 520);
        mapaDialog.setLocationRelativeTo(this);
        mapaDialog.add(new JScrollPane(mapa));
        mapaDialog.setVisible(true);
    }

    // -----------------------
    // Inventário model (simples)
    // -----------------------
    private static class InventarioTableModel extends AbstractTableModel {
        private final List<Item> itens;
        private final String[] cols = {"Nome", "Qtd", "Descrição"};
        public InventarioTableModel(List<Item> i) { this.itens = i; }
        public int getRowCount() { return itens.size(); }
        public int getColumnCount() { return cols.length; }
        public String getColumnName(int c) { return cols[c]; }
        public Object getValueAt(int r, int c) {
            Item it = itens.get(r);
            return switch(c) { case 0 -> it.getNome(); case 1 -> it.getQuantidade(); default -> it.getDescricao(); };
        }
    }

    // -----------------------
    // --- COMPONENTES CUSTOMIZADOS VISUAIS ---
    // -----------------------

    /**
     * CustomBar: barra de progresso desenhada com degradê e texto central.
     * - Usa cor base e cor de brilho para criar efeito "metalizado/neon".
     */
    private static class CustomBar extends JComponent {
        private int value = 0;
        private int max = 100;
        private final Color base;
        private final Color glow;
        private final String label;
        private final int arc = 14;

        public CustomBar(Color base, Color glow, String label) {
            this.base = base;
            this.glow = glow;
            this.label = label;
            setPreferredSize(new Dimension(360, 36));
            setMinimumSize(new Dimension(120, 28));
        }

        public void setValue(int v) { this.value = Math.max(0, Math.min(v, max)); repaint(); }
        public void setMax(int m) { this.max = Math.max(1, m); repaint(); }
        public int getValue() { return value; }
        public int getMax() { return max; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth(), h = getHeight();
            // Fundo escuro com leve borda
            g2.setColor(new Color(18, 18, 20));
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // Borda sutil
            g2.setColor(new Color(60, 50, 40, 160));
            g2.drawRoundRect(1, 1, w - 3, h - 3, arc, arc);

            // Calcula fração
            double frac = (max > 0) ? ((double) value / max) : 0;
            int fillW = (int) Math.round((w - 6) * frac);

            // Desenha degradê da barra
            GradientPaint gp = new GradientPaint(3, 3, glow.brighter(), fillW + 3, h - 3, base.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(3, 3, Math.max(0, fillW), h - 6, arc - 4, arc - 4);

            // Desenha brilho leve no topo
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(3, 3, Math.max(0, fillW), (h - 6) / 2, arc - 4, arc - 4);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // Texto central (label e valores)
            String texto = String.format("%s: %d / %d", label, value, max);
            FontMetrics fm = g2.getFontMetrics(new Font("Arial", Font.BOLD, 12));
            int tw = fm.stringWidth(texto);
            int tx = (w - tw) / 2;
            int ty = (h + fm.getAscent()) / 2 - 2;

            // Texto com sombra leve
            g2.setColor(new Color(0, 0, 0, 160));
            g2.drawString(texto, tx + 1, ty + 1);
            g2.setColor(new Color(230, 230, 230));
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(texto, tx, ty);

            g2.dispose();
        }
    }

    /**
     * StyledButton: botão arredondado e com efeito de hover / cor de destaque.
     */
    private static class StyledButton extends JButton {

    // === Cores base ===
    private Color baseColor = new Color(35, 35, 38);
    private Color hoverColor = new Color(70, 70, 75);

    // === Cores específicas por função ===
    private Color attackAccent = new Color(200, 40, 40);      // vermelho forte (ataque)
    private Color exploreAccent = new Color(40, 140, 220);     // azul/ciano neon (explorar)
    private Color neutralAccent = new Color(110, 110, 110);    // padrão

    // cor final usada para brilho/borda
    private Color currentAccent = neutralAccent;

    private boolean hovering = false;

    public enum Mode {
        ATTACK, EXPLORE, NEUTRAL
    }

    private Mode mode = Mode.NEUTRAL;

    public StyledButton(String text) {
        super(text);

        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setOpaque(false);
        setBorder(null);
        setPreferredSize(new Dimension(200, 60));

        // Hover detection
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovering = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovering = false; repaint(); }
        });
    }

    // ===== Escolher modo visual forte =====
    public void setMode(Mode m) {
        this.mode = m;

        switch (m) {
            case ATTACK: currentAccent = attackAccent; break;
            case EXPLORE: currentAccent = exploreAccent; break;
            default: currentAccent = neutralAccent; break;
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 18;

        // ---- FUNDO ----
        Color fill = hovering ? hoverColor : baseColor;

        // degrade sutil + accent
        GradientPaint gp = new GradientPaint(
                0, 0,
                fill,
                0, h,
                fill.darker()
        );
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        // ---- BORDA DINÂMICA ----
        Color borderColor = hovering ? currentAccent : currentAccent.darker();
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2.3f));
        g2.drawRoundRect(1, 1, w - 3, h - 3, arc, arc);

        // ---- BRILHO NO MODO ATIVO ----
        if (hovering) {
            g2.setPaint(new Color(currentAccent.getRed(), currentAccent.getGreen(), currentAccent.getBlue(), 60));
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }

        // ---- TEXTO COM SOMBRA PROFISSIONAL ----
        FontMetrics fm = g2.getFontMetrics(getFont());
        String text = getText();
        int tw = fm.stringWidth(text);
        int tx = (w - tw) / 2;
        int ty = (h + fm.getAscent()) / 2 - 3;

        // sombra mais nítida
        g2.setColor(new Color(0, 0, 0, 180));
        g2.drawString(text, tx + 2, ty + 2);

        // texto branco
        g2.setColor(Color.WHITE);
        g2.drawString(text, tx, ty);

        g2.dispose();
    }
}


    /**
     * RoundedPanel: painel com cantos arredondados e borda discreta (usado para cards).
     */
    private static class RoundedPanel extends JPanel {
        private final Color fill;
        private final Color border;
        private final int arc;

        public RoundedPanel(Color fill, Color border, int arc) {
            super();
            this.fill = fill;
            this.border = border;
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth(), h = getHeight();
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * BackgroundPanel: painel principal com gradiente escuro e textura sutil.
     */
    private static class BackgroundPanel extends JPanel {
        public BackgroundPanel() { setOpaque(true); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth(), h = getHeight();
            Color c1 = new Color(10, 10, 12);
            Color c2 = new Color(20, 18, 16);
            GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);

            // linhas diagonais sutis para textura
            g2.setColor(new Color(255, 255, 255, 6));
            for (int i = -h; i < w; i += 22) {
                g2.drawLine(i, 0, i + h, h);
            }
            g2.dispose();
        }
    }
}
