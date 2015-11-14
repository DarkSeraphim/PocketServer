package com.pocketserver.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginInfo {
    String value();
    String version() default "1.0.0";
    String description() default "";
    String author() default "";
    String[] dependencies() default "";
}
