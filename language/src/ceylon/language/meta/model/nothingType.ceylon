"The singleton closed type for [[Nothing|ceylon.language::nothing]]."
shared object nothingType satisfies Type<Nothing> {
    
    string => "Nothing";
    
    typeOf(Anything instance) => false;
    
    exactly(Type<Anything> type) => type == nothingType;
    
    supertypeOf(Type<Anything> type) => exactly(type);
    
    subtypeOf(Type<Anything> type) => true;
    
}
