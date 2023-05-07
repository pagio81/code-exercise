package com.francesco.codeexercise.service.serialisation;

import com.francesco.codeexercise.model.Trip;
import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

/**
 * Hides CSVWriter for the rest of the system and allows to easily
 * replace it with another writer
 */
@Log4j2
public class TripWriter implements AutoCloseable{
  private CSVPrinter printer;

  public boolean open(File ouputFile) {
    try {
      printer = new CSVPrinter(new FileWriter(ouputFile), CSVFormat.EXCEL.builder().setQuoteMode(
          QuoteMode.NONE).setEscape('/').build());
      printer.printRecord("Started","Finished","DurationSecs","FromStopId","ToStopId",
          "ChargeAmount","CompanyId","BusID","PAN","Status");
      return true;
    } catch (IOException e) {
      log.error("Error opening output file "+ouputFile.getName(), e);
      return false;
    }
  }

  public void close(){
    try {
      printer.close();
    } catch (IOException e){
      log.error("Error closing output file", e);
    }
  }

  public boolean writeTrip(Trip trip) {
    try {
      printer.printRecord(trip.getStarted(),trip.getFinished(),trip.getDurationSecs(),
          trip.getFromStopId(),trip.getToStopId(),trip.getChargeAmount(),trip.getCompanyId(),
          trip.getBusID(),trip.getPan(),trip.getStatus());
      return true;
    } catch (IOException e) {
      log.error("Error writing trip "+trip, e);
      return false;
    }
  }

  /**
   * useful to test the content of the file before it is closed as unit tests use delete on exit
   */
  @VisibleForTesting
  void flush() {
    try {
      printer.flush();
    } catch (IOException e) {
      log.error("Error flushing file", e);
    }
  }

}
