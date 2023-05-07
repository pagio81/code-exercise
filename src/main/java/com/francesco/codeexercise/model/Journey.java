package com.francesco.codeexercise.model;

import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Journey implements Comparable<Journey> {

  String tagOn;
  String tagOff;

  /**
   * The journey where tagging on from Stop1 and tagging off from Stop2 should be the same as
   * tagging on from Stop2 and tagging off Stop 1 as travelling in opposite direction is the same
   *
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Journey trip = (Journey) o;
    return Objects.equals(tagOn, trip.tagOn) && Objects.equals(tagOff,
        trip.tagOff) || Objects.equals(tagOn, trip.tagOff) && Objects.equals(tagOff,
        trip.tagOn);
  }

  /**
   * The journey where tagging on from Stop1 and tagging off from Stop2 should be the same as
   * tagging on from Stop2 and tagging off Stop 1
   *
   * @return
   */
  @Override
  public int hashCode() {
    int hash = 17;
    int hashMultiplicator = 79;
    int hashSum = Optional.ofNullable(tagOn).map(t -> t.hashCode()).orElse(0) +
        Optional.ofNullable(tagOff).map(t -> t.hashCode()).orElse(0);
    hash = hashMultiplicator * hash * hashSum;
    return hash;
  }

  /**
   * Compare 2 journeys, used for sorting the trips in Map. Journeys are sorted by tagOn,tagOff The
   * trip where tagging on from Stop1 and tagging off from Stop2 should be the same as tagging on
   * from Stop2 and tagging off Stop 1 and should return 0.
   *
   * @param t the object to be compared.
   * @return
   */
  @Override
  public int compareTo(Journey t) {
    var tOn = t.tagOn;
    var tOff = t.tagOff;
    //scenario ofn trips on opposit direction
    if (Objects.equals(t.tagOff, this.tagOn) && Objects.equals(t.tagOn, this.tagOff)) {
      tOn = t.tagOff;
      tOff = t.tagOn;
    }
    if (compareTag(this.tagOn, tOn) == 0) {
      return compareTag(this.tagOff, tOff);
    }
    return compareTag(this.tagOn, tOn);
  }

  /**
   * Compare nullabale tags
   *
   * @param tag1
   * @param tag2
   * @return
   */
  private int compareTag(String tag1, String tag2) {
    if (tag1 != null && tag2 == null) {
      return 1;
    }
    if (tag1 == null && tag2 != null) {
      return -1;
    }
    if (tag1 == null && tag2 == null) {
      return 0;
    }
    return tag1.compareTo(tag2);
  }
}
