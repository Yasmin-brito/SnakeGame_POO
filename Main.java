import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

       String nomeJogador;
        do{
            nomeJogador = JOptionPane.showInputDialog(null, "Digite o nome da sua cobra: ");
        } while(nomeJogador == null || nomeJogador.trim().isEmpty());
        
        painel.setNomeJogador(nomeJogador);
        painel.starGame();
    }
}
