package com.francesco.codeexercise.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tap {
  Long id;
  String dateTimeUTC;
  TapType tapType;
  String stopId;
  String companyId;
  String busId;
  String pan;

}
