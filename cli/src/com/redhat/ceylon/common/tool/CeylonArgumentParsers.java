package com.redhat.ceylon.common.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CeylonArgumentParsers extends ArgumentParserFactory {
    private static Class<?> ceylonString;
    private static Class<?> ceylonInteger;
    private static Class<?> ceylonBoolean;
    private static Method stringInstance;
    private static Method integerInstance;
    private static Method booleanInstance;
    
    static {
        try {
            ceylonString = Class.forName("ceylon.language.String");
            stringInstance = ceylonString.getDeclaredMethod("instance", String.class);
        } catch (Exception e) {
            ceylonString = null;
            stringInstance = null;
        }
        try {
            ceylonInteger = Class.forName("ceylon.language.Integer");
            integerInstance = ceylonInteger.getDeclaredMethod("instance", long.class);
        } catch (Exception e) {
            ceylonInteger = null;
        }
        try {
            ceylonBoolean = Class.forName("ceylon.language.Boolean");
            booleanInstance = ceylonBoolean.getDeclaredMethod("instance", boolean.class);
        } catch (Exception e) {
            ceylonBoolean = null;
        }
    }
    
    public static final ArgumentParser<Object> STRING_PARSER = new ArgumentParser<Object>() {
        @Override
        public Object parse(String argument, Tool tool) {
            try {
                return stringInstance.invoke(null, argument);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    };
    
    public static final ArgumentParser<Object> BOOLEAN_PARSER = new ArgumentParser<Object>() {
        @Override
        public Object parse(String argument, Tool tool) {
            try {
                return booleanInstance.invoke(null, argument.matches("1|yes|true"));
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    };
    
    public static final ArgumentParser<Object> INTEGER_PARSER = new ArgumentParser<Object>() {
        @Override
        public Object parse(String argument, Tool tool) {
            try {
                return integerInstance.invoke(null, Long.valueOf(argument));
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    };
    
    public ArgumentParser<?> forClass(Class<?> setterType, ToolLoader toolLoader, boolean isSimpleType) {
        if (ceylonString != null && ceylonString.isAssignableFrom(setterType)) {
            return STRING_PARSER;
        } else if (ceylonInteger != null && ceylonInteger.isAssignableFrom(setterType)) {
            return INTEGER_PARSER;
        } else if (ceylonBoolean != null && ceylonBoolean.isAssignableFrom(setterType)) {
            return BOOLEAN_PARSER;
        }
        return null;
    }
    
}
