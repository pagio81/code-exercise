package com.francesco.codeexercise.service.serialisation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import com.francesco.codeexercise.model.Journey;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class JourneySerializerTest {

  @Test
  public void serialize_trip_2_stops() throws Exception {
    var trip = Journey.builder().tagOn("Stop1").tagOff("Stop2").build();
    JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    var tripSerializer = new JourneySerializer();
    tripSerializer.serialize(trip, jsonGenerator, null);
    verify(jsonGenerator, times(1)).writeFieldName(captor.capture());

    assertThat(captor.getValue()).isEqualTo("Stop1_Stop2");
  }

  @Test
  public void serialize_trip_one_stop() throws Exception {
    var trip = Journey.builder().tagOn("Stop3").build();
    JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    var tripSerializer = new JourneySerializer();
    tripSerializer.serialize(trip, jsonGenerator, null);
    verify(jsonGenerator, times(1)).writeFieldName(captor.capture());

    assertThat(captor.getValue()).isEqualTo("Stop3");
  }
}
