package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes({"ceylon.language.Equality",
		        "ceylon.language.Comparable<ceylon.language.Character>",
		        "ceylon.language.Ordinal<ceylon.language.Character>"})
public final class Character
        implements Comparable<Character>, Ordinal<Character> {
	
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

    public static Character instance(int c) {
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
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Equality") java.lang.Object obj) {
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

	@Override
	public Comparison compare(@Name("other") Character other) {
        long x = codePoint;
        long y = other.codePoint;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
	}

	/*@Override
	public boolean largerThan(@Name("other") Character other) {
		return codePoint>other.codePoint;
	}

	@Override
	public boolean smallerThan(@Name("other") Character other) {
		return codePoint<other.codePoint;
	}

	@Override
	public boolean asLargeAs(@Name("other") Character other) {
		return codePoint>=other.codePoint;
	}

	@Override
	public boolean asSmallAs(@Name("other") Character other) {
		return codePoint<=other.codePoint;
	}*/
    
    @Override
    public Character getPredecessor() {
    	return new Character(codePoint-1);
    }
    
    @Override
    public Character getSuccessor() {
    	return new Character(codePoint+1);
    }
    
}
