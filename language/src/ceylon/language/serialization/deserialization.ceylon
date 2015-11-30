import ceylon.language.meta.model{
    ClassModel
}

"A new [[DeserializationContext]]."
shared DeserializationContext<Id> deserialization<Id>(Boolean whitelisted(ClassModel<> clazz) => true) 
        given Id satisfies Object 
    => DeserializationContextImpl<Id>(whitelisted);
