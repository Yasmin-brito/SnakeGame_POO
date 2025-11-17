import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

    private Cobra cobra;
    private final Maca maca;
    private final Barreira barreira;
    private final Timer timer;
    
    public JogoPainel(){
        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);
        addKeyListener(this);

        int startX = 200;
        int startY = 160;

        cobra = new Cobra(startX, startY, passo);
        cobra.setDirecao(1, 0);

        maca = new Maca(startX + 160, startY + 160, passo);
        barreira = new Barreira(startX, startY, passo);

        timer = new Timer(velocidade, this);
        timer.start();
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
        g.setColor(Color.GRAY);

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
        int novoDelay = Math.max(50, timer.getDelay() -13);
        timer.setDelay(novoDelay);
    }

    private void gameOver (Graphics g){
        gameOver = true;
        timer.stop();
        repaint();
        String[] opcoes = {"Reiniciar", "Fechar"};

        int escolha = JOptionPane.showOptionDialog( this, "Game Over!\nPontuação final: " + pontuacao + "\n\nO que deseja fazer?", "Fim de jogo",
        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

        if (escolha == JOptionPane.YES_OPTION) {
            reiniciarJogo();

        } else {
            System.exit(0);
        }
    }

    private void reiniciarJogo(){
        pontuacao = 0;
        gameOver = false;
        cobra.resetar(200, 160);
        maca.spawnRandom(largura, altura, passo, cobra.getSegmentos(), barreira.getBlocos());
        barreira.getBlocos().clear();
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

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
