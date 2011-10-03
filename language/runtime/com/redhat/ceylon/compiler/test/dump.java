package com.redhat.ceylon.compiler.test;

import java.lang.reflect.Field;

public final class dump extends ceylon.language.Object {
    public static void run(ceylon.language.Object obj) {
        StringBuilder builder = new StringBuilder();
        formatObject(builder, obj, "");
        System.out.println(builder.toString());
    }

    private static void formatObject(StringBuilder builder,
                                     Object obj,
                                     String indent) {
        Class klass = obj.getClass();
        builder.append(klass.getName());
        builder.append(" {\n");

        String moreIndent = indent + "  ";
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

            Class type = field.getType();
            String typeName = type.getName();

            builder.append(moreIndent);
            builder.append(typeName);
            builder.append(' ');
            builder.append(name);
            builder.append(" = ");

            if (typeName.equals("char")) {
                formatChar(builder, (Character) value);
            }
            else if (typeName.equals("java.lang.String")) {
                formatString(builder, (String) value);
            }
            else if (!type.isPrimitive()) {
                formatObject(builder, value, moreIndent);
            }
            else {
                builder.append(value);
            }

            builder.append('\n');
        }
        builder.append(indent);
        builder.append("}");
    }

    private static void formatChar(StringBuilder builder, char value) {
        builder.append('\'');
        builder.append(escape(value, '\''));
        builder.append('\'');
    }

    private static void formatString(StringBuilder builder, String value) {
        builder.append('"');
        for (int i = 0; i < value.length(); i++) {
            builder.append(escape(value.charAt(i), '"'));
        }
        builder.append('"');
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
