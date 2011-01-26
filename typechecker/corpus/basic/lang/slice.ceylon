doc "The ternary range operator |x[from..to]|, along
     with the binary upper range |x[from...]| operator. 
     The returned sequence does not reflect changes 
     to the original sequence."
shared T[] slice<T>(T[] sequence, Natural from, Natural? to=sequence.lastIndex) 
        given T satisfies Equality<T> {
         
    object rangeSequence satisfies T[] {
        shared actual Natural? lastIndex {
            if (exists Natural last = sequence.lastIndex) {
                if (exists to) {
                    if (to<last) {
                        return to-from
                    }
                    else {
                        return last-from
                    }
                }
            }
            else {
                return null
            }
        } 
        shared actual Gettable<T?> value(Natural index) {
            T? value {
                if (exists to) {
                    if (index>to) {
                        return null
                    }
                }
                return sequence[index+from]
            }
            return value
        }
     }

    return copy(rangeSequence) //take a shallow copy
    
}