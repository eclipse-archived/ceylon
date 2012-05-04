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
        return instance(value.substring(1));
    }
    
    @Override
    public Character getFirst() {
        return FixedSized$impl._getFirst(this);
    }
    
}
