@noanno
class SingletonCtors {
    shared actual String string;
    shared new one {
        string="one";
    }
    shared new two {
        string="two";
    }
    
    shared Integer use(SingletonCtors inst) {
        switch(inst)
        case(SingletonCtors.one) {
            return 1;
        }
        case(SingletonCtors.two) {
            return 2;
        }
        else{
            return 3;
        }
    }
}