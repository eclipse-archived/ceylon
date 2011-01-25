doc "The binary join operator |x + y|. The returned 
     sequence does not reflect changes to the original 
     sequences."
shared T[] join<T>(T[]... sequences) 
        given T satisfies Equality<T> {
            
    object joinedSequence satisfies T[] {
        shared actual Natural? lastIndex {
            variable Natural? result := null;
            for (T[] s in sequences) {
                if (exists Natural last = s.lastIndex) {
                    if (exists result) {
                        result += last
                    }
                    else {
                        result := last
                    }
                }
            }
            return result
        }
        shared actual Gettable<T?> value(Natural index) {
            T? value {
                variable Natural i := index;
                for (T[] s in sequences) {
                    if (exists Natural last = s.lastIndex) {
                        if (i<=last) {
                            return s[i]
                        }
                        else {
                            i-=last
                        }
                    }
                }
                return null
                
            }
            return value
        }
    }
    
    return copy(joinedSequence) //take a shallow copy
    
}