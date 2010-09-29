// import java.util.*;
import java.util.List;
import java.util.Map;
import java.lang.Comparable;

Void foo<X> (X x) 
   given X satisfies List
{
}
    
class P() {
void foo<X> (X x) 
   given X satisfies List
{
}

}

/*   class TypeWithParameter<X, Y>(X init)
       given X satisfies List
       given X satisfies Comparable
   {
        mutable X x := init;
        X process(X input) { return input }
   }

class Generics () {
    Integer? x() { return null; }
    Float? xx() { return null; }
}

*/