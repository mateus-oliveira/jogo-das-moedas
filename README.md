# Jogo das Moedas — versão 2: **desacoplada**

Projeto de apoio da **Aula 08 — Design de Classes: Acoplamento e Coesão**
Linguagem de Programação II — IMD/UFRN

Este é o **"depois"**. O jogo agora oferece cinco mapas carregados de um
arquivo TXT. Na versão desacoplada, cada classe mantém uma responsabilidade
clara: `Grid` representa o cenário, `Level` executa uma fase e `MainGame`
coordena a partida inteira.

---

## Como executar o jogo

```bash
cd jogo-das-moedas-v2
javac -d bin $(find src/main/java -name "*.java")
java -cp bin game.MainGame
```

Os cinco mapas ficam em `levels.txt`, na raiz do projeto, separados por uma
linha `---`. Use `#` para paredes, `.` para chao, `@` para o jogador e `$`
para moedas. O nivel 1 e o padrao. Para iniciar em outro nivel, informe o
numero depois do modo de entrada/saida; a progressao segue ate o ultimo:

```bash
java -cp bin game.MainGame terminal 3
java -cp bin game.MainGame gui 5
```

### Trocando entrada e saída

Esta é a demonstração principal. O **mesmo jogo**, com três pares diferentes
de entrada/saída:

```bash
java -cp bin game.MainGame              # teclado + terminal, nivel 1
java -cp bin game.MainGame gui          # botoes clicaveis + janela grafica
java -cp bin game.MainGame joystick     # joystick virtual (mouse) + janela grafica
```

Pelo Eclipse: `Run As` → `Run Configurations...` → aba `Arguments` →
escreva `gui` ou `joystick` em *Program arguments*.

> Em cada modo, o jogo abre janelas diferentes (`GUIRenderer`, `ButtonInputs`,
> `VirtualJoystickInput`), mas a fase não precisa saber disso. Quem junta as peças
> é só o `MainGame`. O ponto é que **a classe `Level` não muda em nenhum dos três casos**.

---

---

## Estrutura

```
src/main/java/game/
├── MainGame.java                  ← coordena fases e escolhe implementacoes
├── Level.java                     ← regras e fluxo de uma fase
├── inputs/
│   ├── Command.java               ← comandos reconhecidos pelo jogo
│   ├── IInputs.java               ← contrato das entradas
│   ├── KeyboardInput.java         ← entrada pelo teclado
│   ├── ButtonInputs.java          ← entrada por botões clicáveis (Swing)
│   └── VirtualJoystickInput.java  ← entrada por joystick simulado (mouse)
├── utils/
│   └── LevelLoader.java            ← le o TXT e converte os dados da fase
├── ui/
│   ├── IRenderer.java             ← contrato da saída
│   ├── TerminalRenderer.java      ← desenha no console
│   └── GUIRenderer.java           ← desenha numa janela gráfica (Swing)
└── world/
   ├── Grid.java                  ← dados do cenario, dimensoes e paredes
   ├── Progress.java             ← pontuação acumulada entre missões
   └── gobjects/
      ├── GameObject.java       ← objeto do jogo
      ├── Player.java           ← posição e movimento do jogador
      └── Coin.java             ← moedas coletáveis
```

---

## As três ideias do projeto

### 1. Injeção de dependência

`Level` não usa `new` para nada que venha de fora. Tudo chega pelo construtor:

```java
public Level(Grid grid, Player player, Progress progress,
             List<Coin> coins, IInputs input, IRenderer renderer)
```

`MainGame` carrega cada `Grid`, monta o `Level` correspondente e preserva o
objeto `Progress` entre as fases. A fase depende das interfaces `IInputs` e
`IRenderer`, sem conhecer as implementacoes concretas.

`Player` representa o avatar na fase; `Progress` guarda a pontuação
cumulativa e é compartilhado pelas instâncias de `Level` criadas por `MainGame`.

### 2. Interface para contrato, classe abstrata para reúso

Duas decisões diferentes no mesmo projeto, e vale entender por quê:

| | Escolha | Motivo |
|---|---|---|
| `IInputs` | **interface** | Teclado, joystick e toque não têm **nenhum código em comum** — cada um lê de um lugar diferente. Não há nada a herdar, só um contrato a cumprir. |
| `GameObject` | **classe abstrata** | Jogador e moeda compartilham **estado real** (as coordenadas `x` e `y`) e o código que cuida dele. Só o símbolo desenhado muda — e é o único método abstrato. |

### 3. Polimorfismo

O método `Level.runTurn()` chama `input.waitCommand()` sem nunca perguntar
que tipo de entrada é aquela. Quem decide como responder é o objeto concreto, em tempo de execução.
