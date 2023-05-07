package com.francesco.codeexercise.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.francesco.codeexercise.exception.ValidationException;
import com.francesco.codeexercise.model.Fare;
import com.francesco.codeexercise.model.TripType;
import com.francesco.codeexercise.model.Tap;
import org.junit.jupiter.api.Test;

public class TripServiceTest {

  private final TripService tripService = new TripService();

  @Test
  public void test_duration() {
    var duration = tripService.duration("22-09-2023 10:00:00", "22-09-2023 10:10:00");
    assertThat(duration).isEqualTo(600L);
  }

  @Test
  public void test_duration_cross_days() {
    var duration = tripService.duration("22-09-2023 23:55:00", "23-09-2023 00:05:00");
    assertThat(duration).isEqualTo(600L);
  }

  @Test
  public void test_completed_trip() {
    var tapOn = Tap.builder().pan("5500005555555559").dateTimeUTC("22-09-2023 23:55:00")
        .stopId("Stop1").busId("Bus37").companyId("Company1").build();
    var tapOff = Tap.builder().pan("5500005555555559").dateTimeUTC("23-09-2023 00:05:00")
        .stopId("Stop2").busId("Bus37").companyId("Company1").build();
    var fare = Fare.builder().priceInDollars(3.25).type(TripType.COMPLETED).build();
    var trip = tripService.getTrip(tapOn, tapOff, fare);
    assertThat(trip.getDurationSecs()).isEqualTo(600L);
    assertThat(trip.getBusID()).isEqualTo("Bus37");
    assertThat(trip.getStarted()).isEqualTo("22-09-2023 23:55:00");
    assertThat(trip.getFinished()).isEqualTo("23-09-2023 00:05:00");
    assertThat(trip.getFromStopId()).isEqualTo("Stop1");
    assertThat(trip.getToStopId()).isEqualTo("Stop2");
    assertThat(trip.getChargeAmount()).isEqualTo(3.25);
    assertThat(trip.getStatus()).isEqualTo(TripType.COMPLETED);

  }

  @Test
  public void test_incomplete_trip() {
    var tapOn = Tap.builder().pan("5500005555555559").dateTimeUTC("22-09-2023 23:55:00")
        .stopId("Stop1").busId("Bus37").companyId("Company1").build();
    var fare = Fare.builder().priceInDollars(3.25).type(TripType.INCOMPLETE).build();
    var trip = tripService.getTrip(tapOn, fare);
    assertThat(trip.getDurationSecs()).isEqualTo(-1L);
    assertThat(trip.getBusID()).isEqualTo("Bus37");
    assertThat(trip.getStarted()).isEqualTo("22-09-2023 23:55:00");
    assertThat(trip.getFinished()).isEqualTo("-");
    assertThat(trip.getFromStopId()).isEqualTo("Stop1");
    assertThat(trip.getToStopId()).isEqualTo("-");
    assertThat(trip.getChargeAmount()).isEqualTo(3.25);
    assertThat(trip.getStatus()).isEqualTo(TripType.INCOMPLETE);

  }

  @Test
  public void test_different_pan() {
    var tapOn = Tap.builder().pan("5500005555555559").dateTimeUTC("22-09-2023 23:55:00")
        .stopId("Stop1").busId("Bus37").companyId("Company1").build();
    var tapOff = Tap.builder().pan("5500005555555558").dateTimeUTC("23-09-2023 00:05:00")
        .stopId("Stop2").busId("Bus37").companyId("Company1").build();
    var fare = Fare.builder().priceInDollars(3.25).type(TripType.COMPLETED).build();
    assertThatExceptionOfType(ValidationException.class).isThrownBy(
        () -> tripService.getTrip(tapOn, tapOff, fare));

  }

  @Test
  public void test_different_bus() {
    var tapOn = Tap.builder().pan("5500005555555559").dateTimeUTC("22-09-2023 23:55:00")
        .stopId("Stop1").busId("Bus37").companyId("Company1").build();
    var tapOff = Tap.builder().pan("5500005555555559").dateTimeUTC("23-09-2023 00:05:00")
        .stopId("Stop2").busId("Bus38").companyId("Company1").build();
    var fare = Fare.builder().priceInDollars(3.25).type(TripType.COMPLETED).build();
    assertThatExceptionOfType(ValidationException.class).isThrownBy(
        () -> tripService.getTrip(tapOn, tapOff, fare));

  }

  @Test
  public void test_different_company() {
    var tapOn = Tap.builder().pan("5500005555555559").dateTimeUTC("22-09-2023 23:55:00")
        .stopId("Stop1").busId("Bus37").companyId("Company1").build();
    var tapOff = Tap.builder().pan("5500005555555559").dateTimeUTC("23-09-2023 00:05:00")
        .stopId("Stop2").busId("Bus37").companyId("Company2").build();
    var fare = Fare.builder().priceInDollars(3.25).type(TripType.COMPLETED).build();
    assertThatExceptionOfType(ValidationException.class).isThrownBy(
        () -> tripService.getTrip(tapOn, tapOff, fare));

  }
}
