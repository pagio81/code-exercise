package com.francesco.codeexercise.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.francesco.codeexercise.model.TapType;
import java.io.File;
import java.time.Instant;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

@Log4j2
public class InputFileServiceTest {

  private InputFileService inputFileService = new InputFileService();

  @Test
  public void can_open_file(){
    File f = new File(ClassLoader.getSystemResource("taps.csv").getFile());
    assertThat(f.exists()).isTrue();

    CSVParser parser = inputFileService.openFile(f);
    assertThat(parser).isNotNull();

  }

  @Test
  public void read() throws Exception{

    File f = new File(ClassLoader.getSystemResource("taps.csv").getFile());
    assertThat(f.exists()).isTrue();

    var taps = inputFileService.readTaps(f);

    var firstTapOptional = taps.findFirst();
    assertThat(firstTapOptional).isNotEmpty();

    var firstTap = firstTapOptional.get();

    assertThat(firstTap.getId()).isEqualTo(1l);
    assertThat(firstTap.getDateTimeUTC()).isEqualTo("22-01-2018 13:00:00");
    assertThat(firstTap.getTapType()).isEqualTo(TapType.ON);
    assertThat(firstTap.getStopId()).isEqualTo("Stop1");
    assertThat(firstTap.getCompanyId()).isEqualTo("Company1");
    assertThat(firstTap.getBusId()).isEqualTo("Bus37");
    assertThat(firstTap.getPan()).isEqualTo("5500005555555559");

  }

}
