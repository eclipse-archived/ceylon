abstract class Super2() {
 shared formal Natural formalAttr;
 shared formal Natural formalAttr2;
 shared default Natural defaultAttr = 2;
 shared default Natural defaultGetter {return 2;}
 // FIXME: enable when this compiles
 //shared default Natural defaultGetterSetter {return 2;} assign defaultGetterSetter {}
}

abstract class Super1() extends Super2() {
 // we implement a formal attr
 shared actual Natural formalAttr = 1;
 // we give a default impl to a formal attr
 shared actual default Natural formalAttr2 = 1;
 // we make a default attr formal
 shared actual formal Natural defaultAttr;
 shared actual formal Natural defaultGetter;
 // FIXME: enable when this compiles
 //shared variable actual formal Natural defaultGetterSetter;
}


class KlassWithAttributes() extends Super1() {
    Natural n1 = 1;
    shared Natural n2 = 2;
    variable Natural n3 := 3;
    shared variable Natural n4 := 4;
    Natural n5 {
        return 5;
    }
    shared Natural n6 {
        return 6;
    }
    Natural n7 {
        return 7;
    }
    assign n7 {
    }
    shared Natural n8 {
        return 8;
    }
    assign n8 {
    }
    shared variable String s := ""; 
    shared variable Integer i := +1; 
    shared variable Boolean b := true; 
    shared variable Float f := 1.0; 
    
    void capture() {
        value x = n1;
        value y = n3;
//        value z = n5;
    }

    // override all formal attrs
    shared actual Natural formalAttr2 = 3;
    shared actual Natural defaultAttr = 3;
    shared actual Natural defaultGetter {return 3;}
    // FIXME: enable when this compiles
    //shared actual Natural defaultGetterSetter {return 3;} assign defaultGetterSetter {}
}
