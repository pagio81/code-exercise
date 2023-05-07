package com.francesco.codeexercise.service;

import com.francesco.codeexercise.exception.ValidationException;
import com.francesco.codeexercise.model.FareType;
import com.francesco.codeexercise.model.Tap;
import com.francesco.codeexercise.model.Fare;
import com.francesco.codeexercise.model.Trip;
import com.francesco.codeexercise.service.serialisation.TripWriter;
import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TripService{

  public TripWriter writer(File ouputFile){
    var writer = new TripWriter();
    writer.open(ouputFile);
    return writer;
  }

  private void validate(Tap tagOn, Tap tagOff) {
    if(!Objects.equals(tagOn.getPan(),tagOff.getPan())) {
      throw new ValidationException("PAN does not match: TagOn "+tagOn.getId()
          +" TagOff "+tagOff.getId());
    }

    if(!Objects.equals(tagOn.getBusId(),tagOff.getBusId())) {
      throw new ValidationException("PAN does not match: TagOn "+tagOn.getId()
          +" TagOff "+tagOff.getId());
    }
    if(!Objects.equals(tagOn.getCompanyId(),tagOff.getCompanyId())) {
      throw new ValidationException("Company does not match: TagOn "+tagOn.getId()
          +" TagOff "+tagOff.getId());
    }
  }

  public Trip getTrip(Tap tagOn, Tap tagOff, Fare fare) {
    validate(tagOn, tagOff);
    return Trip.builder()
        .started(tagOn.getDateTimeUTC())
        .finished(tagOff.getDateTimeUTC())
        .durationSecs(duration(tagOn.getDateTimeUTC(),tagOff.getDateTimeUTC()))
        .fromStopId(tagOn.getStopId())
        .toStopId(tagOff.getStopId())
        .chargeAmount(fare.getPriceInDollars())
        .companyId(tagOn.getCompanyId())
        .busID(tagOn.getBusId())
        .pan(tagOn.getPan())
        .status(fare.getType())
        .build();
  }

  public Trip getTrip(Tap tagOn, Fare fare) {
    return Trip.builder()
        .started(tagOn.getDateTimeUTC())
        .finished("-")
        .durationSecs(-1L)
        .fromStopId(tagOn.getStopId())
        .toStopId("-")
        .chargeAmount(fare.getPriceInDollars())
        .companyId(tagOn.getCompanyId())
        .busID(tagOn.getBusId())
        .pan(tagOn.getPan())
        .status(fare.getType())
        .build();
  }


  @VisibleForTesting
  Long duration(String tagOn, String tagOff) {
    try {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-MM-yyyy HH:mm:ss");
      var tagOnTime = LocalDateTime.parse(tagOn, dtf);
      var tagOffTime = LocalDateTime.parse(tagOff, dtf);
      return Duration.between(tagOnTime,tagOffTime).getSeconds();
    } catch (Exception e) {
      log.error("error calculating duration between "+tagOn+" and "+tagOff);
      return -1L;
    }

  }

}
