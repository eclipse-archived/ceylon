class ClassWithStaticMembers {
    
    shared static void hello() {}
    shared static String name = "Trompon";
    shared static class Inner() {}
    shared static object inner {
        shared String greeting = "Hello";
    }
    shared static interface Inter {}
    shared static alias Name => String;
    
    shared new () {
        hello();
        String nameIt = name;
        String greeting = inner.greeting;
        Inner innerIt = Inner();
        object impl satisfies Inter {}
        Name nameStr = "Gavin";
    }
}

void testClassWithStaticMembers() {
    ClassWithStaticMembers.hello();
    String name = ClassWithStaticMembers.name;
    String greeting = ClassWithStaticMembers.inner.greeting;
    ClassWithStaticMembers.Inner innerIt = ClassWithStaticMembers.Inner();
    object impl satisfies ClassWithStaticMembers.Inter {}
    ClassWithStaticMembers.Name nameStr = "Gavin";
}

interface InterfaceWithStaticMembers {
    @error shared static void hello() {}
    @error shared static String name = "Trompon";
    @error shared static class Inner() {}
    @error shared static object inner {
        shared String greeting = "Hello";
    }
    @error shared static interface Inter {}
    @error shared static alias Name => String;
}

@error class ClassWithParamsAndStaticMembers() {
    shared static void hello() {}
    shared static String name = "Trompon";
    shared static class Inner() {}
    shared static object inner {
        shared String greeting = "Hello";
    }
    shared static interface Inter {}
    shared static alias Name => String;
}

class BadClassWithInitializerAndStaticMembers {
    print("hello");
    @error shared static void hello() {}
    @error shared static String name = "Trompon";
    @error shared static class Inner() {}
    @error shared static object inner {
        shared String greeting = "Hello";
    }
    @error shared static interface Inter {}
    @error shared static alias Name => String;
    shared new () {}
}

class BadClassWithInitializerAndStaticMembers2 {
    shared new make() {}
    @error shared static void hello() {}
    @error shared static String name = "Trompon";
    @error shared static class Inner() {}
    @error shared static object inner {
        shared String greeting = "Hello";
    }
    @error shared static interface Inter {}
    @error shared static alias Name => String;
    shared new () {}
}

class GoodClassWithInitializerAndStaticMembers {
    shared static void hello() {}
    shared static String name = "Trompon";
    shared static class Inner() {}
    shared static object inner {
        shared String greeting = "Hello";
    }
    shared static interface Inter {}
    shared static alias Name => String;
    print("hello");
    shared new () {}
}

class ClassWithCircularStaticMembers {
    static void foo() {
        @error bar();
        @error ClassWithCircularStaticMembers.bar();
        @error baz();
    }
    static void bar() {
        foo();
        @error baz();
    }
    shared new () {}
    void baz() {
        foo();
        bar();
    }
}

class Factory {
    shared static Factory instance = Factory.create();
    shared static Factory createFactory() => Factory.create();
    shared static Factory instance2 = create();
    shared static Factory createFactory2() => create();
    new create() {}
}

class WithStaticSetter {
    static variable value name_ = "";
    shared static String name => name_;
    assign name => name_ = name;
    shared new() {}
    void method() {
        WithStaticSetter.name = "Foo";
    }
}

class WithSplitStatic {
    @error static variable String name_;
    name_ = "";
    shared new() {}
}

class WithGenericStatic<T> {
    shared static T pipe(T t) => t;
    shared static WithGenericStatic<T> create(T t) 
            => WithGenericStatic<T>(t);
    shared static class Inner<S>(T t, S s) {
        shared [T,S] pair() => [t,s];
    }
    shared new(T t) {}
}

void testWithGenericStatic() {
    String s = WithGenericStatic.pipe("");
    Character c = WithGenericStatic.pipe(' ');
    @type:"Boolean" WithGenericStatic.pipe(true);
    WithGenericStatic<Integer> wgsi = WithGenericStatic.create(0);
    WithGenericStatic<Float> wgsf = WithGenericStatic.create(0.0);
    WithGenericStatic<Integer> wgsi_ = WithGenericStatic(0);
    WithGenericStatic<Float> wgsf_ = WithGenericStatic(0.0);
    @type:"WithGenericStatic<Integer>" WithGenericStatic.create(0);
    @type:"WithGenericStatic<Integer>" WithGenericStatic(0);
    
    @type:"WithGenericStatic<String>.Inner<Integer>" WithGenericStatic.Inner("", 0);
    @type:"[String, Integer]" WithGenericStatic.Inner("", 0).pair();
}