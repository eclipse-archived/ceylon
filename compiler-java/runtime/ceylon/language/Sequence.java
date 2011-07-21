package ceylon.language;

public interface Sequence<Element> 
    extends Correspondence<Natural, Element>, Iterable<Element>, 
        Sized, Cloneable<Sequence<Element>> {
    
    public Natural getLastIndex();
    
    public Element getFirst();
    
    public Sequence<Element> getRest();
    
    public Boolean isEmpty();
        // return false;
    
    public Natural getSize();
        //return lastIndex+1;
    
    public Element getLast();
    /*{
        if (exists Element x = value(lastIndex)) {
            return x;
        }
        else {
            return first; //actually never occurs
        } 
    }*/
    
    public Boolean defines(Natural index);
//        return index<=lastIndex;
    
    //this depends on efficient implementation of rest
    /*
    shared actual default object iterator 
            extends Object()
            satisfies Iterator<Element> {
        shared actual Element head { 
            return first;
        }
        shared actual Iterator<Element> tail {
            return rest.iterator;
        }
    }
    */
    
    public Iterator<Element> iterator();
        //return SequenceIterator(0);
    
/*    class SequenceIterator(Natural from)
            extends Object()
            satisfies Iterator<Element> {
        shared actual Element? head { 
            return value(from);
        }
        shared actual Iterator<Element> tail {
            return SequenceIterator(from+1);
        }
    }
*/
}
