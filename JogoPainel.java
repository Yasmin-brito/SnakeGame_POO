import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class JogoPainel extends JPanel implements KeyListener, ActionListener{
    private final int passo = 40;
    private final int largura = 1200;
    private final int altura = 800;
    private final int velocidade = 250;

    private int pontuacao;
    private boolean gameOver = false;
    private boolean mostrandoRanking = false;
    private boolean rankingFromGameOver = false;
    private String nomeJogador;

    private BufferedImage imagemDeFundo;
    private BufferedImage imagemRanking;

    private Cobra cobra;
    private final Maca maca;
    private final Barreira barreira;
    private final Timer timer;
    
    public JogoPainel(){
        try {
            imagemDeFundo = ImageIO.read(new File("img/fotoFundo.jpg"));
        } catch (Exception e) {
            imagemDeFundo = null;
        }
        setFocusable(true);
        addKeyListener(this);

        int startX = 200;
        int startY = 160;

        cobra = new Cobra(startX, startY, passo);
       
        maca = new Maca(startX + 160, startY + 160, passo);
        barreira = new Barreira(startX, startY, passo);

        timer = new Timer(velocidade, this);
    }

    public void starGame(){
        if (!timer.isRunning()) {
            timer.start();
            cobra.setDirecao(1, 0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (gameOver) {
            return;
        }
        atualizarJogo();
        repaint();
    }
    private void atualizarJogo(){
        try {
            cobra.mover(largura, altura, maca);

            if (pegouApple(cobra, maca)) {
                pontuacao++;
                if (pontuacao % 3 == 0) {
                    barreira.adicionaBarreiraAleatoria(largura, altura, passo, cobra.getSegmentos(), maca.getX(), maca.getY());
                }

                if(pontuacao % 5 == 0){
                    aumentarVelocidade();
                }

            }

            verificarColisoes();

        } catch (RuntimeException ex) {
            if ("colidiu".equals(ex.getMessage())) {
                gameOver(null);
            }
        }
    }

    private void verificarColisoes(){
        if (cobra.bateuNoCorpo() || cobra.verificaColisao(barreira)) {
            gameOver(null);
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(imagemDeFundo != null){
            g.drawImage(imagemDeFundo, 0, 0, largura, altura, this);
        } else{
            g.setColor(Color.LIGHT_GRAY);
        }

        if(mostrandoRanking){
            desenharRanking(g);
            return;
        }

        desenharElementos(g);
        desenharPontuacao(g);
    }

    private void desenharElementos(Graphics g){
        cobra.desenharElement(g);
        maca.desenharElement(g);
        barreira.desenharElement(g);
    }

    private void desenharPontuacao(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Pontuação: " + pontuacao, 20, 30);
    }

    private void desenharRanking(Graphics g){
        try {
            imagemRanking = ImageIO.read(new File("img/background.jpg"));
            if(imagemRanking != null){
                g.drawImage(imagemRanking, 0, 0, largura, altura, this);
            }
        } catch (Exception e) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        g.setColor(new Color(92,51,23));
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("TOP 5 - RANKING", 460, 120);

        List<Recorde> top5 = HistoricoPontuacao.carregarTop5();

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        int y = 230;

        if (top5.isEmpty()) {
            g.drawString("Nenhum recorde registrado ainda", 350, y);
        } else {
            for (int i = 0; i < top5.size(); i++) {
                Recorde r = top5.get(i);
                g.drawString((i + 1) + "º  " + r.getNome() + " — " + r.getPontos() + " pontos", 250, y);
                y += 50;
            }
        }

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("[V] Voltar", 250, y + 80);
    }

    private boolean pegouApple(Cobra cobra, Maca maca){
        Point cabeca = cobra.getHead();
        if(cabeca.x == maca.getX() && cabeca.y == maca.getY()){
            cobra.grow();
            maca.spawnRandom(largura, altura, passo, cobra.getSegmentos(), barreira.getBlocos());
            return true;
        }
        return false;
    }

    private void aumentarVelocidade(){
        int novoDelay = Math.max(50, timer.getDelay() -15);
        timer.setDelay(novoDelay);
    }

    private void gameOver(Graphics g){
        gameOver = true;
        timer.stop();
        mostrarOpcoesGameOver(null);
        repaint();
    }

    private void mostrarOpcoesGameOver(Graphics g){
        HistoricoPontuacao.salvarPontuacao(nomeJogador, pontuacao);

        String[] opcoes = {"Ver Ranking", "Reiniciar", "NovoJogo", "Sair"};
        
        int escolha = JOptionPane.showOptionDialog(
            this,
            "Game Over!\n\nPontuação final: " + pontuacao + 
            "\n\nO que deseja fazer?",
            "Fim de Jogo",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );

        if (escolha == 0) {  
            mostrandoRanking = true;
            rankingFromGameOver = true;
            repaint();
        } 
        else if (escolha == 1) {  
            reiniciarJogo(); 

        } else if(escolha == 2){
            novoJogo();
        }
        else if (escolha == 3 || escolha == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
    }

    private void novoJogo (){
        do{
            nomeJogador = JOptionPane.showInputDialog(null, "Digite o nome da sua cobra: ");
        } while(nomeJogador == null || nomeJogador.trim().isEmpty());
        
        setNomeJogador(nomeJogador);
        reiniciarJogo();
        starGame();
    }


    private void reiniciarJogo(){
        pontuacao = 0;
        gameOver = false;
        mostrandoRanking = false;
        rankingFromGameOver = false;

        timer.setDelay(240);
        cobra.resetar(200, 160);

        barreira.getBlocos().clear();
        maca.spawnRandom(largura, altura, passo, cobra.getSegmentos(), barreira.getBlocos());
        
        timer.start();
        repaint();
    }

    public void setNomeJogador(String nome){
        this.nomeJogador = nome;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

        if(mostrandoRanking){
            if(tecla == KeyEvent.VK_V){
                if (rankingFromGameOver) {
                    // volta ao menu de opções (sem gravar novamente)
                    mostrandoRanking = false;
                    rankingFromGameOver = false;
                    mostrarOpcoesGameOver(null);
                } else {
                    // apenas fecha a tela de ranking e volta ao jogo
                    mostrandoRanking = false;
                    repaint();
                }
            }
            return;
        }

        switch (tecla) {
            case KeyEvent.VK_UP:
                cobra.setDirecao(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                cobra.setDirecao(0, 1);
                break;
            case KeyEvent.VK_LEFT:
               cobra.setDirecao(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
               cobra.setDirecao(1, 0);
                break;
            default:
                break;
        } 
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
