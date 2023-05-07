package com.francesco.codeexercise.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class JourneyTest {

  @Test
  public void test_equal_with_same_direction() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff("Stop2")
        .build();

    var tripTwo = Journey.builder().tagOn("Stop1").tagOff("Stop2")
        .build();

    assertThat(tripOne).isEqualTo(tripTwo);
    assertThat(tripTwo).isEqualTo(tripOne);

    assertThat(tripOne.hashCode()).isEqualTo(tripTwo.hashCode());
    assertThat(tripTwo.hashCode()).isEqualTo(tripOne.hashCode());

    assertThat(tripOne.compareTo(tripTwo)).isEqualTo(0);
  }

  @Test
  public void test_equal_with_different_direction() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff("Stop2")
        .build();

    var tripTwo = Journey.builder().tagOn("Stop2").tagOff("Stop1")
        .build();

    assertThat(tripOne).isEqualTo(tripTwo);
    assertThat(tripTwo).isEqualTo(tripOne);

    assertThat(tripOne.hashCode()).isEqualTo(tripTwo.hashCode());
    assertThat(tripTwo.hashCode()).isEqualTo(tripOne.hashCode());

    //necessary fro treemap to work correctly
    assertThat(tripOne.compareTo(tripTwo)).isEqualTo(0);

  }

  @Test
  public void test_equal_with_one_stop() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff("Stop1")
        .build();

    var tripTwo = Journey.builder().tagOn("Stop1").tagOff("Stop1")
        .build();

    assertThat(tripOne).isEqualTo(tripTwo);
    assertThat(tripTwo).isEqualTo(tripOne);

    assertThat(tripOne.hashCode()).isEqualTo(tripTwo.hashCode());
    assertThat(tripTwo.hashCode()).isEqualTo(tripOne.hashCode());

    assertThat(tripOne.compareTo(tripTwo)).isEqualTo(0);
  }

  @Test
  public void test_equal_with_only_one_stop() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff(null)
        .build();

    var tripTwo = Journey.builder().tagOn("Stop1").tagOff(null)
        .build();

    assertThat(tripOne).isEqualTo(tripTwo);
    assertThat(tripTwo).isEqualTo(tripOne);

    assertThat(tripOne.hashCode()).isEqualTo(tripTwo.hashCode());
    assertThat(tripTwo.hashCode()).isEqualTo(tripOne.hashCode());

    assertThat(tripOne.compareTo(tripTwo)).isEqualTo(0);


  }

  @Test
  public void test_equal_with_only_one_stop_different_direction() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff(null)
        .build();

    var tripTwo = Journey.builder().tagOn(null).tagOff("Stop1")
        .build();

    assertThat(tripOne).isEqualTo(tripTwo);
    assertThat(tripTwo).isEqualTo(tripOne);

    assertThat(tripOne.hashCode()).isEqualTo(tripTwo.hashCode());
    assertThat(tripTwo.hashCode()).isEqualTo(tripOne.hashCode());

    assertThat(tripOne.compareTo(tripTwo)).isEqualTo(0);


  }


  @Test
  public void test_not_equal() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff("Stop2")
        .build();

    var tripTwo = Journey.builder().tagOn("Stop2").tagOff("Stop3")
        .build();

    assertThat(tripOne).isNotEqualTo(tripTwo);
    assertThat(tripTwo).isNotEqualTo(tripOne);

    assertThat(tripOne.hashCode()).isNotEqualTo(tripTwo.hashCode());
    assertThat(tripTwo.hashCode()).isNotEqualTo(tripOne.hashCode());

    assertThat(tripOne.compareTo(tripTwo)).isNotEqualTo(0);
  }

  @Test
  public void test_not_equal_with_only_one_stop() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff(null)
        .build();

    var tripTwo = Journey.builder().tagOn("Stop2").tagOff(null)
        .build();

    assertThat(tripOne).isNotEqualTo(tripTwo);
    assertThat(tripTwo).isNotEqualTo(tripOne);

  }

  @Test
  public void test_not_equal_with_only_one_stop_part_2() {
    var tripOne = Journey.builder().tagOn("Stop1").tagOff(null)
        .build();

    var tripTwo = Journey.builder().tagOn(null).tagOff("Stop2")
        .build();

    assertThat(tripOne).isNotEqualTo(tripTwo);
    assertThat(tripTwo).isNotEqualTo(tripOne);

  }
}
