package ceylon.language;

import static java.lang.Long.MAX_VALUE;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.language.EmptyString;
import com.redhat.ceylon.compiler.java.language.SequenceString;
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
                 "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::String>",
                 "ceylon.language::FixedSized<ceylon.language::Character>",
                 "ceylon.language::Summable<ceylon.language::String>",
                 "ceylon.language::Castable<ceylon.language::String>",
                 "ceylon.language::Cloneable<ceylon.language::String>"})
@ValueType
public abstract class String
    implements Comparable<String>, Sequential<Character>,
               Summable<String>, Castable<String>,
               FixedSized<Character> {

    public final java.lang.String value;

    protected String(java.lang.String s) {
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
    @TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
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
    @TypeInfo("ceylon.language::Character|ceylon.language::Nothing")
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
        return Correspondence$impl._getKeys(this);
    }

    @Ignore
    public static Category getKeys(java.lang.String value) {
        // TODO We're still boxing here!
        return Correspondence$impl._getKeys(instance(value));
    }

    @Override
    @Ignore
    public boolean definesEvery(@Name("keys")
    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<ceylon.language::Integer>")
    Iterable<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return Correspondence$impl._definesEvery(this, keys);
    }

    @Ignore
    public static boolean definesEvery(java.lang.String value, Iterable<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        // TODO We're still boxing here!
        return Correspondence$impl._definesEvery(instance(value), keys);
    }

    @Override
    @Ignore
    public boolean definesEvery() {
        return Correspondence$impl._definesEvery(this, (Iterable)empty_.getEmpty$());
    }

    @Ignore
    public static boolean definesEvery(java.lang.String value) {
        // TODO We're still boxing here!
        return Correspondence$impl._definesEvery(instance(value), (Iterable)empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Iterable<? extends Integer> definesEvery$keys() {
        return (Iterable)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean definesAny(@Name("keys")
    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<ceylon.language::Integer>")
    Iterable<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return Correspondence$impl._definesAny(this, keys);
    }

    @Ignore
    public static boolean definesAny(java.lang.String value, Iterable<? extends Integer> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        // TODO We're still boxing here!
        return Correspondence$impl._definesAny(instance(value), keys);
    }

    @Override
    @Ignore
    public boolean definesAny() {
        return Correspondence$impl._definesAny(this, (Iterable)empty_.getEmpty$());
    }

    @Ignore
    public static boolean definesAny(java.lang.String value) {
        // TODO We're still boxing here!
        return Correspondence$impl._definesAny(instance(value), (Iterable)empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Iterable<? extends Integer> definesAny$keys() {
        return (Iterable)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public List<? extends Character> items(@Name("keys")
    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<ceylon.language::Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl._items(this, keys);
    }

    @Ignore
    public static List<? extends Character> items(java.lang.String value, Iterable<? extends Integer> keys) {
        // TODO We're still boxing here!
        return Correspondence$impl._items(instance(value), keys);
    }

    @Override
    @Ignore
    public List<? extends Character> items() {
        return Correspondence$impl._items(this, (Iterable)empty_.getEmpty$());
    }

    @Ignore
    public static List<? extends Character> items(java.lang.String value) {
        // TODO We're still boxing here!
        return Correspondence$impl._items(instance(value), (Iterable)empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Iterable<? extends Integer> items$keys() {
        return (Iterable)empty_.getEmpty$();
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
                    result = exhausted_.getExhausted$();
                }
                return result;
            }
        }

        return new StringIterator();
    }

    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<ceylon.language::Character>")
    public List<? extends Character> getCharacters() {
        return getCharacters(value);
    }

    @Ignore
    public static List<? extends Character> getCharacters(java.lang.String value) {
        int length = value.length();
        if (length == 0) {
            return (List)empty_.getEmpty$();
        }
        Character[] chars = new Character[(int)getSize(value)];
        for (int offset = 0, i = 0; offset < length; i++) {
            int codePoint = value.codePointAt(offset);
            chars[i] = Character.instance(codePoint);
            offset += java.lang.Character.charCount(codePoint);
        }
        return new ArraySequence<Character>(chars);
    }

    @TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
    public Integer firstOccurrence(@Name("substring") java.lang.String substring) {
        int index = value.indexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @Ignore
    public static Integer firstOccurrence(java.lang.String value, java.lang.String substring) {
        int index = value.indexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
    public Integer lastOccurrence(@Name("substring") java.lang.String substring) {
        int index = value.lastIndexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @Ignore
    public static Integer lastOccurrence(java.lang.String value, java.lang.String substring) {
        int index = value.lastIndexOf(substring);
        return (index >= 0) ? Integer.instance(value.codePointCount(0, index)) : null;
    }

    @TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
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

    @TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
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
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Object>")
    Iterable<?> elements) {
        return Category$impl._containsAny(this, elements);
    }

    @Ignore
    public static boolean containsAny(java.lang.String value, Iterable<?> elements) {
        // TODO We're still boxing here!
        return Category$impl._containsAny(instance(value), elements);
    }

    @Override
    @Ignore
    public boolean containsAny() {
        return Category$impl._containsAny(this, empty_.getEmpty$());
    }

    @Ignore
    public static boolean containsAny(java.lang.String value) {
        // TODO We're still boxing here!
        return Category$impl._containsAny(instance(value), empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Iterable<?> containsAny$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean containsEvery(@Sequenced @Name("elements")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Object>")
    Iterable<?> elements) {
        return Category$impl._containsEvery(this, elements);
    }

    @Ignore
    public static boolean containsEvery(java.lang.String value, Iterable<?> elements) {
        // TODO We're still boxing here!
        return Category$impl._containsEvery(instance(value), elements);
    }

    @Override
    @Ignore
    public boolean containsEvery() {
        return Category$impl._containsEvery(this, empty_.getEmpty$());
    }

    @Ignore
    public static boolean containsEvery(java.lang.String value) {
        // TODO We're still boxing here!
        return Category$impl._containsEvery(instance(value), empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Iterable<?> containsEvery$elements() {
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
    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    Iterable<? extends String> strings) {
        return join(value, strings);
    }

    @Ignore
    public static java.lang.String join(java.lang.String value, Iterable<? extends String> strings) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        Iterator<? extends String> it = strings.getIterator();
        java.lang.Object elem = it.next();
        if (elem != exhausted_.getExhausted$()) {
            result.append(elem);
            for (;!((elem = it.next()) instanceof Finished);) {
                result.append(value).append(elem);
            }
        }
        return result.toString();
    }

    @Ignore
    public java.lang.String join() {
        return join((Iterable)empty_.getEmpty$());
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
            @Name("to") @TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
            final Integer to) {
        return instance(span(value, from.longValue(), to));
    }

    @Ignore
    public static java.lang.String span(java.lang.String value, long from, final Integer to) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        long toIndex = to == null ? MAX_VALUE : to.longValue();
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
            return Iterable$impl._getSequence(instance(value));
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
            return Iterable$impl._getSequence(instance(value));
        }
    }

    @Ignore
    public static Character find(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return null;
        } else {
            return Iterable$impl._find(instance(value), f);
        }
    }

    @Ignore
    public static Character findLast(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return null;
        } else {
            return List$impl._findLast(instance(value), f);
        }
    }

    @Ignore
    public static Iterable<? extends Character> sort(java.lang.String value, Callable<? extends Comparison> f) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return instance(string_.string(Iterable$impl._sort(instance(value), f)));
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
    public <Result> List<? extends Result> collect(Callable<? extends Result> f) {
        return new MapIterable<Character, Result>(this, f).getSequence();
    }
    @Ignore
    public static <Result> List<? extends Result> collect(java.lang.String value, Callable<? extends Result> f) {
        return new MapIterable<Character, Result>(instance(value), f).getSequence();
    }

    @Ignore
    public static List<? extends Character> filter(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return instance(value);
        } else {
            return String.instance(string_.string(new FilterIterable<Character>(instance(value), f)));
        }
    }

    @Override @Ignore
    public List<? extends Character> select(Callable<? extends Boolean> f) {
        return filter(value, f);
    }
    @Ignore
    public static List<? extends Character> select(java.lang.String value, Callable<? extends Boolean> f) {
        return filter(value, f);
    }

    @Ignore
    public static <Result> Result fold(java.lang.String value, Result ini, Callable<? extends Result> f) {
        if (value.isEmpty()) {
            return ini;
        } else {
            return Iterable$impl._fold(instance(value), ini, f);
        }
    }

    @Ignore
    public static boolean any(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return false;
        } else {
            return Iterable$impl._any(instance(value), f);
        }
    }

    @Ignore
    public static boolean every(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return false;
        } else {
            return Iterable$impl._every(instance(value), f);
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
            return instance(string_.string(Iterable$impl._by(instance(value), step)));
        }
    }

    @Ignore
    public static long count(java.lang.String value, Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return 0;
        } else {
            return Iterable$impl._count(instance(value), f);
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
            return Iterable$impl._getIndexed(instance(value));
        }
    }

    @Ignore
    public static <Other>Iterable chain(java.lang.String value, Iterable<? extends Other> other) {
        if (value.isEmpty()) {
            return other;
        } else {
            return Iterable$impl._chain(instance(value), other);
        }
    }
    @Ignore
    public static <Key> Map<? extends Key, ? extends Sequence<? extends Character>> group(java.lang.String value, Callable<? extends Key> grouping) {
        if (value.isEmpty()) {
            return new InternalMap<Key, Sequence<? extends Character>>(java.util.Collections.<Key,Sequence<Character>>emptyMap());
        } else {
            return Iterable$impl._group(instance(value), grouping);
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
            return List$impl._withLeading(instance(value), e);
        }
    }

    @Ignore @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <Other>Sequence withTrailing(java.lang.String value, Other e) {
        if (value.isEmpty()) {
            return new ArraySequence(e);
        } else {
            return List$impl._withTrailing(instance(value), e);
        }
    }

    private static final class Tokens implements Iterable<String> {
        private final java.lang.String str;
        private final java.lang.Object separator;
        private final boolean keepSeparators;
        private final boolean groupSeparators;

        public Tokens(java.lang.String str, java.lang.Object separator,
                boolean keepSeparators, boolean groupSeparators) {
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
                        return exhausted_.getExhausted$();
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
        public boolean getEmpty() {
            return getIterator().next() == exhausted_.getExhausted$();
        }

        @Override
        @Ignore
        public String getFirst() {
        	return Iterable$impl._getFirst(this);
        }
        @Override @Ignore
        public String getLast() {
            return Iterable$impl._getLast(this);
        }

        @Override
        @Ignore
        public Iterable<? extends String> getRest() {
        	return Iterable$impl._getRest(this);
        }

        @Override
        @Ignore
        public List<? extends String> getSequence() {
            return Iterable$impl._getSequence(this);
        }
        @Override @Ignore
        public String find(Callable<? extends Boolean> f) {
            return Iterable$impl._find(this, f);
        }
        @Override @Ignore
        public String findLast(Callable<? extends Boolean> f) {
            return Iterable$impl._findLast(this, f);
        }
        @Override
        @Ignore
        public List<? extends String> sort(Callable<? extends Comparison> f) {
            return Iterable$impl._sort(this, f);
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
        public <Result> List<? extends Result> collect(Callable<? extends Result> f) {
            return new MapIterable<String, Result>(this, f).getSequence();
        }
        @Override
        @Ignore
        public List<? extends String> select(Callable<? extends Boolean> f) {
            return new FilterIterable<String>(this, f).getSequence();
        }
        @Override
        @Ignore
        public <Result> Result fold(Result ini, Callable<? extends Result> f) {
            return Iterable$impl._fold(this, ini, f);
        }
        @Override @Ignore
        public boolean any(Callable<? extends Boolean> f) {
            return Iterable$impl._any(this, f);
        }
        @Override @Ignore
        public boolean every(Callable<? extends Boolean> f) {
            return Iterable$impl._every(this, f);
        }
        @Override @Ignore
        public Iterable<? extends String> skipping(long n) {
            return Iterable$impl._skipping(this, n);
        }
        @Override @Ignore
        public Iterable<? extends String> taking(long n) {
            return Iterable$impl._taking(this, n);
        }
        @Override @Ignore
        public Iterable<? extends String> by(long n) {
            return Iterable$impl._by(this, n);
        }
        @Override @Ignore
        public long count(Callable<? extends Boolean> f) {
            return Iterable$impl._count(this, f);
        }
        @Override @Ignore
        public Iterable<? extends String> getCoalesced() {
            return Iterable$impl._getCoalesced(this);
        }
        @Override @Ignore
        public Iterable<? extends Entry<? extends Integer, ? extends String>> getIndexed() {
            return Iterable$impl._getIndexed(this);
        }
        @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
            return Iterable$impl._chain(this, other);
        }
        @Override @Ignore
        public <Key> Map<? extends Key, ? extends Sequence<? extends String>> group(Callable<? extends Key> grouping) {
            return Iterable$impl._group(this, grouping);
        }
    }

    private static final class Occurs implements Iterable<Integer> {
        private final java.lang.String str;
        private final java.lang.String oc;

        public Occurs(java.lang.String str, java.lang.String oc) {
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
                    return exhausted_.getExhausted$();
                }
            }

            return new OccurrenceIterator();
        }

        @Override
        public boolean getEmpty() {
            return getIterator().next() == exhausted_.getExhausted$();
        }

        @Override
        @Ignore
        public Integer getFirst() {
        	return Iterable$impl._getFirst(this);
        }
        @Override @Ignore
        public Integer getLast() {
            return Iterable$impl._getLast(this);
        }

        @Override
        @Ignore
        public Iterable<? extends Integer> getRest() {
        	return Iterable$impl._getRest(this);
        }

        @Override
        @Ignore
        public List<? extends Integer> getSequence() {
            return Iterable$impl._getSequence(this);
        }
        @Override @Ignore
        public Integer find(Callable<? extends Boolean> f) {
            return Iterable$impl._find(this, f);
        }
        @Override @Ignore
        public Integer findLast(Callable<? extends Boolean> f) {
            return Iterable$impl._findLast(this, f);
        }
        @Override
        @Ignore
        public List<? extends Integer> sort(Callable<? extends Comparison> f) {
            return Iterable$impl._sort(this, f);
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
        public <Result> List<? extends Result> collect(Callable<? extends Result> f) {
            return new MapIterable<Integer, Result>(this, f).getSequence();
        }
        @Override @Ignore
        public List<? extends Integer> select(Callable<? extends Boolean> f) {
            return new FilterIterable<Integer>(this, f).getSequence();
        }
        @Override
        @Ignore
        public <Result> Result fold(Result ini, Callable<? extends Result> f) {
            return Iterable$impl._fold(this, ini, f);
        }
        @Override @Ignore
        public boolean any(Callable<? extends Boolean> f) {
            return Iterable$impl._any(this, f);
        }
        @Override @Ignore
        public boolean every(Callable<? extends Boolean> f) {
            return Iterable$impl._every(this, f);
        }
        @Override @Ignore
        public Iterable<? extends Integer> skipping(long n) {
            return Iterable$impl._skipping(this, n);
        }
        @Override @Ignore
        public Iterable<? extends Integer> taking(long n) {
            return Iterable$impl._taking(this, n);
        }
        @Override @Ignore
        public Iterable<? extends Integer> by(long n) {
            return Iterable$impl._by(this, n);
        }
        @Override @Ignore
        public long count(Callable<? extends Boolean> f) {
            return Iterable$impl._count(this, f);
        }
        @Override @Ignore
        public Iterable<? extends Integer> getCoalesced() {
            return Iterable$impl._getCoalesced(this);
        }
        @Override @Ignore
        public Iterable<? extends Entry<? extends Integer, ? extends Integer>> getIndexed() {
            return Iterable$impl._getIndexed(this);
        }
        @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
            return Iterable$impl._chain(this, other);
        }
        @Override @Ignore
        public <Key> Map<? extends Key, ? extends Sequence<? extends Integer>> group(Callable<? extends Key> grouping) {
            return Iterable$impl._group(this, grouping);
        }
    }
}
