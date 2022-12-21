package com.mindee.http;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointInfo {
    String endpointName() default "Pankaj";
    String version();
    String accountName() default "Mindee";
}
