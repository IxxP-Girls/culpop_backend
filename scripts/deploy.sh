#!/bin/bash

REPOSITORY=/home/ubuntu
PROJECT_NAME=culpop_backend

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"
git pull

echo "> 프로젝트 빌드 시작"
./mvnw clean package -DskipTests -Dmaven.compiler.fork=true -Dmaven.compiler.fork.arguments=-Xmx512m

echo "> Build 파일 복사"
cd $REPOSITORY
cp $REPOSITORY/$PROJECT_NAME/target/culpop-0.0.1-SNAPSHOT.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(ps aux | grep "culpop-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')

echo "> pid : $CURRENT_PID"

if [ -z $CURRENT_PID ]; then
        echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -15 $CURRENT_PID"
        sudo kill -15 $CURRENT_PID
        sleep 10
fi

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME &