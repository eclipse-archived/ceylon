package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
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
    public Character getFirst() {
        return null;
    }

    @Override public Iterable<? extends Character> getSequence() { return Iterable$impl._getSequence(this); }
    @Override public Character find(Callable<Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <Result> Iterable<Result> map(Callable<Result> f) { return new MapIterable(this, f); }
    @Override public Iterable<? extends Character> filter(Callable<Boolean> f) { return new FilterIterable(this, f); }
    @Override public <Result> Result fold(Result ini, Callable<Result> f) { return Iterable$impl._fold(this, ini, f); }
}
