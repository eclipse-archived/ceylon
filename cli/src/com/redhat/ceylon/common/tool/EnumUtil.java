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
                sb.append("'").append(n.replace('_', '-')).append("', ");
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
    
    /**
     * Turns a list of strings containing the names of enumeration options from the given
     * enumeration class into a set of enumerations.
     * @param enumClass The enumeration's class
     * @param elems The list of enumeration option names or <code>null</code>
     * @return An <code>EnumSet</code> containing the options found or <code>null</code>
     * if <code>elems</code> was <code>null</code>
     * @throws IllegalArgumentException if a string was found with an option name that
     * was not found in the given enumeration
     */
    public static <ENUM extends Enum<ENUM>> EnumSet<ENUM> enumsFromStrings(Class<ENUM> enumClass, List<String> elems) {
        return enumsFromStrings(enumClass, elems, false);
    }
    
    /**
     * Turns a list of strings containing the names of enumeration options from the given
     * enumeration class into a set of enumerations. String containing option names that
     * are not part of the given enumeration will be silently skipped.
     * @param enumClass The enumeration's class
     * @param elems The list of enumeration option names or <code>null</code>
     * @return An <code>EnumSet</code> containing the options found or <code>null</code>
     * if <code>elems</code> was <code>null</code>
     */
    public static <ENUM extends Enum<ENUM>> EnumSet<ENUM> enumsFromPossiblyInvalidStrings(Class<ENUM> enumClass, List<String> elems) {
        return enumsFromStrings(enumClass, elems, true);
    }
    
    private static <ENUM extends Enum<ENUM>> EnumSet<ENUM> enumsFromStrings(Class<ENUM> enumClass, List<String> elems, boolean skipErrors) {
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
                if (skipErrors) {
                    try {
                        result.add(EnumUtil.valueOf(enumClass, elem));
                    } catch (IllegalArgumentException ex) {
                        // Ignore any invalid enumeration options
                    }
                } else {
                    result.add(EnumUtil.valueOf(enumClass, elem));
                }
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
