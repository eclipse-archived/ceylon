package ceylon.language;

public interface Empty extends Correspondence<Natural, Nothing>, 
    Iterable<Nothing> , Sized{
    public Natural getSize(); 
        //return 0; 
    public Boolean getEmpty();
        //return true; 
    public Iterator<Nothing> iterator();
        //return emptyIterator;
    public Nothing value(Natural key);
        //return null;
    public Nothing getFirst();
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