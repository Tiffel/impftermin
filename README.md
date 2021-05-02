# Vorwort

Ein Container, der mithilfe von Selenium sachsen.impfterminvergabe.de bedient und nach freien Impfterminen sucht. Der
Container bietet einen VNC Server für einen virtuellen Desktop an, in dem man, wenn denn ein Termin gefunden wurde, die
Anwendung weiter bedienen kann.

Es wird **nicht** automatisch ein Termin gebucht, da mangels freier Termine dieser Teil nicht automatisiert werden
konnte

# Hinweis

Diese Software hat zur Laufzeit die Logindaten für sachsen.impfterminvergabe.de. Dieser Container, die verwendeten
Container Images und Libraries wurden
**NICHT** abgesichert bzw. auf Sicherheit geprüft. Teilweise wurden sogar Sicherheitsfeatures deaktiviert.

Es ist einfach ein Wochenendprojekt. Verwendung auf eigene Verantwortung, es ist nicht zu empfehlen, diesen Container im
Internet zu deployen. Im Heimnetzwerk sollte das Risiko überschaubar sein, das ein Angreifer sich Zugriff auf den
Container, die Logindaten und damit viele persönliche Daten verschafft.

# Lizenz

GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007 siehe LICENCE.txt

# Konfiguration und ausführen

TODO

# Development

Das Modul browserpush funktioniert noch nicht. Am besten erstmal ignorieren.

## Vorraussetzungen

* java 11
* maven 3
* docker

## java krams bauen und paketieren

selenium/src/main/resources/config.properties erstellen und anpassen

```mvn package```

## container bauen

```docker build -t impfen --build-arg BROWSER_PASSWORD=mysupersecretpassword . ```

## starten

```docker run -p 8080:80 impfen```

## gucken

https://localhost:8080/

login mit root:mysupersecretpassword