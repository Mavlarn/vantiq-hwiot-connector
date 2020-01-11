package io.nari.serviceiq.hwiot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static io.nari.serviceiq.hwiot.ConnectorConstants.*;


public class AbstractConnectorMain {

    public static final String LOCAL_CONFIG_FILE_NAME = "config.json";
    static final Logger log = LoggerFactory.getLogger(AbstractConnectorMain.class);

    public static Map<String, String> constructConfig() {

        String vantiqUrl;
        String token;
        String sourceName;
        String homeDir = System.getProperty("user.dir");

        Map<String, String> configMap = new HashMap<>();

        File locDir = new File(homeDir);
        if (!locDir.exists()) {
            log.error("Location specified for configuration directory ({}) does not exist.", locDir);
            return null;
        }

        String configFileName = locDir.getAbsolutePath() + File.separator + LOCAL_CONFIG_FILE_NAME;
        InputStream cfr = null;
        Map<String, Object> props = null;
        try {
            File configFile = new File(configFileName);
            if (configFile.exists()) {
                cfr = new FileInputStream(configFileName);
                ObjectMapper mapper = new ObjectMapper();
                props = mapper.readValue(configFile, Map.class);

                if (props.containsKey(VANTIQ_HOME_DIR)) {
                    homeDir = (String)props.get(VANTIQ_HOME_DIR);
                }
                vantiqUrl = (String)props.get(VANTIQ_URL);
                token = (String)props.get(VANTIQ_TOKEN);
                sourceName = (String)props.get(VANTIQ_SOURCE_NAME);

                if (vantiqUrl == null || token == null || sourceName == null) {
                    log.error("Invalid parameters.");
                    return null;
                }
                configMap.put(VANTIQ_URL, vantiqUrl);
                configMap.put(VANTIQ_TOKEN, token);
                configMap.put(VANTIQ_SOURCE_NAME, sourceName);
                configMap.put(VANTIQ_HOME_DIR, homeDir);
            }

        } catch (IOException e) {
            log.error("Config file ({}) was not readable: {}", configFileName, e.getMessage());
            return null;
        } finally {
            if (cfr != null) {
                try {
                    cfr.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }
        }
        log.debug("Extension Config: {}", configMap);
        return configMap;
    }
}
