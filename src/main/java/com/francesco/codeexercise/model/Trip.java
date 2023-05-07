package com.francesco.codeexercise.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Trip {

  String started;
  String finished;
  Long durationSecs;
  String fromStopId;
  String toStopId;
  double chargeAmount;
  String companyId;
  String busID;
  String pan;
  FareType status;
}
