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