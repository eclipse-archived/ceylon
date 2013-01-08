package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.language.EmptyString;
import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.language.SequenceString;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes({"ceylon.language::Sequential<ceylon.language::Character>",
                 "ceylon.language::Comparable<ceylon.language::String>",
                 "ceylon.language::Summable<ceylon.language::String>",
                 "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::String>",
                 "ceylon.language::Castable<ceylon.language::String>",
                 "ceylon.language::Cloneable<ceylon.language::String>"})
@ValueType
public abstract class String
    implements Comparable<String>, Sequential<Character>,
               Summable<String>, Castable<String> {
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;
    protected final ceylon.language.Iterable$impl<Character> $ceylon$language$Iterable$this;
    protected final ceylon.language.List$impl<Character> $ceylon$language$List$this;

    public final java.lang.String value;

    protected String(java.lang.String s) {
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Character>(this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Character>(this);
        value = s;
    }

    private Correspondence$impl<Integer,Character> correspondence$impl = new Correspondence$impl<Integer,Character>(this);
    
    @Ignore
    @Override
    public Correspondence$impl<? super Integer,? extends Character> $ceylon$language$Correspondence$impl(){
        return correspondence$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super Integer, ? extends Character>.Items Items$new(Sequence<? extends Integer> keys) {
        return correspondence$impl.Items$new(keys);
    }

    @Override
    public java.lang.String toString() {
        return value;
    }

    @Ignore
    public static java.lang.String toString(java.lang.String value) {
        return value;
    }

    @Ignore
    public static ceylon.language.String instance(java.lang.String s) {
        if (s==null) return null;
        return s.isEmpty() ? EmptyString.instance : new SequenceString(s);
    }

    @Ignore
    public static ceylon.language.String instance(java.lang.String... strings) {
        StringBuffer buf = new StringBuffer();
        for (java.lang.String s: strings)
            buf.append(s);
        return instance(buf.toString());
    }

    @Ignore
    public static ceylon.language.String instance(String... strings) {
        StringBuffer buf = new StringBuffer();
        for (String s: strings)
            buf.append(s.value);
        return instance(buf.toString());
    }

    public java.lang.String getUppercased() {
        return value.toUpperCase();
    }

    @Ignore
    public static java.lang.String getUppercased(java.lang.String value) {
        return value.toUpperCase();
    }

    public java.lang.String getLowercased() {
        return value.toLowerCase();
    }

    @Ignore
    public static java.lang.String getLowercased(java.lang.String value) {
        return value.toLowerCase();
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        if (that instanceof String) {
            String s = (String)that;
            return value.equals(s.value);
        }
        else {
            return false;
        }
    }

    @Ignore
    public static boolean equals(java.lang.String value, java.lang.Object that) {
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

    @Ignore
    public static int hashCode(java.lang.String value) {
        return value.hashCode();
    }

    @Override
    public Comparison compare(@Name("other") String other) {
        int c = value.compareTo(other.value);
        return (c < 0) ? smaller_.getSmaller$() :
            ((c == 0) ? equal_.getEqual$() : larger_.getLarger$());
    }

    @Ignore
    public static Comparison compare(java.lang.String value, java.lang.String otherValue) {
        int c = value.compareTo(otherValue);
        return (c < 0) ? smaller_.getSmaller$() :
            ((c == 0) ? equal_.getEqual$() : larger_.getLarger$());
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

    @Ignore
    public static <CastValue extends String> CastValue castTo(java.lang.String value) {
        // FIXME Is this correct?
        return (CastValue) instance(value);
    }

    @Override
    public String plus(@Name("other") String other) {
        return instance(value + other.value);
    }

    @Ignore
    public static java.lang.String plus(java.lang.String value, java.lang.String otherValue) {
        return value + otherValue;
    }

    @Override
    @TypeInfo("ceylon.language::Integer")
    public long getSize() {
        //TODO: should we cache this value in an instvar?
        // But remember that we'll mostly be using the static verion
        // of this method! So an instvar won't help much.
        return value.codePointCount(0, value.length());
    }

    @Ignore
    public static long getSize(java.lang.String value) {
        return value.codePointCount(0, value.length());
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer getLastIndex() {
        long length = getSize();
        return (length == 0) ? null : Integer.instance(length - 1);
    }

    @Ignore
    public static Integer getLastIndex(java.lang.String value) {
        long length = getSize(value);
        return (length == 0) ? null : Integer.instance(length - 1);
    }

    @Override
    public boolean getEmpty() {
        return value.isEmpty();
    }

    @Ignore
    public static boolean getEmpty(java.lang.String value) {
        return value.isEmpty();
    }

    @Override
    @TypeInfo("ceylon.language::Character|ceylon.language::Null")
    public Character item(@Name("index") Integer key) {
        return item(value, key.longValue());
    }

    @Ignore
    public static Character item(java.lang.String value, long key) {
        int index = (int)key;
        int length = value.length();
        if (index < 0 || index >= length) {
            return null;
        }
        int offset = value.offsetByCodePoints(0, index);
        int codePoint = value.codePointAt(offset);
        return Character.instance(codePoint);
    }

    @Override
    public boolean defines(@Name("index") Integer key) {
        long index = key.longValue();
        return index >= 0 && index < getSize();
    }

    @Ignore
    public static boolean defines(java.lang.String value, long key) {
        long index = key;
        return index >= 0 && index < getSize(value);
    }

    @Override
    @Ignore
    public Category getKeys() {
        return $ceylon$language$Correspondence$this.getKeys();
    }

    @Ignore
    public static Category getKeys(java.lang.String value) {
        // TODO We're still boxing here!
        return instance(value).getKeys();
    }

    @Override
    @Ignore
    public boolean definesEvery(@Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    Sequential<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }

    @Ignore
    public static boolean definesEvery(java.lang.String value, Sequential<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        // TODO We're still boxing here!
        return instance(value).definesEvery(keys);
    }

    @Override
    @Ignore
    public boolean definesEvery() {
        return $ceylon$language$Correspondence$this.definesEvery((Sequential)empty_.getEmpty$());
    }

    @Ignore
    public static boolean definesEvery(java.lang.String value) {
        // TODO We're still boxing here!
        return instance(value).definesEvery((Sequential)empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequential<? extends Integer> definesEvery$keys() {
        return (Sequential)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean definesAny(@Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    Sequential<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }

    @Ignore
    public static boolean definesAny(java.lang.String value, Sequential<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        // TODO We're still boxing here!
        return instance(value).definesAny(keys);
    }

    @Override
    @Ignore
    public boolean definesAny() {
        return $ceylon$language$Correspondence$this.definesAny((Sequential)empty_.getEmpty$());
    }

    @Ignore
    public static boolean definesAny(java.lang.String value) {
        // TODO We're still boxing here!
        return instance(value).definesAny((Sequential)empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequential<? extends Integer> definesAny$keys() {
        return (Sequential)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public Sequential<? extends Character> items(@Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    Sequential<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.items(keys);
    }

    @Ignore
    public static Sequential<? extends Character> items(java.lang.String value, Sequential<? extends Integer> keys) {
        // TODO We're still boxing here!
        return instance(value).items(keys);
    }

    @Override
    @Ignore
    public Sequential<? extends Character> items() {
        return $ceylon$language$Correspondence$this.items((Sequential)empty_.getEmpty$());
    }

    @Ignore
    public static Sequential<? extends Character> items(java.lang.String value) {
        // TODO We're still boxing here!
        return instance(value).items((Sequential)empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequential<? extends Integer> items$keys() {
        return (Sequential)empty_.getEmpty$();
    }

    @Override
    @TypeInfo("ceylon.language::Iterator<ceylon.language::Character>")
    public Iterator<Character> getIterator() {
        return getIterator(value);
    }

    @Ignore
    public static Iterator<Character> getIterator(final java.lang.String value) {
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
                    result = finished_.getFinished$();
                }
                return result;
            }
        }

        return new StringIterator();
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language::Character>")
    public Sequential<? extends Character> getCharacters() {
        return getCharacters(value);
    }

    @Ignore
    public static Sequential<? extends Character> getCharacters(java.lang.String value) {
        int length = value.length();
        if (length == 0) {
            return (Sequential)empty_.getEmpty$();
        }
        Character[] chars = new Character[(int)getSize(value)];
        for (int offset = 0, i = 0; offset < length; i++) {
            int codePoint = value.codePointAt(offset);
            chars[i] = Character.instance(codePoint);
            offset += java.lang.Character.charCount(codePoint);
        }
        return new ArraySequence<Character>(chars);
    }

    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer firstOccurrence(@Name("substring") java.lang.String substring) {
        int index = value.indexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @Ignore
    public static Integer firstOccurrence(java.lang.String value, java.lang.String substring) {
        int index = value.indexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer lastOccurrence(@Name("substring") java.lang.String substring) {
        int index = value.lastIndexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @Ignore
    public static Integer lastOccurrence(java.lang.String value, java.lang.String substring) {
        int index = value.lastIndexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer firstCharacterOccurrence(@Name("character")
    @TypeInfo("ceylon.language::Character") int character) {
        int index = value.indexOf(character);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @Ignore
    public static Integer firstCharacterOccurrence(java.lang.String value, int character) {
        int index = value.indexOf(character);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer lastCharacterOccurrence(@Name("character")
    @TypeInfo("ceylon.language::Character") int character) {
        int index = value.lastIndexOf(character);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @Ignore
    public static Integer lastCharacterOccurrence(java.lang.String value, int character) {
        int index = value.lastIndexOf(character);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @Override
    public boolean contains(@Name("element") java.lang.Object element) {
        if (element instanceof String) {
            return value.indexOf(((String)element).value) >= 0;
        }
        else if (element instanceof Character) {
            return value.indexOf(((Character)element).intValue()) >= 0;
        }
        else {
            return false;
        }
    }

    @Ignore
    public static boolean contains(java.lang.String value, java.lang.Object element) {
        if (element instanceof String) {
            return value.indexOf(((String)element).value) >= 0;
        }
        else if (element instanceof Character) {
            return value.indexOf(((Character)element).intValue()) >= 0;
        }
        else {
            return false;
        }
    }

    public boolean startsWith(@Name("substring") java.lang.String substring) {
        return value.startsWith(substring);
    }

    @Ignore
    public static boolean startsWith(java.lang.String value, java.lang.String substring) {
        return value.startsWith(substring);
    }

    public boolean endsWith(@Name("substring") java.lang.String substring) {
        return value.endsWith(substring);
    }

    @Ignore
    public static boolean endsWith(java.lang.String value, java.lang.String substring) {
        return value.endsWith(substring);
    }

    @Override
    @Ignore
    public boolean containsAny(@Sequenced @Name("elements")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    Sequential<?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }

    @Ignore
    public static boolean containsAny(java.lang.String value, Sequential<?> elements) {
        // TODO We're still boxing here!
        return instance(value).containsAny(elements);
    }

    @Override
    @Ignore
    public boolean containsAny() {
        return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
    }

    @Ignore
    public static boolean containsAny(java.lang.String value) {
        // TODO We're still boxing here!
        return instance(value).containsAny(empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequential<?> containsAny$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean containsEvery(@Sequenced @Name("elements")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    Sequential<?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }

    @Ignore
    public static boolean containsEvery(java.lang.String value, Sequential<?> elements) {
        // TODO We're still boxing here!
        return instance(value).containsEvery(elements);
    }

    @Override
    @Ignore
    public boolean containsEvery() {
        return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
    }

    @Ignore
    public static boolean containsEvery(java.lang.String value) {
        // TODO We're still boxing here!
        return instance(value).containsEvery(empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequential<?> containsEvery$elements() {
        return empty_.getEmpty$();
    }

    public boolean longerThan(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
        try {
            value.offsetByCodePoints(0, (int)length+1);
            return true;
        }
        catch (IndexOutOfBoundsException iobe) {
            return false;
        }
    }

    @Ignore
    public static boolean longerThan(java.lang.String value, long length) {
        try {
            value.offsetByCodePoints(0, (int)length+1);
            return true;
        }
        catch (IndexOutOfBoundsException iobe) {
            return false;
        }
    }

    public boolean shorterThan(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
        try {
            value.offsetByCodePoints(0, (int)length);
            return false;
        }
        catch (IndexOutOfBoundsException iobe) {
            return true;
        }
    }

    @Ignore
    public static boolean shorterThan(java.lang.String value, long length) {
        try {
            value.offsetByCodePoints(0, (int)length);
            return false;
        }
        catch (IndexOutOfBoundsException iobe) {
            return true;
        }
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String getTrimmed() {
        return value.trim();
    }

    @Ignore
    public static java.lang.String getTrimmed(java.lang.String value) {
        return value.trim();
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String getNormalized() {
        return getNormalized(value);
    }

    @Ignore
    public static java.lang.String getNormalized(java.lang.String value) {
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

    @TypeInfo("ceylon.language::String")
    public java.lang.String initial(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
    	return initial(value, length);
    }

    @Ignore
    public static java.lang.String initial(java.lang.String value, long length) {
        if (length <= 0) {
            return "";
        } else if (length >= getSize(value)) {
            return value;
        } else {
            int offset = value.offsetByCodePoints(0, (int)length);
            return value.substring(0, (int)offset);
        }
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String terminal(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
    	return terminal(value, length);
    }

    @Ignore
    public static java.lang.String terminal(java.lang.String value, long length) {
        if (length <= 0) {
            return "";
        } else if (length >= getSize(value)) {
            return value;
        } else {
            int offset = value.offsetByCodePoints(0, value.length()-(int)length);
            return value.substring(offset, value.length());
        }
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String join(@Name("strings") @Sequenced
    @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
    Sequential<? extends String> strings) {
        return join(value, strings);
    }

    @Ignore
    public static java.lang.String join(java.lang.String value, Iterable<? extends String> strings) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        Iterator<? extends String> it = strings.getIterator();
        java.lang.Object elem = it.next();
        if (elem != finished_.getFinished$()) {
            result.append(elem);
            for (;!((elem = it.next()) instanceof Finished);) {
                result.append(value).append(elem);
            }
        }
        return result.toString();
    }

    @Ignore
    public java.lang.String join() {
        return join((Sequential)empty_.getEmpty$());
    }

    @Ignore
    public static java.lang.String join(java.lang.String value) {
        return join(value, (Iterable)empty_.getEmpty$());
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String segment(@Name("from") final Integer from,
            @Name("length") final long length) {
        return instance(segment(value, from.longValue(), length));
    }

    @Ignore
    public static java.lang.String segment(java.lang.String value, final long from, final long length) {
        long fromIndex = from;
        long resultLength = length;
        long len = getSize(value);
        if (fromIndex >= len || resultLength <= 0) {
            return "";
        }
        if ((fromIndex + resultLength) > len) {
            resultLength = len - fromIndex;
        }
        int start = value.offsetByCodePoints(0, (int)fromIndex);
        int end = value.offsetByCodePoints(start, (int)resultLength);
        return value.substring(start, end);
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String span(@Name("from") final Integer from,
            @Name("to") final Integer to) {
        return instance(span(value, from.longValue(), to.longValue()));
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String spanFrom(@Name("from") final Integer from) {
        return instance(spanFrom(value, from.longValue()));
    }
    
    @Ignore
    public static java.lang.String spanFrom(java.lang.String value, long from) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        if (from >= len) {
            return "";
        }
        long toIndex = len - 1;
        if (from < 0) {
            from = 0;
        }
        int start = value.offsetByCodePoints(0, (int)from);
        int end = value.offsetByCodePoints(start, (int)(toIndex - from + 1));
        java.lang.String result = value.substring(start, end);
        return result;
    }
    
    @Override
    @TypeInfo("ceylon.language::String")
    public String spanTo(@Name("to")  final Integer to) {
        return instance(spanTo(value, to.longValue()));
    }
     
    @Ignore
    public static java.lang.String spanTo(java.lang.String value, final long to) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        long toIndex = to;
        if (toIndex < 0) {
            return "";
        }
        if (toIndex >= len) {
            toIndex = len - 1;
        }
        int start = 0;
        int end = value.offsetByCodePoints(start, (int)(toIndex + 1));
        java.lang.String result = value.substring(start, end);
        return result;
    }
    

    @Ignore
    public static java.lang.String span(java.lang.String value, long from, long toIndex) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        boolean reverse = toIndex < from;
        if (reverse) {
            long _tmp = toIndex;
            toIndex = from;
            from = _tmp;
        }
    	if (toIndex < 0 || from >= len) {
    		return "";
    	}
    	if (toIndex >= len) {
    		toIndex = len - 1;
    	}
    	if (from < 0) {
    		from = 0;
    	}
        int start = value.offsetByCodePoints(0, (int)from);
        int end = value.offsetByCodePoints(start, (int)(toIndex - from + 1));
        java.lang.String result = value.substring(start, end);
        return reverse ? getReversed(result) : result;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getReversed() {
        return instance(getReversed(value));
    }

    @Ignore
    public static java.lang.String getReversed(java.lang.String value) {
        long len = getSize(value);
        if (len < 2) {
            return value;
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        int offset = value.length();
        while (offset > 0) {
            int c = value.codePointBefore(offset);
            builder.appendCodePoint(c);
            offset -= java.lang.Character.charCount(c);
        }
        return builder.toString();
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String repeat(
            @TypeInfo("ceylon.language::Integer")
            @Name("times") long times) {
        return repeat(value, times);
    }

    @Ignore
    public static java.lang.String repeat(java.lang.String value, long times) {
        int len = value.length();
        if (times<=0 || len==0) return "";
        if (times==1) return value;
        java.lang.StringBuilder builder = new java.lang.StringBuilder(len*(int)times);
        for (int i=0; i<times; i++) {
            builder.append(value);
        }
        return builder.toString();
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String replace(
            @Name("substring") java.lang.String substring,
            @Name("replacement") java.lang.String replacement) {
        return value.replace(substring, replacement);
    }

    @Ignore
    public static java.lang.String replace(java.lang.String value, java.lang.String substring, java.lang.String replacement) {
        return value.replace(substring, replacement);
    }

    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    public Iterable<? extends String> split(
            @TypeInfo(value="ceylon.language::Iterable<ceylon.language::Character>|ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>", erased=true)
            @Defaulted
            @Name("separator") java.lang.Object separator,
            @Defaulted
            @Name("discardSeparators") boolean discardSeparators,
            @Defaulted
            @Name("groupSeparators") boolean groupSeparators) {
        if (value.isEmpty()) {
            return new Singleton<String>(this);
        }
        return new Tokens(value, separator, !discardSeparators, groupSeparators);
    }

    @Ignore
    public static Iterable<? extends String> split(java.lang.String value,
            java.lang.Object separator,
            boolean discardSeparators,
            boolean groupSeparators) {
        if (value.isEmpty()) {
            return new Singleton<String>(instance(value));
        }
        return new Tokens(value, separator, !discardSeparators, groupSeparators);
    }

    @Ignore
    public Iterable<? extends String> split(
            java.lang.Object separator,
            boolean discardSeparators) {
        return split(separator, discardSeparators, split$groupSeparators(separator, discardSeparators));
    }

    @Ignore
    public static Iterable<? extends String> split(java.lang.String value,
            java.lang.Object separator,
            boolean discardSeparators) {
        return split(value, separator, discardSeparators, split$groupSeparators(separator, discardSeparators));
    }

    @Ignore
    public Iterable<? extends String> split(
            java.lang.Object separator) {
        return split(separator, split$discardSeparators(separator));
    }

    @Ignore
    public static Iterable<? extends String> split(java.lang.String value,
            java.lang.Object separator) {
        return split(value, separator, split$discardSeparators(separator));
    }

    @Ignore
    public Iterable<? extends String> split() {
        return split(split$separator());
    }

    @Ignore
    public static Iterable<? extends String> split(java.lang.String value) {
        return split(value, split$separator());
    }

    @Ignore
    public static Callable<? extends Boolean> split$separator(){
        return new AbstractCallable<Boolean>("whitespace") {
            @Override
            public Boolean $call(java.lang.Object ch) {
                return Boolean.instance(((Character) ch).getWhitespace());
            }
        };
    }

    @Ignore
    public static boolean split$discardSeparators(java.lang.Object separator){
        return true;
    }

    @Ignore
    public static boolean split$groupSeparators(java.lang.Object separator, boolean discardSeparators){
        return true;
    }

    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    public Iterable<? extends String> getLines() {
        return split(new AbstractCallable<Boolean>("whitespace") {
            @Override
            public Boolean $call(java.lang.Object ch) {
                return Boolean.instance(((Character) ch).toString().equals("\n"));
            }
        }, true);
    }

    @Ignore
    public static Iterable<? extends String> getLines(java.lang.String value) {
        return split(value, new AbstractCallable<Boolean>("whitespace") {
            @Override
            public Boolean $call(java.lang.Object ch) {
                return Boolean.instance(((Character) ch).toString().equals("\n"));
            }
        }, true);
    }

    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer>")
    public Iterable<? extends Integer> occurrences(
            @Name("substring") java.lang.String substring) {
        return new Occurs(value, substring);
    }

    @Ignore
    public static Iterable<? extends Integer> occurrences(java.lang.String value, java.lang.String substring) {
        return new Occurs(value, substring);
    }

    @Override
    public String getClone() {
        return this;
    }

    @Ignore
    public static java.lang.String getClone(java.lang.String value) {
        return value;
    }

    @Ignore
    public static Iterable<? extends Character> getRest(java.lang.String value) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return instance(value).getRest();
        }
    }

    @Ignore
    public static Character getFirst(java.lang.String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return item(value, 0);
        }
    }

    @Ignore
    public static Character getLast(java.lang.String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return item(value, getLastIndex(value).longValue());
        }
    }

    @Ignore
    public static Iterable<? extends Character> getSequence(java.lang.String value) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return instance(value).getSequence();
        }
    }

    @Ignore
    public static Character find(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return null;
        } else {
            return instance(value).find(f);
        }
    }

    @Ignore
    public static Character findLast(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return null;
        } else {
            return instance(value).findLast(f);
        }
    }

    @Ignore
    public static Iterable<? extends Character> sort(java.lang.String value, Callable<? extends Comparison> f) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return instance(string_.string(instance(value).sort(f)));
        }
    }

    @Override @Ignore
    public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
        return new MapIterable<Character, Result>(this, f);
    }

    @Ignore
    public static <Result> Iterable<? extends Result> map(java.lang.String value, Callable<? extends Result> f) {
        return new MapIterable<Character, Result>(instance(value), f);
    }

    @Override @Ignore
    public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
        return new MapIterable<Character, Result>(this, f).getSequence();
    }
    @Ignore
    public static <Result> Sequential<? extends Result> collect(java.lang.String value, Callable<? extends Result> f) {
        return new MapIterable<Character, Result>(instance(value), f).getSequence();
    }

    @Ignore
    public static Sequential<? extends Character> filter(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return String.instance(string_.string((new FilterIterable<Character>(instance(value), f)).getSequence()));
        }
    }

    @Override @Ignore
    public Sequential<? extends Character> select(Callable<? extends Boolean> f) {
        return filter(value, f);
    }
    @Ignore
    public static Sequential<? extends Character> select(java.lang.String value, Callable<? extends Boolean> f) {
        return filter(value, f);
    }

    @Ignore
    public static <Result> Result fold(java.lang.String value, Result ini, Callable<? extends Result> f) {
        if (value.isEmpty()) {
            return ini;
        } else {
            return instance(value).fold(ini, f);
        }
    }

    @Ignore
    public static boolean any(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return false;
        } else {
            return instance(value).any(f);
        }
    }

    @Ignore
    public static boolean every(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return false;
        } else {
            return instance(value).every(f);
        }
    }

    @Ignore
    public static Iterable<? extends Character> skipping(java.lang.String value, long skip) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return instance(segment(value, skip, getSize(value)));
        }
    }

    @Ignore
    public static Iterable<? extends Character> taking(java.lang.String value, long take) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return instance(segment(value, 0, take));
        }
    }

    @Ignore
    public static Iterable<? extends Character> by(java.lang.String value, long step) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return instance(string_.string(instance(value).by(step).getSequence()));
        }
    }

    @Ignore
    public static long count(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return 0;
        } else {
            return instance(value).count(f);
        }
    }

    @TypeInfo("ceylon.language::String")
    @Override
    public String getCoalesced() {
        return this; //Can't have null characters
    }

    @Ignore
    public static java.lang.String getCoalesced(java.lang.String value) {
        return value;
    }

    @Ignore
    public static Iterable<? extends Entry<? extends Integer, ? extends Character>> getIndexed(java.lang.String value) {
        if (value.isEmpty()) {
            return (Iterable)instance(value);
        } else {
            return instance(value).getIndexed();
        }
    }

    @Ignore
    public static <Other>Iterable chain(java.lang.String value, Iterable<? extends Other> other) {
        if (value.isEmpty()) {
            return other;
        } else {
            return instance(value).chain(other);
        }
    }
    @Ignore
    public static <Key> Map<? extends Key, ? extends Sequence<? extends Character>> group(java.lang.String value, Callable<? extends Key> grouping) {
        if (value.isEmpty()) {
            return new InternalMap<Key, Sequence<? extends Character>>(java.util.Collections.<Key,Sequence<Character>>emptyMap());
        } else {
            return instance(value).group(grouping);
        }
    }

    @Ignore
    public static <Other>String withLeading(java.lang.String value) {
        return instance(value);
    }

    @Ignore
    public static <Other>String withTrailing(java.lang.String value) {
        return instance(value);
    }

    @Ignore @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <Other>Sequence withLeading(java.lang.String value, Other e) {
        if (value.isEmpty()) {
            return new ArraySequence(e);
        } else {
            return instance(value).withLeading(e);
        }
    }

    @Ignore @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <Other>Sequence withTrailing(java.lang.String value, Other e) {
        if (value.isEmpty()) {
            return new ArraySequence(e);
        } else {
            return instance(value).withTrailing(e);
        }
    }

    private static final class Tokens implements Iterable<String> {
        private final ceylon.language.Iterable$impl<String> $ceylon$language$Iterable$this;
        private final ceylon.language.Category$impl $ceylon$language$Category$this;
        
        private final java.lang.String str;
        private final java.lang.Object separator;
        private final boolean keepSeparators;
        private final boolean groupSeparators;

        public Tokens(java.lang.String str, java.lang.Object separator,
                boolean keepSeparators, boolean groupSeparators) {
            this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<String>(this);
            this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
            this.str = str;
            this.separator = separator;
            this.keepSeparators = keepSeparators;
            this.groupSeparators = groupSeparators;
        }

        @Override
        public Iterator<? extends String> getIterator() {
            abstract class TokenIterator implements Iterator<String> {
                protected final char[] chars = str.toCharArray();
                protected int index = 0;
                private boolean first = true;
                private boolean lastTokenWasSeparator = false;

                @Override
                public java.lang.Object next() {
                    if (!eof()) {
                        int start = index;
                        // if we start with a separator, or if we returned a separator the last time
                        // and we are still looking at a separator: return an empty token once
                        if(((first && start == 0)
                                || lastTokenWasSeparator)
                                && peekSeparator()){
                            first = false;
                            lastTokenWasSeparator = false;
                            return String.instance("");
                        }
                        // are we looking at a separator
                        if(eatSeparator()){
                            if(groupSeparators){
                                // eat them all in one go if we group them
                                do{}while(eatSeparator());
                            }
                            // do we return them?
                            if(keepSeparators){
                                lastTokenWasSeparator = true;
                                return String.instance(new java.lang.String(chars, start, index-start));
                            }
                            // keep going and eat the next word
                            start = index;
                        }
                        // eat until the next separator
                        while(!eof() && !peekSeparator()){
                            eatChar();
                        }
                        lastTokenWasSeparator = false;
                        return String.instance(new java.lang.String(chars, start, index-start));
                    } else if (lastTokenWasSeparator){
                        // we're missing a last empty token before the EOF because the string ended
                        // with a returned separator
                        lastTokenWasSeparator = false;
                        return String.instance("");
                    } else {
                        return finished_.getFinished$();
                    }
                }

                protected boolean eof(){
                    return index >= chars.length;
                }

                private boolean eatSeparator() {
                    boolean ret = peekSeparator();
                    if(ret)
                        eatChar();
                    return ret;
                }

                private void eatChar() {
                    if(java.lang.Character.isHighSurrogate(chars[index]))
                        index += 2;
                    else
                        index++;
                }

                protected abstract boolean peekSeparator();
            }

            if (separator instanceof Callable) {
                return new TokenIterator() {
                    @SuppressWarnings("unchecked")
                    protected final boolean peekSeparator() {
                        if(eof())
                            return false;
                        int charCodePoint = java.lang.Character.codePointAt(chars, index);
                        return ((Callable<Boolean>)separator).$call(Character.instance(charCodePoint)).booleanValue();
                    }
                };
            } else if (separator instanceof java.lang.String) {
                return new TokenIterator() {
                    protected final boolean peekSeparator() {
                        if(eof())
                            return false;
                        int charCodePoint = java.lang.Character.codePointAt(chars, index);
                        return ((java.lang.String)separator).indexOf(charCodePoint) >= 0;
                    }
                };
            } else if (separator instanceof String) {
                return new TokenIterator() {
                    protected final boolean peekSeparator() {
                        if(eof())
                            return false;
                        int charCodePoint = java.lang.Character.codePointAt(chars, index);
                        return ((String)separator).value.indexOf(charCodePoint) >= 0;
                    }
                };
            } else {
                return new TokenIterator() {
                    @SuppressWarnings("unchecked")
                    protected final boolean peekSeparator() {
                        if(eof())
                            return false;
                        int charCodePoint = java.lang.Character.codePointAt(chars, index);
                        java.lang.Object $tmp;
                        for (Iterator<? extends Character> iter = ((Iterable<? extends Character>)separator).getIterator();
                                !(($tmp = iter.next()) instanceof Finished);) {
                            if (((Character)$tmp).getInteger() == charCodePoint) {
                                return true;
                            }
                        }
                        return false;
                    }
                };
            }
        }

        @Override
        public long getSize() {
            return $ceylon$language$Iterable$this.getSize();
        }

        @Override
        public boolean getEmpty() {
            return getIterator().next() == finished_.getFinished$();
        }

        @Override
        @Ignore
        public String getFirst() {
        	return $ceylon$language$Iterable$this.getFirst();
        }
        @Override @Ignore
        public String getLast() {
            return $ceylon$language$Iterable$this.getLast();
        }

        @Override
        @Ignore
        public Iterable<? extends String> getRest() {
        	return $ceylon$language$Iterable$this.getRest();
        }

        @Override
        @Ignore
        public Sequential<? extends String> getSequence() {
            return $ceylon$language$Iterable$this.getSequence();
        }
        @Override @Ignore
        public String find(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.find(f);
        }
        @Override @Ignore
        public String findLast(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.findLast(f);
        }
        @Override
        @Ignore
        public Sequential<? extends String> sort(Callable<? extends Comparison> f) {
            return $ceylon$language$Iterable$this.sort(f);
        }
        @Override
        @Ignore
        public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
            return new MapIterable<String, Result>(this, f);
        }
        @Override
        @Ignore
        public Iterable<? extends String> filter(Callable<? extends Boolean> f) {
            return new FilterIterable<String>(this, f);
        }
        @Override
        @Ignore
        public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
            return new MapIterable<String, Result>(this, f).getSequence();
        }
        @Override
        @Ignore
        public Sequential<? extends String> select(Callable<? extends Boolean> f) {
            return new FilterIterable<String>(this, f).getSequence();
        }
        @Override
        @Ignore
        public <Result> Result fold(Result ini, Callable<? extends Result> f) {
            return $ceylon$language$Iterable$this.fold(ini, f);
        }
        @Override @Ignore
        public boolean any(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.any(f);
        }
        @Override @Ignore
        public boolean every(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.every(f);
        }
        @Override @Ignore
        public Iterable<? extends String> skipping(long n) {
            return $ceylon$language$Iterable$this.skipping(n);
        }
        @Override @Ignore
        public Iterable<? extends String> taking(long n) {
            return $ceylon$language$Iterable$this.taking(n);
        }
        @Override @Ignore
        public Iterable<? extends String> by(long n) {
            return $ceylon$language$Iterable$this.by(n);
        }
        @Override @Ignore
        public long count(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.count(f);
        }
        @Override @Ignore
        public Iterable<? extends String> getCoalesced() {
            return $ceylon$language$Iterable$this.getCoalesced();
        }
        @Override @Ignore
        public Iterable<? extends Entry<? extends Integer, ? extends String>> getIndexed() {
            return $ceylon$language$Iterable$this.getIndexed();
        }
        @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
            return $ceylon$language$Iterable$this.chain(other);
        }
        @Override @Ignore
        public <Key> Map<? extends Key, ? extends Sequence<? extends String>> group(Callable<? extends Key> grouping) {
            return $ceylon$language$Iterable$this.group(grouping);
        }
        @Override @Ignore
        public boolean contains(@Name("element") java.lang.Object element) {
            return $ceylon$language$Iterable$this.contains(element);
        }
        @Override @Ignore
        public boolean containsEvery(
                @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
            return $ceylon$language$Category$this.containsEvery(elements);
        }
        @Override @Ignore
        public boolean containsEvery() {
            return $ceylon$language$Category$this.containsEvery();
        }
        @Override @Ignore
        public Sequential<?> containsEvery$elements() {
            return $ceylon$language$Category$this.containsEvery$elements();
        }
        @Override @Ignore
        public boolean containsAny(
                @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
            return $ceylon$language$Category$this.containsAny(elements);
        }
        @Override @Ignore
        public boolean containsAny() {
            return $ceylon$language$Category$this.containsAny();
        }
        @Override @Ignore
        public Sequential<?> containsAny$elements() {
            return $ceylon$language$Category$this.containsAny$elements();
        }
    }

    private static final class Occurs implements Iterable<Integer> {
        private final ceylon.language.Iterable$impl<Integer> $ceylon$language$Iterable$this;
        private final ceylon.language.Category$impl $ceylon$language$Category$this;
        
        private final java.lang.String str;
        private final java.lang.String oc;

        public Occurs(java.lang.String str, java.lang.String oc) {
            this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Integer>(this);
            this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
            this.str = str;
            this.oc = oc;
        }

        @Override
        public Iterator<? extends Integer> getIterator() {
            class OccurrenceIterator implements Iterator<Integer> {
                private int pos=0;

                @Override
                public java.lang.Object next() {
                    int idx = str.indexOf(oc, pos);
                    if (idx >= pos) {
                        pos = idx+oc.length();
                        return Integer.instance(idx);
                    }
                    return finished_.getFinished$();
                }
            }

            return new OccurrenceIterator();
        }

        @Override
        public long getSize() {
            return $ceylon$language$Iterable$this.getSize();
        }

        @Override
        public boolean getEmpty() {
            return getIterator().next() == finished_.getFinished$();
        }

        @Override
        @Ignore
        public Integer getFirst() {
        	return $ceylon$language$Iterable$this.getFirst();
        }
        @Override @Ignore
        public Integer getLast() {
            return $ceylon$language$Iterable$this.getLast();
        }

        @Override
        @Ignore
        public Iterable<? extends Integer> getRest() {
        	return $ceylon$language$Iterable$this.getRest();
        }

        @Override
        @Ignore
        public Sequential<? extends Integer> getSequence() {
            return $ceylon$language$Iterable$this.getSequence();
        }
        @Override @Ignore
        public Integer find(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.find(f);
        }
        @Override @Ignore
        public Integer findLast(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.findLast(f);
        }
        @Override
        @Ignore
        public Sequential<? extends Integer> sort(Callable<? extends Comparison> f) {
            return $ceylon$language$Iterable$this.sort(f);
        }
        @Override
        @Ignore
        public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
            return new MapIterable<Integer, Result>(this, f);
        }
        @Override
        @Ignore
        public Iterable<? extends Integer> filter(Callable<? extends Boolean> f) {
            return new FilterIterable<Integer>(this, f);
        }
        @Override @Ignore
        public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
            return new MapIterable<Integer, Result>(this, f).getSequence();
        }
        @Override @Ignore
        public Sequential<? extends Integer> select(Callable<? extends Boolean> f) {
            return new FilterIterable<Integer>(this, f).getSequence();
        }
        @Override
        @Ignore
        public <Result> Result fold(Result ini, Callable<? extends Result> f) {
            return $ceylon$language$Iterable$this.fold(ini, f);
        }
        @Override @Ignore
        public boolean any(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.any(f);
        }
        @Override @Ignore
        public boolean every(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.every(f);
        }
        @Override @Ignore
        public Iterable<? extends Integer> skipping(long n) {
            return $ceylon$language$Iterable$this.skipping(n);
        }
        @Override @Ignore
        public Iterable<? extends Integer> taking(long n) {
            return $ceylon$language$Iterable$this.taking(n);
        }
        @Override @Ignore
        public Iterable<? extends Integer> by(long n) {
            return $ceylon$language$Iterable$this.by(n);
        }
        @Override @Ignore
        public long count(Callable<? extends Boolean> f) {
            return $ceylon$language$Iterable$this.count(f);
        }
        @Override @Ignore
        public Iterable<? extends Integer> getCoalesced() {
            return $ceylon$language$Iterable$this.getCoalesced();
        }
        @Override @Ignore
        public Iterable<? extends Entry<? extends Integer, ? extends Integer>> getIndexed() {
            return $ceylon$language$Iterable$this.getIndexed();
        }
        @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
            return $ceylon$language$Iterable$this.chain(other);
        }
        @Override @Ignore
        public <Key> Map<? extends Key, ? extends Sequence<? extends Integer>> group(Callable<? extends Key> grouping) {
            return $ceylon$language$Iterable$this.group(grouping);
        }
        @Override @Ignore
        public boolean contains(@Name("element") java.lang.Object element) {
            return $ceylon$language$Iterable$this.contains(element);
        }
        @Override @Ignore
        public boolean containsEvery(
                @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
            return $ceylon$language$Category$this.containsEvery(elements);
        }
        @Override @Ignore
        public boolean containsEvery() {
            return $ceylon$language$Category$this.containsEvery();
        }
        @Override @Ignore
        public Sequential<?> containsEvery$elements() {
            return $ceylon$language$Category$this.containsEvery$elements();
        }
        @Override @Ignore
        public boolean containsAny(
                @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
            return $ceylon$language$Category$this.containsAny(elements);
        }
        @Override @Ignore
        public boolean containsAny() {
            return $ceylon$language$Category$this.containsAny();
        }
        @Override @Ignore
        public Sequential<?> containsAny$elements() {
            return $ceylon$language$Category$this.containsAny$elements();
        }
    }
}
