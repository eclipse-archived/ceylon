class Conditions(String* elems) {

    void x(Anything o) {
        if (is Object o) {
            value n = 1;
            if (o.string.size==n) {}
        }
    }
    
    void y(Object that) {
        if (is Conditions that) {
            value s = elems.count((String e) => true);
            if (1==s) {}
        }
    }
    
    void z(Anything o) {
        if (is Object o) {
            class Inner1() {
                Object x = o;
                assert (is String x);
                shared String s = x;
            }
            print(Inner1().s);
            
        }
        assert (is Object o);
        class Inner() {
            Object x = o;
            assert (is String x);
            shared String s = x;
        }
        print(Inner().s);
    }

}

void bogusExistsNonempty(String? name, String[] names) {
    if (exists @error String? name1 = name) {}
    if (nonempty @error String[] names1 = names) {}
}

class Foo() {
    shared void foo() {}
}

shared void narrowFoo(Object x) {
    if (!is Foo x) {
        @type:"Object" value xx = x;
    } else {
        @type:"Foo" value xx = x;
        x.foo();
    }
}

void narrowingInElse(String? stringOrNull, String[] seq) {
    
    switch (stringOrNull)
    case (is Null) {
        @type:"Null" value n = stringOrNull;
    }
    else { 
        stringOrNull.join { "foo", "bar" };
        @type:"String" value s = stringOrNull;
    }
    
    switch (stringOrNull)
    case (is String) {
        stringOrNull.join { "foo", "bar" };
        @type:"String" value s = stringOrNull;
    }
    else { 
        @type:"Null" value n = stringOrNull;
    }
    
    switch (stringOrNull)
    case (is String) {
        stringOrNull.join { "foo", "bar" };
        @type:"String" value s = stringOrNull;
    }
    case (is Null) { 
        @type:"Null" value n = stringOrNull;
    }
    else {
        @type:"Nothing" value n = stringOrNull;
    }
    
    if (is Null stringOrNull) {
        @type:"Null" value n = stringOrNull;
    }
    else {
        stringOrNull.join { "foo", "bar" };
        @type:"String" value s = stringOrNull;
    }
    
    if (exists stringOrNull) {
        @type:"String" value s = stringOrNull;
    }
    else {
        @type:"Null" value n = stringOrNull;
    }
    
    if (!exists stringOrNull) {
        @type:"Null" value n = stringOrNull;
    }
    else {
        @type:"String" value s = stringOrNull;
    }
    
    if (nonempty seq) {
        @type:"Sequence<String>" value s = seq;
    }
    else {
        @type:"Empty" value e = seq;
    }
    
    if (!nonempty seq) {
        @type:"Empty" value e = seq;
    }
    else {
        @type:"Sequence<String>" value s = seq;
    }
    
    Boolean|Integer|String a = "1";
    if (is String a) {
        a.compare("");
    }
    else if (is Integer a){
        a.largerThan(nothing);
    }
    else {
        assert (a);
        @type:"Boolean" value b = a;
    }

}

void negatedConditions(Integer? maybe, Integer[] maybeEmpty) {
    if (!exists Null m = maybe) {
        @type:"Null" value n = m;
    }
    if (!exists @error Anything m = maybe) {}

    if (!nonempty [] m = maybeEmpty) {
        @type:"Empty" value n = m;
    }
    if (!nonempty @error Integer[] m = maybeEmpty) {}

    if (exists Object m = maybe) {
        @type:"Object" value n = m;
    }
    if (exists @error Anything m = maybe) {}
    
    if (nonempty [Anything+] m = maybeEmpty) {
        @type:"Sequence<Anything>" value n = m;
    }
    if (nonempty @error Integer[] m = maybeEmpty) {}
}
