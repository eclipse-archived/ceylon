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
    @Override public Character find(Callable<? extends Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <Result> Iterable<Result> map(Callable<? extends Result> f) { return new MapIterable<Character, Result>(this, f); }
    @Override public Iterable<? extends Character> filter(Callable<? extends Boolean> f) { return new FilterIterable<Character>(this, f); }
    @Override public <Result> Result fold(Result ini, Callable<? extends Result> f) { return Iterable$impl._fold(this, ini, f); }
}
