package com.redhat.ceylon.common.tool;

import java.util.ServiceLoader;

public abstract class ArgumentParserFactory {

    private static final ServiceLoader<ArgumentParserFactory> parserFactories;
    
    static {
        parserFactories = ServiceLoader.load(ArgumentParserFactory.class, ArgumentParserFactory.class.getClassLoader());
    }
    
    public abstract ArgumentParser<?> forClass(Class<?> setterType, ToolLoader toolLoader, boolean isSimpleType);
    
    public static ArgumentParser<?> instance(Class<?> setterType, ToolLoader toolLoader, boolean isSimpleType) {
        for (ArgumentParserFactory parserFactory : parserFactories) {
            ArgumentParser<?> parser = parserFactory.forClass(setterType, toolLoader, isSimpleType);
            if (parser != null) {
                return parser;
            }
        }
        return null;
    }
    
}
