shared object toplevel {
    shared void fn(String s="") {}
    shared variable Integer val = 0;
    shared interface Interface {
        shared actual String string => "Interface";
    }
    shared class Class() satisfies Interface {}
    //shared object inner {}
}