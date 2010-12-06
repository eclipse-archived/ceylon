package com.redhat.ceylon.compiler.test;

import java.lang.reflect.Field;

public final class dump extends ceylon.language.Object {
    public static void run(ceylon.language.Object obj) {
        Class klass = obj.getClass();
        System.out.println(klass.getName() + " {");
        for (Field field: klass.getDeclaredFields()) {
            String name = field.getName();
            if (name.equals("$assertionsDisabled"))
                continue;

            Object value;
            try {
                if (!field.isAccessible())
                    field.setAccessible(true);
                value = field.get(obj);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            String type = field.getType().getName();
            if (type.equals("char")) {
                value = formatChar((Character) value);
            }
            else if (type.equals("java.lang.String")) {
                value = formatString((String) value);
            }

            System.out.println("  "
                               + type
                               + " "
                               + name
                               + " = "
                               + value);
        }
        System.out.println("}");
    }

    private static String formatChar(char value) {
        return "'" + escape(value, '\'') + "'";
    }

    private static String formatString(String value) {
        StringBuilder builder = new StringBuilder();
        builder.append('"');
        for (int i = 0; i < value.length(); i++) {
            builder.append(escape(value.charAt(i), '"'));
        }
        builder.append('"');
        return builder.toString();
    }

    private static String escape(char value, char quote) {
        if (value == quote)
            return "\\" + value;

        switch (value) {
        case '\b':
            return "\\b";
        case '\t':
            return "\\t";
        case '\n':
            return "\\n";
        case '\f':
            return "\\f";
        case '\r':
            return "\\r";
        case '\\':
            return "\\\\";
        default:
            return Character.toString(value);
        }
    }
}
