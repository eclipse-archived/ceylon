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

/*
 /**
 * Implementation of ceylon.language.serialization.References
 * 
 * This has to be implemented in Java because it needs to call
 * {@link Serializable#$references$()},
 * whose name is not permitted in Ceylon, 
 * but must be illegal so it cannot collide with a user class member 
 */
 public class ReferencesImpl extends BaseIterable<Entry<? extends ReachableReference, ? extends Object>, java.lang.Object> implements References {
 
    private final Object instance;
 
    ReferencesImpl(Serializable instance) {
        super(TypeDescriptor.klass(Entry.class, 
                    ReachableReference.$TypeDescriptor$, Anything.$TypeDescriptor$), 
                Null.$TypeDescriptor$);
        this.instance = instance;
    }
    
    @Override
    public Iterator<? extends Entry<? extends ReachableReference, ? extends Object>> iterator() {
        return new BaseIterator<Entry<? extends ReachableReference, ? extends Object>>(TypeDescriptor.klass(Entry.class, ReachableReference.$TypeDescriptor$, Anything.$TypeDescriptor$)) {
            Iterator<? extends ReachableReference> it = getReferences().iterator();
            @Override
            public Object next() {
                java.lang.Object next = it.next();
                if (next == finished_.get_()) {
                    return finished_.get_();
                } else {
                    ReachableReference ref = (ReachableReference)next;
                    return new Entry<ReachableReference, Object>(
                            ((ReifiedType)ref).$getType$(), Anything.$TypeDescriptor$, 
                            ref, ref.referred(instance));
                }
            }
            
        };
    }
 
    @Override
    public Object getInstance() {
        return instance;
    }
 
    @Override
    public Iterable<? extends ReachableReference, ? extends Object> getReferences() {
        return new BaseIterable<ReachableReference, Object>(ReachableReference.$TypeDescriptor$, Null.$TypeDescriptor$) {
 
            @Override
            public Iterator<? extends ReachableReference> iterator() {
                return reach_.get().references(instance);
            }
        };
    }
 }
 */