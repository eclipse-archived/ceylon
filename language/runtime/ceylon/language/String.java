package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;


public final class String extends Object 
    implements Comparable<String>, Ordered<Character>, 
               Correspondence<Natural,Character>, Format,
               Sized, Summable<String>, Castable<String>,
               Category {

    public final java.lang.String value;

    private String(java.lang.String s) {
        value = s;
    }

    public java.lang.String toString() {
        return value;
    }

    public static ceylon.language.String instance(java.lang.String s) {
        return new ceylon.language.String(s);
    }

    public java.lang.String getUppercased() {
        return value.toUpperCase();
    }

    public java.lang.String getLowercased() {
        return value.toLowerCase();
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        if (that instanceof String) {
            String s = (String)that;
            return value.equals(s.value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static ceylon.language.String instance(java.lang.String... strings) {
        StringBuffer buf = new StringBuffer();
        for (java.lang.String s: strings)
            buf.append(s);
        return new ceylon.language.String(buf.toString());
    }

    public static ceylon.language.String instance(String... strings) {
        StringBuffer buf = new StringBuffer();
        for (String s: strings)
            buf.append(s.value);
        return new ceylon.language.String(buf.toString());
    }

    @Override
    public Comparison compare(@Name("other") String other) {
        int c = value.compareTo(other.value);
        return (c < 0) ? Comparison.SMALLER :
            ((c == 0) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Override
    public java.lang.String getFormatted() {
        return value;
    }

    @Override
    public boolean largerThan(@Name("other") String other) {
        return value.compareTo(other.value) > 0;
    }

    @Override
    public boolean smallerThan(@Name("other") String other) {
        return value.compareTo(other.value) < 0;
    }

    @Override
    public boolean asLargeAs(@Name("other") String other) {
        return value.compareTo(other.value) >= 0;
    }

    @Override
    public boolean asSmallAs(@Name("other") String other) {
        return value.compareTo(other.value) <= 0;
    }

    @Override
    public <CastValue extends String> CastValue castTo() {
        return (CastValue) this;
    }

    @Override
    public String plus(@Name("other") String string) {
        return instance(value + string.value);
    }

    @Override
    @TypeInfo("ceylon.language.Natural")
    public long getSize() {
        //TODO: should we cache this value in an instvar?
        return value.codePointCount(0, value.length());
    }

    @Override
    public boolean getEmpty() {
        return value.isEmpty();
    }

    @Override
    public Character item(@Name("index") Natural key) {
        int index = key.intValue();
        int length = value.length();
        if (index < 0 || index >= length)
            return null;
        for (int offset = 0, i = 0; offset <= length; i++) {
            int codePoint = value.codePointAt(offset);
            if (i==index)
                return new Character(codePoint);
            offset += java.lang.Character.charCount(codePoint);
        }
        return null;
    }

    @Override
    public boolean defines(@Name("key") Natural key) {
        int index = key.intValue();
        return index >= 0 && index < getSize();
    }

    @Override
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(@Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Natural>")
    Iterable<? extends Natural> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(@Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Natural>")
    Iterable<? extends Natural> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Character>")
    public Iterable<? extends Character> items(@Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Natural>")
    Iterable<? extends Natural> keys) {
        return Correspondence$impl.items(this, keys);
    }

    class StringIterator implements Iterator<Character> {

        private final int offset;
        private final int codePoint;

        StringIterator(int offset){
            this.offset = offset;
            codePoint = value.codePointAt(offset);
        }

        @Override
        public Character getHead() {
            return new Character(codePoint);
        }

        @Override
        public Iterator<Character> getTail() {
        	int nextOffset = offset + java.lang.Character.charCount(codePoint);
            return nextOffset == value.length() ? 
                    null : new StringIterator(nextOffset);
        }

    }

    @Override
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterator<ceylon.language.Character>")
    public Iterator<Character> getIterator() {
        return value.isEmpty() ? null : new StringIterator(0);
    }

    @Override
    public Character getFirst() {
        return Ordered$impl.getFirst(this);
    }

    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Character>")
    public Iterable<? extends Character> getCharacters() {
        int length = value.length();
        if (length==0) return $empty.getEmpty();
        Character[] chars = new Character[(int)getSize()];
        for (int offset = 0, i = 0; offset < length; i++) {
            int codePoint = value.codePointAt(offset);
            chars[i] = new Character(codePoint);
            offset += java.lang.Character.charCount(codePoint);
        }
        return new ArraySequence<Character>(chars);
    }
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Natural")
    public Natural firstOccurrence(@Name("substring") java.lang.String substring) {
    	int index = value.indexOf(substring);
		return index>=0 ? Natural.instance(index) : null;
    }
    
    @Override
    public boolean contains(@Name("element") 
    @TypeInfo("ceylon.language.Equality") 
    Object element) {
    	if (element instanceof String) {
    	    return firstOccurrence(((String)element).value)!=null;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<? extends Object> elements) {
    	return Category$impl.containsAny(this, elements);
    }

    @Override
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<? extends Object> elements) {
    	return Category$impl.containsEvery(this, elements);
    }
    
    public boolean longerThan(@TypeInfo("ceylon.language.Natural") 
    @Name("length") long length) {
        return getSize()>length; //TODO: really inefficient!
    }

    public boolean shorterThan(@TypeInfo("ceylon.language.Natural") 
    @Name("length") long length) {
    	return getSize()<length; //TODO: really inefficient!
    }
    
    public java.lang.String getTrimmed() {
    	return value.trim();
    }

}
