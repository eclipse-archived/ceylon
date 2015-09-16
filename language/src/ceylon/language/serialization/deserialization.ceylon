
"A new [[DeserializationContext]]."
shared DeserializationContext<Id> deserialization<Id>() given Id satisfies Object 
        => DeserializationContextImpl<Id>();
