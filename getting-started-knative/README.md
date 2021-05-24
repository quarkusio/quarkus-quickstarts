# Getting started with knative

### Knative services
Knative services are used to deploy an application. Each Knative service is defined by a route and a configuration, which have the same name as the service. The configuration and route are created by the service controller, and their configuration is derived from the configuration of the service. Each time the configuration is updated, a new revision is created. Revisions are immutable snapshots of a particular configuration, and use underlying Kubernetes resources to scale the number of pods based on traffic.

 
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
## Installing Knative

You can install the Serving component, Eventing component, or both on your cluster by using one of the following deployment options:

## Creating Knative services

To create an application using Knative, you must create a YAML file that defines a Knative service. This YAML file specifies metadata about the application, points to the hosted image of the app and allows the service to be configured.
