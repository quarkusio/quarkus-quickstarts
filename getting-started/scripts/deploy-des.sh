#!/bin/bash

echo "Iniciando a implantação no ambiente DES..."

# 1. Cria o namespace 'des' se ele ainda não existir.
#    O comando '|| true' faz com que o script não pare se o namespace já existir.
kubectl create namespace des || true

# 2. Implanta os manifestos na pasta kubernetes/des.
#    O '-f' aponta para o diretório, aplicando todos os arquivos YAML nele.
kubectl apply -f kubernetes/des/

# 3. Aguarda o Deployment ser concluído.
echo "Aguardando o Deployment 'getting-started-des' ficar pronto..."
kubectl rollout status deployment/getting-started-des -n des

echo "Implantação no ambiente DES concluída com sucesso!"

# 4. Exibe o URL do serviço para validação manual.
minikube service getting-started-service-des --namespace=des --url