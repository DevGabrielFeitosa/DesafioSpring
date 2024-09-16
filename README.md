## Overview

Para facilitar a verificação de algumas funcionalidades segue um vídeo no youtube:
([clique aqui](https://youtu.be/8IiHIMixp7w))



## Requisitos

- **Java JDK 17 ou superior** instalado. ([Download aqui](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html))
- **IntelliJ IDEA** (versão recomendada: 2023). ([Download aqui](https://www.jetbrains.com/idea/download))
- **Git** instalado. ([Download aqui](https://git-scm.com/downloads))

## Passos para Configurar e Rodar o Projeto

### 1. Verifique a Instalação do Java

Certifique-se de que o Java 17 está instalado e configurado corretamente em seu sistema.

### 2. Clone o Repositório do Projeto

Use o Git para clonar o repositório do projeto:

```bash
git clone https://github.com/DevGabrielFeitosa/DesafioSpring.git
```

### 3. Baixe as Dependências

Execute o comando:

```
mvn clean install
```

### 4. Configure a IDE

Se você estiver usando o IntelliJ IDEA:

- Abra o IntelliJ IDEA.
- Selecione "Open" e navegue até a pasta do projeto.
- O IntelliJ IDEA deve reconhecer o projeto e configurar o JDK automaticamente. Caso contrário, vá para File > Project Structure > Project e selecione o JDK 17.

### 5. Execute Testes (Opcional)
Para executar os testes do projeto, use os seguintes comandos:

```
mvn test
```

### Tecnologias usadas no projeto:

- Java 17.
- SpringBoot 3.
- Banco H2
- Bootstrap.
- Jquery.
- Ajax.
