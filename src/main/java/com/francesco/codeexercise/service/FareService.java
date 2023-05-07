package com.francesco.codeexercise.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.francesco.codeexercise.model.Fare;
import com.francesco.codeexercise.model.FareType;
import com.francesco.codeexercise.model.Trip;
import com.francesco.codeexercise.service.serialisation.TripDeserializer;
import com.francesco.codeexercise.service.serialisation.TripSerializer;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Component responsible in calculating fares given a tap-on/tap-off
 * For simplicity the fare database is loaded from a file and serialised in a TreeMap.
 * TreeMaps have an efficient access O(log-n) in best and worst case scenario and scale better than
 * Hashmaps
 *
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class FareService {

  private final ObjectMapper objectMapper;
  private Map<Trip, Fare> fares;

  private static final String FARE_DB_NAME = "fares.json";

  @VisibleForTesting
  @PostConstruct
  boolean load() {
    try {
      configureObjectMapper();
      fares = objectMapper.readValue(ClassLoader.getSystemResource(FARE_DB_NAME), new TypeReference<HashMap<Trip, Fare>>() {});
      return !fares.isEmpty();
    } catch (Exception e) {
      log.error("Error loading file database", e);
      return false;
    }
  }

  /**
   * registers custom serialisers/deserialisers as Object Mapper
   * by default cannot serialise POJOs as keys ina  Map
   */
  private void configureObjectMapper() {
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(Trip.class, new TripSerializer());
    module.addKeyDeserializer(Trip.class, new TripDeserializer());
    objectMapper.registerModule(module);
  }

  /**
   * Returns a fare for a given tag on / tag off.
   * Its efficiency leverages on the TreeMap structure O(log-n)
   *
   * If TagOn same as TagOff the trip is cancelled and fare is zero
   *
   * @param tagOn
   * @param tagOff
   * @return the given fare
   */
  public Fare getFare(String tagOn, String tagOff) {
    if(Objects.equals(tagOn,tagOff)) {
      return Fare.builder().type(FareType.CANCELLED).priceInDollars(0).build();
    }
    return fares.get(Trip.builder().tagOn(tagOn).tagOff(tagOff).build());
  }

}
