shared abstract class Comparable2<in Other>() 
        given Other satisfies Comparable2<Other>
                    abstracts Comparable2<Other> { //self-referential lower bound 
    
    shared formal Integer compare(Other that);
    
    shared Integer reverseCompare(Other that) { 
        return that.compare(this); //this is Other
    }
    
}