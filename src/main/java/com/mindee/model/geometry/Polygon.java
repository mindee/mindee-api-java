package com.mindee.model.geometry;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.PolygonDeserializer;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Polygon {
  private List<Point> coordinates;

  public Polygon(List<Point> coordinates) {
    this.coordinates = coordinates;
  }
}
