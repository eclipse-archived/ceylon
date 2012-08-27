
@nomodel
abstract class RefiningVoidMembersClass<T>(T t) {
    shared void notRefinable(){}
    
    //shared default void defaultGettervoid {}
    shared default Void defaultGetterVoid {
        return 1;
    }
    shared default T defaultGetterT {
        return t;
    }
    shared default void defaultReturnsvoidImplicit(){}
    shared default void defaultReturnsvoidExplicit(){return;}
    shared default Void defaultReturnsVoidImplicit(){
        return 1;
    }
    shared default Void defaultReturnsVoidExplicit(){
        return 1;
    }
    shared default T defaultReturnsT() {
        return t;    
    }
    
    //shared formal void formalGettervoid;
    shared formal Void formalGetterVoid;
    shared formal T formalGetterT;
    shared formal void formalReturnsvoid();
    shared formal Void formalReturnsVoid();
    shared formal T formalReturnsT();
}

@nomodel
abstract class RefiningVoidMembersClass_Boxed() extends RefiningVoidMembersClass<Void>(1) {
    /*shared actual Void defaultGettervoid {
        return 1;
    }*/
    shared actual Void defaultGetterT {
        return 1;
    }
    shared actual Void defaultReturnsvoidExplicit(){
        return 1;
    }
    shared actual Void defaultReturnsT() {
        return 1;    
    }
    
    shared actual Void formalGetterT {
        return 1;
    }
    shared actual Void formalReturnsvoid() {
        return 1;
    }
    shared actual Void formalReturnsT() {
        return 1;
    }
}

@nomodel
abstract class RefiningVoidMembersClass_Unboxed() extends RefiningVoidMembersClass<Void>(1) {
    /* shared actual void defaultGetterVoid {
    }*/
    shared actual void defaultReturnsVoidExplicit(){
        return;
    }
    shared actual void defaultReturnsVoidImplicit(){
    }
    shared actual void defaultReturnsT() {
    }
    /* shared actual void formalGetterVoid {
    };*/
    /* shared actual void formalGetterT {
    };*/
    shared actual void formalReturnsVoid() {}
    shared actual void formalReturnsT() {}
}


@nomodel
interface RefiningVoidMembersInterface<T> {
    
    shared void notRefinable(){}
    
    //shared default void defaultGettervoid {}
    shared default Void defaultGetterVoid {
        return 1;
    }
    shared default T defaultGetterT {
        return defaultReturnsT();
    }
    shared default void defaultReturnsvoidImplicit(){}
    shared default void defaultReturnsvoidExplicit(){return;}
    shared default Void defaultReturnsVoidImplicit(){
        return 1;
    }
    shared default Void defaultReturnsVoidExplicit(){
        return 1;
    }
    shared default T defaultReturnsT() {
        return defaultGetterT;
    }
    
    //shared formal void formalGettervoid;
    shared formal Void formalGetterVoid;
    shared formal T formalGetterT;
    shared formal void formalReturnsvoid();
    shared formal Void formalReturnsVoid();
    shared formal T formalReturnsT();
}

@nomodel
abstract class RefiningVoidMembersInterface_Boxed() satisfies RefiningVoidMembersInterface<Void> {
    shared actual Void defaultGetterT {
        return 1;
    }
    shared actual Void defaultReturnsvoidExplicit(){
        return 1;
    }
    shared actual Void defaultReturnsT() {
        return 1;    
    }
    
    shared actual Void formalGetterT {
        return 1;
    }
    shared actual Void formalReturnsvoid() {
        return 1;
    }
    shared actual Void formalReturnsT() {
        return 1;
    }   
}

@nomodel
abstract class RefiningVoidMembersInterface_Unboxed() satisfies RefiningVoidMembersInterface<Void> {
    /* shared actual void defaultGetterVoid {
    }*/
    shared actual void defaultReturnsVoidExplicit(){
        return;
    }
    shared actual void defaultReturnsVoidImplicit(){
    }
    shared actual void defaultReturnsT() {
    }
    /* shared actual void formalGetterVoid {
    };*/
    /* shared actual void formalGetterT {
    };*/
    shared actual void formalReturnsVoid() {}
    shared actual void formalReturnsT() {}
}