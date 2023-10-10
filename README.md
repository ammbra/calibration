# GC Calibration content

This repo contains 2 Java applications that can be run with any JDK>=21.

## Package applications, build and push their container images

Make sure you are in the root of this repo and issue the following command:

```shell
mvn clean verify
```

Build and push each container image within its respective directory using a command similar to:

```shell
docker buildx build --platform=linux/amd64 --push --tag ghcr.io/ammbra/eager-factorial:1.0 . --no-cache
```

## Deploy the applications

Each folder contains a `k8s` folder containing a Kubernetes resources necessary for a minimum deploy.

You can run:

```shell
kubectl apply -f eager-factorial/k8s/kubernetes-default.yml
kubectl apply -f continuous-editor/k8s/kubernetes-default.yml
```

Once you deployed the applications, you can change the JVM parameters using each `k8s/gc.sh` scripts.

## Use k6 for load testing the applications

You can use the k6 scripts available in `eager-factorial/k6` and `continuous-editor/k6` to load test the deployed applications.
I used [testkube](https://testkube.io/) to run those tests in Kubernetes cluster.
