nohup java -Xmx256m -Xmx512m -jar -Dspring.profiles.active=test ether-im-gateway.jar > ./gateway.log 2>&1 &
nohup java -Xmx512m -Xmx1024m -Xmx1024m -jar -Dspring.profiles.active=test ether-im-message.jar > ./message.log 2>&1 &
nohup java -Xmx512m -Xmx2024m -jar -Dspring.profiles.active=test ether-im-push.jar > ./push.log 2>&1 &
