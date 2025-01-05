package org.aai.atc.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Getter
public class AppConfig {
    private final Properties properties = new Properties();


    public AppConfig(String activeProfile) {
        String propertiesFile = activeProfile.equalsIgnoreCase("default")? "/application.properties" : "/application-" + activeProfile + ".properties";
        try (InputStream inputStream = AppConfig.class.getResourceAsStream(propertiesFile)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load application properties", e);
        }
    }


}
