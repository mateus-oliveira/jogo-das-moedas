# Jogo das Moedas — versão 2: **desacoplada**

Projeto de apoio da **Aula 08 — Design de Classes: Acoplamento e Coesão**
Linguagem de Programação II — IMD/UFRN

Este é o **"depois"**. Mesmo jogo, mesmo mapa, mesmas regras, mesma tela —
e nenhuma linha de "funcionalidade nova". O que mudou foi **onde cada responsabilidade mora**.

---

## Como executar o jogo

### Pelo Eclipse

1. Abra o projeto no Eclipse como um projeto Java comum.
2. Selecione a pasta `jogo-desacoplado` e confirme
3. Botão direito em `Main.java` → `Run As` → `Java Application`

### Pelo terminal

```bash
cd jogo-desacoplado
javac -d bin $(find src/main/java -name "*.java")
java -cp bin game.Main
```

### Trocando entrada e saída

Esta é a demonstração principal. O **mesmo jogo**, com dois pares diferentes
de entrada/saída:

```bash
java -cp bin game.Main              # teclado + terminal (padrão)
java -cp bin game.Main gui          # botões clicáveis + janela gráfica (Swing)
```

Pelo Eclipse: `Run As` → `Run Configurations...` → aba `Arguments` →
escreva `gui` em *Program arguments*.

> No modo `gui`, o jogo abre duas janelas: uma com o mapa desenhado
> (`GUIRenderer`) e outra só com os botões de comando (`ButtonInputs`).
> Elas não sabem uma da existência da outra — quem as junta é o `Main`.
> O ponto é que **a classe `Game` não muda em nenhum dos dois casos**.

---

---

## Estrutura

```
src/main/java/game/
├── Main.java                      ← escolhe as implementações concretas
├── Game.java                      ← regras e fluxo do jogo
├── inputs/
│   ├── Command.java               ← comandos reconhecidos pelo jogo
│   ├── IInputs.java               ← contrato das entradas
│   ├── KeyboardInput.java         ← entrada pelo teclado
│   └── ButtonInputs.java          ← entrada por botões clicáveis (Swing)
├── ui/
│   ├── IRenderer.java             ← contrato da saída
│   ├── TerminalRenderer.java      ← desenha no console
│   └── GUIRenderer.java           ← desenha numa janela gráfica (Swing)
└── world/
   ├── Map.java                   ← mapa e paredes
   └── gobjects/
      ├── GameObject.java        ← objeto do jogo
      ├── Player.java            ← jogador e pontuação
      └── Coin.java              ← moedas coletáveis
```

---

## As três ideias do projeto

### 1. Injeção de dependência

`Game` não usa `new` para nada que venha de fora. Tudo chega pelo construtor:

```java
public Game(Map map, Player player, List<Coin> coins,
            IInputs input, IRenderer renderer)
```

Por isso o teste consegue entregar um controle de mentira, e o `Main` consegue
entregar um joystick, sem que `Game` saiba da diferença.

### 2. Interface para contrato, classe abstrata para reúso

Duas decisões diferentes no mesmo projeto, e vale entender por quê:

| | Escolha | Motivo |
|---|---|---|
| `IInputs` | **interface** | Teclado, joystick e toque não têm **nenhum código em comum** — cada um lê de um lugar diferente. Não há nada a herdar, só um contrato a cumprir. |
| `Entity` | **classe abstrata** | Jogador e moeda compartilham **estado real** (as coordenadas `x` e `y`) e o código que cuida dele. Só o símbolo desenhado muda — e é o único método abstrato. |

### 3. Polimorfismo

O método `Game.runTurn()` chama `input.waitCommand()` sem nunca perguntar
que tipo de entrada é aquela. Quem decide como responder é o objeto concreto, em tempo de execução.

O teste `InputPolymorphismTest` prova isso: o mesmo percurso feito pelo teclado e por
botões clicáveis produz **exatamente a mesma pontuação**.

---

## Sobre os testes

O arquivo mais importante para a aula não é um teste — é o
`src/test/java/game/fakes/IInputsFake.java`:

```java
public class IInputsFake implements IInputs {
    // devolve comandos combinados de antemão
}
```

É uma **quarta fonte de entrada**, que existe só para os testes. Nenhuma linha
de `Game` precisou ser alterada para ela funcionar — exatamente como aconteceria
se amanhã chegasse um controle de Xbox.

Há dois caminhos para o mesmo objetivo, e os dois estão no projeto:

| Arquivo | Abordagem |
|---|---|
| `GameTest.java` | dublês escritos à mão (`IInputsFake`, `RenderizadorFake`) |
| `GameComMockitoTest.java` | a biblioteca **Mockito** cria o dublê sozinha |

Os dois só funcionam porque `Game` depende de **interfaces**. Se ela fizesse
`new EntradaTeclado()` lá dentro, não haveria onde encaixar nenhum dos dois.

---

## Exercícios propostos

1. **Crie uma `EntradaComandosGravados`** que leia uma sequência de comandos de um
   vetor de `String` (tipo `"cima,cima,direita"`) e faça o jogo rodar sozinho com ela.
   *Quantos arquivos existentes você precisou modificar? Resposta esperada: apenas o `Main`.*

2. **Crie um `RenderizadorSilencioso`** que não imprime nada. Rode o jogo com ele.
   *Quanto da classe `Game` você precisou tocar?*

3. **Adicione uma `Parede` móvel** ou um novo tipo de entidade herdando de `Entidade`.
   *Onde exatamente o código novo encosta no código velho?*

4. **Mude o valor da moeda para 25 pontos** e compile novamente com `javac`.
   *Qual teste quebra? Ele quebrou porque a regra mudou ou porque o teste era frágil?*

5. **Compare os dois `GameTest.java`** (este e o do `jogo-acoplado`) lado a lado.
   Conte quantas linhas de cada arquivo falam sobre **as regras do jogo** e quantas
   falam sobre **contornar o acoplamento**.
