package tech.cscheer.impfen.selenium;

import tech.cscheer.impfen.selenium.page.Impfzentrum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigProperties {
    private String username;
    private String password;
    private List<Impfzentrum> impfzentren;

    public ConfigProperties() {
        Properties prop = new Properties();
        String propFileName = "config.properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("something went wrong reading the properties file " + propFileName);
        }
        this.username = prop.getProperty("USERNAME");
        if (username.isBlank()) {
            throw new IllegalStateException("username cannot be empty");
        }
        this.password = prop.getProperty("PASSWORD");
        if (password.isBlank()) {
            throw new IllegalStateException("password cannot be empty");
        }

        this.impfzentren = Arrays.stream(prop.getProperty("IMPFZENTREN").split(",")).map(Impfzentrum::valueOf).collect(Collectors.toList());
        if (impfzentren.isEmpty()) {
            throw new IllegalStateException("Impfzentren cannot be empty");
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Impfzentrum> getImpfzentren() {
        return impfzentren;
    }
}
