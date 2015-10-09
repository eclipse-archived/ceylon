import ceylon.language.meta.declaration {
    ReferenceDeclaration
}

"An instance referring to another instance via a reference attribute."
shared sealed interface Member // or Reference
        satisfies ReachableReference {
    "The attribute making the reference."
    shared formal ReferenceDeclaration attribute;
    
    "The [[referred]] instance reachable from the given [[instance]].
     
     Note: If this member refers to a `late` declaration and the 
     attribute of the given instance has not been initialized this 
     method will return [[uninitializedLateValue]]."
    shared actual formal Anything referred(Object/*<Instance>*/ instance);
    
}

"The type of [[uninitializedLateValue]]."
shared abstract class UninitializedLateValue() of uninitializedLateValue {}

"A singleton used to indicate that a `late` [[Member]] of a particular 
 instance has not been initialized.
 
 For example, given
 
     class Example() {
         shared late Example parent;
     }
     
 Then
     
     value ex = Example();// uninitialized parent
     value context = serialization();
     value refs = context.references(ex);
     assert(is Member parentRef = refs.find((element) => element is Member));
     assert(parentRef.referred(ex) == uninitializedLateValue);
     
 Thus, *if a serialization library supports it*, it is 
 possible to serialize uninitialized `late` values.
"
shared object uninitializedLateValue extends UninitializedLateValue() {}

