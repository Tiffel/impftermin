FROM docker.pkg.github.com/fcwu/docker-ubuntu-vnc-desktop/app:develop
RUN apt-get update && apt-get install -y openjdk-11-jre && rm -rf /var/lib/apt/lists/*

COPY selenium/target/libs /root/selenium/libs
COPY selenium/target/selenium-1.0.jar /root/selenium/selenium-1.0.jar

#see https://github.com/fcwu/docker-ubuntu-vnc-desktop
ENV RESOLUTION=1920x1080
ENV DISPLAY=:0.0
ENV OPENBOX_ARGS='--startup "java -jar selenium/selenium-1.0.jar"'
WORKDIR /root
EXPOSE 80