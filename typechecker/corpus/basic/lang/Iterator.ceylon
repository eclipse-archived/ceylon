public interface Iterator<out X> {

    doc "The status of the iterator. |true|
         indicates that the iterator contains
         more elements."
    public Boolean more;
    
    doc "The current element."
    public X current;

    doc "Advance to the next element, returning
         the next element."
    throw #ExhaustedIteratorException
          "if the iterator contains no
           more elements."
    public X next();
    
}