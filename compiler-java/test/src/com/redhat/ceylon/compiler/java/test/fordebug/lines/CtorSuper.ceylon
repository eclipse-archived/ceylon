shared class CtorSuper {
    Integer v;
    shared Integer v2;
    
    shared new (Integer i) {
        v = i;
        v2 = v^2;
        print(v2);
        print("");
    }
    
    shared new constructor2(Integer j) {
        v = j * j;
        v2 = v^3;
        print(v2);
        print("");
    }
}
shared class CtorSuper2 extends CtorSuper {
    Integer v;
    
    shared new (Integer i) 
            extends CtorSuper(i) {
        v = i;
        print(v);
        print("");
    }
    
    shared new constructor2(Integer j) 
            extends CtorSuper.constructor2(j){
        v = j * j;
        print(v);
        print("");
    }
}