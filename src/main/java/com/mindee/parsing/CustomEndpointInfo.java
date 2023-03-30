package com.mindee.parsing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomEndpointInfo {
  /**
   * The name of the product associated to the expected model.
   */
  String endpointName();

  /**
   * The name of the account that owns the API. Required when using custom builder.
   */
  String accountName();

  /**
   * The version number of the API. Without the v (for example for the v1.2: 1.2).
   * 1 by default.
   */
  String version() default "1";
}
