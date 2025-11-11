import javax.swing.JFrame;

public class Main extends JFrame {
    public static void main(String[] args) {
        JogoPainel painel = new JogoPainel();
        
        Main A = new Main();
        A.add(painel);
        A.setSize(1200,800);
        A.setDefaultCloseOperation(EXIT_ON_CLOSE);
        A.setVisible(true);
        A.setResizable(false);
        A.setLocationRelativeTo(null);

    }
}
