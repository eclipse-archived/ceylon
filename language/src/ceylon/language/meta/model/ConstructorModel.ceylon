shared sealed interface ConstructorModel<out Type=Anything,in Arguments=Nothing>
        satisfies Functional & Declared 
        given Arguments satisfies Anything[] {
    
    shared actual formal ClassModel<Type,Nothing> container;
}