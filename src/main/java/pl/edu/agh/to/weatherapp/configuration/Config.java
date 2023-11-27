package pl.edu.agh.to.weatherapp.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import lombok.Getter;


public class Config {
    private final Logger logger = Logger.getLogger(Config.class.toString());

    @Getter
    private String weatherApiKey = null;

    public boolean isInitialized() {
        if (weatherApiKey == null) {
            logger.severe("weatherApi is null.");
            return false;
        }
        return true;
    }

    public void init() throws IOException {
        JsonParser jsonParser = createJsonParser();
        read(jsonParser);

        if (!isInitialized()) {
            throw new IOException("Config hasn't been initialized correctly");
        }

        logger.info("Config has been initialised successfully.");
    }

    private JsonParser createJsonParser() throws IOException {
        FileReader configReader = new FileReader("config.json");
        JsonFactory jsonFactory = JsonFactory.builder()
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
            .build();
        return jsonFactory.createParser(configReader);
    }

    private void read(JsonParser parser) throws IOException {
        if (parser.nextToken() != JsonToken.START_OBJECT) {
            throw new IOException("Invalid JSON parser.");
        }

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            parser.nextToken();
            if (parser.getCurrentName().equals("weatherApiKey")) {
                weatherApiKey = parser.getText();
            }
        }
    }
}
