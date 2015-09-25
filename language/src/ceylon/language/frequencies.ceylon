"Produces a [[Map]] mapping elements to frequencies where
 each [[entry|Entry]] maps a distinct element of the given 
 [[stream|elements]] to the number of times the element was
 produced by the stream. Elements are considered distinct 
 if they are not [[equal|Object.equals]].
 
 This is an eager operation, and the resulting map does
 not reflect changes to the given stream."
shared Map<Element,Integer> frequencies<Element>
        ({Element*} elements)
        given Element satisfies Object
        => elements.summarize(identity, 
            (Integer? count, _) 
                    => if (exists count) 
                    then count+1 else 1);