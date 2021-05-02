# vorraussetzungen

* java 11
* maven 3
* docker
* openssl (oder ein existierendes zerfifikat)

# java krams bauen und paketieren

selenium/src/main/resources/config.properties erstellen und anpassen

```mvn package```

# ssl zertificat generieren

```openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout nginx.key -out nginx.crt```

# container bauen

```docker build -t impfen --build-arg BROWSER_PASSWORD=mysupersecretpassword . ```

# starten

```docker run -p 8443:443 impfen```

# gucken

https://localhost:8443/

login mit root:mysupersecretpassword