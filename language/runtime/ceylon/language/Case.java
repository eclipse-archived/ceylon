package ceylon.language;

public abstract class Case extends IdentifiableObject {
    private final java.lang.String string;
    
    public Case(java.lang.String caseName){
        this.string = caseName;
    }
    
    public java.lang.String toString(){
        return string;
    }
}
