@nomodel
class ClassVariable(){
    shared variable String publicvar;
    variable String uncapturedvar;
    variable String capturedvar;
    publicvar := "";
    uncapturedvar := "";
    capturedvar := "";
    
    void m() {
        capturedvar := "new value";
    }
}