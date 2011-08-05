package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;

@SatisfiedTypes({
    "ceylon.language.Correspondence<ceylon.language.Natural,ceylon.language.Bottom>",
    "ceylon.language.Iterable<ceylon.language.Bottom>",
    "ceylon.language.Sized"
})
public interface Empty extends Correspondence, 
    Iterable , Sized{
    public Natural getSize(); 
        //return 0; 
    public boolean getEmpty();
        //return true; 
    public Iterator iterator();
        //return emptyIterator;
    public java.lang.Object value(Equality key);
        //return null;
    public java.lang.Object getFirst();
        //return null;

}

/*
object emptyIterator satisfies Iterator<Bottom> {
    
    shared actual Nothing head { 
        return null; 
    }
    shared actual Iterator<Bottom> tail { 
        return this; 
    }
    
}

*/