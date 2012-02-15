package ceylon.language;

import java.util.StringTokenizer;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes({"ceylon.language.Comparable<ceylon.language.String>",
	             "ceylon.language.List<ceylon.language.Character>",
	             "ceylon.language.FixedSized<ceylon.language.Character>",
	             "ceylon.language.Summable<ceylon.language.String>",
                 "ceylon.language.Castable<ceylon.language.String>",
                 "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.String>",
                 "ceylon.language.Cloneable<ceylon.language.String>"})
public final class String
    implements Comparable<String>, List<Character>,
               Summable<String>, Castable<String>,
               FixedSized<Character> {

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
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Equality") java.lang.Object that) {
        if (that instanceof String) {
            String s = (String)that;
            return value.equals(s.value);
        } 
        else {
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
        return (c < 0) ? smaller.getSmaller() :
            ((c == 0) ? equal.getEqual() : larger.getLarger());
    }

    /*@Override
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
    }*/

    @Override
    public <CastValue extends String> CastValue castTo() {
        return (CastValue) this;
    }

    @Override
    public String plus(@Name("other") String string) {
        return instance(value + string.value);
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public long getSize() {
        //TODO: should we cache this value in an instvar?
        return value.codePointCount(0, value.length());
    }

    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer getLastIndex() {
        int length = value.length();
        return length==0 ? null : Integer.instance(length-1);
    }

    @Override
    public boolean getEmpty() {
        return value.isEmpty();
    }

    @Override
    public Character item(@Name("index") Integer key) {
        long index = key.longValue();
        int length = value.length();
        if (index < 0 || index >= length)
            return null;
        //TODO: use Character.offsetByCodePoints
        for (int offset = 0, i = 0; offset <= length; i++) {
            int codePoint = value.codePointAt(offset);
            if (i==index)
                return new Character(codePoint);
            offset += java.lang.Character.charCount(codePoint);
        }
        return null;
    }

    @Override
    public boolean defines(@Name("key") Integer key) {
        long index = key.longValue();
        return index >= 0 && index < getSize();
    }

    @Override
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(@Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(@Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Character>")
    public List<? extends Character> items(@Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl.items(this, keys);
    }

    @Override
    @TypeInfo("ceylon.language.Iterator<ceylon.language.Character>")
    public Iterator<Character> getIterator() {
        class StringIterator implements Iterator<Character> {
            private int offset = 0;

            @Override
            public java.lang.Object next() {
                java.lang.Object result;
                if (offset < value.length()) {
                    int codePoint = value.codePointAt(offset);
                    result = Character.instance(codePoint);
                    offset += java.lang.Character.charCount(codePoint);
                } else {
                    result = exhausted.getExhausted();
                }
                return result;
            }
        }
        
        return new StringIterator();
    }

    @Override
    public Character getFirst() {
        return FixedSized$impl.getFirst(this);
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
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer firstOccurrence(@Name("substring") java.lang.String substring) {
        int index = value.indexOf(substring);
        return index>=0 ? Integer.instance(index) : null;
    }
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer lastOccurrence(@Name("substring") java.lang.String substring) {
        int index = value.lastIndexOf(substring);
        return index>=0 ? Integer.instance(index) : null;
    }
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer firstCharacterOccurrence(@Name("character") 
    @TypeInfo("ceylon.language.Character") int character) {
        int index = value.indexOf(character);
        return index>=0 ? Integer.instance(index) : null;
    }
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer lastCharacterOccurrence(@Name("character") 
    @TypeInfo("ceylon.language.Character") int character) {
        int index = value.lastIndexOf(character);
        return index>=0 ? Integer.instance(index) : null;
    }
    
    @Override
    public boolean contains(@Name("element") 
    @TypeInfo("ceylon.language.Equality") 
    java.lang.Object element) {
        if (element instanceof String) {
            return value.indexOf(((String)element).value)>=0;
        }
        else if (element instanceof Character) {
            return value.indexOf(((Character)element).intValue())>=0;
        }
        else {
            return false;
        }
    }
    
    @Override
    public long count(@Name("element") 
    @TypeInfo("ceylon.language.Equality") 
    java.lang.Object element) {
        throw new Exception(null, null); //TODO!!!!!
    }
    
    public boolean startsWith(@Name("substring") java.lang.String substring) {
        return value.startsWith(substring);
    }
    
    public boolean endsWith(@Name("substring") java.lang.String substring) {
        return value.endsWith(substring);
    }
    
    @Override
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<?> elements) {
        return Category$impl.containsAny(this, elements);
    }

    @Override
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<?> elements) {
        return Category$impl.containsEvery(this, elements);
    }
    
    public boolean longerThan(@TypeInfo("ceylon.language.Integer") 
    @Name("length") long length) {
        return getSize()>length; //TODO: really inefficient!
    }

    public boolean shorterThan(@TypeInfo("ceylon.language.Integer") 
    @Name("length") long length) {
        return getSize()<length; //TODO: really inefficient!
    }
    
    public java.lang.String getTrimmed() {
        return value.trim();
    }
    
    public java.lang.String getNormalized() {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        boolean previousWasWhitespace=false;
        for (int i=0;i<value.length();) {
            int c = java.lang.Character.codePointAt(value, i);
            boolean isWhitespace = java.lang.Character.isWhitespace(c);
            if (!isWhitespace) {
                result.appendCodePoint(c);
            }
            else if (!previousWasWhitespace) {
                result.append(" ");
            }
            previousWasWhitespace = isWhitespace;
            i+=java.lang.Character.charCount(c);
        }
        return result.toString();
    }
    
    @TypeInfo("ceylon.language.String")
    public java.lang.String initial(@TypeInfo("ceylon.language.Integer") 
    @Name("length") long length) {
    	if (length<=0) return "";
        return length>value.length() ? value : 
            value.substring(0, (int)length);
    }

    @TypeInfo("ceylon.language.String")
    public java.lang.String terminal(@TypeInfo("ceylon.language.Integer") 
    @Name("length") long length) {
    	if (length<=0) return "";
        return length>value.length() ? value : 
            value.substring(value.length()-(int) length, value.length());
    }
    
    public java.lang.String join(@Name("strings") @Sequenced
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    Iterable<? extends String> strings) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        Iterator<? extends String> it = strings.getIterator();
        java.lang.Object elem = it.next();
        if (elem != exhausted.getExhausted()) {
            result.append(elem);
            for (;!((elem = it.next()) instanceof Finished);) {
                result.append(value).append(elem);
            }
        }
        return result.toString();
    }
    
    @Override
    @TypeInfo("ceylon.language.String")
    public String segment(@Name("from") final Integer from, 
            @Name("length") final Integer length) {
        long fromIndex = from.longValue();
        long resultLength = length.longValue();
        int len = value.length();
        if (fromIndex>=len || resultLength==0) return instance("");
        if (fromIndex+resultLength>len) resultLength = len-fromIndex;
        return instance(value.substring((int) fromIndex, 
                (int) (fromIndex+resultLength)));
    }
    
    @Override
    @TypeInfo("ceylon.language.String")
    public String span(@Name("from") final Integer from, 
            @Name("to") @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer") 
            final Integer to) {
        int len = value.length();
        if (len==0) return instance("");
        long fromIndex = from.longValue();
        long toIndex = to==null ? len-1 : to.longValue();
        if (fromIndex>=len||toIndex<fromIndex) return instance("");
        if (toIndex>=len) toIndex = len-1;
        return instance(value.substring((int) fromIndex, (int) toIndex+1));
    }
    
    public java.lang.String getReversed() {
        int len = value.length();
        if (len<2) return value;
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (int i=len-1; i>=0; i--) {
            builder.append(value.charAt(i));
        }
        return builder.toString();
    }
    
    public java.lang.String repeat(
            @TypeInfo("ceylon.language.Integer") 
            @Name("times") long times) {
        int len = value.length();
        if (times<=0 || len==0) return "";
        if (times==1) return value;
        java.lang.StringBuilder builder = new java.lang.StringBuilder(len*(int)times);
        for (int i=0; i<times; i++) {
            builder.append(value);
        }
        return builder.toString();
    }
    
    public java.lang.String replace( 
            @Name("substring") java.lang.String substring,
            @Name("replacement") java.lang.String replacement) {
        return value.replace(substring, replacement);
    }
    
    @TypeInfo("ceylon.language.Iterable<ceylon.language.String>")
    public Iterable<? extends String> split(
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterable<ceylon.language.Character>")
            @Defaulted
            @Name("separator") Iterable<? extends Character> separators,
            @Defaulted
            @Name("discardSeparators") boolean discardSeparators) {
        java.lang.String delims;
        if (separators==null) {
            delims = " \t\n\r\f";
        }
        else if (separators instanceof String) {
            delims = separators.toString();
        }
        else {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            java.lang.Object elem;
            for (Iterator<? extends Character> iter=separators.getIterator(); !((elem = iter.next()) instanceof Finished);) {
                builder.append(elem);
            }
            delims = builder.toString();
        }
        return new Tokens(value, delims, !discardSeparators);
    }

    private static final class Tokens implements Iterable<String> {
        private java.lang.String str;
        private java.lang.String delims;
        private boolean keepSeparators;
        
        public Tokens(java.lang.String str, java.lang.String delims, boolean keepSeparators) {
            this.str = str;
            this.delims = delims;
            this.keepSeparators = keepSeparators;
        }

        @Override
        public Iterator<? extends String> getIterator() {
            class TokenIterator implements Iterator<String> {
                private final StringTokenizer tokens;

                private TokenIterator(StringTokenizer tokens) {
                    this.tokens = tokens;
                }

                @Override
                public java.lang.Object next() {
                    java.lang.Object result;
                    if (tokens.hasMoreTokens()) {
                        result = String.instance(tokens.nextToken());
                    } else {
                        result = exhausted.getExhausted();
                    }
                    return result;
                }
            }
            
            return new TokenIterator(new StringTokenizer(str, delims, keepSeparators));
        }

        @Override
        public boolean getEmpty() {
            return getIterator().next() != exhausted.getExhausted();
        }

        /*@Override
        public String getFirst() {
            java.lang.Object result = getIterator().next();
            return (String) ((result != exhausted.getExhausted()) ? result : null);
        }*/
    }

    @Override
    public String getClone() {
        return this;
    }

    @Ignore
    public static class String$impl {
        public static Iterable<? extends Character> split$separators(String $this){
            return null;
        }
        public static boolean split$discardSeparators(String $this, Iterable<? extends Character> separators){
            return false;
        }
    }
}
