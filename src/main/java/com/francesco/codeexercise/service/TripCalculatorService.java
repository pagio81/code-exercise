package com.francesco.codeexercise.service;

import com.francesco.codeexercise.model.Tap;
import com.francesco.codeexercise.model.TapType;
import com.francesco.codeexercise.service.serialisation.TripWriter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class TripCalculatorService {

  private final FareService fareService;
  private final TapService tapService;
  private final TripService tripService;

  /**
   * process trips. Reads tap file and every time finds a tag off, calculates fare and writes the
   * trip on output file, Then process all tag ons without a tag off.
   *
   * @param inputFile
   * @param outputFile
   */
  public void processTrips(File inputFile, File outputFile) {
    //   Assumption: if user in the same file tagged on and off multiple times, e.g. change bus,
    //   the tags are registered in the correct order and it is ok as outcome 2 trips
    //
    //   a possible fix would be to use as key of "taps" the concatenation of PAN and BusId
    final Map<String, Tap> taps = new HashMap<>();
    try (var writer = tripService.writer(outputFile)) {
      tapService.getTaps(inputFile).forEach(tap -> {
        if (TapType.ON.equals(tap.getTapType())) {
          var previousTapOn = taps.put(tap.getPan(), tap);
          if (previousTapOn != null) {
            log.warn("Found 2 tap ons for same PAN, this should not be possible?");
          }
        } else {
          var tagOn = taps.get(tap.getPan());
          var tagOff = tap;
          var fare = fareService.getFare(tagOn.getStopId(), tagOff.getStopId());
          writer.writeTrip(tripService.getTrip(tagOn, tagOff, fare));

          var removed = taps.remove(tagOn.getPan());
          if (removed == null) {
            log.warn("Could not remove PAN");
          }
        }
      });
      processIncompleteTrips(taps, writer);
    }
  }

  /**
   * process all tags that received a tag on but not a tag off
   *
   * @param taps
   * @param writer
   */
  void processIncompleteTrips(Map<String, Tap> taps, TripWriter writer) {
    taps.keySet().stream().forEach(pan -> {
      var tagOn = taps.get(pan);
      var fare = fareService.getFare(tagOn.getStopId(), null);

      writer.writeTrip(tripService.getTrip(tagOn, fare));
    });
  }

}
