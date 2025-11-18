import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Point;

public class Cobra implements Elementos {
    private int x, y;
    private final int passo;
    private double dx = 0, dy = 0;
    private double lastDx = 1, lastDy = 0;
    private boolean crescendo = false;
    private ArrayList<Point> segmentos;

    public Cobra (int x, int y, int passo){
        this.x = x;
        this.y = y;
        this.passo = passo;
        segmentos = new ArrayList<>(); 
        segmentos.add(new Point(x, y));
    }

	@Override
	public void desenharElement(Graphics g) {
        g.setColor(new Color(255,127,0));

        for (Point p : segmentos){
            g.fillRoundRect(p.x, p.y, passo, passo, 30, 30);
        }
	}

    public void setDirecao(double dx, double dy){
        if (dx != -lastDx || dy != -lastDy) {
            this.dx = dx;
            this.dy = dy;
            lastDx = dx;
            lastDy = dy;
        }
    }
    public void mover(int largura, int altura, Maca maca){
        x += dx * passo;
        y += dy * passo;

        if(x < 0){
            x = largura - passo;
        }
        if(x >= largura){
            x = 0;
        }
        if(y >= altura){
            y = 0;
        }
        if(y < 0){
            y = altura - passo;

        }
        segmentos.add(0, new Point(x, y));


        if(!crescendo && segmentos.size() > 1){
            segmentos.remove(segmentos.size() -1);
        }
        crescendo = false;

    }

    public boolean bateuNoCorpo(){
        Point cabeca = getHead();
        for(int i = 1; i < segmentos.size(); i++){
            if (cabeca.equals(segmentos.get(i))) {
                return true;
            }
        }
        return false;
    }
    public boolean verificaColisao(Barreira barreira){
        Point cabeca = getHead();
        for(Point bloco : barreira.getBlocos()){
            if (cabeca.equals(bloco)) {
                return true;
            }
        }
        return false;
    }

    public void grow(){
        crescendo = true;

    }

    public void resetar(int startX, int startY){
        segmentos.clear();
        x = startX;
        y = startY;
        dx = 1;
        dy = 0;
        lastDx = 1;
        lastDy = 0;
        segmentos.add(new Point(x, y));
    }

    public Point getHead(){
        return segmentos.get(0);
    }
    
    public ArrayList<Point> getSegmentos(){
        return segmentos;
    }
}
