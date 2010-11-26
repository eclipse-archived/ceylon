package com.redhat.ceylon.compiler.test;

import java.lang.reflect.Field;

public final class dump extends ceylon.language.Object {
    public static void run(ceylon.language.Object obj) {
        Class klass = obj.getClass();
        System.out.println(klass.getName() + " {");
        for (Field field: klass.getDeclaredFields()) {
            Object value;
            try {
                if (!field.isAccessible())
                    field.setAccessible(true);
                value = field.get(obj);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("  "
                               + field.getType().getName()
                               + " "
                               + field.getName()
                               + " = "
                               + value);
        }
        System.out.println("}");
    }
}
