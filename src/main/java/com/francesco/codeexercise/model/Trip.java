package com.francesco.codeexercise.model;

import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip implements Comparable<Trip>{
  String tagOn;
  String tagOff;

  /**
   * To avoid the fare database grows too much, it is useful to make sure
   * the direction of the trip doesn't matter.
   * Tagging on from Stop1 and Tagging off from Stop2
   * should be the same as tagging on from Stop2 and tagging off Stop 1
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
    Trip trip = (Trip) o;
    return Objects.equals(tagOn, trip.tagOn) && Objects.equals(tagOff,
        trip.tagOff) || Objects.equals(tagOn, trip.tagOff) && Objects.equals(tagOff,
        trip.tagOn);
  }


  /**
   * To avoid the fare database grows too much, it is useful to make sure
   * the direction of the trip doesn't matter.
   * Tagging on from Stop1 and Tagging off from Stop2
   * should be the same as tagging on from Stop2 and tagging off Stop 1
   * @return hash-code
   */
  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public int compareTo(Trip t) {
    var tOn = t.tagOn;
    var tOff = t.tagOff;
    //scenario ofn trips on opposit direction
    if(Objects.equals(t.tagOff,this.tagOn) && Objects.equals(t.tagOn,this.tagOff)) {
      tOn = t.tagOff;
      tOff = t.tagOn;
    }
    if (compareTag(this.tagOn,tOn) == 0) {
      return compareTag(this.tagOff,tOff);
    }
    return compareTag(this.tagOn, tOn);
  }

  private int compareTag(String tag1, String tag2) {
    if(tag1 != null && tag2 == null) {
      return 1;
    }
    if(tag1 == null && tag2 != null) {
      return -1;
    }
    if(tag1 == null && tag2 == null) {
      return 0;
    }
    return tag1.compareTo(tag2);
  }
}
