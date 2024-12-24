#!/bin/bash

tag=$1

cd ..
cd ..
docker build --platform linux/amd64 -t cbluebird/forum-auth:$tag -f ./deploy/docker/auth/Dockerfile .
docker build --platform linux/amd64 -t cbluebird/forum-gateway:$tag -f ./deploy/docker/gateway/Dockerfile .
docker build --platform linux/amd64 -t cbluebird/forum-notice:$tag -f ./deploy/docker/notice/Dockerfile .
docker build --platform linux/amd64 -t cbluebird/forum-oss:$tag -f ./deploy/docker/oss/Dockerfile .
docker build --platform linux/amd64 -t cbluebird/forum-post:$tag -f ./deploy/docker/post/Dockerfile .
docker build --platform linux/amd64 -t cbluebird/forum-user:$tag -f ./deploy/docker/user/Dockerfile .
docker build --platform linux/amd64 -t cbluebird/forum-search:$tag -f ./deploy/docker/search/Dockerfile .

docker push cbluebird/forum-auth:$tag
docker push cbluebird/forum-gateway:$tag
docker push cbluebird/forum-notice:$tag
docker push cbluebird/forum-oss:$tag
docker push cbluebird/forum-post:$tag
docker push cbluebird/forum-user:$tag
docker push cbluebird/forum-search:$tag
