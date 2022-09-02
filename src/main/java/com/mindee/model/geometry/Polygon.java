package com.mindee.model.geometry;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Polygon {
  private List<Point> coordinates = new ArrayList<>();

  public Polygon(List<Point> coordinates) {
    this.coordinates = coordinates;
  }
}