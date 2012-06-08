package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
class StringOfSome extends String implements Some<Character> {
    
    StringOfSome(java.lang.String s) { 
        super(s); 
    }
        
    @Override
    public boolean getEmpty() {
        return false;
    }

    @Override
    public FixedSized<? extends Character> getRest() {
        return span(Integer.instance(1), null);
    }
    
    @Override
    public Character getFirst() {
        return FixedSized$impl._getFirst(this);
    }
    
    @Override public Iterable<? extends Character> getSequence() { return Iterable$impl._getSequence(this); }
    @Override public Character find(Callable<? extends Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <Result> Iterable<Result> map(Callable<? extends Result> f) { return new MapIterable<Character, Result>(this, f); }
    @Override public Iterable<? extends Character> filter(Callable<? extends Boolean> f) { return new FilterIterable<Character>(this, f); }
    @Override public <Result> Result fold(Result ini, Callable<? extends Result> f) { return Iterable$impl._fold(this, ini, f); }
}
