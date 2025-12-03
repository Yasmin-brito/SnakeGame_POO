import java.io.*;
import java.util.*;

public class HistoricoPontuacao {
    private static final String arquivo = "score/pontuacoes.txt";

    public static void salvarPontuacao(String nome, int score) {
        List<Recorde> lista = carregar();

        if (nome == null || nome.trim().isEmpty()) {
            nome = "Jogador";
        }

        String nomeTrim = nome.trim();
        lista.removeIf(r -> r.getNome().equalsIgnoreCase(nomeTrim));
        lista.add(new Recorde(nomeTrim, score));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, false))) {
            for (Recorde r : lista) {
                bw.write(r.getNome() + ":" + r.getPontos());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Recorde> carregar() {
        List<Recorde> lista = new ArrayList<>();

        File f = new File(arquivo);
        if (!f.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(":");
                if (partes.length == 2) {
                    try {
                        lista.add(new Recorde(partes[0], Integer.parseInt(partes[1])));
                    } catch (NumberFormatException ex) {
                        // ignora linha inválida
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        lista.sort((a, b) -> Integer.compare(b.getPontos(), a.getPontos()));
        return lista;
    }

    public static List<Recorde> carregarTop5() {
        List<Recorde> lista = carregar();
        return lista.subList(0, Math.min(5, lista.size()));
    }
}
