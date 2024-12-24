#!/bin/bash

kubectl create namespace ns-forum
kubectl create configmap auth-config --from-file=auth/application.yaml -n ns-forum
kubectl create configmap gateway-config --from-file=gateway/application.yaml -n ns-forum
kubectl create configmap notice-config --from-file=notice/application.yaml -n ns-forum
kubectl create configmap oss-config --from-file=oss/application.yaml -n ns-forum
kubectl create configmap post-config --from-file=post/application.yaml -n ns-forum
kubectl create configmap user-config --from-file=user/application.yaml -n ns-forum
kubectl create configmap search-config --from-file=search/application.yaml -n ns-forum
kubectl create configmap log-config --from-file=logback-spring.xml -n ns-forum

kubectl apply -f forum/auth.yaml -n ns-forum
kubectl apply -f forum/gateway.yaml -n ns-forum
kubectl apply -f forum/notice.yaml -n ns-forum
kubectl apply -f forum/oss.yaml -n ns-forum
kubectl apply -f forum/post.yaml -n ns-forum
kubectl apply -f forum/user.yaml -n ns-forum