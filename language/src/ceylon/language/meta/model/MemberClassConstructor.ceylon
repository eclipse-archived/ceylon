shared sealed interface MemberClassConstructor<in Container=Nothing, out Type=Anything, in Arguments=Nothing> 
        satisfies ConstructorModel<Type,Arguments>  
            & Qualified<Constructor<Type, Arguments>,Container> 
        given Arguments satisfies Anything[] {
    
    //shared actual formal MemberClass<Anything,Type,Nothing> container;// actually always a MemberClass
    
    //shared formal Constructor<Type, Arguments> bind(Object container); 
}