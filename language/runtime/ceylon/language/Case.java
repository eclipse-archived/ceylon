package ceylon.language;

public abstract class Case extends IdentifiableObject {
    private final String string;
    
    public Case(String caseName){
        this.string = caseName;
    }
    
    public String getString(){
        return string;
    }
}
