# Getting started with knative

### Knative services
Knative is Kubernetes-based platform to build, deploy, and manage modern serverless workloads. Each Knative service is defined by a route and a configuration, which have the same name as the service. The configuration and route are created by the service controller, and their configuration is derived from the configuration of the service. Each time the configuration is updated, a new revision is created. Revisions are immutable snapshots of a particular configuration, and use underlying Kubernetes resources to scale the number of pods based on traffic.

There are two core Knative components that can be installed and used together or independently to provide different functions:

- [Knative Serving](https://knative.dev/v0.21-docs/serving/): Easily manage stateless services on Kubernetes by reducing the developer effort required for autoscaling, networking, and rollouts.

- [Knative Eventing](https://knative.dev/v0.21-docs/eventing/): Easily route events between on-cluster and off-cluster components by exposing event routing as configuration rather than embedded in code.

 
### System requirements

For prototyping purposes, Knative will work on most local deployments of Kubernetes. For example, you can use a local, one-node cluster that has 2 CPU and 4GB of memory.

For production purposes, it is recommended that:

- If you have only one node in your cluster, you will need at least 6 CPUs, 6 GB of memory, and 30 GB of disk storage.
- If you have multiple nodes in your cluster, for each node you will need at least 2 CPUs, 4 GB of memory, and 20 GB of disk storage.

## Prerequisites

Before installing Knative, you must meet the following prerequisites:

-You have a cluster that uses Kubernetes v1.18 or newer.

-You  should have installed the kubectl CLI

-Your Kubernetes cluster must have access to the internet, since Kubernetes needs to be able to fetch images.

## How to create a cluster 

[Interactive tutorial](https://kubernetes.io/docs/tutorials/kubernetes-basics/create-cluster/cluster-interactive/)

[Reference](https://kubernetes.io/docs/tutorials/kubernetes-basics/create-cluster/)

## kubectl

The Kubernetes command-line tool, [kubectl](https://kubernetes.io/docs/reference/kubectl/kubectl/), allows you to run commands against Kubernetes clusters. You can use kubectl to deploy applications, inspect and manage cluster resources, and view logs. For more information including a complete list of kubectl operations, see the [kubectl reference documentation](https://kubernetes.io/docs/reference/kubectl/).

## Install kubectl on your pc

kubectl is installable on a variety of Linux platforms, macOS and Windows. Find your preferred operating system below.

   [Install kubectl on Linux](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux)
   
   [Install kubectl on macOS](https://kubernetes.io/docs/tasks/tools/install-kubectl-macos)
   
   [Install kubectl on Window](https://kubernetes.io/docs/tasks/tools/install-kubectl-windows)
   
## Installing Knative

This guide assumes that you want to install an upstream Knative release on a Kubernetes cluster. A growing number of vendors have managed [Knative offerings](https://knative.dev/v0.21-docs/knative-offerings); see the Knative Offerings page for a full list.

Knative v0.21.0 requires a Kubernetes cluster [v1.18 or newer](https://knative.dev/community/contributing/mechanics/RELEASE-VERSIONING-PRINCIPLES#knative-version-tables), as well as a compatible kubectl. This guide assumes that you’ve already created a Kubernetes cluster, and that you are using bash in a Mac or Linux environment; some commands will need to be adjusted for use in a Windows environment.

### Installing the Serving component 



1. Install the [Custom Resource Definitions](https://kubernetes.io/docs/concepts/extend-kubernetes/api-extension/custom-resources/) (aka CRDs):

       kubectl apply --filename https://github.com/knative/serving/releases/download/v0.21.0/serving-crds.yaml

2. Install the core components of Serving (see below for optional extensions):

        kubectl apply --filename https://github.com/knative/serving/releases/download/v0.21.0/serving-core.yaml

### Pick a networking layer (alphabetical):

- [Instructions to install Ambassador](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [Instructions to install Contour](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [Instructions to install Gloo](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [Instructions to install Istio](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [Instructions to install Kong](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [Instructions to install Kourier](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
       

### Configure DNS

- [Magic DNS(xip.io)](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [Real DNS](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [Temporary DNS](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)

We ship a simple Kubernetes Job called “default domain” that will (see caveats) configure Knative Serving to use xip.io as the default DNS suffix.

Caveat: This will only work if the cluster LoadBalancer service exposes an IPv4 address or hostname, so it will not work with IPv6 clusters or local setups like Minikube. For these, see “Real DNS” or “Temporary DNS”.

8. Monitor the Knative components until all of the components show a STATUS of Running or Completed:

        kubectl get pods --namespace knative-serving

At this point, you have a basic installation of Knative Serving.

### Optional Serving extensions

- [HPA autoscaling](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [TLS with cert-manager](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [TLS via HTTP01](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [TLS wildcard support](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
- [DomainMapping CRD](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/)
   
### Installing the Eventing component


1. Install the [Custom Resource Definitions](https://kubernetes.io/docs/concepts/extend-kubernetes/api-extension/custom-resources/) (aka CRDs):

          kubectl apply --filename https://github.com/knative/eventing/releases/download/v0.21.0/eventing-crds.yaml

2. Install the core components of Eventing (see below for optional extensions):

          kubectl apply --filename https://github.com/knative/eventing/releases/download/v0.21.0/eventing-core.yaml
      
3. Install a default Channel (messaging) layer (alphabetical):

- [Apache Kafka Channel](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/#installing-the-eventing-component)
- [Google Cloud Pub/Sub Channel](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/#installing-the-eventing-component)
- [In-Memory(Standalone)](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/#installing-the-eventing-component)
- [NATS Channel](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/#installing-the-eventing-component)

4. Install a Broker (eventing) layer:

- [Apache Kafka Broker](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/#installing-the-eventing-component)
- [MT-Channel-Based](https://knative.dev/v0.21-docs/install/any-kubernetes-cluster/#installing-the-eventing-component)

5. Monitor the Knative components until all of the components show a STATUS of Running:

         kubectl get pods --namespace knative-eventing
     
At this point, you have a basic installation of Knative Eventing!

### Getting-Started with Knative Eventing

You can find a number of samples for Knative Eventing [here](https://knative.dev/v0.21-docs/eventing/samples/index.html). A quick-start guide is available [here](https://knative.dev/v0.21-docs/eventing/getting-started).

## Installing Knative components using Operator

Knative provides an [operator](https://github.com/knative/operator) as a tool to install, configure and manage Knative. The Knative operator leverages custom objects in the cluster to define and manage the installed Knative software. This guide explains how to install and uninstall Knative using Knative operator.

