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

Die folgenden Umgebungsvariablen müssen/können in einer Datei env.txt liegen. Siehe env.example

Ausführen des fertigen Containeres:
```docker run -p 8080:80 --env-file env.txt ghcr.io/tiffel/impftermin```

## Umgebungsvariablen

```
HTTP_PASSWORD - Basic Auth Passwort für den VNC Server. Optional, keine Basic Auth, wenn leer. Username ist 'root', wenn gesetzt
PORTAL_USERNAME* - Vorgangsnummer Impfportal
PORTAL_PASSWORD* - Passwort Impfportal

EMAIL_ENABLED - Soll eine Email versand werden, wenn ein Termin auswählbar ist? Boolean (true|false)
EMAIL_ON_STARTUP - Sendet eine Mail beim hochfahren, wenn EMAIL_ENABLED auch true. (true|false)
EMAIL_USERNAME** - Benutzername für das Emailkonto
EMAIL_PASSWORD** - Passwort für das Emailkonto
EMAIL_RECIPIENTS** - Liste an Empfängern [email@host.de,email2@host.de]
EMAIL_ENABLE_SMTP_AUTH** - Enable smtp host (true|false)
EMAIL_ENABLE_STARTTLS** - Enable starttls (true|false)
EMAIL_SMTP_HOST** - Smtp Host
EMAIL_SMTP_PORT** - Smtp Port
RESOLUTION - default ist 1920x1080, für mobile nutzung empfiehlt sich RESOLUTION=720x1280

VNC_LINK - Link zum VNC, wird in der Email mit versandt
VACCINATION_CENTERS* - Liste von Impfzentren, welche auf Termine geprüft werden sollen (als kommaseparierte Liste, Bsp: DRESDEN,BORNA,...)
[BELGERN, BORNA, KAMENZ, CHEMNITZ, DRESDEN, EICH, ANNABERG, GRIMMA, LOEBAU, LEIPZIG, MITTWEIDA, PLAUEN, PIRNA, RIESA, ZWICKAU]

RESTART_ON_ERROR - Im Fehlerfall neustarten (true|false)
SLEEP_MINUTES_MIN - Minimale Pause in Minuten nach einem Durchlauf. Default: 1
SLEEP_MINUTES_MAX - Maximale Pause in Minuten nach einem Durchlauf. Default: SLEEP_MINUTES_MIN*3

*Pflicht
** Pflicht, wenn EMAIL_ENABLED = true
```


# Development

Das Modul browserpush funktioniert noch nicht. Am besten erstmal ignorieren.

## Vorraussetzungen

* java 11
* maven 3
* docker

## java krams bauen und paketieren

```mvn package```

## lokalen container bauen
Achtung, hierfür muss man an der Github container registry authentifiziert sein (warum auch immer)

https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-docker-registry#authenticating-to-github-packages

```docker build -t impftermin . ```

## starten

env.txt erstellen und konfigurieren. siehe env.example und Abschnitt Umgebungsvariablen

```docker run -p 8080:80 --env-file env.txt impftermin```

## gucken

https://localhost:8080/

login mit root:mysupersecretbrowserpassword
