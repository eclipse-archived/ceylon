@error class WithBothInitAndDefaultConst() {
    shared new withBothInitAndDefaultConst() {}
}

class WithNeitherInitNorConst() {} //TODO: should be an error

class WithConst extends Basic {
    shared new const() {}
}

@error class WithNoConst extends Basic() {}
@error class WithInit() extends Basic {}

@error class WithConstAndParams() {
    new const() {}
}

class WithDefaultConst {
    shared new () {}
}

class ExtendsWithDefaultConstBroken extends WithDefaultConst {
    @error shared new () {}
}

class ExtendsWithDefaultConstOk extends WithDefaultConst {
    shared new () extends WithDefaultConst() {}
}

class ExtendsWithConstBroken extends WithConst {
    @error shared new () {}
}

class ExtendsWithConstOk extends WithConst {
    shared new () extends const() {}
}

class WithConstAndDefaultConst {
    shared new () {}
    new const() {}
}

class WithAttributes {
    String name = "Trompon";
    Integer init;
    variable Integer count;
    print(name);
    shared new () {
        count = 0;
        init = count;
    }
    new constWithParameter(Integer initial) {
        count = initial;
        init = initial;
    }
    void inc() {
        count++;
    }
    void reset() {
        count = init;
    }
}

class WithSharedAttributes {
    shared String name = "Trompon";
    shared Integer init;
    shared variable Integer count;
    print(name);
    shared new () {
        count = 0;
        init = count;
    }
    new constWithParameter(Integer initial) {
        count = initial;
        init = initial;
    }
    shared void inc() {
        count++;
    }
    shared void reset() {
        count = init;
    }
}

class BrokenWithAttributes {
    String name;
    variable Integer count;
    Integer init;
    shared new () {
        init = 0;
    }
    new constWithParameter(Integer initial) {
        count = initial;
    }
    void inc() {
        @error count++;
    }
    void reset() {
        @error count = init;
    }
}

class BrokenWithSharedAttributes {
    @error shared String name;
    @error shared variable Integer count;
    @error shared Integer init;
    shared new () {
        init = 0;
    }
    new constWithParameter(Integer initial) {
        count = initial;
    }
    shared void inc() {
        @error count++;
    }
    shared void reset() {
        @error count = init;
    }
}

class WithAttributesAndMisplacedStatement {
    String name = "Trompon";
    Integer init;
    variable Integer count;
    shared new () {
        count = 0;
        init = count;
    }
    new constWithParameter(Integer initial) {
        count = initial;
        init = initial;
    }
    print(name);
    void inc() {
        count++;
    }
    void reset() {
        count = init;
    }
}

abstract class WithSplitInitializer {
    shared String name;
    shared Float x;
    shared new() { x=0.0; }
    shared new create() { x=1.0; }
    name = "Gavin";
    Integer count = 1;
}

class WithAttributesAndMispacedUsage {
    String name = "Trompon";
    Integer init;
    variable Integer count;
    print(name);
    @error print(init);
    shared new () {
        @error print(count);
        count = 0;
        init = count;
    }
    new constWithParameter(Integer initial) {
        @error print(init);
        count = initial;
        init = initial;
    }
    void inc() {
        count++;
    }
    void reset() {
        count = init;
    }
}

class Alias1() => WithDefaultConst();
class Alias2() => WithConst.const();
@error class BrokenAlias1() => WithConst();
@error class BrokenAlias2() => WithConst;
@error class AliasWithNoParams => WithNeitherInitNorConst();

class Super {
    shared new create() {}
}

class Broken extends Super {
    @error shared new () extends Super() {}
}

class MoreBroken extends Super {
    @error new broken() extends Basic() {}
}

class Sub1 extends Super {
    new create() extends Super.create() {}
}
class BrokenSub1 extends Super {
    @error new create() extends Super.create {}
}

class Sub2() extends Super.create() {}
@error class BrokenSub2() extends Super.create {}

class Alias() => Super.create();

class Sub3() extends Alias() {}
class Sub4 extends Alias {
    shared new () extends Alias() {}
}

class Silly extends Basic {
    shared new () extends Basic() {}
}

class Unshared() { }

shared class SharedWithConstructor {
    shared new (@error Unshared bar) { }
}

abstract class AbstractWithConstructor {
    shared new constructorForAbstract() {}
}

class InheritsAbstractWithConstructor1() 
        extends AbstractWithConstructor.constructorForAbstract() {
}

class InheritsAbstractWithConstructor2 
        extends AbstractWithConstructor {
    shared new constructor() extends constructorForAbstract() {}
}

void testRefs<T>() {
    @error value val1 = WithConst;
    @error value val2 = T;
    @error value val3 = Identifiable;
    @error value val4 = AbstractWithConstructor.constructorForAbstract;
    
    @type:"InheritsAbstractWithConstructor1" 
    value new1 = InheritsAbstractWithConstructor1();
    
    @type:"InheritsAbstractWithConstructor2" 
    value new2 = InheritsAbstractWithConstructor2.constructor();
}

shared class C<out X> {
    shared new ({X*} items) {}
    shared class B<out Y> {
        shared new (@error [X*] items) {}
        shared new other([Y*] items) {}
    }
}

shared class Supertype(Integer x) {
    shared Integer zsub = 0;
}

shared class Subtype extends Supertype {
    Integer xsub = 1;
    shared Integer ysub = 1;
    
    @error shared new subtype() extends Supertype(xsub) {}
    @error shared new create() extends Supertype(ysub) {}
    @error shared new extra() extends Supertype(ysub) {}
}

class WithDupeConstructor {
    new dupe() {}
    @error new dupe(String string) {}
}
class WithDupeDefaultConstructor {
    shared new () {}
    @error new (String string) {}
}

class WithUnsharedDefaultConstructor {
    @error new () {}
}

shared class Thing {
    String name;
    
    sealed shared new(String name) {
        this.name = name;
    }
    
    sealed shared new another(String name) {
        this.name = name;
    }
}

shared class WithPartialConstructor {
    Float length;
    String name;
    abstract new withLength(Float x, Float y) {
        length = (x^2+y^2)^0.5;
    }
    shared new withNameAndCoords(String name, Float x, Float y) 
            extends withLength(x, y) {
        this.name = name;
    }
    string = "``name``:``length``";
}

shared class WithNonPartialConstructorDelegation {
    Float length;
    String name;
    new withLengthAndName(Float x, Float y, String name) {
        length = (x^2+y^2)^0.5;
        this.name = name;
    }
    shared new withNameAndCoords(String name, Float x, Float y) 
            extends withLengthAndName(x, y, name) {}
    string = "``name``:``length``";
}

shared class WithDefaultConstructorDelegation {
    Float length;
    String name;
    shared new (Float x, Float y, String name) {
        length = (x^2+y^2)^0.5;
        this.name = name;
    }
    shared new withNameAndCoords(String name, Float x, Float y) 
            extends WithDefaultConstructorDelegation(x, y, name) {}
    string = "``name``:``length``";
}

shared class WithBrokenPartialConstructor {
    Float length;
    String name;
    abstract new withLength(Float x, Float y) {
        //length = (x^2+y^2)^0.5;
    }
    @error shared abstract new withBrokenLength(Float x, Float y) {
        length = (x^2+y^2)^0.5;
    }
    shared new withNameAndCoords(String name, Float x, Float y) 
            extends withLength(x, y) {
        this.name = name;
    }
    @error string = "``name``:``length``";
    @error print(withLength(1.0, 2.0));
}

class WithBrokenDelegation<Element> {
    
    shared new(){}
    
    @error shared new foo(Element f) 
            extends WithBrokenDelegation<Integer>(){} 
    
    shared new foo0(Element f) 
            extends WithBrokenDelegation<Element>(){} 
    
    shared new baz(){}
    
    @error shared new bar(Element f) 
            extends WithBrokenDelegation<Integer>.baz(){}
    
    shared new bar0(Element f) 
            extends WithBrokenDelegation<Element>.baz(){} 
}

class WithInnerClassExtendingPartialConstructor {
    abstract new partial() {}   
    @error shared class Inner() extends partial() {}
}

class Foobar {
    abstract new partial() {}
    new create() {}
    @error class First() => Foobar.partial();
    @error shared class Second() => Foobar.create();
}

class WithMethod {
    shared void accept(Float x);
    shared new new1() {
        accept(Float x) => print(x);
    }
    shared new new2(void accept(Float x)) {
        this.accept = accept;
    }
}

void testWithMethod() {
    WithMethod.new1()
            .accept(1.0);
    WithMethod.new2((x) => print("hello " + x.string))
            .accept(3.0);
}

@error shared new(){}
