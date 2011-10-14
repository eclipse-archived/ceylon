package ceylon.language;

public final class Character extends Object
{
    public final char value;

    private Character(char c) {
        value = c;
    }
    
    public  boolean getLowercase() {
        return java.lang.Character.isLowerCase(value);
    }    
    
    public boolean getUppercase(){
        return java.lang.Character.isUpperCase(value);
    }
    
    public boolean getTitlecase(){
        return java.lang.Character.isTitleCase(value);
    }
    
    public boolean getDigit(){
        return java.lang.Character.isDigit(value);
    }
    
    public boolean getLetter(){
        return java.lang.Character.isLetter(value);
    }
    
    public boolean getWhitespace(){
        return java.lang.Character.isWhitespace(value);
    }       

    public boolean getControl(){
        return java.lang.Character.isISOControl(value);
    }       

    public static ceylon.language.Character instance(char c) {
        return new ceylon.language.Character(c);
    }

    public ceylon.language.Character getLowercased() {
        return instance(java.lang.Character.toLowerCase(value));
    }

    public ceylon.language.Character getUppercased() {
        return instance(java.lang.Character.toUpperCase(value));
    }

    public ceylon.language.Character getTitlecased() {
        return instance(java.lang.Character.toTitleCase(value));
    }

    public java.lang.String toString() {
        return java.lang.Character.toString(value);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Character) {
            Character other = (Character) obj;
            return value == other.value;
        }
        else {
            return false;
        }
    }
    
}
