package com.redhat.ceylon.compiler.java.test.issues.bug71xx;


class Bug7125Java {
    public static class A {}
    public static class B extends A {}

    public static class C {}
    public static class D {}

    public static class Base {
        public C foo(A a) {
            System.out.println("A version called");
            return new C();
        }
        public D foo(B b) {
            System.out.println("B version called");
            return new D();
        }
    }

    public static class Sub extends Base {
        public C foo(A a) {
            System.out.println("A version called");
            return new C();
        }
    }
}
