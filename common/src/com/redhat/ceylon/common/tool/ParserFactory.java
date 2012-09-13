package com.redhat.ceylon.common.tool;

public @interface ParserFactory {

    Class<? extends ArgumentParserFactory> value() default ArgumentParserFactory.class;
    
}
