suppressWarnings("unusedDeclaration")
class SmallErrors() {
    @error:"type cannot be annotated small: Integer|Float"
    small Integer|Float a = 0;
    @error:"type cannot be annotated small: Integer?"
    small Integer? b = 0;
    
    shared small Integer smallFunction() => -2147483649;
    shared small Integer smallFunction2() {
        return -2147483649;
    }
    shared small Integer smallValue => -2147483649;
    shared small Integer smallValue2 {
        return -2147483649;
    }
    
    void specifyAndAssign() {
        small Integer c = 2147483648;
        small Integer d = -2147483649;
        
        small value toobig = 2147483648;
        small value toosmall = -2147483649;
        small Integer toobig2;
        small Integer toosmall2;
        toobig2 = 2147483648;
        toosmall2 = -2147483649;
        // AssignOp
        small variable value s = 0;
        print(s = 2147483648);
        print(s = -2147483649);
        
        small value smallFunction3 => -2147483649;
    }
}
abstract class SmallRefinement() {
    shared formal small Integer little();
    shared formal Integer big();
    
    shared formal void littleParam(small Integer a);
    shared formal void bigParam(Integer a);
}
class SmallRefiner() extends SmallRefinement() {
    shared actual Integer little() => 1;
    @error:"refining member cannot be made small when refined member is not small:'big' in 'SmallRefinement' is not small, but 'big' in 'SmallRefiner' is"
    shared actual small Integer big() => 2;
    
    shared actual void littleParam(Integer a) {}
    
    shared actual void bigParam(
        @error:"parameter 'a' of 'bigParam' cannot be annotated small: corresponding parameter 'a' of 'bigParam' is not small"
        small Integer a) {}
}