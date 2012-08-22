package ceylon.language;

import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
@Ceylon(major = 3)
class StringOfNone extends String implements None<Character> {

    static StringOfNone instance = new StringOfNone();

    private StringOfNone() {
        super("");
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public boolean getEmpty() {
        return true;
    }

    @Override
    public Iterable<? extends Character> getRest() {
        return this;
    }

    @Override
    public Character getFirst() {
        return null;
    }
    @Override public Character getLast() {
        return null;
    }

    @Override
    @Ignore
    public Iterable<? extends Character> getSequence() {
        return this;
    }
    @Override @Ignore
    public Character find(Callable<? extends Boolean> f) {
        return null;
    }
    @Override @Ignore
    public Character findLast(Callable<? extends Boolean> f) {
        return null;
    }
    @Override
    @Ignore
    public Iterable<? extends Character> sort(Callable<? extends Comparison> f) {
        return this;
    }
    @Override
    @Ignore
    public Iterable<? extends Character> filter(Callable<? extends Boolean> f) {
        return this;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override @Ignore
    public <Result> Iterable<? extends Result> collect(Callable<? extends Result> f) {
        return (Iterable)empty_.getEmpty();
    }
    @Override @Ignore
    public Iterable<? extends Character> select(Callable<? extends Boolean> f) {
        return this;
    }
    @Override
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) {
        return ini;
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return false;
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return false;
    }
    @Override @Ignore
    public Iterable<? extends Character> skipping(long skip) {
        return this;
    }
    @Override @Ignore
    public Iterable<? extends Character> taking(long take) {
        return this;
    }
    @Override @Ignore
    public Iterable<? extends Character> by(long step) {
        return this;
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return 0;
    }
    @Override @Ignore
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterable<? extends Entry<? extends Integer, ? extends Character>> getIndexed() { return (Iterable)this; }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) { return other; }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Character>> group(Callable<? extends Key> grouping) {
        return new InternalMap<Key, Sequence<? extends Character>>(java.util.Collections.<Key,Sequence<Character>>emptyMap());
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Sequence withLeading(Other e) {
        return List$impl._withLeading(this, e);
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Sequence withTrailing(Other e) {
        return List$impl._withTrailing(this, e);
    }
}
