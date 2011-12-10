package ceylon.language;

import java.util.StringTokenizer;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes({"ceylon.language.Comparable<ceylon.language.String>",
	             "ceylon.language.Ordered<ceylon.language.Character>",
	             "ceylon.language.Correspondence<ceylon.language.Integer,ceylon.language.Character>",
	             "ceylon.language.Format",
	             "ceylon.language.Sized",
	             "ceylon.language.Summable<ceylon.language.String>",
                 "ceylon.language.Castable<ceylon.language.String>",
                 "ceylon.language.Category",
                 "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.String>"})
public final class String
    implements Comparable<String>, Ordered<Character>, 
               Correspondence<Integer,Character>, Format,
               Sized, Summable<String>, Castable<String>,
               Category, Ranged<Integer,String> {

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
        return (c < 0) ? Comparison.SMALLER :
            ((c == 0) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Override
    public java.lang.String getFormatted() {
        return value;
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
    public Iterable<? extends Character> items(@Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
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
        return length>value.length() ? value : 
            value.substring(0, (int)length);
    }

    @TypeInfo("ceylon.language.String")
    public java.lang.String terminal(@TypeInfo("ceylon.language.Integer") 
    @Name("length") long length) {
        return length>value.length() ? value : 
            value.substring(value.length()-(int) length, value.length());
    }
    
    public java.lang.String join(@Name("strings") @Sequenced
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    Iterable<? extends String> strings) {
        Iterator<? extends String> it = strings.getIterator();
        if (it==null) {
            return "";
        }
        else {
            java.lang.StringBuilder result = new java.lang.StringBuilder();
            result.append(it.getHead());
            for (it=it.getTail();it!=null;it=it.getTail()) {
                result.append(value).append(it.getHead());
            }
            return result.toString();
        }
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
        if (times==0 || len==0) return "";
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
    
    @TypeInfo("ceylon.language.Ordered<ceylon.language.String>")
    public Ordered<? extends String> split(
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterable<ceylon.language.Character>")
            @Name("separator") Iterable<? extends Character> separators,
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
            for (Iterator<? extends Character> it=separators.getIterator(); it!=null; it=it.getTail()) {
                builder.append(it.getHead());
            }
            delims = builder.toString();
        }
        return new Tokens(new StringTokenizer(value, delims, !discardSeparators));
    }

    private static final class Tokens implements Ordered<String> {
        private final StringTokenizer tokens;
        private final boolean empty;
        private final String first;
        private TokenIterator it;
        private final boolean more;

        private Tokens(StringTokenizer tokens) {
            this.tokens = tokens;
            empty = !tokens.hasMoreTokens();
            first = empty ? null : String.instance(tokens.nextToken());
            more = tokens.hasMoreTokens();
        }

        @Override
        public Iterator<? extends String> getIterator() {
            if (!more) {
                return null;
            }
            else if (it==null) {
                it = new TokenIterator(tokens, first);
                return it;
            }
            else {
                return it;
            }
        }

        @Override
        public boolean getEmpty() {
            return empty;
        }

        @Override
        //@TypeInfo("ceylon.language.Nothing|ceylon.language.String")
        public String getFirst() {
            return first;
        }
    }

    private static final class TokenIterator implements Iterator<String> {
        private final StringTokenizer tokens;
        private final String head;
        private TokenIterator it;
        private final boolean more;

        private TokenIterator(StringTokenizer tokens, String head) {
            this.tokens = tokens;
            this.head = head;
            more = tokens.hasMoreTokens();
        }

        @Override
        public String getHead() {
            return head;
        }

        @Override
        public Iterator<? extends String> getTail() {
            if (!more) {
                return null;
            }
            else if (it==null) {
                it = new TokenIterator(tokens, 
                        String.instance(tokens.nextToken()));
                return it;
            }
            else {
                return it;
            }
        }
    }

}
