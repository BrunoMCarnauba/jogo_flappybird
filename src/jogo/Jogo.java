package jogo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Jogo desenvolvido para a disciplina de programação estruturada do curso de sistemas de informação. 
 * O objetivo do jogo, assim como no original Flappy Bird, é percorrer a maior distância que conseguir, sem colidir com obstáculos que são gerados em posições aleatórias.
 * A maior pontuação do jogador fica salva no banco de dados.
 * As aulas do canal "1iber Programador" sobre como fazer o jogo Pac-Man em Java, ajudaram bastante no desenvolvimento: https://www.youtube.com/watch?v=v676dlIZvsw&list=PLc_8_1G9IIB6gb0N-HF2af-ECoNhX7pVd
 * @author Bruno
 */
public class Jogo {
    //atributos
        JFrame janela;

    //Menu
        JPanel painelMenu;
        JButton botoes[];
        JLabel fundoMenu;
        ImageIcon imagemFundoMenu;

    //Cadastro
        JPanel painelCadastro;
        /** botoesCadastro[0]: Botão entrar; botoesCadastro[1]: Botão cadastrar.*/
        JButton botoesCadastro[];
        /** Posição 0 para campo do login, Posição 1 para campo do cadastro */
        JTextField campoTextoLogin[];    //Para Login
        /** Posição 0 para campo do login, Posição 1 para campo do cadastro */
        JPasswordField campoTextoSenha[];   //Para Senha
        JLabel fundoCadastro;
        ImageIcon imagemFundoCadastro;

    //Instruções
        JPanel painelInstrucoes;
        ImageIcon imagemFundoInstrucoes;
        JLabel labelFundoInstrucoes;
        JButton botaoVoltarInstrucoes;

    //Ranking
        JPanel painelRanking;
        ImageIcon imagemFundoRanking;
        JLabel labelFundoRanking;
        String saidaNomesRanking;
        String saidaPontosRanking;
        JLabel labelNomesRanking;
        JLabel labelPontosRanking;
        JButton botaoConsultaRanking;
        JButton botaoVoltarParaMenu;

    //Sobre
        JPanel painelSobre;
        ImageIcon imagemFundoSobre;
        JLabel labelFundoSobre;
        JButton botaoVoltarSobre;
    
    //Jogo
        JPanel painelJogo;
        JLabel fundoJogo;
        ImageIcon imagemFundoJogo;
        Timer timer;
        int pressionado;
    //Jogo - Personagem
        JLabel personagem;
        ImageIcon imagemPersonagem;
        ImageIcon imagemPersonagemConfuso;
        ImageIcon imagemPersonagemChateado;
        String jogador;
        /** pontos: Pegará a pontuação da 'partida' atual */
        int pontos;
        /** pontuacao: Mostrará a pontuação do jogador */
        JLabel pontuacao;
        int velocidade;
        int opcaoGameOver;
        /** status: 0 = Iniciando ; 1 = Rodando ; 2 = GameOver ou pause  */
        int status;
    //Jogo - Cano
        JLabel canoCima[];
        JLabel canoBaixo[];
        ImageIcon imagemCanoCima;
        ImageIcon imagemCanoBaixo;
        Random gerador = new Random();
        int randomYCano1;
        int randomYCano2;
        int randomYCano3;
        ImageIcon imagemChao;
        JLabel chao;

    //Banco de dados
        PreparedStatement pst = null;
        ResultSet rs = null;
        BancoDeDados conexao = new BancoDeDados();
    
    /**
     * Primeira função chamada pelo Main (primeiro método chamado ao iniciar a aplicação).
     * Cria e configura a janela da aplicação, e chama a função menu() para criar o painel menu e colocá-lo na janela criada.
     */
    public Jogo() {
        //Janela
        janela = new JFrame("HeliStaculos");
        janela.setSize(384, 512);
        janela.setLayout(null);
        janela.setLocationRelativeTo(null);
        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);    //Desativa a ação padrão executada ao tentar fechar o programa pelo "X" (apenas fechar). A nova ação foi configurada abaixo.

        janela.addWindowListener(new WindowAdapter() {  //Pra quando clicar no botão de fechar, desconectar o banco e fechar a tela
            public void windowClosing(WindowEvent e) {
                conexao.desconecta();
                System.exit(0); //Fecha a janela
            }
        });
        
        menu(); //Cria todo o menu, adiciona ele à janela criada e o torna visível

        conexao.conexao();  //Conecta com o banco de dados
    }
    
    /**
     * Cria e configura o painel menu, bem como os componentes que estão dentro dele, como botões e imagens. No fim, adiciona o painel na janela da aplicação e o torna visível.
     */
    public void menu() {
        //Menu - Painel
        painelMenu = new JPanel();
        painelMenu.setLayout(null);
        painelMenu.setBounds(0, 0, janela.getWidth(), janela.getHeight()); //Painel do tamanho da janela
        painelMenu.setVisible(true);
        painelMenu.setBackground(Color.blue);

        //Criando botões
        botoes = new JButton[6];
        for (int i = 0; i < botoes.length; i++) {
            botoes[i] = new JButton();
        }

        //Menu - Background
        fundoMenu = new JLabel();
        fundoMenu.setBounds(0, 0, janela.getWidth(), janela.getHeight());	//Aparece na janela - Imagem cobre toda janela
        imagemFundoMenu = new ImageIcon("imagens/fundoMenu.png");	//Pega imagem com o (caminho da imagem)
        imagemFundoMenu = new ImageIcon(imagemFundoMenu.getImage().getScaledInstance(janela.getWidth(), janela.getHeight(), Image.SCALE_DEFAULT));	//Pega a imagem e muda o seu tamanho para o da janela usando o algoritmo padrão de dimensionamento
        fundoMenu.setIcon(imagemFundoMenu); //Põe a imagem no JLabel
        fundoMenu.setVisible(true);
        painelMenu.add(fundoMenu, 0); //Adiciona o JLabel com a imagem de fundo ao painelMenu, na posição 0

        //Adicionando texto aos botões
        botoes[0].setText("Jogar"); //Nome do botão 0
        botoes[1].setText("Ranking"); //Nome do botão 1
        botoes[2].setText("Instruções");
        botoes[3].setText("Conquistas");
        botoes[4].setText("Sobre");
        botoes[5].setText("Sair");

        //Posicionando cada botão em uma posição na tela e setando algumas configurações padrões
        for (int i = 0; i < botoes.length; i++) {
            botoes[i].setBounds(janela.getWidth() - (200 + 50), (i + 1) * 50, 200, 40);    //Posicao X (horizontal): Largura da janela - (tamanho botão + 50), pra que fique mais pra direita da tela mas que não fique colado na borda. Posição Y (vertical): (i+1)*height, para que fique um abaixo do outro. Parâmetro 3: Largura do botao, Parâmetro 4: Altura do botao.
            botoes[i].setVisible(true); //Visivel
            botoes[i].setBackground(Color.WHITE);   //Cor do botão
            painelMenu.add(botoes[i], 0);   //Adicionando ao painel
        }
        
        botoes[3].setEnabled(false);    //Função conquistas não está disponível ainda.

        //Ações botões
        eventoMenu();   //Metódo com eventos/ações do Menu

        //Depois das configurações, vai adicionar o painel na janela e mostra-la.
        janela.add(painelMenu, 0); //Adiciona o painelMenu à janela

        janela.setVisible(true);    //Torna a janela visível
    }
    
    /**
     * Configura as ações para os eventos de cada botão do menu, ou seja, o código que cada um deverá executar quando algum evento ocorrer (quando for clicado).
     */
    public void eventoMenu() {
        //Ações dos botões
        /** Botão Jogar */
        botoes[0].addMouseListener(new MouseAdapter() {  //Adicionando ação ao botão

            public void mousePressed(MouseEvent e) { //Função chamada quando o mouse é pressionado
                System.out.println("Botão 'Jogar' acionado");
                if (status == 0) {  //Se ainda não tiver sido realizado login ou cadastro
                    cadastro(); //Chama a tela de cadastro/login
                } else {    //Se já tiver sido realizado login ou cadastro
                    JOptionPane.showMessageDialog(null, "Bom jogo " + jogador + "!");
                    jogar();    //Chama a função/método "jogar", que prepara o cenário e inicia o jogo
                }
            }
        });

        /** Botão Ranking */
        botoes[1].addMouseListener(new MouseAdapter() {  //Adicionando ação ao botão

            public void mousePressed(MouseEvent e) { //Função chamada quando o mouse é pressionado
                System.out.println("Botão 'Ranking' acionado");
                painelMenu.setVisible(false);
                ranking();
            }
        });

        /** Botão Instrucao */
        botoes[2].addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                System.out.println("Botão 'Instruções' acionado");
                painelMenu.setVisible(false);
                instrucoes();
            }
        });
        
        /** Botão Sobre */
        botoes[4].addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                System.out.println("Botão 'Sobre' acionado.");
                painelMenu.setVisible(false);
                sobre();
                
            }
        });
        
        /** Botão Sair */
        botoes[5].addMouseListener(new MouseAdapter() {
            
            public void mousePressed(MouseEvent e) {
                System.out.println("Botão 'Sair' acionado. Programa encerrado.");
                conexao.desconecta();
                System.exit(0);
            }
        });
    }//Fim evento menu

    /**
     * Cria e configura o painel "ranking", bem como os componentes que estão dentro dele, como botões, imagens e textos.
     * Essa função busca no banco de dados as pontuações dos usuários em ordem decrescente e adiciona os 10 primeiros resultados no painel.
     * No fim, adiciona o painel na janela da aplicação e o torna visível.
     */
    public void ranking() { //Função Ranking
        painelRanking = new JPanel();
        painelRanking.setSize(janela.getWidth(), janela.getHeight());
        painelRanking.setLayout(null);
        painelRanking.setBackground(Color.BLUE);

        imagemFundoRanking = new ImageIcon("imagens/fundoRanking.png");
        imagemFundoRanking = new ImageIcon(imagemFundoRanking.getImage().getScaledInstance(painelRanking.getWidth(), painelRanking.getHeight(), Image.SCALE_DEFAULT));
        labelFundoRanking = new JLabel();
        labelFundoRanking.setIcon(imagemFundoRanking);
        labelFundoRanking.setSize(janela.getWidth(), janela.getHeight());
        labelFundoRanking.setVisible(true);
        painelRanking.add(labelFundoRanking, 0);

        botaoVoltarParaMenu = new JButton();
        botaoVoltarParaMenu.setBounds(10, 430, 211, 50);
        botaoVoltarParaMenu.setText("Voltar");
        botaoVoltarParaMenu.setVisible(true);
        painelRanking.add(botaoVoltarParaMenu, 0);

        botaoConsultaRanking = new JButton();
        botaoConsultaRanking.setBounds(224, 430, 145, 50);
        botaoConsultaRanking.setText("Consultar ranking");
        botaoConsultaRanking.setVisible(true);
        painelRanking.add(botaoConsultaRanking, 0);

        //Ranking -> TOP 10
        saidaNomesRanking = "<html>";
        saidaPontosRanking = "<html>";
        try {
            conexao.executaSQL("select j.nome, j.maior_pontuacao from jogadores j order by j.maior_pontuacao desc limit 10");
            while(conexao.rs.next() == true){   //enquanto houver resultado
               saidaNomesRanking += conexao.rs.getString("nome") + "<br/>"; //a tag <br/> quebra linha no html
               saidaPontosRanking += conexao.rs.getInt("maior_pontuacao") + "<br/>";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(painelRanking, "Não foi possível acessar o ranking com os 10 primeiros.\nErro: " + ex);
        }
        saidaNomesRanking += "</html>";
        saidaPontosRanking += "</html>";
        
        //labelNomesRanking
        labelNomesRanking = new JLabel();
        labelNomesRanking.setBounds(18, 80, 197, 341);
        labelNomesRanking.setVerticalAlignment(SwingConstants.TOP); //Para o texto começar no topo do label, e não no centro
        labelNomesRanking.setHorizontalAlignment(SwingConstants.CENTER); //Para centralizar o texto na horizontal
        labelNomesRanking.setText(saidaNomesRanking);
        labelNomesRanking.setFont(new Font("Arial", Font.BOLD, 20)); //Editando a fonte
        labelNomesRanking.setForeground(Color.WHITE);
        //labelPontosRanking
        labelPontosRanking = new JLabel();
        labelPontosRanking.setBounds(232, 80, 131, 341);
        labelPontosRanking.setVerticalAlignment(SwingConstants.TOP);
        labelPontosRanking.setHorizontalAlignment(SwingConstants.CENTER);
        labelPontosRanking.setText(saidaPontosRanking);
        labelPontosRanking.setFont(new Font("Arial", Font.BOLD, 20)); //Editando a fonte
        labelPontosRanking.setForeground(Color.WHITE);
        
        labelNomesRanking.setVisible(true);
        labelPontosRanking.setVisible(true);
        painelRanking.add(labelNomesRanking, 0);
        painelRanking.add(labelPontosRanking, 0);

        painelRanking.setVisible(true);

        eventosRanking();

        janela.add(painelRanking, 0);

    }
    
    /**
     * Configura as ações para os eventos de cada botão do painel (tela) "ranking", ou seja, o código que cada um deverá executar quando algum evento ocorrer (quando for clicado).
     */
    public void eventosRanking() {

        botaoVoltarParaMenu.addMouseListener(new MouseAdapter() {  //Adicionando ação ao botão

            public void mousePressed(MouseEvent e) { //Função chamada quando o mouse é pressionado
                System.out.println("Botão 'Voltar para o Menu' acionado");
                painelRanking.setVisible(false);
                painelMenu.setVisible(true);
            }
        });

        botaoConsultaRanking.addMouseListener(new MouseAdapter() {  //Adicionando ação ao botão

            public void mousePressed(MouseEvent e) { //Função chamada quando o mouse é pressionado
                System.out.println("Botão 'Consultar ranking' acionado");

                try {
                    String nomeJogador = JOptionPane.showInputDialog(painelRanking, "Qual o nome do jogador que você\nquer saber a pontuação?");
                    if(nomeJogador != null){
                        conexao.executaSQL("select j.nome, j.maior_pontuacao from jogadores as j where j.nome='" + nomeJogador + "' order by j.maior_pontuacao desc limit 5");
                        if (conexao.rs.first()) {
                            JOptionPane.showMessageDialog(painelRanking, "O jogador " + conexao.rs.getString("nome") + " fez " + conexao.rs.getInt("maior_pontuacao") + " pontos!");
                        } else {
                            JOptionPane.showMessageDialog(painelRanking, "Esse nome não existe!");
                        }   
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(painelRanking, "A consulta da pontuação do jogador especificado falhou.\nErro: " + ex);
                }

            }
        });

    }

    /**
     * Cria e configura o painel (tela) "instruções", bem como os componentes que estão dentro dele, como botões e imagens. 
     * Também adiciona o painel na janela da aplicação e o torna visível.
     */
    public void instrucoes() {
        painelInstrucoes = new JPanel();
        painelInstrucoes.setSize(janela.getWidth(), janela.getHeight());
        painelInstrucoes.setLayout(null);
        painelInstrucoes.setBackground(Color.BLUE);
        painelInstrucoes.setVisible(true);

        imagemFundoInstrucoes = new ImageIcon("imagens/fundoInstrucoes.png");
        imagemFundoInstrucoes = new ImageIcon(imagemFundoInstrucoes.getImage().getScaledInstance(painelInstrucoes.getWidth(), painelInstrucoes.getHeight(), Image.SCALE_DEFAULT));
        labelFundoInstrucoes = new JLabel();
        labelFundoInstrucoes.setIcon(imagemFundoInstrucoes);
        labelFundoInstrucoes.setSize(painelInstrucoes.getWidth(), painelInstrucoes.getHeight());
        labelFundoInstrucoes.setVisible(true);
        painelInstrucoes.add(labelFundoInstrucoes, 0);

        botaoVoltarInstrucoes = new JButton();
        botaoVoltarInstrucoes.setBounds(101, 425, 185, 52);
        botaoVoltarInstrucoes.setText("Voltar");
        botaoVoltarInstrucoes.setVisible(true);
        painelInstrucoes.add(botaoVoltarInstrucoes, 0);

        janela.add(painelInstrucoes, 0);
        eventosInstrucoes();
    }
    
    /**
     * Configura os eventos dos botões do painel (tela) "instruções", ou seja, o código que cada um deverá executar quando for clicado.
     */
    public void eventosInstrucoes() {

        botaoVoltarInstrucoes.addMouseListener(new MouseAdapter() {  //Adicionando ação ao botão

            public void mousePressed(MouseEvent e) { //Função chamada quando o mouse é pressionado
                System.out.println("Botão 'Voltar para o Menu' acionado");
                painelInstrucoes.setVisible(false);
                painelMenu.setVisible(true);
            }
        });
    }

    /**
     * Cria e configura o painel (tela) "sobre", bem como os componentes que estão dentro dele, como botões e imagens.
     * Também adiciona o painel na janela da aplicação e o torna visível.
     */
    public void sobre(){
        painelSobre = new JPanel();
        painelSobre.setSize(janela.getWidth(), janela.getHeight());
        painelSobre.setLayout(null);
        painelSobre.setVisible(true);
        
        imagemFundoSobre = new ImageIcon ("imagens/fundoSobre.png");
        imagemFundoSobre = new ImageIcon(imagemFundoSobre.getImage().getScaledInstance(painelSobre.getWidth(), painelSobre.getHeight(), Image.SCALE_DEFAULT));
        labelFundoSobre = new JLabel();
        labelFundoSobre.setSize(painelSobre.getWidth(),painelSobre.getHeight());
        labelFundoSobre.setIcon(imagemFundoSobre);
        labelFundoSobre.setVisible(true);
        painelSobre.add(labelFundoSobre,0);
        
        botaoVoltarSobre = new JButton();
        botaoVoltarSobre.setBounds(101, 425, 185, 52);
        botaoVoltarSobre.setText("Voltar");
        botaoVoltarSobre.setVisible(true);
        painelSobre.add(botaoVoltarSobre, 0);
        
        eventosSobre();
        
        janela.add(painelSobre,0);
    } 
    
    /**
     * Configura os eventos dos botões do painel (tela) "sobre", ou seja, o código que cada um deverá executar quando for clicado.
     */
    public void eventosSobre(){
                botaoVoltarSobre.addMouseListener(new MouseAdapter() {  //Adicionando ação ao botão

            public void mousePressed(MouseEvent e) { //Função chamada quando o mouse é pressionado
                System.out.println("Botão 'Voltar para o Menu' acionado");
                painelSobre.setVisible(false);
                painelMenu.setVisible(true);
            }
        });
                
    }
    
    /**
     * Cria e configura o painel (tela) "cadastro", bem como os componentes que estão dentro dele, como botões e imagens.
     * Essa tela serve tanto para o cadastro como para a autenticação do usuário.
     * Também adiciona o painel na janela da aplicação e o torna visível.
     */
    public void cadastro() { //Função Cadastro
        painelMenu.setVisible(false);

        painelCadastro = new JPanel();
        painelCadastro.setSize(janela.getWidth(), janela.getHeight());
        painelCadastro.setLayout(null);
        painelCadastro.setVisible(true);

        imagemFundoCadastro = new ImageIcon("imagens/fundoCadastro.png");
        imagemFundoCadastro = new ImageIcon(imagemFundoCadastro.getImage().getScaledInstance(janela.getWidth(), janela.getHeight(), Image.SCALE_DEFAULT));
        fundoCadastro = new JLabel();
        fundoCadastro.setBounds(0, 0, painelCadastro.getWidth(), painelCadastro.getHeight());
        fundoCadastro.setIcon(imagemFundoCadastro);
        fundoCadastro.setVisible(true);
        painelCadastro.add(fundoCadastro, 0);

        botoesCadastro = new JButton[2];
        botoesCadastro[0] = new JButton();
        botoesCadastro[1] = new JButton();

        botoesCadastro[0].setBounds(145, 198, 100, 35);
        botoesCadastro[0].setText("Entrar");
        botoesCadastro[1].setBounds(145, janela.getHeight() - 75, 100, 35);
        botoesCadastro[1].setText("Cadastrar");
        for (int i = 0; i < botoesCadastro.length; i++) {
            botoesCadastro[i].setBackground(Color.WHITE);
            botoesCadastro[i].setVisible(true);
            painelCadastro.add(botoesCadastro[i], 0);
        }

        campoTextoLogin = new JTextField[2];
        campoTextoSenha = new JPasswordField[2];
        //Campos Login
        campoTextoLogin[0] = new JTextField();
        campoTextoLogin[0].setBounds(113, 78, 160, 35);  //Login Nickname
        campoTextoSenha[0] = new JPasswordField();
        campoTextoSenha[0].setBounds(113, 148, 160, 35);  //Login Senha
        //Campos Cadastro
        campoTextoLogin[1] = new JTextField();
        campoTextoLogin[1].setBounds(113, 318, 160, 35);  //Cadastro Nickname
        campoTextoSenha[1] = new JPasswordField();
        campoTextoSenha[1].setBounds(113, 384, 160, 35);  //Cadastro Senha

        for (int i = 0; i < campoTextoLogin.length; i++) {
            campoTextoLogin[i].setVisible(true);
            campoTextoSenha[i].setVisible(true);
            painelCadastro.add(campoTextoLogin[i], 0);
            painelCadastro.add(campoTextoSenha[i], 0);
        }

        eventoCadastro();

        janela.add(painelCadastro, 0);

    }

    /**
     * Configura os eventos dos botões do painel (tela) "cadastro", ou seja, o código que cada um deverá executar quando for clicado.
     * O botão "entrar" faz uma consulta no banco de dados e inicia o jogo caso o login seja válido.
     * O botão "cadastrar" faz uma consulta no banco de dados para verificar se o nickname já existe, se não existir, faz o cadastro do usuário no banco de dados e por fim inicia o jogo.
     */
    public void eventoCadastro() {  //Ações dos botões da tela de cadastro e login
        /** Botão entrar */
        botoesCadastro[0].addMouseListener(new MouseAdapter() {  //Adicionando ação ao botão

            public void mousePressed(MouseEvent e) { //Função chamada quando o mouse é pressionado
                System.out.println("Botão 'Entrar' acionado");

                try {
                    pst = conexao.conn.prepareStatement("Select * from jogadores where nome = ? and senha = ?");
                    pst.setString(1, campoTextoLogin[0].getText());
                    pst.setString(2, campoTextoSenha[0].getText());
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        jogador = campoTextoLogin[0].getText();
                        JOptionPane.showMessageDialog(painelCadastro, "Bem-vindo novamente, " + jogador + "!\nBom jogo!");
                        painelCadastro.setVisible(false);
                        jogar();
                    } else {
                        JOptionPane.showMessageDialog(painelCadastro, "Nickname e Senha inválidos!");
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(painelCadastro, "Houve um erro no login!\nErro: " + ex);
                }
            }
        });
        /** Botão cadastrar */
        botoesCadastro[1].addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                System.out.println("Botão 'Cadastrar' acionado");

                if (campoTextoLogin[1].getText().length() > 2 && campoTextoLogin[1].getText().length() < 7) { //Verifica se o Nickname tem mais de 2 caracteres e menos que 8
                    try { //Verifica se já existe o nickname
                        pst = conexao.conn.prepareStatement("Select * from jogadores where nome = ?");
                        pst.setString(1, campoTextoLogin[1].getText());
                        rs = pst.executeQuery();
                        if (rs.next()) {    //Verifica se há algum registro no banco com o nickname digitado
                            JOptionPane.showMessageDialog(painelCadastro, "O Nickname já existe!\nDigite outro Nickname e tente novamente.");
                        } else {
                            try {   //Se não existir o nickname o cadastro é realizado.
                                jogador = campoTextoLogin[1].getText();
                                pst = conexao.conn.prepareStatement("insert into jogadores (nome,senha,maior_pontuacao) values (?,?,0)");
                                pst.setString(1, campoTextoLogin[1].getText());
                                pst.setString(2, campoTextoSenha[1].getText());
                                pst.executeUpdate();
                                JOptionPane.showMessageDialog(painelCadastro, "Bem-vindo, " + jogador + "!\nBom jogo!");
                                painelCadastro.setVisible(false);
                                jogar();
                            } catch (SQLException ex) { //Se houver erro no cadastro
                                JOptionPane.showMessageDialog(null, "Houve um erro no cadastro!\nErro: " + ex);
                            }
                        }
                    } catch (SQLException ex) { //Se houver erro na verificação
                        JOptionPane.showMessageDialog(painelCadastro, "Houve um erro na verificação do Nickname!\nErro: " + ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(painelCadastro, "O Nickname precisa ter no mínimo 3 caracteres e no máximo 6!");
                }
            }
        });

    }

    /**
     * Cria e configura o painel (tela) do jogo, bem como os componentes que estão dentro dele, como imagens e textos.
     * Essa tela prepara o cenário inicial do jogo.
     * No fim, libera o movimento do jogador chamando a função "mover", onde são feitas alterações frequentes nos itens do cenário.
     */ 
    public void jogar() {
        //Criando painel Jogo
        painelMenu.setVisible(false); //Vai ficar invisivel para que o outro painel apareça.
        painelJogo = new JPanel();
        painelJogo.setLayout(null); //Com layout null, posso colocar componentes onde eu quiser
        painelJogo.setBounds(0, 0, janela.getWidth(), janela.getHeight()); //Painel do tamanho da janela
        painelJogo.setVisible(true); //Deixando o painel jogar visivel

        //Pondo imagem de fundo no painel
        fundoJogo = new JLabel();
        fundoJogo.setBounds(0, 0, janela.getWidth(), janela.getHeight());	//Imagem cobre toda a janela
        imagemFundoJogo = new ImageIcon("imagens/fundoJogo.png");	//Pega imagem do caminho determinado
        imagemFundoJogo = new ImageIcon(imagemFundoJogo.getImage().getScaledInstance(janela.getWidth(), janela.getHeight(), Image.SCALE_DEFAULT));  //Pega a imagem e muda o seu tamanho para o da janela usando o algoritmo padrão de dimensionamento
        fundoJogo.setIcon(imagemFundoJogo); //Põe a imagem no JLabel
        fundoJogo.setVisible(true); //Torna o Label visivel
        painelJogo.add(fundoJogo, 0); //Põe o JLabel com a imagem de fundo no painelJogo na posição 0

        //Criando personagem
        personagem = new JLabel();
        personagem.setBounds(15, 5, 47, 48);
        imagemPersonagem = new ImageIcon("imagens/personagemAnimado.gif");
        imagemPersonagem = new ImageIcon(imagemPersonagem.getImage().getScaledInstance(47, 48, Image.SCALE_DEFAULT));
        imagemPersonagemConfuso = new ImageIcon("imagens/personagemConfuso.png");
        imagemPersonagemConfuso = new ImageIcon(imagemPersonagemConfuso.getImage().getScaledInstance(47, 48, Image.SCALE_DEFAULT));
        imagemPersonagemChateado = new ImageIcon("imagens/personagemChateado.png");
        imagemPersonagemChateado = new ImageIcon(imagemPersonagemChateado.getImage().getScaledInstance(47, 48, Image.SCALE_DEFAULT));
        personagem.setIcon(imagemPersonagem);
        personagem.setVisible(true);
        painelJogo.add(personagem, 0);

        //Criando Canos
        canoCima = new JLabel[3];
        canoBaixo = new JLabel[3];
        for (int i = 0; i < canoCima.length; i++) {
            canoCima[i] = new JLabel();
            canoBaixo[i] = new JLabel();
        }
        //Configurando Canos
        imagemCanoCima = new ImageIcon("imagens/canoCima.png");
        imagemCanoCima = new ImageIcon(imagemCanoCima.getImage().getScaledInstance(52, 270, Image.SCALE_DEFAULT));
        imagemCanoBaixo = new ImageIcon("imagens/canoBaixo.png");
        imagemCanoBaixo = new ImageIcon(imagemCanoBaixo.getImage().getScaledInstance(52, 318, Image.SCALE_DEFAULT));

        for (int i = 0; i < canoCima.length; i++) {
            canoCima[i].setBounds(janela.getWidth() + (imagemCanoCima.getIconWidth() * (i + 1) * 4), 0, imagemCanoCima.getIconWidth(), imagemCanoCima.getIconHeight());  //"janela.getWidth() +" porque eu quero que crie os canos fora da janela. "(larguraCano*(i+1)*4)" para que a distância entre um cano e outro seja de 4 canos. A variável "i" faz com que a cada repetição os novos canos fiquem cada vem mais distantes da janela (mais pra direita)
            canoCima[i].setIcon(imagemCanoCima);
            canoCima[i].setVisible(true);
            painelJogo.add(canoCima[i], 0);

            canoBaixo[i].setBounds(janela.getWidth() + (imagemCanoBaixo.getIconWidth() * (i + 1) * 4), janela.getHeight() + imagemCanoCima.getIconHeight(), imagemCanoBaixo.getIconWidth(), imagemCanoBaixo.getIconHeight());    //janela.getHeigh()+alturaCanoCima para que sua posição na vertical inicie bem abaixo do cano de cima.
            canoBaixo[i].setIcon(imagemCanoBaixo);
            canoBaixo[i].setVisible(true);
            painelJogo.add(canoBaixo[i], 0);
        }

        randomYCano1 = gerador.nextInt(canoCima[0].getHeight() - 10);
        randomYCano2 = gerador.nextInt(canoCima[1].getHeight() - 10);
        randomYCano3 = gerador.nextInt(canoCima[2].getHeight() - 10);

        //chao
        imagemChao = new ImageIcon("imagens/chao.png");
        imagemChao = new ImageIcon(imagemChao.getImage().getScaledInstance(768, 112, Image.SCALE_DEFAULT));
        chao = new JLabel();
        chao.setBounds(0, janela.getHeight() - imagemChao.getIconHeight(), 768, 112);
        chao.setIcon(imagemChao);
        chao.setVisible(true);
        painelJogo.add(chao, 0);

        //pontuacao
        pontos = 0;
        pontuacao = new JLabel();
        pontuacao.setBounds(janela.getWidth() - 50, 5, 90, 45);
        pontuacao.setFont(new Font("Dialog", Font.BOLD, 27)); //Editando a fonte
        pontuacao.setForeground(Color.WHITE);   //Mudando a cor da fonte
        pontuacao.setVisible(true);
        pontuacao.setText("" + pontos);
        painelJogo.add(pontuacao, 0);

        janela.add(painelJogo);

        status = 1;   //Diz se o game está iniciando, se está rodando, se está pausado ou se deu game over.

        mover();
    }
    
    /**
     * Configura os eventos (o que deverá ser executado) ao pressionar ou soltar a tecla espaço, e inicia um timer que a cada 40 milissegundos executa o trecho de código que está
     * dentro dele, que correponde a atualização dos itens do cenário, como a mudança da posição (movimento) das imagens referentes ao personagem e aos canos (obstáculos), e alteração dos textos presentes na tela.
     * A cada execução do timer também é checado se houve colisão do personagem com algum obstáculo, se sim, o timer é parado (o trecho de código dentro dele não é mais executado a cada
     * 40 milissegundos, ou seja, os itens não se movimentam mais) e se o jogador tiver batido seu recorde, sua pontuação é salva no banco de dados.
     */
    public void mover() {

        System.out.println("Timer ligado!");

        timer = new Timer(40, new ActionListener() {  //É tipo uma repetição infinita, um ciclo que se repete ao passar o tempo (40 milissegundos)
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Timer rodando!"); // P/ testar
                //System.out.println(velocidade);   // P/ testar
                /** Se o jogo estiver em andamento */
                if (status == 1) {
                    /** Personagem - Controla a velocidade */
                    if (pressionado == 0 && velocidade < 20) {    //Quando não tiver sendo pressionado e a velocidade for menor que 20 (limite), adiciona +1.
                        velocidade += 1;                     //Fará o personagem cair gradativamente com velocidade limite de 20
                    } else if (pressionado == 1 && velocidade > -20) { //Quando tiver sendo pressionado e a velocidade maior que -20 (limite), adiciona -2 a velocidade.
                        velocidade -= 2;    //Fará o personagem ir para cima com velocidade limite de -20
                    }
                    /** Personagem - Muda a posição do personagem */
                    personagem.setBounds(personagem.getX(), personagem.getY() + velocidade, personagem.getWidth(), personagem.getHeight());

                    /** Faz o chão se mover */
                    if (chao.getX() > personagem.getX() - 256) {   //256 é a metdade da largura da janela. Em quanto não percorre a metade da janela ele fica tirando 5 da posição X (horizontal) do chao
                        chao.setBounds(chao.getX() - 5, chao.getY(), chao.getWidth(), chao.getHeight());
                    } else { //Se já tiver percorrido a metade da janela
                        chao.setBounds(0, chao.getY(), chao.getWidth(), chao.getHeight());
                    }

                    /** Move os canos 0 (Cima e baixo) */
                    if (canoCima[0].getX() > -canoCima[0].getWidth()) {    //Se status==1 e se ele não tiver passado e saido da tela
                        canoCima[0].setBounds(canoCima[0].getX() - 5, -randomYCano1, canoCima[0].getWidth(), canoCima[0].getHeight());
                        canoBaixo[0].setBounds(canoBaixo[0].getX() - 5, canoCima[0].getY() + canoCima[0].getHeight() + 100, canoBaixo[0].getWidth(), canoBaixo[0].getHeight()); //"+100" é o espaço dado entre o cano de cima e o cano de baixo
                    } else {  //Se tiver saido da tela e o jogo tiver rodando
                        //Gera nova posição Y
                        randomYCano1 = gerador.nextInt(canoCima[0].getHeight() - 100);
                        //Move de volta para fora da tela na direita e para uma nova posição Y (vertical)
                        canoCima[0].setBounds(janela.getWidth() + (canoCima[0].getWidth() * 4), -randomYCano1, canoCima[0].getWidth(), canoCima[0].getHeight());
                        canoBaixo[0].setBounds(janela.getWidth() + (canoBaixo[0].getWidth() * 4), canoCima[0].getY() - canoCima[0].getHeight() - 100, canoBaixo[0].getWidth(), canoBaixo[0].getHeight());
                    }
                    /** Move os canos 1 (Cima e baixo) */
                    if (canoCima[1].getX() > -canoCima[0].getWidth()) {    //Se status==1 e se ele não tiver passado e saido da tela
                        canoCima[1].setBounds(canoCima[1].getX() - 5, -randomYCano2, canoCima[1].getWidth(), canoCima[1].getHeight());
                        canoBaixo[1].setBounds(canoBaixo[1].getX() - 5, canoCima[1].getY() + canoCima[1].getHeight() + 100, canoBaixo[1].getWidth(), canoBaixo[1].getHeight());
                    } else {  //Se tiver saido da tela e o jogo tiver rodando
                        randomYCano2 = gerador.nextInt(canoCima[1].getHeight() - 100); //Gera nova posição Y
                        canoCima[1].setBounds(janela.getWidth() + (canoCima[1].getWidth() * 4), -randomYCano2, canoCima[1].getWidth(), canoCima[1].getHeight());
                        canoBaixo[1].setBounds(janela.getWidth() + (canoCima[1].getWidth() * 4), canoCima[1].getY() - canoCima[1].getHeight() - 100, canoBaixo[1].getWidth(), canoBaixo[1].getHeight());
                    }
                    /** Move os canos 2 (Cima e baixo) */
                    if (canoCima[2].getX() > -canoCima[0].getWidth()) {    //Se status==1 e se ele não tiver passado e saido da tela
                        canoCima[2].setBounds(canoCima[2].getX() - 5, -randomYCano3, canoCima[2].getWidth(), canoCima[2].getHeight());
                        canoBaixo[2].setBounds(canoBaixo[2].getX() - 5, canoCima[2].getY() + canoCima[2].getHeight() + 100, canoBaixo[2].getWidth(), canoBaixo[2].getHeight());
                    } else {  //Se tiver saido da tela e o jogo tiver rodando
                        randomYCano3 = gerador.nextInt(canoCima[2].getHeight() - 100); //Gera nova posição Y
                        canoCima[2].setBounds(janela.getWidth() + (canoCima[2].getWidth() * 4), -randomYCano3, canoCima[2].getWidth(), canoCima[2].getHeight());
                        canoBaixo[2].setBounds(janela.getWidth() + (canoBaixo[2].getWidth() * 4), canoCima[2].getY() - canoCima[2].getHeight() - 100, canoBaixo[2].getWidth(), canoBaixo[2].getHeight());
                    }

                    //System.out.println(""+canoCima[2].getX()+" < -37 e "+canoCima[2].getX()+" > -42"); //P/ testar condição de adicionar ponto com o cano 3
                    /** Se houver colisão do personagem com o cano, ou o personagem tiver saido da janela*/
                    if (colisao(canoCima[0]) || colisao(canoCima[1]) || colisao(canoCima[2]) || colisao(canoBaixo[0]) || colisao(canoBaixo[1]) || colisao(canoBaixo[2]) || colisao(chao) || personagem.getY() < -personagem.getHeight()) {  //Se algum cano colidir com o personagem
                        status = 2;
                        opcaoGameOver = 1;  //O valor padrão (1) fará o jogo voltar para o menu. Se for 0, o jogo é reiniciado.
                        if (pontos<=5){
                        personagem.setIcon(imagemPersonagemChateado);}
                        else {
                        personagem.setIcon(imagemPersonagemConfuso);
                        }

                        try {
                            conexao.executaSQL("select j.maior_pontuacao from jogadores j where j.nome='" + jogador + "'");
                            conexao.rs.first();
                            if (pontos > conexao.rs.getInt("maior_pontuacao")) {
                                System.out.println("'pontos' é maior que 'maior_pontuacao'.");
                                pst = conexao.conn.prepareStatement("update jogadores set maior_pontuacao=? where nome='" + jogador + "'"); //Atualizando maior pontuação
                                pst.setInt(1, pontos);
                                pst.executeUpdate();
                                opcaoGameOver = JOptionPane.showConfirmDialog(painelJogo, "GameOver! Mas... \nNovo recorde, parabéns!!\nVocê fez " + pontos + " pontos!\n\nDeseja jogar novamente?");
                                if (opcaoGameOver == 0) {
                                    status = 1; //Fará o jogo voltar a rodar.
                                } else {
                                    System.out.println("Usuário bateu o recorde. Voltando para o Menu.");
                                    painelJogo.setVisible(false);
                                    painelMenu.setVisible(true);
                                    timer.stop();
                                    System.out.println("Timer desligado!");
                                }
                            } else {
                                opcaoGameOver = JOptionPane.showConfirmDialog(painelJogo, "GameOver!\nVocê fez " + pontos + " pontos.\nSua pontuação recorde é de: " + conexao.rs.getInt("maior_pontuacao") + " pontos\n\nDeseja jogar novamente?");
                                if (opcaoGameOver == 0) {
                                    status = 1;
                                } else {
                                    System.out.println("Usuário não bateu o recorde. Voltando para o Menu.");
                                    painelJogo.setVisible(false);
                                    painelMenu.setVisible(true);
                                    timer.stop();   //Para o timer (Para a execução do trecho de código a cada alguns milissegundos)
                                    System.out.println("Timer desligado!");
                                }
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(painelJogo, "Não foi possível acessar ou atualizar a 'maior_pontuacao' do jogador!\nErro: " + ex);
                        }

                        //Resetando variáveis e as posições dos label.
                        personagem.setIcon(imagemPersonagem);
                        pontos = 0;
                        pontuacao.setText("" + pontos);
                        velocidade = 0;
                        pressionado = 0;
                        for (int i = 0; i < canoCima.length; i++) {                            
                            canoCima[i].setBounds(janela.getWidth() + (imagemCanoCima.getIconWidth() * (i + 1) * 4), 0, imagemCanoCima.getIconWidth(), imagemCanoCima.getIconHeight());
                            canoBaixo[i].setBounds(janela.getWidth() + (imagemCanoBaixo.getIconWidth() * (i + 1) * 4), janela.getHeight() + imagemCanoCima.getIconHeight(), imagemCanoBaixo.getIconWidth(), imagemCanoBaixo.getIconHeight());
                        }
                        personagem.setBounds(15, 5, 47, 48);

                    } //Se não houve colisão, então verifica se o jogador ganhou ponto (se o cano passou do jogador)
                    else if ((canoCima[0].getX() < (personagem.getX() - canoCima[0].getWidth()) && canoCima[0].getX() >= (10 - canoCima[0].getWidth())) || (canoCima[1].getX() < 15 - 52 && canoCima[1].getX() >= 10 - 52) || (canoCima[2].getX() < 15 - 52 && canoCima[2].getX() >= 10 - 52)) {    //O valor no lugar do '10' foi sendo testado para funcionar o incremento apenas quando o cano tiver acabado de passar do pássaro, e não sempre que o cano estiver na esquerda do pássaro
                        pontos++;
                        pontuacao.setText("" + pontos);
                    }
                }//Fecha if do Status == 1
            }
        });

        timer.start();  //Iniciando o timer
        janela.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {    //Quando alguma tecla for pressionada

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {  //Se a tecla espaço foi pressionada
                    //System.out.println("Espaço acionado");
                    pressionado = 1;    //Fará com que no timer acima, a posição do personagem seja alterada
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {   //Quando alguma tecla for solta

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {  //Se a tecla espaço foi solta
                    //System.out.println("Soltou o espaço");
                    pressionado = 0;
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });

    }
    
    /**
     * Checa se está havendo colisão do personagem com o componente passado por parâmetro, se sim, retorna true.
     * Tutorial: https://youtu.be/OEg4ySDkmaM de Carlos Henrique Java
     * @param componenteB
     * @return boolean
     */
    public boolean colisao(Component componenteB) {
        Component a = personagem;   //Personagem é o componente a
        int aX = a.getX();
        int aY = a.getY();
        int ladoDireitoA = aX + a.getWidth();
        int ladoEsquerdoA = aX;
        int ladoBaixoA = aY + a.getHeight();
        int ladoCimaA = aY;

        int posicaoXComponenteB = componenteB.getX();
        int posicaoYComponenteB = componenteB.getY();
        int ladoDireitoComponenteB = posicaoXComponenteB + componenteB.getWidth();
        int ladoEsquerdoComponenteB = posicaoXComponenteB;
        int ladoBaixoComponenteB = posicaoYComponenteB + componenteB.getHeight();
        int ladoCimaComponenteB = posicaoYComponenteB;

        boolean bateu = false;

        boolean colidiuDireita = false;
        boolean colidiuCima = false;
        boolean colidiuBaixo = false;
        boolean colidiuEsquerda = false;

        if (ladoDireitoA >= ladoEsquerdoComponenteB) {    //Quer dizer que o personagem está na direita do ladoEsquerdoComponenteB
            colidiuDireita = true;
        }
        if (ladoEsquerdoA <= ladoDireitoComponenteB) {    //Quer dizer que o personagem está na esquerda do ladoDireitoComponenteB
            colidiuEsquerda = true;
        }
        if (ladoCimaA <= ladoBaixoComponenteB) {  //Quer dizer que o personagem está acima do ladoBaixoComponenteB
            colidiuCima = true;
        }
        if (ladoBaixoA >= ladoCimaComponenteB) {  //Quer dizer que o personagem está abaixo do ladoCimaComponenteB
            colidiuBaixo = true;
        }
        
        /** Se houve colisão (se o personagem está invadindo a posição do ComponenteB) */
        if (colidiuDireita && colidiuEsquerda && colidiuBaixo && colidiuCima) {
            bateu = true;
        }

        return bateu;
    }

}
