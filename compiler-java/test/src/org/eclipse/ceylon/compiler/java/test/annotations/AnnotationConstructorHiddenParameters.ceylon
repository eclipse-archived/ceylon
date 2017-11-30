import ceylon.language.meta.declaration{FunctionDeclaration}

@nomodel
shared final annotation class AnnotationConstructorHiddenParameters(String path) 
        satisfies OptionalAnnotation<AnnotationConstructorHiddenParameters, FunctionDeclaration>{}

@nomodel
"Annotates a method to define an HTTP endpoint."
shared annotation AnnotationConstructorHiddenParameters endpoint(path="")
{
    "The path under which the endpoint is served.
     See [[de.dlkw.conjurup.core::Server.addEndpoint]] and
     [[de.dlkw.conjurup.core::EndpointScanner]]."
    String path;
    return AnnotationConstructorHiddenParameters(path);
}