@nomodel
abstract class ClassVariable(){
    shared variable String publicvar;
    variable String uncapturedvar;
    variable String capturedvar;
    shared formal variable String formalvar;
    publicvar := "";
    uncapturedvar := "";
    capturedvar := "";
    formalvar := "";
    
    void m() {
        capturedvar := "new value";
    }
}