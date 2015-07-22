class Outer1129() {
  shared class Inner {
    shared String name;
    shared new foo {
      name = "foo";
    }
    shared new() {
      name = "Inner";
    }
  }
  shared void test() {
    check(Inner.foo.name=="foo", "#1129.4");
  }
}

class Simple1129 {
  shared String name;
  shared new() {
    name="default";
  }
  shared new foo {
    name="Foo!";
  }
}

class Delegating1129 {
  shared String name;
  shared new(String s) {
    name=s;
  }
  shared new foo extends Delegating1129("foo") {}
}

class ToplevelJs476 {
  shared Integer x;
  shared new(){
    x=1;
    check(x+1==2, "#484.1");
  }
  shared new bar(){
    value f2 = ToplevelJs476();
    x=2;
    check(x+1==3, "#484.2");
  }
  shared ToplevelJs476 test485_1() {
    return ToplevelJs476.bar();
  }
  shared ToplevelJs476 test485_2() {
    return bar();
  }
}

class IssueJs525<T> satisfies Identifiable {
  value sb=StringBuilder().append("a");
  shared T t;
  shared new foo(T x) {
    if (x is String) {
      sb.append("b");
    }
    t=x;
  }
  sb.append("c");
  shared new bar(T x) {
    if (x is Integer) {
      sb.append("d");
    }
    t=x;
  }
  sb.append("e");
  string = sb.string;
}

StringBuilder constructorSB = StringBuilder();
class TestJs528 {
    constructorSB.append("1");
    shared new c1() {
        constructorSB.append(", 1.5");
    }
    constructorSB.append(", 2");
    shared new c2() extends c1() {
        constructorSB.append(", 2.5");
    }
    constructorSB.append(", 3");
    shared new c3() extends c1() {
        constructorSB.append(", 3.5");
    }
    constructorSB.append(", 4");
}

class TestJs528Default {
    constructorSB.append("1");
    shared new () {
        constructorSB.append(", 1.5");
    }
    constructorSB.append(", 2");
    shared new c2() extends TestJs528Default() {
        constructorSB.append(", 2.5");
    }
    constructorSB.append(", 3");
    shared new c3() extends TestJs528Default() {
        constructorSB.append(", 3.5");
    }
    constructorSB.append(", 4");
}

class TestJs538 {
  Integer x;
  shared new foo(Integer a, Integer b) {
    x=a+b;
    check(a==1 && b==2, "#538.1");
  }
  hash => x;
}

class TestJs540 {
  shared new bar(Integer a=1) {
    check(a==1, "#540");
  }
  shared new() extends bar() {}
}

class TestJs541 {
  abstract new bar(){
    check(true,"#541");
  }
  shared new() extends bar() {}
}

class TestJs555<F> {
    shared F f;
    shared new bar(F f){
        this.f = f;
        check(`F`==`Integer`, "#555.2");
    }

    shared new (F f) extends bar(f){
        check(`F`==`Integer`, "#555.3");
    }
}

class ParentConstr() {}
class ParentConstr2<T>() {}

class TestJs557<F> extends ParentConstr {
    constructorSB.append("1");
    shared F f;
    constructorSB.append(",2");
    shared new bar(F f) extends ParentConstr() {
        this.f = f;
        check(`F`==`Integer`, "#557.3");
    }
    constructorSB.append(",3");

    shared new (F f) extends bar(f){
    }
    constructorSB.append(",4");
}

class TestJs565 {
  value sb=StringBuilder();
  sb.append("a");
  abstract new baz(){
    sb.append("b");
  } 
  shared new() extends baz() {
    sb.append("c");
  }
  sb.append("d");
  shared actual String string=>sb.string;
}

class TestJs566(){
    shared class Bar {
        shared new baz(){}
    }
}

class TestJs573 {
    value sb = StringBuilder();
    sb.append("1");
    shared new (){
        sb.append("2");
    }
    shared new bar() extends TestJs573(){
        sb.append("3");
    }
    shared new baz() extends bar(){
        sb.append("4");
    }
    string => sb.string;
}

class OtherJs573 {
    value sb = StringBuilder().append("1");
    shared new (){
        sb.append("a");
    }
    sb.append("2");
    shared new bar() extends OtherJs573(){
        sb.append("b");
    }
    sb.append("3");
    shared new baz() extends bar(){
        sb.append("c");
    }
    sb.append("4");
    string => sb.string;
}

class TestJs582<A>{
  shared A a;
  shared new bar(A i){
    check(`A`==`Integer`, "#582");
    a=i;
  }
}

class TestJs584 {
    shared new foo(){
      check(true,"#584");
    }
}
class BoomJs584() => TestJs584.foo();
class CheckJs584() extends BoomJs584(){}

class TestJs585_1(){    
    shared class Bar {      
        shared new (){
            Object b = outer;
            check(b is TestJs585_1, "#585.1");
        }               
    }   
}

class TestJs585_2(){    
    shared class Bar {      
        shared new baz(){
            Object b = outer;
            check(b is TestJs585_2, "#585.2");
        }               
    }   
}

class Js594 {
    shared new foo{}
}

class Parent595() {
    constructorSB.append("a");
}

class Test595 extends Parent595 {
    constructorSB.append("b");
    shared new bar extends Parent595() {
        constructorSB.append("c");
    }
    constructorSB.append("d");
}

class Test597 {
  constructorSB.append("a");
  shared new foo {
    constructorSB.append("b");
  }
  constructorSB.append("c");
}

class Test599 {
    shared Integer i();
    shared Integer j();
    shared Integer k;
    shared new (){
        Integer l;
        l=2;
        check(l==2, "#599.1");
        i() => 3;
        j = () {
          return 5;
        };
        k=8;
    }
}
class Test599Value {
    shared Integer i();
    shared Integer j();
    shared Integer k;
    shared new foo {
        Integer l;
        l=2;
        check(l==2, "#599.5");
        i() => 3;
        j = () {
          return 5;
        };
        k=8;
    }
}

class Test596(shared String s){
    shared class Bar{
        shared new bar{
            check(s=="596", "JS #596");
        }
    }
}

class MultiValueCons satisfies Identifiable {
  shared Integer num;
  shared new one {
    num=1;
  }
  shared new two {
    num=2;
  }
}

class Js603 extends ParentConstr {
    shared new foo extends ParentConstr(){}
    shared Integer i() => 42;
}
class Js603Params extends ParentConstr2<String> {
    shared new foo extends ParentConstr2<String>(){}
    shared Integer i() => 42;
}

@test
shared void testConstructors() {
  value o=Outer1129();
  check(o.Inner.foo.name=="foo", "spec#1129.1");
  check(Outer1129().Inner.foo.name=="foo", "spec#1129.2");
  value oi=o.Inner();
  check(oi.foo.name=="foo", "spec#1129.3");
  o.test();
  value ref=Outer1129.Inner.foo;
  check(ref(o).name=="foo", "spec#1129.5");
  check(Simple1129.foo.name=="Foo!", "spec#1129.6");
  check(Simple1129().foo.name=="Foo!", "spec#1129.7");
  check(Delegating1129.foo.name=="foo", "spec#1129.8");

  check(ToplevelJs476.bar().x==2, "#476 toplevel");
  class NestedJs476 {
    shared Integer x;
    shared new(){
      x=1;
    }
    shared new bar(){
      value f2 = NestedJs476();
      x=2;
    }
  }
  check(NestedJs476.bar().x==2, "#476 nested");
  check(ToplevelJs476().test485_1().x==2, "#485.1");
  check(ToplevelJs476().test485_2().x==2, "#485.2");

  value f1=IssueJs525.foo("x");
  value f2=IssueJs525.bar(1);
  check(f1.t=="x", "#525.1");
  check(f2.t==1, "#525.2");
  check(f1.string=="abce", "#525.3");
  check(f2.string=="acde", "#525.4");
  check((f1 of Object) is Identifiable, "#525.5");
  check((f2 of Object) is Identifiable, "#525.6");
  check((f1 of Object) is IssueJs525<String>, "#525.7");
  check((f2 of Object) is IssueJs525<Integer>, "#525.8");

  TestJs528.c1();
  check(constructorSB.string=="1, 1.5, 2, 3, 4", "#528.1");
  constructorSB.clear();
  TestJs528.c2();
  check(constructorSB.string=="1, 1.5, 2, 2.5, 3, 4", "#528.2");
  constructorSB.clear();
  TestJs528.c3();
  check(constructorSB.string=="1, 1.5, 2, 3, 3.5, 4", "#528.3");
  constructorSB.clear();
  TestJs528Default();
  check(constructorSB.string=="1, 1.5, 2, 3, 4", "#536.1");
  constructorSB.clear();
  TestJs528Default.c2();
  check(constructorSB.string=="1, 1.5, 2, 2.5, 3, 4", "#536.2");
  constructorSB.clear();
  TestJs528Default.c3();
  check(constructorSB.string=="1, 1.5, 2, 3, 3.5, 4", "#536.3");
  constructorSB.clear();
  check(curry(TestJs538.foo)(1)(2).hash == 3, "#538.2");
  check(TestJs555(42).f==42, "#555.1");
  constructorSB.clear();
  TestJs557(1);
  check(constructorSB.string=="1,2,3,4", "#557.1 expected 1,2,3,4 got ``constructorSB``");
  constructorSB.clear();
  TestJs557.bar(1);
  check(constructorSB.string=="1,2,3,4", "#557.2 expected 1,2,3,4 got ``constructorSB``");
  constructorSB.clear();
  Anything test566 = TestJs566().Bar.baz();
  check(test566 exists, "#566");
  class TestJs564{ 
    shared new bar(){
      check(true, "#564");
    }
  }
  (TestJs564.bar)();
  check(TestJs565().string=="abcd", "#565");
  TestJs540();
  TestJs541();
  value test582 = object extends TestJs582<Integer>.bar(42){};
  check(test582.a==42);
  CheckJs584();
  check(TestJs573().string=="12", "#573.1 expected cd got ``TestJs573()``");
  check(TestJs573.bar().string=="123", "#573.2 expected cde got ``TestJs573.bar()``");
  check(TestJs573.baz().string=="1234", "#573.3 expected cdef got ``TestJs573.baz()``");
  check(OtherJs573().string=="1a234", "#573.4 expected cd got ``OtherJs573()``");
  check(OtherJs573.bar().string=="1a2b34", "#573.5 expected cde got ``OtherJs573.bar()``");
  check(OtherJs573.baz().string=="1a2b3c4", "#573.6 expected cdef got ``OtherJs573.baz()``");
  TestJs585_1().Bar();
  TestJs585_2().Bar.baz();
  Anything test594 = Js594.foo;
  check(test594 exists, "JS #594");
  constructorSB.clear();
  variable Object unused=Test595.bar;
  check(constructorSB.string=="abcd", "JS #595");
  constructorSB.clear();
  unused=Test597.foo;
  check(constructorSB.string=="abc", "JS #597 expected abc got ``constructorSB``");
  value t599=Test599();
  check(t599.i() == 3, "JS #599.2");
  check(t599.j() == 5, "JS #599.3");
  check(t599.k == 8, "JS #599.4");
  check(Test599Value.foo.i() == 3, "JS#599.6");
  check(Test599Value.foo.j() == 5, "JS#599.7");
  check(Test599Value.foo.k == 8, "JS#599.8");
  check(Test596("596").Bar.bar!=t599, "JS #596.2");
  value t596=Test596("596");
  check(t596.Bar.bar!=t599, "JS#596.3");
  check(t596.Bar.bar===t596.Bar.bar, "JS#596.4");
  check(!(t596.Bar.bar===Test596("596").Bar.bar), "JS#596.5");
  check(MultiValueCons.one.num==1, "Multiple enumerated 1");
  check(MultiValueCons.two.num==2, "Multiple enumerated 2");
  check(!((MultiValueCons.one of Identifiable)===(MultiValueCons.two of Identifiable)), "Multiple enumerated 3");
  check(Js603.foo.i() == 42, "JS#603 1");
  check(Js603Params.foo.i() == 42, "JS#603 2");
  check((Js603Params.foo of Object) is ParentConstr2<String>, "JS#603 3");
}
