# Jogo das Moedas — versão 2: **desacoplada**

Projeto de apoio da **Aula 08 — Design de Classes: Acoplamento e Coesão**
Linguagem de Programação II — IMD/UFRN

Este é o **"depois"** — o terceiro e último passo da aula:

- [`v0`](../jogo-das-moedas-v0): uma classe fazendo tudo → **baixa coesão**
- [`v1`](../jogo-das-moedas-v1): quebrado em classes, mas tudo ligado no concreto → **alto acoplamento**
- **`v2` (aqui)**: as mesmas ideias, com as dependências invertidas → **baixo acoplamento**

Na versão desacoplada, cada classe mantém uma responsabilidade clara: `Grid` representa o
cenário, `Level` executa uma fase e `MainGame` coordena a partida inteira.

---

## Como executar o jogo

```bash
cd jogo-das-moedas-v2
javac -d bin $(find src/main/java -name "*.java")
java -cp bin game.MainGame
```

Os cinco mapas ficam em `levels.txt`, na raiz do projeto, separados por uma
linha `---`. Use `#` para paredes, `.` para chao, `@` para o jogador e `$`
para moedas. O nível 1 é o padrão. A entrada padrão é teclado, o renderer
padrão é terminal e ambas as opções podem ser omitidas independentemente:

```bash
java -cp bin game.MainGame
java -cp bin game.MainGame -i buttons
java -cp bin game.MainGame -r gui
java -cp bin game.MainGame -i joystick -r gui -l 5
```

### Trocando entrada e saída

Esta é a demonstração principal. O **mesmo jogo** recebe opções independentes:
`-i` escolhe a entrada (`keyboard`, `buttons` ou `joystick`) e `-r` escolhe o
renderer (`terminal` ou `gui`). A opção `-l` escolhe o nível inicial.

```bash
java -cp bin game.MainGame -i keyboard -r terminal
java -cp bin game.MainGame -i keyboard -r gui
java -cp bin game.MainGame -i buttons -r terminal
java -cp bin game.MainGame -i joystick -r gui
```

Pelo Eclipse: `Run As` → `Run Configurations...` → aba `Arguments` →
escreva, por exemplo, `-i keyboard -r gui` em *Program arguments*.

O jogo pode combinar qualquer entrada com qualquer renderer. Quem junta as
peças é só o `MainGame`; a classe `Level` não muda conforme a combinação.
`InputsFactory` recebe `InputsEnum` e `RendererFactory` recebe `RendererEnum`.

As duas implementam a mesma interface genérica, que tem **dois** parâmetros de tipo —
`E` é o que se cria, `K` é a chave que decide qual criar:

```java
public interface IFactory<E, K> {
    E create(K key);
}
```

Ou seja, `InputsFactory implements IFactory<IInputs, InputsEnum>` e
`RendererFactory implements IFactory<IRenderer, RendererEnum>`.

---

## Estrutura

```
src/main/java/game/
├── MainGame.java                  ← coordena fases e escolhe implementacoes
├── Level.java                     ← regras e fluxo de uma fase
├── enums/
│   ├── CommandsEnum.java          ← comandos reconhecidos pela fase
│   ├── InputsEnum.java             ← modos de entrada
│   └── RendererEnum.java           ← modos de saída
├── inputs/
│   ├── IInputs.java               ← contrato das entradas
│   ├── InputsFactory.java          ← cria entrada conforme InputsEnum
│   ├── KeyboardInput.java         ← entrada pelo teclado
│   ├── ButtonInputs.java          ← entrada por botões clicáveis (Swing)
│   └── VirtualJoystickInput.java  ← entrada por joystick simulado (mouse)
├── utils/
│   ├── LevelLoader.java            ← le o TXT e converte os dados da fase
│   └── IFactory.java               ← contrato generico para factories
├── ui/
│   ├── IRenderer.java             ← contrato da saída
│   ├── RendererFactory.java        ← cria saida conforme RendererEnum
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
`IRenderer`, sem conhecer as implementacoes concretas. `InputsFactory` e
`RendererFactory` escolhem essas implementacoes com base em opções
independentes e implementam `IFactory<IInputs, InputsEnum>` e
`IFactory<IRenderer, RendererEnum>`, respectivamente.

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

---

## O mesmo jogo, classe por classe

A v2 não inventou conceitos novos. Ela pegou o que já existia na v1 e **inverteu as
dependências**. Abra os dois projetos lado a lado:

| v1 (acoplada) | v2 (desacoplada) | o que mudou |
|---|---|---|
| `Game` | `Level` + `MainGame` + `Progress` | as regras foram separadas da montagem e do placar |
| `Player`, `Coin` | `Player`, `Coin` + `GameObject` | o que era copiado e colado virou herança de estado real |
| — | `Grid` | o `char[][]` cru virou um objeto imutável que sabe responder `isWall()` |
| `KeyboardInput` | `KeyboardInput` + **`IInputs`** | surgiu o contrato: agora existe "uma entrada", não "o teclado" |
| `JoystickInput` | `VirtualJoystickInput`, `ButtonInputs` + `InputsFactory` | de 2 entradas para 3, **sem tocar nas regras** |
| `ConsoleRenderer` | `TerminalRenderer`, `GUIRenderer` + **`IRenderer`** | mesma história do outro lado |
| `boolean useJoystick` | `InputsEnum` + `RendererEnum` + factories | as escolhas de entrada e saída ficam centralizadas e independentes |
| `String` cru como comando | `CommandsEnum` | o comando virou um tipo, e carrega o próprio delta de movimento |
| `LevelLoader` | `utils/LevelLoader` | recebe o `Path` em vez de fixar `"levels.txt"` |

### A comparação que resume a aula

```bash
grep -n "new \|System.out" src/main/java/game/Level.java
```

Esse comando não devolve nada. `Level` aplica **todas** as regras do jogo — colisão, coleta,
pontuação, fim de fase — sem criar um único objeto e sem imprimir uma única linha.

Agora rode o mesmo comando no `Game.java` da v1. Depois conte, nos dois projetos, quantos
arquivos você precisaria abrir para adicionar uma entrada nova.
