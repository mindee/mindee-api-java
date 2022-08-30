package com.mindee.geometry;

public final class Point {
  private Double x;
  private Double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * @return the x
   */
  public Double getX() {
    return x;
  }

  /**
   * @return the y
   */
  public Double getY() {
    return y;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Point) {
      Point pointToCompare = (Point) obj;
      if (this.getX().equals(pointToCompare.getX())
          && this.getY().equals(pointToCompare.getY())) {
        return true;
      }
    }
    return false;
  }

}