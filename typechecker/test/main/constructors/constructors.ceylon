@error class WithBothInitAndDefaultConst() {
    new WithBothInitAndDefaultConst() {}
}

class WithNeitherInitNorConst() {} //TODO: should be an error

class WithConst extends Basic {
    new Const() {}
}

@error class WithNoConst extends Basic() {}
@error class WithInit() extends Basic {}

@error class WithConstAndParams() {
    new Const() {}
}

class WithDefaultConst {
    new WithDefaultConst() {}
}

class ExtendsWithDefaultConstBroken extends WithDefaultConst {
    @error new ExtendsWithDefaultConstBroken() {}
}

class ExtendsWithDefaultConstOk extends WithDefaultConst {
    new ExtendsWithDefaultConstOk() extends WithDefaultConst() {}
}

class WithConstAndDefaultConst {
    new WithConstAndDefaultConst() {}
    new Const() {}
}

class WithAttributes {
    String name = "Trompon";
    Integer init;
    variable Integer count;
    print(name);
    new WithAttributes() {
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
    new WithSharedAttributes() {
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
    new BrokenWithAttributes() {
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
    new BrokenWithSharedAttributes() {
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
    new WithAttributesAndMisplacedStatement() {
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
    new WithAttributesAndMispacedUsage() {
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

