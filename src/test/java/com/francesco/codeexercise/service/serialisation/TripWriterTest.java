package com.francesco.codeexercise.service.serialisation;

import static org.assertj.core.api.Assertions.assertThat;

import com.francesco.codeexercise.model.FareType;
import com.francesco.codeexercise.model.Trip;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class TripWriterTest {

  @Test
  public void can_open_and_close_file() throws Exception {
    File file = new File("test.csv");
    file.deleteOnExit();

    try (var writer = new TripWriter()) {
      var result = writer.open(file);
      assertThat(file.exists()).isTrue();
      assertThat(result).isTrue();
    }
    assertThat(!file.exists()).isFalse();
  }

  @Test
  public void can_write_a_trip() throws Exception {
    File file = new File("test.csv");
    file.deleteOnExit();

    try (var writer = new TripWriter()) {
      var result = writer.open(file);
      assertThat(file.exists()).isTrue();
      writer.writeTrip(Trip.builder()
          .started("10-08-2023 10:00:00")
          .finished("10-08-2023 10:10:00")
          .durationSecs(600L)
          .fromStopId("Stop1")
          .toStopId("Stop2")
          .chargeAmount(3.25)
          .companyId("Company1")
          .busID("Bus37")
          .pan("5500005555555559")
          .status(FareType.COMPLETE)
          .build());
      writer.flush();

      var lines = IOUtils.readLines(new FileInputStream(file), StandardCharsets.UTF_8);
      assertThat(lines.size()).isEqualTo(2);
      assertThat(lines.get(0)).isEqualTo("Started,Finished,DurationSecs,FromStopId,ToStopId,"
          + "ChargeAmount,CompanyId,BusID,PAN,Status");//header
      assertThat(lines.get(1)).isEqualTo("10-08-2023 10:00:00,10-08-2023 10:10:00,600,Stop1,Stop2,"
          + "3.25,Company1,Bus37,5500005555555559,COMPLETE");//header

      assertThat(result).isTrue();
    }
    assertThat(!file.exists()).isFalse();
  }


}
