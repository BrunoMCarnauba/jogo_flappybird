# Jogo - Flappy Bird

Jogo desenvolvido para a disciplina de programação estruturada do curso de sistemas de informação. O objetivo do jogo, assim como no original Flappy Bird, é percorrer a maior distância que conseguir sem colidir com obstáculos que são gerados em posições aleatórias. A maior pontuação do jogador fica salva no banco de dados. Você pode conferir como ficou o jogo assistindo a este [video de demonstração](https://youtu.be/rOyW6CfIi4M).

## Descrição técnica

Jogo desenvolvido em Java com a biblioteca para criação de interface gráfica Swing <br/>
O sistema de gerenciamento de banco de dados utilizado foi o [PostgreSQL](https://www.postgresql.org/) com a aplicação gráfica [pgAdmin III](https://www.pgadmin.org/)

### Preparando o ambiente

É necessário ter o [Java Development Kit (JDK)](https://www.oracle.com/technetwork/pt/java/javase/downloads/index.html) instalado no computador. <br/>
O desenvolvimento foi realizado com a IDE [NetBeans](https://netbeans.org/) <br/>
Para criar a estrutura do banco de dados do jogo, crie um banco de dados chamado "heliStaculos", com encoding UTF8 e [restaure o banco](https://youtu.be/-ma9mRI_q5U?t=230) a partir do arquivo "banco_de_dados_heliStaculos.backup". As configurações como usuário e senha do banco são colocadas na classe "src/jogo/BancoDeDados.java".

### Extra

As aulas de [como criar o jogo Pac-Man em Java](https://www.youtube.com/watch?v=v676dlIZvsw&list=PLc_8_1G9IIB6gb0N-HF2af-ECoNhX7pVd) do canal [1iber Programador](https://www.youtube.com/channel/UC6TgW67XY-GBbNVGo3f3EkQ) auxiliaram muito no desenvolvimento. <br/>
As aulas a seguir ajudaram na implementação do banco de dados no jogo: [Como criar a conexão](https://youtu.be/O4BdT0q-740), [Como salvar](https://youtu.be/nVv97kXW6uY), [Como pegar dados](https://youtu.be/gDOxpJ_kjEw), [Como editar dados](https://youtu.be/L_iHdsDIk78) e [Como fazer backup e restore](https://youtu.be/-ma9mRI_q5U).

### O que falta implementar?

- Conquistas; <br/>