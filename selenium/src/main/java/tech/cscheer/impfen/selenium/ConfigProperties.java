package tech.cscheer.impfen.selenium;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    private String username;
    private String password;

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
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
