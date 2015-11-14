package com.redhat.ceylon.common.tool;

import java.util.ArrayList;
import java.util.EnumSet;
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
        EnumSet<ENUM> values = EnumSet.allOf(enumClass);
        List<String> result = new ArrayList<>(values.size());
        for (ENUM value : values) {
            result.add(value.toString());
        }
        return result;
    }
    
    public static <ENUM extends Enum<ENUM>> EnumSet<ENUM> enumsFromStrings(Class<ENUM> enumClass, List<String> elems) {
        if (elems != null) {
            EnumSet<ENUM> result = EnumSet.noneOf(enumClass);
            for (String elem : elems) {
                elem = elem.trim();
                elem = elem.replace('-', '_');
                for (ENUM e : EnumSet.allOf(enumClass)) {
                    if (e.name().equalsIgnoreCase(elem)) {
                        elem = e.name();
                    }
                }
                result.add(EnumUtil.valueOf(enumClass, elem));
            }
            return result;
        } else {
            return null;
        }
    }
    
    public static <ENUM extends Enum<ENUM>> String enumsToString(EnumSet<ENUM> elems) {
        if (elems != null) {
            StringBuilder buf = new StringBuilder();
            for (ENUM e : elems) {
                if (buf.length() > 0) {
                    buf.append(",");
                }
                buf.append(e.name());
            }
            return buf.toString();
        } else {
            return null;
        }
    }
}
