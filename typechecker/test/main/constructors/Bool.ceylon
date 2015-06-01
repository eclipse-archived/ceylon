class Bool {
    shared actual String string;
    shared new true {
        string = "true";
    }
    shared new false {
        string = "false";
    }
}

Bool t = Bool.true;

void tryit(Bool bool) {
    switch(bool)
    case (Bool.true) {}
    case (Bool.false) {}
    @error switch(bool)
    case (Bool.true) {}
}

class BrokenBool {
    @error shared actual String string;
    shared new true {
        //string = "true";
    }
    shared new false {
        string = "false";
    }
}


class PrivateBool {
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
}

abstract class AbstractSingleton {
    @error new instance {}
}

class BrokenFlow {
    shared new instance {}
    print("hello");
}

@error class WithParamsAndSingleton() {
    shared new instance {}
}

class OuterClass() {
    class InnerBool {
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