package com.mindee.parsing;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointInfo {
  String endpointName();

  String version();

  String accountName() default "Mindee";
}
