#!/bin/bash

echo "🚀 배포 시작"

RUNNING_CONTAINER=$(sudo docker ps)
echo "실행중인 컨테이너 목록: ${RUNNING_CONTAINER}"

# 실행 중인 컨테이너 확인
EXIST_BLUE=$(sudo docker ps -q -f name=timo-repo-blue)

echo "현재 실행 중인 컨테이너: ${EXIST_BLUE}"

if [ -z "${EXIST_BLUE}" ]; then
    START_CONTAINER="blue"
    TERMINATE_CONTAINER="green"
    START_PORT=8081
    TERMINATE_PORT=8082
    NEW_NGINX_CONF="timo-api-blue.conf"
else
    START_CONTAINER="green"
    TERMINATE_CONTAINER="blue"
    START_PORT=8082
    TERMINATE_PORT=8081
    NEW_NGINX_CONF="timo-api-green.conf"
fi

echo "✅ 새로 실행할 컨테이너: spring-${START_CONTAINER}"

# 새 컨테이너 실행 (docker-compose)
sudo docker compose -f /home/ubuntu/docker-compose.${START_CONTAINER}.yml up -d --build

# 헬스체크 (최대 10번, 5초 간격)
for i in {1..10}; do
    echo "🔄 서버 상태 확인 중..."
    HEALTH_STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://127.0.0.1:${START_PORT}/actuator/health)

    if [ "$HEALTH_STATUS" -eq 200 ]; then
        echo "✅ 서버 정상 작동 (HTTP $HEALTH_STATUS)"
        break
    else
        echo "❌ 서버 작동 실패 (HTTP $HEALTH_STATUS), 재시도..."
        sleep 5
    fi
done

if [ "$HEALTH_STATUS" -ne 200 ]; then
    echo "🚨 헬스체크 실패. 배포 중단."
    exit 1
fi

echo "🔄 Nginx 설정 변경..."
# service-url.inc 사용 X -> timo-api.conf 파일을 blue/green 설정으로 변경
sudo cp /etc/nginx/conf.d/${NEW_NGINX_CONF} /etc/nginx/conf.d/timo-api.conf

echo "♻️ Nginx 재시작..."
sudo systemctl reload nginx

echo "🛑 기존 컨테이너 종료: spring-${TERMINATE_CONTAINER}"
sudo docker-compose -f /home/ubuntu/docker-compose.${TERMINATE_CONTAINER}.yml down

echo "✅ 배포 완료!"
