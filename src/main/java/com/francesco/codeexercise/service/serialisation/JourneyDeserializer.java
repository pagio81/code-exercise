package com.francesco.codeexercise.service.serialisation;

import static com.francesco.codeexercise.service.serialisation.JourneySerializer.DELIMITER;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.francesco.codeexercise.model.Journey;
import java.io.IOException;

public class JourneyDeserializer extends KeyDeserializer {

  /**
   * this deserializes data from internal database, no need to check for malformed keys Eventually
   * accept it will fail and application shouldn't start
   *
   * @param s
   * @param deserializationContext
   * @return
   * @throws IOException
   */
  @Override
  public Object deserializeKey(String s, DeserializationContext deserializationContext)
      throws IOException {
    if (s.indexOf(DELIMITER) >= 0) {
      var tags = s.split(DELIMITER);
      return Journey.builder().tagOn(tags[0]).tagOff(tags[1]).build();
    } else {
      return Journey.builder().tagOn(s).tagOff(null).build();
    }
  }
}
