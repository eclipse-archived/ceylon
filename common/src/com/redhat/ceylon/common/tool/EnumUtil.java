package com.redhat.ceylon.common.tool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class EnumUtil {

    public static <ENUM extends Enum<ENUM>> ENUM valueOf(Class<ENUM> enumClass, String name) {
        try {
            return Enum.valueOf(enumClass, name);
        } catch (IllegalArgumentException e) {
            StringBuffer sb = new StringBuffer();
            for (String n : possibilities(enumClass)) {
                sb.append("'").append(n).append("', ");
            }
            sb.setLength(sb.length() - 2);
            throw new IllegalArgumentException(ToolMessages.msg(
                    "argument.enum.invalid.option", 
                    enumClass.getSimpleName(), sb.toString()));
        }
    }
    
    public static <ENUM extends Enum<ENUM>> Iterable<String> possibilities(Class<ENUM> enumClass) {
        ENUM[] values;
        try {
            Method method = enumClass.getMethod("values");
            method.setAccessible(true);
            values = (ENUM[]) method.invoke(null);
        } catch (ReflectiveOperationException e) {
            // Should never happen
            throw new RuntimeException(e);
        }
        List<String> result = new ArrayList<>(values.length);
        for (ENUM value : values) {
            result.add(value.toString());
        }
        return result;
    }
}
