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
    
    public boolean getDigit(){
        return java.lang.Character.isDigit(value);
    }
    
    public boolean getLetter(){
    	return java.lang.Character.isLetter(value);
    }
    
    public boolean getWhitespace(){
    	return java.lang.Character.isSpaceChar(value);
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		if (value != other.value)
			return false;
		return true;
	}
    
}
