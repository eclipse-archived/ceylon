@nomodel
abstract class ClassAttribute(){
    shared String publicvar;
    String uncapturedvar;
    String capturedvar;
    shared formal String formalvar;
    publicvar = "";
    uncapturedvar = "";
    capturedvar = "";
    formalvar = "";
    
    String m() {
        return capturedvar;
    }
}