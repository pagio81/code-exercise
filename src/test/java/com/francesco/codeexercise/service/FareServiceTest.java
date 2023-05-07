package com.francesco.codeexercise.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.francesco.codeexercise.model.FareType;
import org.junit.jupiter.api.Test;


class FareServiceTest {

  @Test
  public void load_fares_is_successful() {
    var fareService = new FareService(new ObjectMapper());
    assertThat(fareService.load()).isTrue();
  }

  @Test
  public void get_fare_complete_trips() {
    var fareService = new FareService(new ObjectMapper());
    fareService.load();

    var fare = fareService.getFare("Stop1", "Stop2");
    assertThat(fare.getType()).isEqualTo(FareType.COMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(3.25);

    fare = fareService.getFare("Stop2", "Stop1");
    assertThat(fare.getType()).isEqualTo(FareType.COMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(3.25);

    fare = fareService.getFare("Stop1", "Stop3");
    assertThat(fare.getType()).isEqualTo(FareType.COMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(7.30);

    fare = fareService.getFare("Stop3", "Stop2");
    assertThat(fare.getType()).isEqualTo(FareType.COMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(5.50);


  }

  @Test
  public void get_fare_incomplete_trips() {
    var fareService = new FareService(new ObjectMapper());
    fareService.load();

    var fare = fareService.getFare("Stop1", null);
    assertThat(fare.getType()).isEqualTo(FareType.INCOMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(7.30);

    fare = fareService.getFare(null, "Stop1");
    assertThat(fare.getType()).isEqualTo(FareType.INCOMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(7.30);

    fare = fareService.getFare(null, "Stop3");
    assertThat(fare.getType()).isEqualTo(FareType.INCOMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(7.30);

    fare = fareService.getFare(null, "Stop2");
    assertThat(fare.getType()).isEqualTo(FareType.INCOMPLETE);
    assertThat(fare.getPriceInDollars()).isEqualTo(5.50);

  }

  @Test
  public void get_fare_cancelled_trips() {
    var fareService = new FareService(new ObjectMapper());
    fareService.load();

    var fare = fareService.getFare("Stop1", "Stop1");
    assertThat(fare.getType()).isEqualTo(FareType.CANCELLED);
    assertThat(fare.getPriceInDollars()).isEqualTo(0);
  }


}
