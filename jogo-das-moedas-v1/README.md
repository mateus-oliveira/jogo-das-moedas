# Jogo das Moedas — versão 1: **acoplada**

Projeto de apoio da **Aula 08 — Design de Classes: Acoplamento e Coesão**
Linguagem de Programação II — IMD/UFRN

Este é o **degrau do meio** da aula.

Na [versão 0](../jogo-das-moedas-v0) havia uma classe fazendo tudo. Alguém olhou aquilo,
concordou que estava ruim, e **quebrou em classes**: nasceram `Player`, `Coin`,
`KeyboardInput` e `ConsoleRenderer`. Oito arquivos onde antes havia três.

Parece progresso. Depois pediram **suporte a joystick** — e foi aí que o problema apareceu.

> O projeto funciona. O problema não é ele não rodar — é o que acontece quando alguém
> precisa **mudar** alguma coisa nele.

---

## Como executar o jogo

```bash
cd jogo-das-moedas-v1
javac -d bin $(find src/main/java -name "*.java")
java -cp bin game.MainGame
```

O primeiro argumento é o dispositivo de entrada, o segundo é o nível inicial (de 1 a 5).
Ao coletar todas as moedas, o jogo avança automaticamente até o último nível, e a
pontuação acumula entre as fases:

```bash
java -cp bin game.MainGame                 # teclado, nível 1
java -cp bin game.MainGame keyboard 3      # teclado, começando no nível 3
java -cp bin game.MainGame joystick        # joystick simulado: joga sozinho
```

Os mapas ficam em `levels.txt`, na raiz do projeto, e são separados por uma
linha `---`. Use `#` para paredes, `.` para chão, `@` para o jogador e `$`
para moedas.

**Controles (teclado):** `w` (cima), `a` (esquerda), `s` (baixo), `d` (direita), `q` (sair).
Um comando por vez, seguido de Enter.

> O modo `joystick` é um dispositivo *simulado*: ele sorteia uma direção por turno e joga
> sozinho, então pode demorar para limpar um nível. Ele não está aqui para ser divertido —
> está aqui para mostrar **quanto código foi preciso mexer para ele existir**.

---

## O que observar no código

### 1. Quebrar em classes não é o mesmo que desacoplar

Compare esta versão com a v0. O número de classes quase triplicou, mas `Game` continua
decidindo tudo o que importa:

| `Game` decide… | onde |
|---|---|
| que existe teclado | `new KeyboardInput()` no construtor |
| que existe joystick | `new JoystickInput()` no construtor |
| que a saída é o console | `new ConsoleRenderer()`, e mais 10 `System.out` soltos |
| que tecla significa o quê | o `switch` de `waitCommand()` |
| como se desenha uma tela | o `clear()` com código ANSI |

O `ConsoleRenderer` foi extraído, e ainda assim `Game` imprime direto em **10 lugares**.
Confira você mesmo:

```bash
grep -c "System.out" src/main/java/game/Game.java
```

Extrair uma classe move código. **Inverter uma dependência** é outra coisa — e é ela que
está faltando aqui.

### 2. A segunda entrada — as cinco cicatrizes

O jogo nasceu com teclado. O joystick chegou depois. Como não havia nenhuma abstração de
"entrada", a única forma de encaixá-lo foi um `boolean`:

```java
private final boolean useJoystick;
```

Esse `boolean` se espalhou. Procure os comentários `CICATRIZ` no código:

| # | Arquivo | Onde | O que faz |
|---|---|---|---|
| 1 | `Game.java` | construtor | escolhe qual dispositivo instanciar — e deixa **um campo sempre `null`** |
| 2 | `Game.java` | `readCommand()` | roteia a leitura para o dispositivo certo |
| 3 | `Game.java` | `deviceName()` | descobre o nome do dispositivo para escrever na tela |
| 4 | `Game.java` | `waitCommand()` | **entende os dois vocabulários** |
| 5 | `MainGame.java` | `main()` | converte texto em `boolean` com `if/else` |

**Cinco edições, em duas classes que não têm nada a ver com dispositivo de entrada.**
`Game` é a classe das *regras* do jogo. Ela não deveria saber o que é uma tecla.

```bash
grep -n "useJoystick" src/main/java/game/*.java
```

### 3. A cicatriz mais cara: dois vocabulários

Repare no que cada dispositivo devolve:

| dispositivo | "para cima" | "sair" |
|---|---|---|
| `KeyboardInput` | `"w"` | `"q"` |
| `JoystickInput` | `"up"` | `"quit"` |

Duas linguagens diferentes para dizer a mesma coisa, e nenhuma delas existe como tipo —
são `String` soltas. Resultado: `Game.waitCommand()` teve que aprender as duas.

```java
switch (command) {
    case "w", "up" -> destinationY -= 1;
    case "s", "down" -> destinationY += 1;
    case "a", "left" -> destinationX -= 1;
    case "d", "right" -> destinationX += 1;
    case "q", "quit" -> { running = false; return; }
    ...
}
```

**Dez literais de texto para cinco conceitos.** Cada dispositivo novo com vocabulário
próprio soma mais cinco, dentro da classe de regras.

### 4. A representação do mapa vazou

O `LevelLoader` decidiu que um mapa é um `char[][]`. Essa decisão escapou dele e hoje
mora em **quatro** arquivos:

```bash
grep -l 'char\[\]\[\]' src/main/java/game/*.java
```

`LevelLoader`, `Game`, `Player` e `ConsoleRenderer`. Note que `Player.canMoveTo()` recebe
o **mapa inteiro** e conhece o caractere `'#'` — um jogador não deveria precisar de nada
disso para saber se pode andar.

### 5. `Player` e `Coin` não têm nada em comum

Abra as duas classes lado a lado. Os campos `x` e `y`, o construtor, os getters e o
`isAt()` são idênticos — copiados e colados. O compilador não sabe que as duas são
"coisas que ocupam uma posição no mapa", porque ninguém disse isso a ele.

Consequência: o `ConsoleRenderer` não consegue perguntar a elas como se desenham, e teve
que escrever `'@'` e `'$'` na mão. Se a moeda virar `'*'`, há dois lugares para lembrar.

### 6. O `draw()` de sete parâmetros

```java
void draw(char[][] map, Player player, List<Coin> coins,
          int score, int remainingCoins, int levelNumber, String deviceName)
```

Qualquer coisa nova que precise aparecer na tela — vidas, tempo, nome da fase — muda essa
assinatura **e** a chamada dentro de `Game`. Dois arquivos, toda vez.

### 7. Um bug de brinde

`Game.loadLevel()` apaga `'@'` e `'$'` de dentro do `map`. E `map` é o **mesmo array** que
está guardado na lista `levels`, não uma cópia — o mapa original foi destruído ao ser
carregado. Reinicie uma fase já jogada e você encontra um mapa sem jogador e sem moedas.

O jogo nunca volta atrás, então o bug não aparece. Mas ele está lá, e não é um descuido
isolado: é o preço de passar **estado mutável cru** de uma classe para outra.

---

## As perguntas da aula

Antes de olhar a versão 2, tente responder — de preferência tentando escrever o código:

1. **"O jogo vai ganhar suporte a botões clicáveis numa janela Swing."**
   Escreva a classe `ButtonInputs` e ligue-a no jogo. Ao terminar, **liste os arquivos que
   você precisou abrir.** Compare sua lista com as cinco cicatrizes da seção 2.

2. **"E se `useJoystick` precisasse virar três valores?"**
   Um `boolean` não tem terceiro valor. O que muda em `MainGame`? E na assinatura do
   construtor de `Game`? E em quem chama esse construtor?

3. **"Troque a representação do mapa de `char[][]` para `String[]`."**
   Quantos arquivos param de compilar? Por que o `Player` é um deles — o que uma
   representação de mapa tem a ver com um jogador?

4. **"Faça o placar mostrar o valor de cada moeda coletada."**
   Quantas assinaturas de método você mudou para exibir um número que o `Coin` já sabia?

5. **"Amanhã o jogo vira uma aplicação gráfica em vez de console.
   Quanto da classe `Game` sobrevive?"**
   Conte as linhas de `Game` que mencionam `System.out` ou códigos ANSI. Essas não
   sobrevivem. As regras do jogo — colisão, coleta, pontuação — estão misturadas com elas.

---

## Passo anterior e próximo passo

- ⬅️ [`jogo-das-moedas-v0`](../jogo-das-moedas-v0) — uma classe fazendo tudo: **baixa coesão**.
- ➡️ [`jogo-das-moedas-v2`](../jogo-das-moedas-v2) — as mesmas ideias, com as dependências
  invertidas: **baixo acoplamento**.

Na v2, procure `Level.java` e compare com o `Game.java` daqui. É o mesmo jogo, com as
mesmas regras. A diferença:

```bash
grep -n "new \|System.out" ../jogo-das-moedas-v2/src/main/java/game/Level.java
```

Esse comando não devolve nada. `Level` aplica as regras do jogo sem criar um único objeto e
sem imprimir uma única linha — tudo chega pronto pelo construtor. Na v2 existem **três**
entradas e **duas** saídas, e `Level` não muda em nenhuma das combinações.
