package org.eclipse.ceylon.compiler.java.test.interop;

import org.eclipse.ceylon.common.NonNull;
//NOTE: DO NOT REMOVE, apparently we generate a Class type in this damn package
import java.lang.Class;

public class ClassLiteral {
    public class MemberClass {}
    public static class StaticMemberClass {}
    @NonNull
    public static String classString(Class<?> c) { 
        return c.toString(); 
    }
}
