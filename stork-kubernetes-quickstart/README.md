Quarkus guide: https://quarkus.io/guides/stork

The kubernetes folder contains two manifests corresponding to two simple applications, they are the instances of the service we will discover using Stork Kubernetes Service discovery.
These manifests can be applied to the cluster using kubectl:
```shell
kubectl create namespace development
kubectl apply -f kubernetes-setup.yml -n=development
kubectl apply -f target/kubernetes/kubernetes.yml -n=development
```
Go to my-service.127.0.0.1.nip.io/api
