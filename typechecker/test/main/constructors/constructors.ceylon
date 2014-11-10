@error class WithBothInitAndDefaultConst() {
    shared new WithBothInitAndDefaultConst() {}
}

class WithNeitherInitNorConst() {} //TODO: should be an error

class WithConst extends Basic {
    shared new Const() {}
}

@error class WithNoConst extends Basic() {}
@error class WithInit() extends Basic {}

@error class WithConstAndParams() {
    new Const() {}
}

class WithDefaultConst {
    shared new WithDefaultConst() {}
}

class ExtendsWithDefaultConstBroken extends WithDefaultConst {
    @error shared new ExtendsWithDefaultConstBroken() {}
}

class ExtendsWithDefaultConstOk extends WithDefaultConst {
    shared new ExtendsWithDefaultConstOk() extends WithDefaultConst() {}
}

class ExtendsWithConstBroken extends WithConst {
    @error shared new ExtendsWithConstBroken() {}
}

class ExtendsWithConstOk extends WithConst {
    shared new ExtendsWithConstOk() extends Const() {}
}

class WithConstAndDefaultConst {
    shared new WithConstAndDefaultConst() {}
    new Const() {}
}

class WithAttributes {
    String name = "Trompon";
    Integer init;
    variable Integer count;
    print(name);
    shared new WithAttributes() {
        count = 0;
        init = count;
    }
    new ConstWithParameter(Integer initial) {
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
    shared new WithSharedAttributes() {
        count = 0;
        init = count;
    }
    new ConstWithParameter(Integer initial) {
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
    shared new BrokenWithAttributes() {
        init = 0;
    }
    new ConstWithParameter(Integer initial) {
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
    shared new BrokenWithSharedAttributes() {
        init = 0;
    }
    new ConstWithParameter(Integer initial) {
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
    shared new WithAttributesAndMisplacedStatement() {
        count = 0;
        init = count;
    }
    new ConstWithParameter(Integer initial) {
        count = initial;
        init = initial;
    }
    @error print(name);
    void inc() {
        @error count++; //TODO: remove useless extra error
    }
    void reset() {
        @error count = init; //TODO: remove useless extra error
    }
}

class WithAttributesAndMispacedUsage {
    String name = "Trompon";
    Integer init;
    variable Integer count;
    print(name);
    @error print(init);
    shared new WithAttributesAndMispacedUsage() {
        @error print(count);
        count = 0;
        init = count;
    }
    new ConstWithParameter(Integer initial) {
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
class Alias2() => WithConst.Const();
@error class BrokenAlias1() => WithConst();
@error class BrokenAlias2() => WithConst;
@error class AliasWithNoParams => WithNeitherInitNorConst();

class Super {
    shared new New() {}
}

class Broken extends Super {
    @error shared new Broken() extends Super() {}
}

class MoreBroken extends Super {
    @error new Broken() extends Basic() {}
}

class Sub1 extends Super {
    new New() extends Super.New() {}
}
class BrokenSub1 extends Super {
    @error new New() extends Super.New {}
}

class Sub2() extends Super.New() {}
@error class BrokenSub2() extends Super.New {}

class Alias() => Super.New();

class Sub3() extends Alias() {}
class Sub4 extends Alias {
    shared new Sub4() extends Alias() {}
}

class Silly extends Basic {
    shared new Silly() extends Basic() {}
}
