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
        return Iterable$impl._getFirst(this);
    }
    
    @Override 
    @Ignore
    public Iterable<? extends Character> getSequence() { 
        return Iterable$impl._getSequence(this); 
    }
    @Override @Ignore
    public Character find(Callable<? extends Boolean> f) {
        return Iterable$impl._find(this, f);
    }
    @Override @Ignore
    public Character findLast(Callable<? extends Boolean> f) {
        return List$impl._findLast(this, f);
    }
    @Override 
    @Ignore
    public Iterable<? extends Character> sorted(Callable<? extends Comparison> f) { 
        return String.instance($string.string(Iterable$impl._sorted(this, f))); 
    }
    @Override 
    @Ignore
    public <Result> Iterable<Result> map(Callable<? extends Result> f) { 
        return new MapIterable<Character, Result>(this, f); 
    }
    @Override 
    @Ignore
    public Iterable<? extends Character> filter(Callable<? extends Boolean> f) { 
        return String.instance($string.string(new FilterIterable<Character>(this, f)));
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
    public Iterable<? extends Character> skipping(long skip) {
        return this.segment(Integer.instance(skip), this.getSize());
    }
    @Override @Ignore
    public Iterable<? extends Character> taking(long take) {
        return this.segment(Integer.instance(0), take);
    }
    @Override @Ignore
    public Iterable<? extends Character> by(long step) {
        return String.instance($string.string(Iterable$impl._by(this, step)));
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return Iterable$impl._count(this, f);
    }
}
