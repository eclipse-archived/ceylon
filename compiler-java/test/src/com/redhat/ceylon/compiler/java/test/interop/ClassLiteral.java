package com.redhat.ceylon.compiler.java.test.interop;

import com.redhat.ceylon.common.NonNull;

public class ClassLiteral {
    class MemberClass {}
    static class StaticMemberClass {}
    @NonNull
    static String classString(Class<?> c) { 
        return c.toString(); 
    }
}
