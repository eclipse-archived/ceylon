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
        string = "true";
    }
    shared new false {
        string = "false";
    }
}

Bool t = Bool.true;
BoolOpen f = BoolOpen.false;

void tryit(Bool bool, BoolOpen boolOpen) {
    switch(bool)
    case (Bool.true) {}
    case (Bool.false) {}
    
    @error switch(bool)
    case (Bool.true) {}
    
    @error switch(boolOpen)
    case (BoolOpen.true) {}
    case (BoolOpen.false) {}
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

@error class BoolWithNew1 of true|false {
    shared new true {}
    shared new false {}
    shared new New() {}
    @error switch(bool)
    case (BoolWithNew.true) {}
    case (BoolWithNew.false) {}
}

class BoolWithNew2 {
    shared new true {}
    shared new false {}
    shared new New() {}
    @error switch(bool)
    case (BoolWithNew.true) {}
    case (BoolWithNew.false) {}
}

interface InterFace {
    class Singleton {
        @error new instance {}
    }
}
