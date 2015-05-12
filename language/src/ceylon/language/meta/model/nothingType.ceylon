"The singleton closed type for [[Nothing|ceylon.language::nothing]]."
shared object nothingType satisfies Type<Nothing> {
    
    string => "Nothing";
    
    typeOf(Anything instance) => false;
    
    exactly(Type<> type) => type == nothingType;
    
    supertypeOf(Type<> type) => exactly(type);
    
    subtypeOf(Type<> type) => true;
    
    shared actual Type<Other> union<Other>(Type<Other> type) => type;
    
    shared actual Type<Nothing> intersection<Other>(Type<Other> type) => this;
}
