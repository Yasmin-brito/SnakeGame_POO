# SnakeGame_POO
# 🐍 Snake Game Java

Um jogo clássico da **Cobra (Snake)** desenvolvido em **Java com Swing**.  
O jogador controla a cobra, deve comer maçãs para crescer e acumular pontos, enquanto desvia de barreiras que surgem ao longo da partida.

---

## 🧩 Funcionalidades

- 🐍 Movimento contínuo da cobra em uma grade de 40x40 pixels  
- 🍎 Geração aleatória de maçãs, sempre em posições válidas  
- 🧱 Barreiras que aparecem a cada 3 maçãs comidas (as antigas permanecem)  
- 💥 Fim de jogo quando a cobra colide consigo mesma ou com uma barreira  
- 🧾 Exibição da pontuação atual na tela  
- 🔁 Tela de “Game Over” com opção de **Reiniciar** ou **Fechar** o jogo  
- 🖼️ Suporte a imagem personalizada de maçã (`img/apple.png`)  
- 🚫 Geração de elementos respeitando os limites visíveis do painel  

---

## 🕹️ Como Jogar

| Tecla | Ação |
|-------|------|
| ⬆️ | Mover para cima |
| ⬇️ | Mover para baixo |
| ⬅️ | Mover para esquerda |
| ➡️ | Mover para direita |

### Regras:
- A cada maçã comida, a cobra cresce em tamanho e a pontuação aumenta.  
- A cada **3 maçãs comidas**, uma nova barreira é adicionada no campo.
- A cada **5 maçãs comidas**, a velocidade diminui em 13s.  
- O jogo termina se a cobra colidir com seu corpo ou com uma barreira.  
- Após o “Game Over”, escolha **“Reiniciar”** para jogar novamente ou **“Fechar”** para encerrar.

---

## 🧠 Estrutura do Projeto

src/
├── Main.java **Classe principal (inicializa o JFrame)**
├── JogoPainel.java **Painel do jogo (lógica principal e desenho)**
├── Cobra.java **Classe da cobra (movimento e crescimento)**
├── Maca.java **Classe da maçã (posição e renderização)**
├── Barreira.java **Classe das barreiras (criação e colisões)**
└── Elementos.java **Interface base para elementos do jogo**
