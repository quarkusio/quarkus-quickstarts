#!/bin/bash

# Este comando faz com que o script pare imediatamente se qualquer comando falhar.
set -e

REPO_URL="https://github.com/raphaeldeoliveira/quarkus-quickstarts.git"
PROJECT_DIR="quarkus-quickstarts/getting-started"
APP_NAME="getting-started"

echo "=================================================="
echo "          INICIANDO PIPELINE DE CI/CD             "
echo "=================================================="

# --- 1. CLONAR O REPOSITÓRIO (Simulação) ---
echo "Passo 1: Clonando o repositório..."
if [ -d "$PROJECT_DIR" ]; then
  echo "Diretório já existe, pulando clone."
else
  git clone "$REPO_URL"
fi
cd "$PROJECT_DIR"

# --- 2. OBTER A VERSÃO DA APLICAÇÃO ---
echo "Passo 2: Lendo a versão do pom.xml..."
APP_VERSION=$(grep -oP '<version>\K[^<]+' pom.xml | head -n 1)
echo "Versão da aplicação encontrada: $APP_VERSION"

# --- 3. COMPILAR APLICAÇÃO ---
echo "Passo 3: Compilando a aplicação Quarkus..."
./mvnw clean package

# --- 4. CONSTRUIR A IMAGEM DOCKER ---
echo "Passo 4: Construindo a imagem Docker..."
docker build -t "$APP_NAME":"$APP_VERSION" .
docker tag "$APP_NAME":"$APP_VERSION" "$APP_NAME":latest
echo "Imagem Docker '$APP_NAME' construída e tagueada com sucesso."

# --- 5. IMPLANTAR NO AMBIENTE DES (Desenvolvimento) ---
echo "Passo 5: Implantando no ambiente DES..."
kubectl create namespace des --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -f ../kubernetes/des/

echo "Aguardando o Deployment 'getting-started-des' ficar pronto..."
kubectl rollout status deployment/getting-started-des -n des
echo "Implantação no ambiente DES concluída com sucesso!"

# --- 6. EXECUTAR TESTES DE VALIDAÇÃO ---
echo "Passo 6: Executando validação de saúde do serviço DES..."
SERVICE_URL=$(minikube service getting-started-service-des -n des --url)

if curl --fail --silent "$SERVICE_URL/hello"; then
  echo "Validação de saúde do serviço DES: SUCESSO!"
else
  echo "Validação de saúde do serviço DES: FALHA!"
  exit 1 # Falha na pipeline
fi

# --- 7. IMPLANTAR NO AMBIENTE PRD (Produção) ---
echo "Passo 7: Implantando no ambiente PRD..."
kubectl create namespace prd --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -f ../kubernetes/prd/

echo "Aguardando o Deployment 'getting-started-prd' ficar pronto..."
kubectl rollout status deployment/getting-started-prd -n prd
echo "Implantação no ambiente PRD concluída com sucesso!"

echo "=================================================="
echo "        PIPELINE CONCLUÍDA COM SUCESSO!           "
echo "=================================================="