package com.laboratorio.parlerapiinterface.utils;

import java.io.FileReader;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 16/08/2024
 * @updated 30/09/2024
 */
public class ParlerApiConfig {
    private static final Logger log = LogManager.getLogger(ParlerApiConfig.class);
    private static ParlerApiConfig instance;
    private final Properties properties;

    private ParlerApiConfig() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try {
            this.properties.load(new FileReader("config//parler_api.properties"));
        } catch (Exception e) {
            log.error("Ha ocurrido un error leyendo el fichero de configuración del API de Parler. Finaliza la aplicación!");
            log.error(String.format("Error: %s", e.getMessage()));
            if (e.getCause() != null) {
                log.error(String.format("Causa: %s", e.getCause().getMessage()));
            }
            System.exit(-1);
        }
    }

    public static ParlerApiConfig getInstance() {
        if (instance == null) {
            synchronized (ParlerApiConfig.class) {
                if (instance == null) {
                    instance = new ParlerApiConfig();
                }
            }
        }
        
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}