package com.mindee.geometry;

public class BoundingBox {
  private double minX;
  private double maxX;
  private double minY;
  private double maxY;

  /**
   * @param minX
   * @param maxX
   * @param minY
   * @param maxY
   */
  public BoundingBox(double minX, double maxX, double minY, double maxY) {
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }

  /**
   * @return the minX
   */
  public double getMinX() {
    return minX;
  }

  /**
   * @return the maxX
   */
  public double getMaxX() {
    return maxX;
  }

  /**
   * @return the minY
   */
  public double getMinY() {
    return minY;
  }

  /**
   * @return the maxY
   */
  public double getMaxY() {
    return maxY;
  }

}
