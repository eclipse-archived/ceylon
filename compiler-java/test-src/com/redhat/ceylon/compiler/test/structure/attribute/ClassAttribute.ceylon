@nomodel
class ClassAttribute(){
    shared String publicvar;
    String uncapturedvar;
    String capturedvar;
    publicvar = "";
    uncapturedvar = "";
    capturedvar = "";
    
    String m() {
        return capturedvar;
    }
}