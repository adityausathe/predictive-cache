package com.adus.predictivecache.framework.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PredictivelyCachedResource {

    String DEFAULT_RESOURCE_ID = "$$#!DefaultResourceId!#$$";

    String id() default DEFAULT_RESOURCE_ID;
}
