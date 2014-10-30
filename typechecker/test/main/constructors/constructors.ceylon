class WithBothInitAndDefaultConst() {
    @error new WithInitAndDefaultConst() {}
}

class WithNeitherInitNorConst {} //TODO: should be an error

class WithConst {
    new Const() {}
}

class WithDefaultConst {
    new WithDefaultConst() {}
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
    new WithAttributes() {
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
    new BrokenWithAttributes() {
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
    new WithAttributes() {
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

