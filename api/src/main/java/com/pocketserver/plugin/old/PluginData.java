package com.pocketserver.plugin.old;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginData {

    String name();

    String version() default Plugin.DEFAULT_VERSION;

    String description() default Plugin.DEFAULT_DESCRIPTION;

    String[]dependency() default {};

}
