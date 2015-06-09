import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.meta.model {
    Value
}

class Bool of true|false {
    shared actual String string;
    shared new true {
        string = "true";
    }
    shared new false {
        string = "false";
    }
}

class BoolOpen {
    shared actual String string;
    shared new true {
        string => "true";
    }
    shared new false {
        string => "false";
    }
}

Bool t = Bool.true;
BoolOpen f = BoolOpen.false;

void inferredTypes() {
    @type:"Bool" value t = Bool.true;
    @type:"Value<Bool,Nothing>" value model = `Bool.true`;
    @type:"ValueDeclaration" value valueref = `value Bool.true`;
}

void switchIt(Bool bool, BoolOpen boolOpen) {
    switch (bool)
    case (Bool.true) {}
    case (Bool.false) {}
    
    @error switch (bool)
    case (Bool.true) {}
    
    @error switch (boolOpen)
    case (BoolOpen.true) {}
    case (BoolOpen.false) {}
    
    switch (boolOpen)
    case (BoolOpen.true) {}
    else {}
}

class BrokenBool of true|false {
    @error shared actual String string;
    shared new true {
        //string = "true";
    }
    shared new false {
        string = "false";
    }
}

class IncompleteBool1 of true {
    shared actual String string;
    shared new true {
        string = "true";
    }
    @error shared new false {
        string = "false";
    }
}

class IncompleteBool2 of true|false {
    shared actual String string;
    shared new true {
        string = "true";
    }
    shared new false {
        string = "false";
    }
    @error new create() { string=""; }
}

@error class IncompleteBool3 of true|false|create {
    shared actual String string;
    shared new true {
        string = "true";
    }
    shared new false {
        string = "false";
    }
    @error new create() { string=""; }
}

class UnidentifiableBool 
        of true|false extends Object {
    shared actual String string;
    shared new true extends Object() {
        string = "true";
    }
    shared new false extends Object() {
        string = "false";
    }
    equals(Object that) => 1==1;
    hash => 1;
    @error switch(this)
    case (UnidentifiableBool.true) {}
    case (UnidentifiableBool.false) {}
}


class PrivateBool of true|false {
    shared actual String string;
    new true {
        string = "true";
    }
    new false {
        string = "false";
    }
    switch(this)
    case (PrivateBool.true) {}
    case (PrivateBool.false) {}
    @error
    switch(this)
    case (PrivateBool.true) {}
}

abstract class AbstractSingleton {
    @error new instance {}
}

class GenericSingleton<T> {
    @error new instance {}
}

class BrokenFlow {
    shared new instance {}
    print("hello");
}

@error class WithParamsAndSingleton() {
    shared new instance {}
}

class WithAbstractSingleton {
    @error shared abstract new instance {}
}

class ExtendingObjectWithSingleton extends Object {
    shared new instance extends Object() {}
    equals(Object that) => true;
    hash => 1;
}

class ExtendingObjectWithSingletonViaConstructor extends Object {
    shared Integer count;
    new create() extends Object() {
        count = 0;
    }
    shared new instance extends create() {}
    equals(Object that) => true;
    hash => 1;
}

class ExtendingObjectWithConstructorViaConstructor extends Object {
    shared Integer count;
    new create() extends Object() {
        count = 0;
    }
    shared new instance() extends create() {}
    equals(Object that) => true;
    hash => 1;
}

class ExtendingObjectWithSingletonViaPartialConstructor extends Object {
    shared Integer count;
    abstract new create() extends Object() {}
    shared new instance extends create() {
        count = 0;
    }
    equals(Object that) => true;
    hash => 1;
}

class ExtendingObjectWithBrokenSingletonViaPartialConstructor extends Object {
    @error shared Integer count;
    abstract new create() extends Object() {}
    shared new instance extends create() {}
    equals(Object that) => true;
    hash => 1;
}

class ExtendingObjectWithSingletonBad1 extends Object {
    @error shared new instance {}
    equals(Object that) => true;
    hash => 1;
}

@error class ExtendingObjectWithSingletonBad2 extends Object() {
    @error shared new instance {}
    equals(Object that) => true;
    hash => 1;
}

class OuterClass() {
    @error class InnerBool of true|false {
        shared actual String string;
        shared new true {
            string = "true";
        }
        shared new false {
            string = "false";
        }
    }
    @error switch(this)
    case (InnerBool.true) {}
    case (InnerBool.false) {}
}

class BoolWithNew1 of true|false {
    shared new true {}
    shared new false {}
    @error shared new create() {}
    BoolWithNew1 bool = true;
    switch(bool)
    case (BoolWithNew1.true) {}
    case (BoolWithNew1.false) {}
}

class BoolWithNew2 {
    shared new true {}
    shared new false {}
    shared new create() {}
    BoolWithNew2 bool = true;
    @error switch(bool)
    case (BoolWithNew2.true) {}
    case (BoolWithNew2.false) {}
}

interface InterFace {
    class Singleton {
        @error new instance {}
    }
}

class BoolWithDelegation of true|false {
    shared actual String string;
    abstract new withName(String name) {
        string => name;
    }
    shared new true extends withName("true") {}
    shared new false extends withName("false") {}
}

void switchBoolWithDelegation(BoolWithDelegation bool) {
    switch (bool)
    case (BoolWithDelegation.true) {}
    case (BoolWithDelegation.false) {}
}


class WithNestedBool() {
    @error class Bool of true|false {
        shared actual String string;
        shared new true {
            string = "true";
        }
        shared new false {
            string = "false";
        }
    }
}