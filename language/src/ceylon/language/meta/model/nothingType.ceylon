"The singleton closed type for [[Nothing|ceylon.language::nothing]]."
shared object nothingType satisfies Type<Nothing> {
    
    string => "Nothing";
    
    isTypeOf(Anything instance) => false;
    
    isSuperTypeOf(Type<Anything> type) => isExactly(type);
    
    isSubTypeOf(Type<Anything> type) => true;
    
    isExactly(Type<Anything> type) => type == nothingType;
}
