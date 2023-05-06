package com.francesco.codeexercise.serialisation;

import static com.francesco.codeexercise.service.serialisation.TripSerializer.DELIMITER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.francesco.codeexercise.model.Trip;
import com.francesco.codeexercise.service.serialisation.TripSerializer;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class TripSerializerTest {

  @Test
  public void serialize_trip_2_stops() throws Exception{
    var trip = Trip.builder().tagOn("Stop1").tagOff("Stop2").build();
    JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
    ArgumentCaptor<String> captor =  ArgumentCaptor.forClass(String.class);

    var tripSerializer = new TripSerializer();
    tripSerializer.serialize(trip,jsonGenerator,null);
    verify(jsonGenerator, times(1)).writeFieldName(captor.capture());

    assertThat(captor.getValue()).isEqualTo("Stop1_Stop2");
  }

  @Test
  public void serialize_trip_one_stop() throws Exception{
    var trip = Trip.builder().tagOn("Stop3").build();
    JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
    ArgumentCaptor<String> captor =  ArgumentCaptor.forClass(String.class);

    var tripSerializer = new TripSerializer();
    tripSerializer.serialize(trip,jsonGenerator,null);
    verify(jsonGenerator, times(1)).writeFieldName(captor.capture());

    assertThat(captor.getValue()).isEqualTo("Stop3");
  }
}
