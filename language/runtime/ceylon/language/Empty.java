package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;

@SatisfiedTypes({
    "ceylon.language.Correspondence<ceylon.language.Natural,ceylon.language.Bottom>",
    "ceylon.language.Iterable<ceylon.language.Bottom>",
    "ceylon.language.Sized"
})
public interface Empty extends Correspondence<Natural, java.lang.Object>, 
    Iterable , Sized{
    public Natural getSize(); 
    public boolean getEmpty();
    public Iterator getIterator();
    public java.lang.Object item(Natural key);
    public java.lang.Object getFirst();
}
