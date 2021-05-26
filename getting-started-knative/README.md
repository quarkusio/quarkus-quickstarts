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

#### Limitations of Knative Operator:

Knative Operator is still in Alpha phase. It has not been tested in a production environment, and should be used for development or test purposes only.

### Install Knative with the Knative Operator

You can find the release information of Knative Operator on the [Releases](https://github.com/knative/operator/releases) page.

#### Installing the Knative Operator

From releases:

Install the latest Knative operator with the following command:

     kubectl apply -f https://github.com/knative/operator/releases/download/v0.21.0/operator.yaml

From source code:

You can also install Knative Operator from source using ko.

Install the [ko](https://github.com/google/ko) build tool.

Download the source code using the following command:

    git clone https://github.com/knative/operator.git

Install the operator in the root directory of the source using the following command:

    ko apply -f config/

#### Verify the operator installation

Verify the installation of Knative Operator using the command:

       kubectl get deployment knative-operator

If the operator is installed correctly, the deployment should show a Ready status. Here is a sample output:

     NAME               READY   UP-TO-DATE   AVAILABLE   AGE
     knative-operator   1/1     1            1           19h

#### Track the log

Use the following command to track the log of the operator:

         kubectl logs -f deploy/knative-operator

[Installing the Knative Serving component](https://knative.dev/v0.21-docs/install/knative-with-operators/#installing-the-knative-serving-component)

[Installing the Knative Serving component with different network layers](https://knative.dev/v0.21-docs/install/knative-with-operators/#installing-the-knative-serving-component-with-different-network-layers)

[Installing the Knative Eventing](https://knative.dev/v0.21-docs/install/knative-with-operators/#installing-the-knative-eventing-component)


## Installing the Knative CLI

This guide provides details about how you can install the Knative kn client (CLI) using various methods.

### Install kn using an executable binary 

To install the kn Client, you must download the executable binary for your system. Links to the latest stable executable binary releases are available on the kn [release page](https://github.com/knative/client/releases).

You must place the executable binary in your system path, and make sure that it is executable.

## Install kn using nightly executable binary

Nightly executable binaries are available for users who want to install the most recent pre-release features of kn. These binaries include all kn features, even those not included in the latest stable release.

WARNING: Nightly executable binaries include features which may not be included in the latest stable Knative release, and are therefore not considered to be stable.

To install the kn Client, you must download the executable binary for your system. Links to the latest nightly executable binaries are available here:

-  [macOS](https://storage.googleapis.com/knative-nightly/client/latest/kn-darwin-amd64)

-  [Linux](https://storage.googleapis.com/knative-nightly/client/latest/kn-linux-amd64)

-  [Windows](https://storage.googleapis.com/knative-nightly/client/latest/kn-windows-amd64.exe)

### Install kn using Go

Prerequisite: Building kn requires Go v1.14 or newer. You will first need a working Go environment.

   Check out the [Client repository](https://github.com/knative/client):

            git clone https://github.com/knative/client.git
            cd client/

   Build an executable binary:

            hack/build.sh -f

   Move kn into your system path, and verify that kn commands are working properly. For example:

            kn version

## Install kn using brew

For macOs, you can [install kn using brew](https://github.com/knative/homebrew-client).

### kn container images 

The kn container images are available for users who require these for additional use cases. For example, if you want to use the kn container image with Tekton.

Links to either the nightly container image or the latest stable container image are available here:

   [Nightly container image](https://gcr.io/knative-nightly/knative.dev/client/cmd/kn)
   [Latest release](https://gcr.io/knative-releases/knative.dev/client/cmd/kn)

#### You can run kn from a container image. 

For example:

     docker run --rm -v "$HOME/.kube/config:/root/.kube/config" gcr.io/knative-releases/knative.dev/client/cmd/kn:latest service

To learn more about kn you can see the [documentation](https://github.com/knative/client/blob/master/docs/cmd/kn.md) .

#### Installing istio for Knative

- [Installation Guide](https://knative.dev/v0.21-docs/install/installing-istio/)
- [istioctl installation](https://www.ibm.com/docs/en/cloud-paks/cp-management/1.1.0?topic=guide-installing-istio-cli-istioctl)

#### Verifying your Istio install

View the status of your Istio installation to make sure the install was successful. It might take a few seconds, so rerun the following command until all of the pods show a STATUS of Running or Completed:

     kubectl get pods --namespace istio-system

For more help regarding installation of istio you can refer [here](https://istio.io/latest/docs/setup/getting-started/)

For Uninstallation of istio follow the uninstallation [guide](https://istio.io/docs/setup/install/istioctl/#uninstall-istio)

### Checking the version of your Knative components

To obtain the version of the Knative component that you have running on your cluster, you query for the [component].knative.dev/release label with the following commands:

Knative Serving

    kubectl get namespace knative-serving -o 'go-template={{index .metadata.labels "serving.knative.dev/release"}}'

Knative Eventing

    kubectl get namespace knative-eventing -o 'go-template={{index .metadata.labels "eventing.knative.dev/release"}}'


For Configuring the Serving  Operator Custom Resource you can follow this [guide](https://knative.dev/v0.21-docs/install/operator/configuring-serving-cr/)

### Upgrading your installation

To upgrade your Knative components and plugins, run the **kubectl apply** command to install the subsequent release. We support upgrading by a single minor version number. For example, if you have v0.14.0 installed, you must upgrade to v0.15.0 before attempting to upgrade to v0.16.0. To verify the version number you currently have installed, see [Checking your installation version](https://knative.dev/v0.21-docs/install/check-install-version). 

For the upgradation follow the [Upgradation Guide](https://knative.dev/v0.21-docs/install/upgrade-installation/)

If you installed Knative using the operator, the upgrade process will differ. See the [operator upgrade guide](https://knative.dev/v0.21-docs/install/upgrade-installation-with-operator/) to learn how to upgrade an installation managed by the operators.

### Connecting the `kn` Client to your cluster

The **kn** Client uses your **kubectl** client configuration, the kubeconfig file, to connect to your cluster. This file is usually automatically created when you create a Kubernetes cluster. 

kn looks for your kubeconfig file in the default location of 

                                                  $HOME/.kube/config.

For more information about kubeconfig files, see [Organizing Cluster Access Using kubeconfig Files](https://kubernetes.io/docs/concepts/configuration/organize-cluster-access-kubeconfig/).

Using kubeconfig files with your platform 

Instructions for using kubeconfig files are available for the following platforms:

  [Amazon EKS](https://docs.aws.amazon.com/eks/latest/userguide/create-kubeconfig.html)

  [Google GKE](https://cloud.google.com/kubernetes-engine/docs/how-to/cluster-access-for-kubectl)

  [IBM IKS](https://cloud.ibm.com/docs/containers?topic=containers-getting-started)

  [Red Hat OpenShift Cloud Platform](https://docs.openshift.com/container-platform/4.1/cli_reference/administrator-cli-commands.html#create-kubeconfig)

  Starting [minikube](https://github.com/kubernetes/minikube) writes this file (or gives you an appropriate context in an existing configuration file).


## Getting Started with App Deployment

This guide shows you how to deploy an app using Knative, then interact with it using cURL requests


You need

- A Kubernetes cluster with Knative Serving installed.
- An image of the app that you’d like to deploy available on a container registry. The image of the sample app used in this guide is available on Google Container Registry.

### Sample application

This guide demonstrates the basic workflow for deploying the [Hello World sample app (Go)](https://knative.dev/v0.21-docs/serving/samples/hello-world/helloworld-go) from the [Google Container Registry](https://cloud.google.com/container-registry/docs/pushing-and-pulling). You can use these steps as a guide for deploying your container images from other registries like [Docker Hub](https://docs.docker.com/docker-hub/repos/).

To deploy a local container image, you need to disable image tag resolution by running the following command:

#### Set to dev.local/local-image when deploying local container images

     docker tag local-image dev.local/local-image

### [Learn more about image tag resolution](https://knative.dev/v0.21-docs/serving/tag-resolution).

The Hello World sample app reads in an env variable, TARGET, then prints “Hello World: ${TARGET}!”. If TARGET isn’t defined, it will print “NOT SPECIFIED”.
Creating your Deployment with the Knative CLI

The easiest way to deploy a Knative Service is by using the Knative CLI [kn](https://github.com/knative/client).

Prerequisite: Install the kn binary as described in [Installing the Knative CLI](https://knative.dev/v0.21-docs/install/install-kn)

It will create a corresponding resource description internally as when using a YAML file directly. kn provides a command-line mechanism for managing Services. It allows you to configure every aspect of a Service. The only mandatory flag for creating a Service is --image with the container image reference as value.

To create a Service directly at the cluster, use:

#### Create a Knative service with the Knative CLI kn

     kn service create helloworld-go --image gcr.io/knative-samples/helloworld-go --env TARGET="Go Sample v1"

If you want to deploy the sample app, leave the --image config as-is. If you’re deploying an image of your app, update the name of the Service and the value of the --image flag accordingly.

Now that you have deployed the service, Knative will perform the following steps:

    Create a new immutable revision for this version of the app.
    Perform network programming to create a route, ingress, service, and load balancer for your app.
    Automatically scale your pods up and down based on traffic, including to zero active pods.

# Creating your Deployment with YAML

Alternatively, to deploy an app using Knative, you can also create the configuration in a YAML file that defines a service. For more information about the Service object, see the [Resource Types documentation](https://github.com/knative/serving/blob/master/docs/spec/overview.md#service).

This configuration file specifies metadata about the application, points to the hosted image of the app for deployment, and allows the deployment to be configured. For more information about what configuration options are available, see the [Serving spec documentation](https://github.com/knative/serving/blob/master/docs/spec/spec.md).

To create the same application as in the previous kn example, create a new file named service.yaml, then copy and paste the following content into it:

      apiVersion: serving.knative.dev/v1 # Current version of Knative
      kind: Service
      metadata:
         name: helloworld-go # The name of the app
          namespace: default # The namespace the app will use
      spec:
       template:
       spec:
      containers:
        - image: gcr.io/knative-samples/helloworld-go # Reference to the image of the app
          env:
            - name: TARGET # The environment variable printed out by the sample app
              value: "Go Sample v1"

If you want to deploy the sample app, leave the config file as-is. If you’re deploying an image of your app, update the name of the Service (**.metadata.name**) and the reference to the container image (**.spec.containers[].image**) accordingly.

From the directory where the new **service.yaml** file was created, apply the configuration:

        kubectl apply --filename service.yaml

Now that you have deployed the service, Knative will perform the following steps:

    Create a new immutable revision for this version of the app.
    Perform network programming to create a route, ingress, service, and load balancer for your app.
    Automatically scale your pods up and down based on traffic, including to zero active pods.

## Interacting with your app

To see if your app has been deployed successfully, you need the URL created by Knative.

To find the URL for your service, use either **kn** or **kubectl**

kn

     kn service describe helloworld-go

This will return something like

    Name        helloworld-go
    Namespace   default
    Age         12m
    URL         http://helloworld-go.default.34.83.80.117.xip.io

    Revisions:
     100%  @latest (helloworld-go-dyqsj-1) [1] (39s)
        Image:  gcr.io/knative-samples/helloworld-go (pinned to 946b7c)

    Conditions:
      OK TYPE                   AGE REASON
      ++ Ready                  25s
      ++ ConfigurationsReady    26s
      ++ RoutesReady            25s

kubectl

     kubectl get ksvc helloworld-go

The command will return the following:

     NAME            URL                                                LATESTCREATED         LATESTREADY           READY   REASON
     helloworld-go   http://helloworld-go.default.34.83.80.117.xip.io   helloworld-go-96dtk   helloworld-go-96dtk   True  


Note: If your URL includes example.com then consult the setup instructions for configuring DNS (e.g. with xip.io), or using a Custom Domain.

If you changed the name from helloworld-go to something else when creating the .yaml file, replace helloworld-go in the above commands with the name you entered.

 Now you can make a request to your app and see the results. Replace the URL with the one returned by the command in the previous step.

    # curl http://helloworld-go.default.34.83.80.117.xip.io
    Hello World: Go Sample v1!

If you deployed your app, you might want to customize this cURL request to interact with your application.

It can take a few seconds for Knative to scale up your application and return a response.

Note: Add -v option to get more detail if the curl command failed.

You’ve successfully deployed your first application using Knative!

## Cleaning up

To remove the sample app from your cluster, delete the service record:

kn service delete helloworld-go

Alternatively, you can also delete the service with kubectl via the definition file or by name.

# Delete with the KService given in the yaml file:

     kubectl delete --filename service.yaml

# Or just delete it by name:

     kubectl delete kservice helloworld-go

# Resources you can follow to learn more about Knative

https://knative.dev/v0.21-docs/

https://istio.io/latest/docs/setup/install/istioctl/

https://kubernetes.io/docs/tutorials/kubernetes-basics/create-cluster/

https://knative.dev/docs/serving/getting-started-knative-app/

https://docs.openshift.com/container-platform/4.2/serverless/getting-started-knative-services.html
