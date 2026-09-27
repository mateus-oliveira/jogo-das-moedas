# Jogo das Moedas

## Autor: Professor Mateus Oliveira.

## Contexto

Jogo usado nas aulas de Programação Orientada a Objetos sobre Design de Classes.
Com foco no estudo sobre Acoplamento, Coesão e Associação de classes.

O **mesmo jogo** aparece três vezes neste repositório. As regras, os mapas e a tela são
sempre os mesmos — o que muda é **como as classes se apoiam umas nas outras**.

| | Projeto | O que ensina |
|---|---|---|
| 1 | [`jogo-das-moedas-v0`](jogo-das-moedas-v0) | **Baixa coesão.** Uma única classe `Game` com oito responsabilidades: guarda o mapa, lê o teclado, aplica as regras, desenha a tela e controla o laço. |
| 2 | [`jogo-das-moedas-v1`](jogo-das-moedas-v1) | **Alto acoplamento.** Alguém quebrou aquilo em oito classes — e quase nada melhorou, porque tudo continua ligado no concreto. Quando chegou a segunda entrada, ela deixou cinco cicatrizes. |
| 3 | [`jogo-das-moedas-v2`](jogo-das-moedas-v2) | **Baixo acoplamento.** As mesmas ideias, com as dependências invertidas: interfaces, injeção de dependência e polimorfismo. Três entradas e duas saídas, sem tocar nas regras. |

### A pergunta que liga as três versões

> *"O jogo vai ganhar joystick, botões na tela e uma janela gráfica.
> Quantos arquivos você precisa abrir?"*

- Na **v0**, a resposta é "um, e ele vai virar um monstro".
- Na **v1**, a resposta é "cinco lugares, em classes que não têm nada a ver com entrada".
- Na **v2**, a resposta é "um arquivo novo, e uma linha no `MainGame`".

O ponto da aula é que o **v1 não é a solução** — é a armadilha. Quebrar em classes move
código de lugar. O que resolve é inverter a direção das dependências.

### Ordem sugerida em aula

1. Rodar a **v0** e contar as responsabilidades de `Game`.
2. Abrir a **v1**, rodar `java -cp bin game.MainGame joystick`, e procurar os comentários
   `CICATRIZ` no código. Tentar responder às perguntas do README dela.
3. Só então abrir a **v2** e comparar `Game.java` (v1) com `Level.java` (v2) lado a lado.

### Como executar qualquer uma das versões

Não há Maven nem Gradle: compile com `javac` mesmo. O `levels.txt` é lido do **diretório
atual**, então entre na pasta do projeto antes de rodar.

```bash
cd jogo-das-moedas-v1        # ou v0, ou v2
javac -d bin $(find src/main/java -name "*.java")
java -cp bin game.MainGame
```

Cada projeto tem seu próprio `README.md` com os argumentos aceitos e um `uml.puml` com o
diagrama de classes.
