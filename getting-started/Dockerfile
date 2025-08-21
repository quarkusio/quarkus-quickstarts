# ----- ESTÁGIO 1: CONSTRUÇÃO (BUILD) -----
# Usar uma imagem base com o JDK e o Maven para compilar a aplicação
# Esta é a imagem temporária de "construção".
FROM eclipse-temurin:17-jdk-focal AS build

# Definir o diretório de trabalho dentro do contêiner para a compilação
WORKDIR /build

# Copiar os scripts do Maven Wrapper e os arquivos de configuração do Maven e o código-fonte
# para aproveitar o cache do Docker se as dependências não mudarem
COPY .mvn .mvn/
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY src/ ./src/

# Adicionar permissão de execução ao script do Maven Wrapper (corrigindo o último erro)
RUN chmod +x mvnw

# Executar a compilação da aplicação Quarkus. O comando build do Maven gera o executável JAR na pasta target
RUN ./mvnw clean package -Dquarkus.container-image.build=false

# ----- ESTÁGIO 2: EXECUÇÃO (RUNTIME) -----
# Usar uma imagem base leve (JDK apenas) para o ambiente de execução final
# Esta é a imagem que será implantada.
FROM eclipse-temurin:17-jre-focal

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar apenas os artefatos compilados do estágio de build para o estágio de execução
# Isso evita copiar o código-fonte, dependências de build, etc., resultando em uma imagem menor
COPY --from=build /build/target/quarkus-app/ /app

# Expor a porta que a aplicação Quarkus usa (a porta padrão é 8080)
EXPOSE 8080

# Definir o comando de inicialização da aplicação quando o contêiner for executado
CMD ["java", "-jar", "quarkus-run.jar"]