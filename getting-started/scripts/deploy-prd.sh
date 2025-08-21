#!/bin/bash

echo "Iniciando a implantação no ambiente PRD..."

# 1. Cria o namespace 'prd' se ele ainda não existir.
kubectl create namespace prd || true

# 2. Implanta os manifestos na pasta kubernetes/prd.
kubectl apply -f kubernetes/prd/

# 3. Aguarda o Deployment ser concluído.
echo "Aguardando o Deployment 'getting-started-prd' ficar pronto..."
kubectl rollout status deployment/getting-started-prd -n prd

echo "Implantação no ambiente PRD concluída com sucesso!"

# 4. Exibe o URL do serviço para validação manual.
minikube service getting-started-service-prd --namespace=prd --url