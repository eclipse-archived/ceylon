package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@SatisfiedTypes({
    "ceylon.language.Correspondence<ceylon.language.Natural,ceylon.language.Bottom>",
    "ceylon.language.Iterable<ceylon.language.Bottom>",
    "ceylon.language.Sized"
})
public interface Empty 
        extends Correspondence<Natural, java.lang.Object>, Iterable, Sized {
    @TypeInfo("ceylon.language.Natural")
    public long getSize(); 
    public boolean getEmpty();
    public Iterator getIterator();
    public java.lang.Object item(Natural key);
    public java.lang.Object getFirst();
}
