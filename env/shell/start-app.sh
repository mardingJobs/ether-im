nohup java -Xmx512m -Xmx2048m -XX:+UseConcMarkSweepGC -jar -Dspring.profiles.active=test ether-im-gateway.jar > ./gateway.log 2>&1 &

nohup java -Xmx512m -Xmx2048m -XX:+UseConcMarkSweepGC -jar -Dspring.profiles.active=test ether-im-single-message.jar > ./single-message.log 2>&1 &

nohup java -Xmx512m -Xmx2048m -XX:+UseConcMarkSweepGC -jar -Dspring.profiles.active=test ether-im-push.jar > ./push.log 2>&1 &


echo "start success"
