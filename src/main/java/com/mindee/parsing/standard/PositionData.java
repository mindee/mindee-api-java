package com.mindee.parsing.standard;

import com.mindee.geometry.Polygon;

/**
 * A field with position data.
 */
public interface PositionData {
  Polygon getBoundingBox();
  Polygon getPolygon();
}
