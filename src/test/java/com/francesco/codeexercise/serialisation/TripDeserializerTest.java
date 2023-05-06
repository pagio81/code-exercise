package com.francesco.codeexercise.serialisation;

import static com.francesco.codeexercise.service.serialisation.TripSerializer.DELIMITER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.francesco.codeexercise.model.Trip;
import com.francesco.codeexercise.service.serialisation.TripDeserializer;
import com.francesco.codeexercise.service.serialisation.TripSerializer;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class TripDeserializerTest{

  @Test
  public void deserialize_trip_2_stops() throws Exception{
    var tripDeserializer = new TripDeserializer();
    var expectedTrip = Trip.builder().tagOn("Stop1").tagOff("Stop2").build();

    assertThat(tripDeserializer.deserializeKey("Stop1_Stop2",null)).isEqualTo(expectedTrip);
  }

  @Test
  public void deserialize_trip_one_stop() throws Exception{
    var tripDeserializer = new TripDeserializer();
    var expectedTrip = Trip.builder().tagOn("Stop3").build();

    assertThat(tripDeserializer.deserializeKey("Stop3",null)).isEqualTo(expectedTrip);
  }
}
