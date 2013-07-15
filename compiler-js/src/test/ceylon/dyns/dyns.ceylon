import check { ... }

class Carrier() {}

//Test the dynamic annotation
shared void test() {
    variable Singleton<Object> testSingleton = Singleton(1);
    String zzz;
    value carrier = Carrier();
    dynamic {
        variable value n = value { x=3; y="hello"; };
        n.a={1};
        check(n.x==3, "n.x==");
        check(n.x!=9, "n.x!=");
        check(n.x>1, "n.x>");
        check(n.x<100, "n.x<");
        check(n.x>=3, "n.x>=");
        check(n.x<=3, "n.x<=");
        check(!n.z exists, "n.z exists");
        check(n.a is {Integer*}, "n.a is {Integer*}");
        value n2 = value {3, "hello"};
        check(n2.length==2, "n2.length");
        check(n2[0]==3, "n2[0]");
        value n3 = value{a=1; b='b'; "a","b","c"};
        check(n3.a==1, "n3.a");
        check(n3[0]=="a", "n3[0]");
        check(n3["b"]=='b', "n3[b]");
        check(n3[0]>"0", "n3[0]>");
        check(n3[0]<"z", "n3[0]<");
        check(n3[0]>="a", "n3[0]>=");
        check(n3[0]<="a", "n3[0]<=");
        check(n3[0]!="A", "n3[0]!=");
        check(n3.length==3, "n3.length");
        check("b" in n3, "n3.contains");
        for (uu in n3) {
            print("iterating, ``uu``");
        }
        check(n3 is Iterable<Anything>, "array is Iterable<Anything>");
        function f(value z) => z else n;
        check(f(1)==1, "f(1)");
        check(f(null)==n, "f(null)");
        check(f{z="z";}=="z", "f(z)");
        if (n is Singleton<Object>) {
            fail("what? n is NOT a Singleton!");
        }
        check(!n is Category, "n is a Category?");
        try {
            testSingleton = n;
            fail("dynamic should not be assignable to typed");
        } catch (Exception e) {
            check(true);
            try {
                value poop = Singleton<Anything>(n);
                fail("dynamic should not be passed to typed methods");
            } catch (Exception e2) {
                check(true);
            }
        }
        try {
            Singleton<Object> ts2 = n;
            fail("Typed declaration with dynamic value");
        } catch (Exception e) {
            check(true);
        }
        try {
            zzz = n3[0];
            check(zzz=="a", "typed is assignable to typed");
        } catch (Exception e) {
            fail("typed should be assignable to typed");
        }
        try {
            print(n);
            fail("dynamic should not be passed to typed methods");
        } catch (Exception e) {
            check(true);
        }
        /*these now fail at compile time
        try {
            [Anything,Anything] tuple = [n, n2];
            print(tuple[0]);
            fail("cannot create tuples with dynamic types");
        } catch (Exception e) {
            check(true);
        }
        try {
            {Anything*} iter = {n, n2};
            print(n.first);
            fail("cannot create sequence enumerations with dynamic types");
        } catch (Exception e) {
            check(true);
        }*/
        try {
            value iter = coalesce<Anything>{n,null,n2,null,n3};
            print(iter.first);
            fail("cannot create sequenced args with dynamic types");
        } catch (Exception e) {
            check(true);
        }
        n = value{5};
        check(n.length==1, "reassign n");
        n = Singleton(1);
        check(n.size == 1, "Ceylon attribute in dynamic type");
        try {
            somethingThatDoesntExist.doSomething();
            fail("A sensible exception should have been thrown");
        } catch (Exception e) {
            check("somethingThatDoesntExist" in e.message,
                "Undefined/unknown dynamic exception message should mention somethingThatDoesntExist");
        }
        value buf = Buffer(1);
        check(buf.length==1, "Buffer length");
        try {
          value buf2 = MadeUpType(5);
          check(buf2.length==666, "TuMama length");
          fail("Can't instantiate this");
        } catch (Exception e) {
            check("MadeUpType" in e.message, "exception message should mention MadeUpType");
        }
        check(\iMath.random()>=0, "dynamic >=");
        check(\iMath.random()<=1, "dynamic <=");
        check(\iMath.random()>-1, "dynamic >");
        check(\iMath.random()<1.1, "dynamic <");

        check(0<\iMath.random()+1<2,  "dynamic a< b< c");
        check(0<=\iMath.random()<=1,  "dynamic a<=b<=c");
        check(0<\iMath.random()+1<=2, "dynamic a< b<=c");
        check(0<=\iMath.random()<1.1, "dynamic a<=b< c");
        n = carrier;
        n.test=value {a=3; b="hello";};
    }
    print("carrier object ``carrier``");
    dynamic {
        value n = value {x=carrier;};
        check(n.x.test.a == 3, "carrier 1");
        check(n.x.test.b == "hello", "carrier 2");
        try {
            switch(n)
            case (is String) { fail("n is not a string"); }
            fail("Incomplete dynamic switch should fail at runtime");
        } catch (Exception ex) {
            check(true, "OK incomplete switch fails at runtime");
        }
    }
    results();
}

void nogo() {
    dynamic {
        value d = require("util");
        d.name = "Gavin";
        Integer res = d.add(1, 3);
        value first = d.children[0];
        if (exists first) {
            Integer o = first;
        }
        d.children.set(1, 4);
        String name = d.name;
        value x = d + d + 10;
        d.count++;
        d.count--;
        Boolean b = d.adult && d.male;
        value range = d.start..d.end;
        Boolean lt = d.age<10 || d.age>5 || d.age<=100 || d.age>=500;
        if (d.something == 5 || d.other != 10) {}
        d.name = null;
    }
}
