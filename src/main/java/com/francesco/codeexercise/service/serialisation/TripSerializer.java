package com.francesco.codeexercise.service.serialisation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.francesco.codeexercise.model.Trip;
import java.io.IOException;

public class TripSerializer extends JsonSerializer<Trip> {
  //whatever character not used in stop IDs to build a key as STOP-ID-TAG-ON#STOP-ID-TAG-OFF
  public static final String DELIMITER = "_";
  @Override
  public void serialize(Trip trip, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    //tag off can be null (tag on shouldn't)
    if(trip.getTagOff() == null) {
      jsonGenerator.writeFieldName(trip.getTagOn());
    } else {
      jsonGenerator.writeFieldName(
          String.format("%s%s%s", trip.getTagOn(), DELIMITER, trip.getTagOff()));
    }
  }
}
