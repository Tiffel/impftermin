# vorraussetzungen

* java 11
* maven 3
* docker

# java krams bauen und paketieren

selenium/src/main/resources/config.properties erstellen und anpassen

```mvn package```

# container bauen

```docker build -t impfen --build-arg BROWSER_PASSWORD=mysupersecretpassword . ```

# starten

```docker run -p 8080:80 impfen```

# gucken

https://localhost:80/

login mit root:mysupersecretpassword