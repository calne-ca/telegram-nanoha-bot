mvn clean install
docker build . -t telegram-bots:latest
docker tag telegram-bots:latest 10.1.0.1:5000/telegram-bots:latest
docker push 10.1.0.1:5000/telegram-bots:latest