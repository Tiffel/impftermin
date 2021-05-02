FROM docker.pkg.github.com/fcwu/docker-ubuntu-vnc-desktop/app:develop
ARG BROWSER_PASSWORD
RUN apt-get update && apt-get install -y openjdk-11-jre && rm -rf /var/lib/apt/lists/*

COPY selenium/target/libs /root/selenium/libs
COPY selenium/target/selenium-1.0-SNAPSHOT.jar /root/selenium/selenium-1.0-SNAPSHOT.jar

#see https://github.com/fcwu/docker-ubuntu-vnc-desktop
ENV HTTP_PASSWORD=${BROWSER_PASSWORD}
ENV RESOLUTION=1920x1080
ENV DISPLAY=:0.0
ENV OPENBOX_ARGS='--startup "java -jar selenium/selenium-1.0-SNAPSHOT.jar"'
WORKDIR /root
EXPOSE 80