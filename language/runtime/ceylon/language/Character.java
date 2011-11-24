package ceylon.language;

public final class Character extends Object
{
    public final int codePoint;

    Character(int codePoint) {
        this.codePoint = codePoint;
    }
    
    public  boolean getLowercase() {
        return java.lang.Character.isLowerCase(codePoint);
    }    
    
    public boolean getUppercase(){
        return java.lang.Character.isUpperCase(codePoint);
    }
    
    public boolean getTitlecase(){
        return java.lang.Character.isTitleCase(codePoint);
    }
    
    public boolean getDigit(){
        return java.lang.Character.isDigit(codePoint);
    }
    
    public boolean getLetter(){
        return java.lang.Character.isLetter(codePoint);
    }
    
    public boolean getWhitespace(){
        return java.lang.Character.isWhitespace(codePoint);
    }       

    public boolean getControl(){
        return java.lang.Character.isISOControl(codePoint);
    }       

    public static Character instance(char c) {
        return new Character(c);
    }

    public ceylon.language.Character getLowercased() {
        return new Character(java.lang.Character.toLowerCase(codePoint));
    }

    public ceylon.language.Character getUppercased() {
        return new Character(java.lang.Character.toUpperCase(codePoint));
    }

    public ceylon.language.Character getTitlecased() {
        return new Character(java.lang.Character.toTitleCase(codePoint));
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(java.lang.Character.toChars(codePoint));
    }

    public int intValue() {
        return codePoint;
    }

    @Override
    public int hashCode() {
        return codePoint;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Character) {
            Character other = (Character) obj;
            return codePoint == other.codePoint;
        }
        else {
            return false;
        }
    }
    
}
