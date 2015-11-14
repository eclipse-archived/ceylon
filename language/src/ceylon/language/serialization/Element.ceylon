"""
   An [[Array]] instance referring to another instance via one 
   of its elements.
 
   For example, given:
 
       value arr = Array({"hello"});
       value context = serialization();
       value refs = context.references(arr);
       assert(is Element elementRef = refs.find((element) => element is Element));
       assert(elementRef.referred(arr) == "hello");
       assert(elementRef.index == 0);
"""
shared sealed interface Element /*<Instance>*/
        satisfies ReachableReference /*<Instance>*/{
    "The index of the element in the Array which makes the reference."
    shared formal Integer index;
}
