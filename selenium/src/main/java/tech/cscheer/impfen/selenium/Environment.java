package tech.cscheer.impfen.selenium;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import tech.cscheer.impfen.selenium.page.Impfzentrum;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Environment {
    public static String PORTAL_USERNAME;
    public static String PORTAL_PASSWORD;

    public static boolean EMAIL_ENABLED;
    public static boolean EMAIL_ON_STARTUP;
    public static String EMAIL_USERNAME;
    public static String EMAIL_PASSWORD;
    public static String EMAIL_RECIPIENTS;
    public static boolean EMAIL_ENABLE_SMTP_AUTH;
    public static boolean EMAIL_ENABLE_STARTTLS;
    public static String EMAIL_SMTP_HOST;
    public static int EMAIL_SMTP_PORT;

    public static String VNC_LINK;
    public static List<Impfzentrum> VACCINATION_CENTERS;

    public static void init() {
        PORTAL_USERNAME = getEnvNotEmpty("PORTAL_USERNAME");
        PORTAL_PASSWORD = getEnvNotEmpty("PORTAL_PASSWORD");

        EMAIL_ENABLED = Boolean.parseBoolean(System.getenv("EMAIL_ENABLED"));
        EMAIL_USERNAME = getEmailStringNotEmpty("EMAIL_USERNAME");
        EMAIL_PASSWORD = getEmailStringNotEmpty("EMAIL_PASSWORD");
        EMAIL_RECIPIENTS = getEmailStringNotEmpty("EMAIL_RECIPIENTS");
        EMAIL_ENABLE_SMTP_AUTH = getEmailBoolEnvNotEmpty("EMAIL_ENABLE_SMTP_AUTH");
        EMAIL_ENABLE_STARTTLS = getEmailBoolEnvNotEmpty("EMAIL_ENABLE_STARTTLS");
        EMAIL_SMTP_HOST = getEmailStringNotEmpty("EMAIL_SMTP_HOST");
        EMAIL_SMTP_PORT = getEmailIntEnvNotEmpty("EMAIL_SMTP_PORT");
        EMAIL_ON_STARTUP = getEmailBoolEnv("EMAIL_ON_STARTUP");

        VNC_LINK = System.getenv("VNC_LINK");
        VACCINATION_CENTERS = getVaccinationCenters();
    }

    private static String getEnvNotEmpty(String envName) {
        String value = System.getenv(envName);
        Validate.notBlank(value);
        return value.trim();
    }

    private static String getEmailStringNotEmpty(String envName) {
        return EMAIL_ENABLED ? getEnvNotEmpty(envName) : "";
    }

    private static boolean getEmailBoolEnv(String envName) {
        if (EMAIL_ENABLED) {
            return Optional.ofNullable(System.getenv(envName)).map(String::trim).map(Boolean::parseBoolean).orElse(false);
        }
        return false;
    }

    private static boolean getEmailBoolEnvNotEmpty(String envName) {
        return EMAIL_ENABLED && Boolean.parseBoolean(getEnvNotEmpty(envName));
    }

    private static int getEmailIntEnvNotEmpty(String envName) {
        if (EMAIL_ENABLED) {
            String value = getEnvNotEmpty(envName);
            if (!NumberUtils.isCreatable(value)) {
                throw new IllegalStateException(String.format("%s cannot be empty. Provided: %s", envName, value));
            }
            return Integer.parseInt(value);
        }

        return 0;
    }

    private static List<Impfzentrum> getVaccinationCenters() {
        String vaccinationCenters = System.getenv("VACCINATION_CENTERS");
        if (StringUtils.isBlank(vaccinationCenters)) {
            throw new IllegalStateException("VACCINATION_CENTERS cannot be empty");
        }
        List<Impfzentrum> vaccinationCentersList = Arrays.stream(vaccinationCenters.split(","))
                .filter(StringUtils::isNotBlank)
                .map(entry -> entry.trim().toUpperCase())
                .filter(entry -> EnumUtils.isValidEnum(Impfzentrum.class, entry))
                .map(Impfzentrum::valueOf)
                .collect(Collectors.toList());

        if (vaccinationCentersList.isEmpty()) {
            throw new IllegalStateException(
                    String.format("VACCINATION_CENTERS cannot be empty. Valid entries are: %s",
                            EnumSet.allOf(Impfzentrum.class)));
        }

        return vaccinationCentersList;
    }
}
