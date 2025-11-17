import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Barreira implements Elementos {
    private ArrayList<Point> blocos;
    private int x;
    private int y;
    private final int passo;

    public Barreira(int x, int y, int passo) {
        this.x = x;
        this.y = y;
        this.passo = passo;
        this.blocos = new ArrayList<>();
    }

    public void adicionarBloco(int x, int y) {
        blocos.add(new Point(x, y));
    }

    public void adicionaBarreiraAleatoria(int painelWidth, int painelHeight, int passo, ArrayList<Point> snakeSegments,
            int macaX, int macaY) {
                
        Random rand = new Random();
        int colunas = painelWidth / passo;
        int linhas = painelHeight / passo;

        while (true) {
            int novaCols = rand.nextInt(colunas);
            int novaRow = rand.nextInt(linhas);

            x = novaCols * passo;
            y = novaRow * passo;

            if (y < 60)
                continue;

            ArrayList<Point> novaBarreira = new ArrayList<>();
            boolean horizontal = rand.nextBoolean(); // 0 horizontal, 1 vertical;
            boolean colisao = false;

            for (int i = 0; i < 3; i++) {
                int novoX = x + (horizontal ? i * passo : 0);
                int novoY = y + (horizontal ? 0 : i * passo);

                
                for (Point p : snakeSegments) {
                    if (p.x == novoX && p.y == novoY) {
                        colisao = true;
                        break;
                    }
                }

                if (x == macaX && y == macaY) {
                    colisao = true;
                }


                if (colisao) {
                    break;
                }
                
                novaBarreira.add(new Point(novoX, novoY));
            }
            if(!colisao){
                blocos.addAll(novaBarreira);
                return;
            }
        }
    }

    @Override
    public void desenharElement(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (Point p : blocos) {
            g.fillRect(p.x, p.y, passo, passo);
        }
    }

    public ArrayList<Point> getBlocos() {
        return blocos;
    }
}
