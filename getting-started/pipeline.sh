#!/bin/bash

# Este comando faz com que o script pare imediatamente se qualquer comando falhar.
set -e

# O nome do ambiente alvo (des ou prd) é o primeiro argumento
ENVIRONMENT=$1

REPO_URL="https://github.com/raphaeldeoliveira/quarkus-quickstarts.git"
PROJECT_DIR="quarkus-quickstarts/getting-started"
APP_NAME="getting-started"
DOCKER_USER="raphaelcarvalho30"

# --- 1. CLONAR O REPOSITÓRIO (Simulação) ---
echo "=================================================="
echo "          INICIANDO PIPELINE DE CI/CD             "
echo "        Ambiente Alvo: $ENVIRONMENT                "
echo "=================================================="

echo "Passo 1: Clonando o repositório..."
if [ -d "$PROJECT_DIR" ]; then
  echo "Diretório já existe, pulando clone."
else
  git clone "$REPO_URL"
fi
cd "$PROJECT_DIR"

# --- 2. OBTER A VERSÃO DA APLICAÇÃO ---
echo "Passo 2: Lendo a versão do pom.xml..."
APP_VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "Versão da aplicação encontrada: $APP_VERSION"

# --- 3. CONSTRUIR A IMAGEM DOCKER ---
echo "Passo 3: Compilando e construindo a imagem Docker..."
./mvnw clean package -Dquarkus.container-image.build=false
docker build -t "$DOCKER_USER/$APP_NAME:$APP_VERSION" .
docker tag "$DOCKER_USER/$APP_NAME:$APP_VERSION" "$DOCKER_USER/$APP_NAME:latest"
echo "Imagem Docker '$DOCKER_USER/$APP_NAME' construída e tagueada com sucesso."

# --- 4. EXECUTAR TESTES UNITÁRIOS ---
echo "Passo 4: Executando testes unitários..."
./mvnw test

# --- 5. IMPLANTAR NO AMBIENTE ---
echo "Passo 5: Implantando no ambiente $ENVIRONMENT..."
minikube kubectl -- create namespace "$ENVIRONMENT" || true
minikube kubectl -- apply -f "kubernetes/$ENVIRONMENT/deployment.yaml"
minikube kubectl -- apply -f "kubernetes/$ENVIRONMENT/service.yaml"

echo "Aguardando o Deployment 'getting-started-$ENVIRONMENT' ficar pronto..."
minikube kubectl -- rollout status deployment/"$APP_NAME"-"$ENVIRONMENT" --namespace="$ENVIRONMENT"
echo "Implantação no ambiente $ENVIRONMENT concluída com sucesso!"

# --- 6. EXECUTAR TESTES DE VALIDAÇÃO ---
echo "Passo 6: Executando validação de saúde do serviço $ENVIRONMENT..."
SERVICE_URL=$(minikube kubectl -- service "$APP_NAME"-service-"$ENVIRONMENT" --namespace="$ENVIRONMENT" --url)

if curl --fail --silent "$SERVICE_URL/hello"; then
  echo "Validação de saúde do serviço $ENVIRONMENT: SUCESSO!"
else
  echo "Validação de saúde do serviço $ENVIRONMENT: FALHA!"
  exit 1
fi

echo "=================================================="
echo "        PIPELINE CONCLUÍDA COM SUCESSO!           "
echo "=================================================="