package com.francesco.codeexercise.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.francesco.codeexercise.model.Fare;
import com.francesco.codeexercise.model.TripType;
import com.francesco.codeexercise.model.Tap;
import com.francesco.codeexercise.model.Trip;
import com.francesco.codeexercise.service.serialisation.TripWriter;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class TripCalculatorServiceTest {

  FareService fareService = Mockito.mock(FareService.class);
  TapService tapService = Mockito.mock(TapService.class);
  TripService tripService = Mockito.mock(TripService.class);

  TripWriter tripWriter = Mockito.mock(TripWriter.class);

  @BeforeTestMethod
  public void reset() {
    Mockito.reset(fareService, tapService, tripService, tripWriter);
  }

  private Stream<Tap> getTestData(String fileName) {
    try {
      return new ObjectMapper().readValue(ClassLoader.getSystemResource("test-data/" + fileName),
          new TypeReference<List<Tap>>() {
          }).stream();
    } catch (Exception e) {
      return null;
    }
  }


  @Test
  public void test_process_incomplete() {
    when(fareService.getFare(any(), any())).thenReturn(Fare.builder()
        .priceInDollars(7.30).type(TripType.INCOMPLETE).build());
    when(tripService.getTrip(any(), any())).thenCallRealMethod();

    Map<String, Tap> tagsOn = getTestData("incomplete_trip.json").collect(
        Collectors.toMap(Tap::getPan,
            Function.identity()));

    var captor = ArgumentCaptor.forClass(Trip.class);

    when(tripWriter.writeTrip(captor.capture())).thenReturn(true);

    var tripCalculatorService = new TripCalculatorService(fareService, tapService, tripService);
    tripCalculatorService.processIncompleteTrips(tagsOn, tripWriter);

    var trip = captor.getValue();
    assertThat(trip).isNotNull();
    assertThat(trip.getPan()).isEqualTo("5454545454545454");
    assertThat(trip.getStatus()).isEqualTo(TripType.INCOMPLETE);
    assertThat(trip.getStarted()).isEqualTo("10-09-2023 10:00:00");
    assertThat(trip.getFinished()).isEqualTo("-");
    assertThat(trip.getDurationSecs()).isEqualTo(-1);
    assertThat(trip.getChargeAmount()).isEqualTo(7.30);

  }

  @Test
  public void test_complete_trip() {

    when(tripService.writer(any())).thenReturn(tripWriter);
    when(tapService.getTaps(any())).thenReturn(getTestData("complete_trip.json"));

    when(fareService.getFare(any(), any())).thenReturn(Fare.builder()
        .priceInDollars(3.25).type(TripType.COMPLETED).build());
    when(tripService.getTrip(any(), any())).thenCallRealMethod();
    when(tripService.getTrip(any(), any(), any())).thenCallRealMethod();
    when(tripService.duration(any(), any())).thenCallRealMethod();

    var captor = ArgumentCaptor.forClass(Trip.class);

    when(tripWriter.writeTrip(captor.capture())).thenReturn(true);

    var tripCalculatorService = new TripCalculatorService(fareService, tapService, tripService);
    tripCalculatorService.processTrips(new File("input.csv"), new File("output.csv"));

    var trip = captor.getValue();
    assertThat(trip).isNotNull();
    assertThat(trip.getPan()).isEqualTo("5454545454545454");
    assertThat(trip.getStatus()).isEqualTo(TripType.COMPLETED);
    assertThat(trip.getStarted()).isEqualTo("10-09-2023 10:00:00");
    assertThat(trip.getFinished()).isEqualTo("10-09-2023 10:15:00");
    assertThat(trip.getDurationSecs()).isEqualTo(900L);
    assertThat(trip.getChargeAmount()).isEqualTo(3.25);

  }

  @Test
  public void test_complete_two_trips() {
    when(tripService.writer(any())).thenReturn(tripWriter);
    when(tapService.getTaps(any())).thenReturn(getTestData("complete_two_trips.json"));

    when(fareService.getFare(eq("Stop1"), eq("Stop2"))).thenReturn(Fare.builder()
        .priceInDollars(3.25).type(TripType.COMPLETED).build());
    when(fareService.getFare(eq("Stop2"), eq("Stop3"))).thenReturn(Fare.builder()
        .priceInDollars(5.50).type(TripType.COMPLETED).build());

    when(tripService.getTrip(any(), any())).thenCallRealMethod();
    when(tripService.getTrip(any(), any(), any())).thenCallRealMethod();
    when(tripService.duration(any(), any())).thenCallRealMethod();

    var captor = ArgumentCaptor.forClass(Trip.class);

    when(tripWriter.writeTrip(captor.capture())).thenReturn(true);

    var tripCalculatorService = new TripCalculatorService(fareService, tapService, tripService);
    tripCalculatorService.processTrips(new File("input.csv"), new File("output.csv"));

    verify(tripWriter, times(2)).writeTrip(any());

    var trip1 = captor.getAllValues().get(0);
    assertThat(trip1).isNotNull();
    assertThat(trip1.getPan()).isEqualTo("5454545454545454");
    assertThat(trip1.getStatus()).isEqualTo(TripType.COMPLETED);
    assertThat(trip1.getStarted()).isEqualTo("10-09-2023 10:00:00");
    assertThat(trip1.getFinished()).isEqualTo("10-09-2023 10:15:00");
    assertThat(trip1.getDurationSecs()).isEqualTo(900L);
    assertThat(trip1.getChargeAmount()).isEqualTo(3.25);

    var trip2 = captor.getAllValues().get(1);
    assertThat(trip2).isNotNull();
    assertThat(trip2.getPan()).isEqualTo("5454545454545454");
    assertThat(trip2.getStatus()).isEqualTo(TripType.COMPLETED);
    assertThat(trip2.getStarted()).isEqualTo("10-09-2023 10:16:00");
    assertThat(trip2.getFinished()).isEqualTo("10-09-2023 10:26:00");
    assertThat(trip2.getDurationSecs()).isEqualTo(600L);
    assertThat(trip2.getChargeAmount()).isEqualTo(5.50);


  }

  @Test
  public void test_cancelled_trip() {

    when(tripService.writer(any())).thenReturn(tripWriter);
    when(tapService.getTaps(any())).thenReturn(getTestData("cancelled_trip.json"));

    when(fareService.getFare(any(), any())).thenReturn(Fare.builder()
        .priceInDollars(0).type(TripType.CANCELLED).build());
    when(tripService.getTrip(any(), any())).thenCallRealMethod();
    when(tripService.getTrip(any(), any(), any())).thenCallRealMethod();
    when(tripService.duration(any(), any())).thenCallRealMethod();

    var captor = ArgumentCaptor.forClass(Trip.class);

    when(tripWriter.writeTrip(captor.capture())).thenReturn(true);

    var tripCalculatorService = new TripCalculatorService(fareService, tapService, tripService);
    tripCalculatorService.processTrips(new File("input.csv"), new File("output.csv"));

    var trip = captor.getValue();
    assertThat(trip).isNotNull();
    assertThat(trip.getPan()).isEqualTo("5454545454545454");
    assertThat(trip.getStatus()).isEqualTo(TripType.CANCELLED);
    assertThat(trip.getStarted()).isEqualTo("10-09-2023 10:00:00");
    assertThat(trip.getFinished()).isEqualTo("10-09-2023 10:00:10");
    assertThat(trip.getDurationSecs()).isEqualTo(10L);
    assertThat(trip.getChargeAmount()).isEqualTo(0);
  }


  @Test
  public void test_incomplete_trip() {
    when(tripService.writer(any())).thenReturn(tripWriter);
    when(tapService.getTaps(any())).thenReturn(getTestData("incomplete_trip.json"));

    when(fareService.getFare(any(), any())).thenReturn(Fare.builder()
        .priceInDollars(7.30).type(TripType.INCOMPLETE).build());
    when(tripService.getTrip(any(), any())).thenCallRealMethod();
    when(tripService.getTrip(any(), any(), any())).thenCallRealMethod();
    when(tripService.duration(any(), any())).thenCallRealMethod();

    var captor = ArgumentCaptor.forClass(Trip.class);

    when(tripWriter.writeTrip(captor.capture())).thenReturn(true);

    var tripCalculatorService = new TripCalculatorService(fareService, tapService, tripService);
    tripCalculatorService.processTrips(new File("input.csv"), new File("output.csv"));

    var trip = captor.getValue();
    assertThat(trip).isNotNull();
    assertThat(trip.getPan()).isEqualTo("5454545454545454");
    assertThat(trip.getStatus()).isEqualTo(TripType.INCOMPLETE);
    assertThat(trip.getStarted()).isEqualTo("10-09-2023 10:00:00");
    assertThat(trip.getFinished()).isEqualTo("-");
    assertThat(trip.getDurationSecs()).isEqualTo(-1L);
    assertThat(trip.getChargeAmount()).isEqualTo(7.30);
  }

}
