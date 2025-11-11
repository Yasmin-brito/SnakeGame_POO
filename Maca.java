import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.ImageObserver;

public class Maca implements Elementos {
    private int x;
    private int y;
    private final int passo;
    private Image appleImage;
    private ImageObserver observer;

    public Maca(int x, int y, int passo){
        this.x = x;
        this.y = y;
        this.passo = passo;

        try {
            appleImage = ImageIO. read(new File("img/apple.png"));
        }
        catch(IOException e){
            appleImage = null;
        }
    }

	@Override
	public void desenharElement(Graphics g) {
        if(appleImage != null){
            g.drawImage(appleImage, x, y, passo, passo, observer);
        } else{
            g.setColor(Color.RED);
            g.fillOval(x, y, passo, passo);
        }
        
	}

    public void spawnRandom(int painelWidth, int painelHeight, int passo, ArrayList<Point> snakeSegments, ArrayList<Point> barreiras){
        Random rand = new Random();

        int colunas = (painelWidth / passo)-1 ;
        int linhas = (painelHeight / passo) -1;

        while(true){
            int novaCols = rand.nextInt(colunas);
            int novaRow = rand.nextInt(linhas);

            x = novaCols * passo;
            y = novaRow * passo;

            if (y < 60) continue;

            Point ponto = new Point(x, y);
            boolean colisao = false;

            for (Point p : snakeSegments){
                if (ponto.equals(p)) {
                    colisao = true;
                    break;
                }
            }
            if(!colisao && barreiras != null){
                for(Point b : barreiras){
                    if(ponto.equals(b)){
                        colisao = true;
                        break;
                    }
                }
            }

            if (!colisao){
                break;
            }
        }

    }

    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
}
