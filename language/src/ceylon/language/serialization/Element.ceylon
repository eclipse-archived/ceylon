"An [[Array]] instance referring to another instance via one 
 of its elements."
shared sealed interface Element /*<Instance>*/
        satisfies ReachableReference /*<Instance>*/{
    "The index of the element in the Array which makes the reference."
    shared formal Integer index;
}
