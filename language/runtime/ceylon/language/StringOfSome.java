package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
@Ceylon(major = 1)
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
    @Ignore
    public Character getFirst() {
        return FixedSized$impl._getFirst(this);
    }
    
    @Override 
    @Ignore
    public Iterable<? extends Character> getSequence() { 
        return Iterable$impl._getSequence(this); 
    }
    @Override 
    @Ignore
    public Character find(Callable<? extends Boolean> f) { 
        return Iterable$impl._find(this, f); 
    }
    @Override 
    @Ignore
    public Iterable<? extends Character> sorted(Callable<? extends Comparison> f) { 
        return Iterable$impl._sorted(this, f); 
    }
    @Override 
    @Ignore
    public <Result> Iterable<Result> map(Callable<? extends Result> f) { 
        return new MapIterable<Character, Result>(this, f); 
    }
    @Override 
    @Ignore
    public Iterable<? extends Character> filter(Callable<? extends Boolean> f) { 
        return new FilterIterable<Character>(this, f); 
    }
    @Override 
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) { 
        return Iterable$impl._fold(this, ini, f); 
    }
}
