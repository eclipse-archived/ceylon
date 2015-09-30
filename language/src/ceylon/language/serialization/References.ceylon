import ceylon.language.impl{
    BaseIterator, 
    BaseIterable,
    reach
}

"Exposes the instances directly reachable from a given instance."
shared sealed interface References/*<Instance>*/
    // could be generic 
        satisfies {<ReachableReference/*<Instance>*/->Anything>*} {
    
    "The instance"
    shared formal Anything/*<Instance>*/ instance;
    
    "The references that are reachable from the [[instance]]."
    shared formal Iterable<ReachableReference/*<Instance>*/> references;
}

class ReferencesImpl(instance) extends BaseIterable<ReachableReference->Anything, Null>() 
        satisfies References&Identifiable {
    shared actual Anything instance;
    
    shared actual Iterator<ReachableReference->Anything> iterator() {
        if (exists instance) {
            return object extends BaseIterator<ReachableReference->Anything>() 
                    satisfies Identifiable {
                Iterator<ReachableReference> it = outer.references.iterator();
                
                shared actual <ReachableReference->Anything>|Finished next() {
                    ReachableReference|Finished next = it.next();
                    if (is Finished next) {
                        return finished;
                    } else {
                        return next->next.referred(instance);
                    }
                }
            };
        } else {
            return emptyIterator;
        }
    }
    
    shared actual Iterable<ReachableReference/*<Instance>*/> references {
        return object extends BaseIterable<ReachableReference, Null>() 
                satisfies Identifiable {
            shared actual Iterator<ReachableReference> iterator() {
                return reach.references(instance);
            }
        };
    }
}
