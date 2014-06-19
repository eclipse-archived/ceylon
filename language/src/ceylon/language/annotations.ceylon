import ceylon.language.meta.declaration {
    Module,
    Package,
    Import,
    ClassDeclaration,
    ClassOrInterfaceDeclaration,
    FunctionDeclaration,
    Declaration,
    ValueDeclaration,
    FunctionOrValueDeclaration
} 

"The annotation class for [[annotation]]."
shared final annotation class AnnotationAnnotation()
        satisfies OptionalAnnotation<AnnotationAnnotation, 
            ClassDeclaration|FunctionDeclaration> {}

"Annotation to mark a class as an *annotation class*, or a 
 top-level function as an *annotation constructor*."
see(`interface Annotation`)
shared annotation AnnotationAnnotation annotation() 
        => AnnotationAnnotation(); 

"The annotation class for [[shared]]."
shared final annotation class SharedAnnotation()
        satisfies OptionalAnnotation<SharedAnnotation, 
            FunctionOrValueDeclaration|ClassOrInterfaceDeclaration|
                    Package|Import> {}

"Annotation to mark a type or member as shared. A `shared` 
 member is visible outside the block of code in which it is 
 declared."
shared annotation SharedAnnotation shared() 
        => SharedAnnotation();

"The annotation class for [[variable]]."
shared final annotation class VariableAnnotation()
        satisfies OptionalAnnotation<VariableAnnotation, 
            ValueDeclaration> {}

"Annotation to mark an value as variable. A `variable` value 
 may be assigned multiple times."
shared annotation VariableAnnotation variable() 
        => VariableAnnotation();

"The annotation class for [[abstract]]."
shared final annotation class AbstractAnnotation()
        satisfies OptionalAnnotation<AbstractAnnotation, 
            ClassDeclaration> {}

"Annotation to mark a class as abstract. An `abstract` class 
 may not be directly instantiated. An `abstract` class may 
 have enumerated cases."
shared annotation AbstractAnnotation abstract() 
        => AbstractAnnotation();

"The annotation class for [[final]]."
shared final annotation class FinalAnnotation()
        satisfies OptionalAnnotation<FinalAnnotation, 
            ClassDeclaration> {}

"Annotation to mark a class or interface as sealed. A 
 `sealed` interface may not be satisfies outside of the 
 module in which it is defined. A `sealed` class may not be
 extended or instantiated outside of the module in which it
 is defined."
shared annotation SealedAnnotation sealed() 
        => SealedAnnotation();

"The annotation class for [[sealed]]."
shared final annotation class SealedAnnotation()
        satisfies OptionalAnnotation<SealedAnnotation, 
            ClassOrInterfaceDeclaration> {}

"Annotation to mark a class as final. A `final` class may 
 not be extended. Marking a class as final affects disjoint
 type analysis."
shared annotation FinalAnnotation final() 
        => FinalAnnotation();

"The annotation class for [[actual]]."
shared final annotation class ActualAnnotation()
        satisfies OptionalAnnotation<ActualAnnotation, 
            FunctionOrValueDeclaration|ClassOrInterfaceDeclaration> {}

"Annotation to mark a member of a type as refining a member 
 of a supertype."
shared annotation ActualAnnotation actual() 
        => ActualAnnotation();

"The annotation class for [[formal]]."
shared final annotation class FormalAnnotation()
        satisfies OptionalAnnotation<FormalAnnotation, 
            FunctionOrValueDeclaration|ClassOrInterfaceDeclaration> {}

"Annotation to mark a member whose implementation must be 
 provided by subtypes."  
shared annotation FormalAnnotation formal() 
        => FormalAnnotation();

"The annotation class for [[default]]."
shared final annotation class DefaultAnnotation()
        satisfies OptionalAnnotation<DefaultAnnotation, 
            FunctionOrValueDeclaration|ClassOrInterfaceDeclaration> {}

"Annotation to mark a member whose implementation may be 
 refined by subtypes. Non-`default` declarations may not be 
 refined."
shared annotation DefaultAnnotation default() 
        => DefaultAnnotation();

"The annotation class for [[late]]."
shared final annotation class LateAnnotation()
        satisfies OptionalAnnotation<LateAnnotation, 
            ValueDeclaration> {}

"Annotation to disable definite initialization analysis for 
 a reference."  
shared annotation LateAnnotation late() 
        => LateAnnotation();

"The annotation class for [[native]]."
shared final annotation class NativeAnnotation()
        satisfies OptionalAnnotation<NativeAnnotation, Annotated> {}

"Annotation to mark a member whose implementation is defined 
 in platform-native code."  
shared annotation NativeAnnotation native() 
        => NativeAnnotation();

/*"The annotation class for [[inherited]]."
shared final annotation class InheritedAnnotation()
        satisfies OptionalAnnotation<InheritedAnnotation, 
            ClassDeclaration> {}

"Annotation to mark an annotation class as a *inherited*."
shared annotation InheritedAnnotation inherited() 
        => InheritedAnnotation();*/

"The annotation class for the [[doc]] annotation."
shared final annotation class DocAnnotation(
    "Documentation, in Markdown syntax, about the annotated element"
    shared String description)
        satisfies OptionalAnnotation<DocAnnotation, Annotated> {}

"Annotation to specify API documentation of a program
 element." 
shared annotation DocAnnotation doc(
    "Documentation, in Markdown syntax, about the annotated element"
    String description) => DocAnnotation(description);

"The annotation class for [[see]]."
shared final annotation class SeeAnnotation(
    "The program elements being referred to."
    shared Declaration* programElements)
        satisfies SequencedAnnotation<SeeAnnotation, Annotated> {}

"Annotation to specify API references to other related 
 program elements."
shared annotation SeeAnnotation see(
    "The program elements being referred to."
    Declaration* programElements) 
        => SeeAnnotation(*programElements);

"The annotation class for [[by]]."
shared final annotation class AuthorsAnnotation(
    "The authors, in Markdown syntax, of the annotated element"
    shared String* authors)
        satisfies OptionalAnnotation<AuthorsAnnotation, Annotated> {}

"Annotation to specify API authors."
shared annotation AuthorsAnnotation by(
    "The authors, in Markdown syntax, of the annotated element"
    String* authors) 
        => AuthorsAnnotation(*authors);

"The annotation class for [[throws]]."
shared final annotation class ThrownExceptionAnnotation(
    "The [[Exception]] type that this thrown."
    shared Declaration type,
    "A description, in Markdown syntax, of the circumstances 
     that cause this exception to be thrown." 
    shared String when) 
        satisfies SequencedAnnotation<ThrownExceptionAnnotation, 
            FunctionOrValueDeclaration|ClassDeclaration> {}

"Annotation to mark a program element that throws an 
 exception."
shared annotation ThrownExceptionAnnotation throws(
    "The [[Exception]] type that this thrown."
    Declaration type,
    "A description, in Markdown syntax, of the circumstances 
     that cause this exception to be thrown."
    String when="") 
        => ThrownExceptionAnnotation(type, when);

"The annotation class for [[deprecated]]."
shared final annotation class DeprecationAnnotation(
    "A description, in Markdown syntax, of why the element 
     is deprecated, and of what alternatives are available."
    shared String description)
        satisfies OptionalAnnotation<DeprecationAnnotation, 
            Annotated> {
    "A description, in Markdown syntax, of why the element 
     is deprecated, and what alternatives are available, or 
     null."
    shared String? reason 
            => !description.empty then description;
}

"Annotation to mark program elements which should not be 
 used anymore."
shared annotation DeprecationAnnotation deprecated(
    "A description, in Markdown syntax, of why the element 
     is deprecated, and what alternatives are available."
    String reason="") 
        => DeprecationAnnotation(reason);

"The annotation class for [[tagged]]."
shared final annotation class TagsAnnotation(
    "The tags, in plain text."
    shared String* tags)
        satisfies OptionalAnnotation<TagsAnnotation, Annotated> {}

"Annotation to categorize the API by tag." 
shared annotation TagsAnnotation tagged(
    "The tags, in plain text."
    String* tags) 
        => TagsAnnotation(*tags);

"The annotation class for [[license]]."
shared final annotation class LicenseAnnotation(
    "The name, text, or URL of the license."
    shared String description) 
        satisfies OptionalAnnotation<LicenseAnnotation, Module> {}

"Annotation to specify the URL of the license of a module or 
 package." 
shared annotation LicenseAnnotation license(
    "The name, text, or URL of the license."
    String description) 
        => LicenseAnnotation(description);

"The annotation class for [[optional]]."
shared final annotation class OptionalImportAnnotation() 
        satisfies OptionalAnnotation<OptionalImportAnnotation, 
            Import> {}

"Annotation to specify that a module can be executed even if 
 the annotated dependency is not available."
shared annotation OptionalImportAnnotation optional() 
        => OptionalImportAnnotation();

