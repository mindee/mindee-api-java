package com.mindee.geometry;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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

  /**
   * Create a Polygon from a list of a list of floats.
   */
  public Polygon(Collection<List<Double>> coordinates) {
    this.coordinates = coordinates
      .stream()
      .map(coordinate -> new Point(coordinate.get(0), coordinate.get(1)))
      .collect(Collectors.toList());
  }

  /**
   * Create a Polygon from a list of Points.
   */
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
   * Get a bounding box that encloses the Polygon.
   */
  public Polygon getBoundingBox() {
    var minMaxX = getMinMaxX();
    var minMaxY = getMinMaxY();
    return new Polygon(
      List
        .of(
          new Point(minMaxX.getMin(), minMaxY.getMin()),
          new Point(minMaxX.getMax(), minMaxY.getMin()),
          new Point(minMaxX.getMax(), minMaxY.getMax()),
          new Point(minMaxX.getMin(), minMaxY.getMax())
        )
    );
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
    return coordinates.stream().map(Point::toString).collect(Collectors.joining(", ", "(", ")"));
  }

  /**
   * Compare two polygons based on their Y coordinates.
   * Useful for sorting lists.
   */
  public int compareOnY(Polygon polygon2) {
    double sort = this.getMinMaxY().getMin() - polygon2.getMinMaxY().getMin();
    if (sort == 0) {
      return 0;
    }
    return sort < 0 ? -1 : 1;
  }

  /**
   * Compare two polygons based on their X coordinates.
   * Useful for sorting lists.
   */
  public int compareOnX(Polygon polygon2) {
    double sort = this.getMinMaxX().getMin() - polygon2.getMinMaxX().getMin();
    if (sort == 0) {
      return 0;
    }
    return sort < 0 ? -1 : 1;
  }

  /**
   * Merge the coordinates of the two polygons.
   */
  public Polygon combine(Polygon target) {
    return PolygonUtils.combine(this, target);
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
