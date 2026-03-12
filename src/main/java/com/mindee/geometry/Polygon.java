package com.mindee.geometry;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * Contains any number of vertex coordinates (Points).
 */
@Getter
@JsonDeserialize(using = PolygonDeserializer.class)
public class Polygon {
  /**
   * Position information as a list of points in clockwise order.
   */
  private List<Point> coordinates = new ArrayList<>();

  @Builder
  public Polygon(List<Point> coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * Get the polygon as a Bbox.
   */
  public Bbox getAsBbox() {
    return BboxUtils.generate(this);
  }

  /**
   * Get the central coordinates (centroid) of the Polygon.
   */
  public Point getCentroid() {
    return PolygonUtils.getCentroid(coordinates);
  }

  /**
   * Get the maximum and minimum Y coordinates.
   */
  public MinMax getMinMaxY() {
    return PolygonUtils.getMinMaxY(coordinates);
  }

  /**
   * Get the maximum and minimum Y coordinates.
   */
  public MinMax getMinMaxX() {
    return PolygonUtils.getMinMaxX(coordinates);
  }

  /**
   * Returns true if there are no coordinates.
   */
  public boolean isEmpty() {
    return coordinates.isEmpty();
  }

  /**
   * Default string representation.
   */
  public String toString() {
    if (!isEmpty()) {
      return String.format("Polygon with %s points.", getCoordinates().size());
    }
    return "";
  }

  /**
   * String representation with precise coordinates.
   */
  public String toStringPrecise() {
    StringBuilder builder = new StringBuilder("(");
    for (int i = 0; i < coordinates.size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(coordinates.get(i));
    }
    builder.append(")");
    return builder.toString();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || this.getClass() != object.getClass()) {
      return false;
    }
    Polygon polygon = (Polygon) object;
    if (this.coordinates.size() != polygon.coordinates.size()) {
      return false;
    }
    for (int i = 0; i < this.coordinates.size(); i++) {
      if (!this.coordinates.get(i).equals(polygon.coordinates.get(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return coordinates.hashCode();
  }
}
