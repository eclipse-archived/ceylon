package ceylon.language;


public final class String extends Object 
implements Comparable<String>, Iterable<Character>, 
           Correspondence<Natural,Character>, Format,
           Sized, Summable<String>, Castable<String> {
    public final java.lang.String value;

    private String(java.lang.String s) {
        value = s;
    }

    public java.lang.String toJavaString() {
        return value;
    }

    public static ceylon.language.String instance(java.lang.String s) {
        return new ceylon.language.String(s);
    }

    public java.lang.String uppercase() {
        return value.toUpperCase();
    }

    public java.lang.String lowercase() {
        return value.toLowerCase();
    }

	@Override
    public boolean equals(java.lang.Object that) {
		if (that instanceof String) {
			String s = (String)that;
			return value.equals(s.value);
		} else {
			return false;
		}
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
    public Comparison compare(String other) {
        int c = value.compareTo(other.value);
        return (c < 0) ? Comparison.SMALLER :
            ((c == 0) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Override
    public java.lang.String getFormatted() {
        return value;
    }

    @Override
    public boolean largerThan(String other) {
        return value.length() > other.value.length();
    }

    @Override
    public boolean smallerThan(String other) {
        return value.length() < other.value.length();
    }

    @Override
    public boolean asLargeAs(String other) {
        return value.length() >= other.value.length();
    }

    @Override
    public boolean asSmallAs(String other) {
        return value.length() <= other.value.length();
    }

    @Override
    public <CastValue extends String> CastValue as() {
        return (CastValue)this;
    }

    @Override
    public String plus(String number) {
        return instance(value + number.value);
    }

    @Override
    public Natural getSize() {
        return Natural.instance(value.length());
    }

    @Override
    public boolean getEmpty() {
        return value.isEmpty();
    }

    @Override
    public Character item(Natural key) {
        return Character.instance(value.charAt(key.intValue()));
    }

    @Override
    public boolean defines(Natural key) {
        return key.intValue() >= 0 && key.intValue() < value.length();
    }

    @Override
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(Iterable<? extends Natural> keys) {
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(Iterable<? extends Natural> keys) {
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    public Sequence<? extends Character> items(Iterable<? extends Natural> keys) {
        return Correspondence$impl.items(this, keys);
    }

    class StringIterator implements Iterator<Character>{

        private int index;

        StringIterator(int index){
            this.index = index;
        }
        
        @Override
        public Character getHead() {
            return item(Natural.instance(index));
        }

        @Override
        public Iterator<Character> getTail() {
            return new StringIterator(index+1);
        }
        
    }
    
    @Override
    public Iterator<Character> getIterator() {
        return new StringIterator(0);
    }

    @Override
    public Character getFirst() {
        return Iterable$impl.getFirst(this);
    }

}
