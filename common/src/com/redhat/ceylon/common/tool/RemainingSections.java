package com.redhat.ceylon.common.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemainingSections {
    /**
     * A markdown document containing additional help sections as required to 
     * document the tool. The most prominent heading(s) in the document are 
     * used to identify the help sections. 
     */
    String value();
}
