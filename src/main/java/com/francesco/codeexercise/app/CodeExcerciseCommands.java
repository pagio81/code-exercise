package com.francesco.codeexercise.app;

import com.francesco.codeexercise.service.TripCalculatorService;
import java.io.File;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
@Log4j2
public class CodeExcerciseCommands {

  private static final String INPUT_FILE_NAME = "taps.csv";
  private static final String OUTPUT_FILE_NAME = "trips.csv";

  private final TripCalculatorService tripCalculatorService;

  @ShellMethod("Process input file \""+INPUT_FILE_NAME+"\" and produce output file \""+OUTPUT_FILE_NAME+"\"")
  public void process() {
    processFile(INPUT_FILE_NAME,OUTPUT_FILE_NAME);
  }

  @ShellMethod("Process input/output files with given locations ")
  public void processFile(String input, String output) {
    File fileInput = new File(input);
    log.info("Processing file: {}", ()-> fileInput.getAbsolutePath());
    File fileOutput = new File(output);
    log.info("Write on file: {}", ()-> fileOutput.getAbsolutePath());

    tripCalculatorService.processTrips(fileInput, fileOutput);

  }
}
