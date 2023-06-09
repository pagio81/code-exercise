package com.francesco.codeexercise.service;

import com.francesco.codeexercise.exception.InputFileMalformedException;
import com.francesco.codeexercise.exception.InputFileNotFoundException;
import com.francesco.codeexercise.model.Tap;
import com.francesco.codeexercise.model.TapType;
import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TapService {

  /**
   * Returns a pointer to the tap datasource
   *
   * @param inputFile
   * @return
   */
  @VisibleForTesting
  CSVParser openFile(File inputFile) {
    try {
      if (!inputFile.exists()) {
        throw new InputFileNotFoundException("");
      }

      var csvFormat = CSVFormat.Builder
          .create(CSVFormat.EXCEL.withHeader("ID", "DateTimeUTC", "TapType", "StopId", "CompanyId",
              "BusID", "PAN")).setSkipHeaderRecord(true).build();
      return CSVParser.parse(inputFile, StandardCharsets.UTF_8, csvFormat);
    } catch (IOException e) {
      log.error("Error reading file {}", inputFile);
      throw new InputFileMalformedException("Couldn't read file: " + inputFile.getName(), e);
    }
  }

  /**
   * Returns a stream of taps from the source file. The file is not loaded in  memory, it reads line
   * by line
   *
   * @param inputFile
   * @return
   */
  public Stream<Tap> getTaps(File inputFile) {
    return openFile(inputFile).stream()
        .map(csvRecord -> Tap.builder()
            .id(Long.parseLong(csvRecord.get("ID").trim()))
            .dateTimeUTC(csvRecord.get("DateTimeUTC").trim())
            .tapType(TapType.valueOf(csvRecord.get("TapType").trim()))
            .stopId(csvRecord.get("StopId").trim())
            .companyId(csvRecord.get("CompanyId").trim())
            .busId(csvRecord.get("BusID").trim())
            .pan(csvRecord.get("PAN").trim())
            .build());
  }

}
