package pl.edu.agh.to.weatherapp;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import javafx.util.Pair;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.JsonReadFeature;


public class Config {
  private final Logger logger = Logger.getLogger(Config.class.toString());

  private String _weatherApiKey = null;
  public String weatherApiKey() {
    return _weatherApiKey;
  }

  public void init() throws IOException {
    JsonParser jsonParser = createJsonParser();
    read(jsonParser);

    Pair<Boolean, String> initResult = isInitialized();
    if (!initResult.getKey()) {
      throw new IOException(
        String.format(
          "Config hasn't been initialized correctly. %s is null.",
          initResult.getValue()
        )
      );
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
        _weatherApiKey = parser.getText();
      }
    }
  }

  private Pair<Boolean, String> isInitialized() {
    if (_weatherApiKey == null) {
      return new Pair<>(false, "weatherApiKey");
    }
    return new Pair<>(true, null);
  }
}
