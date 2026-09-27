# Jogo das Moedas — versão 1: **acoplada**

Projeto de apoio da **Aula 08 — Design de Classes: Acoplamento e Coesão**
Linguagem de Programação II — IMD/UFRN

Este é o **"antes"**. Um jogo de console em que você anda pelo mapa (`@`) e coleta moedas (`$`),
escrito do jeito que quase todo mundo escreve na primeira versão: uma classe fazendo tudo.

> O projeto funciona. O problema não é ele não rodar — é o que acontece quando alguém
> precisa **mudar** alguma coisa nele.

---

## Como executar o jogo

```bash
cd jogo-das-moedas-v1
javac -d bin $(find src/main/java -name "*.java")
java -cp bin game.MainGame
```

Escolha o nivel inicial passando seu numero (de 1 a 5). Ao coletar todas as
moedas, o jogo avanca automaticamente ate o ultimo nivel:

```bash
java -cp bin game.MainGame 3
```

Os mapas ficam em `levels.txt`, na raiz do projeto, e sao separados por uma
linha `---`. Use `#` para paredes, `.` para chao, `@` para o jogador e `$`
para moedas. O nivel 1 e usado quando nenhum numero e informado.

**Controles:** `w` (cima), `a` (esquerda), `s` (baixo), `d` (direita), `q` (sair).
Um comando por vez, seguido de Enter.

---

---

## O que observar no código

Abra `Game.java` e conte quantas coisas diferentes ela faz:

| # | Responsabilidade | Linha aproximada |
|---|---|---|
| 1 | Guardar o mapa | campo `mapa` |
| 2 | Guardar posição e pontuação do jogador | campos `jogadorX`, `jogadorY`, `pontos` |
| 3 | Criar o `Scanner` e ler o teclado | campo `teclado` |
| 4 | Traduzir tecla em movimento | `waitCommand` |
| 5 | Verificar colisão com parede | `waitCommand` |
| 6 | Controlar a coleta de moedas | `waitCommand` |
| 7 | Desenhar na tela | `draw` |
| 8 | Controlar o laço do jogo | `run` |

**Oito responsabilidades numa classe só.** Isso é *baixa coesão*.

E a linha que causa mais estrago:

```java
private final Scanner teclado = new Scanner(System.in);
```

`Game` **decidiu sozinha** que a entrada vem do teclado. Isso é *alto acoplamento*.

---

## As perguntas da aula

Antes de olhar a versão 2, tente responder — de preferência tentando escrever o código:

1. **"O jogo vai ganhar suporte a joystick e a controle por toque na tela.
   Quantos arquivos você precisa abrir?"**

2. **"Escreva um teste que prove que cada moeda vale exatamente 10 pontos."**
   (Dica: procure por um `getPontos()`. Não existe.)

3. **"Escreva um teste que prove que mover para a direita aumenta X em 1,
   sem passar pelo laço do jogo inteiro."**
   (Dica: `waitCommand` é `private`.)

4. **"Rodar os testes exige alguém digitando no teclado?"**
   Olhe o `@BeforeEach` do `GameTest` e veja o tamanho da gambiarra necessária para evitar isso.

5. **"Amanhã o jogo vira uma aplicação gráfica em vez de console.
   Quanto da classe `Game` sobrevive?"**

---

## Próximo passo

Depois de responder às perguntas acima, abra o projeto **`jogo-das-moedas-v2`**.
É o mesmo jogo, com o mesmo mapa, as mesmas regras e o mesmo resultado na tela —
mas reorganizado. Compare os dois `GameTest.java` lado a lado: é ali que a diferença aparece.
