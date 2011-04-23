shared interface Matcher<in X> 
        satisfies Equality {

    doc "Determine if the given value matches
         this case, returning |true| iff the
         value matches."
    shared formal Boolean matches(X value);
    
    shared Boolean matchesCase<Y>(Y? y) 
            given Y abstracts X {
        if (exists y) {
            if (is X y) {
                return matches(y);
            }
            else {
                return false;
            }
        }
        else {
            //match only if this is a case (null)
            return this==null.matcher;
        }
    }
        
}