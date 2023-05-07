package com.francesco.codeexercise.service.serialisation;

import static org.assertj.core.api.Assertions.assertThat;

import com.francesco.codeexercise.model.Journey;
import org.junit.jupiter.api.Test;

public class JourneyDeserializerTest {

  @Test
  public void deserialize_trip_2_stops() throws Exception {
    var tripDeserializer = new JourneyDeserializer();
    var expectedTrip = Journey.builder().tagOn("Stop1").tagOff("Stop2").build();

    assertThat(tripDeserializer.deserializeKey("Stop1_Stop2", null)).isEqualTo(expectedTrip);
  }

  @Test
  public void deserialize_trip_one_stop() throws Exception {
    var tripDeserializer = new JourneyDeserializer();
    var expectedTrip = Journey.builder().tagOn("Stop3").build();

    assertThat(tripDeserializer.deserializeKey("Stop3", null)).isEqualTo(expectedTrip);
  }
}
