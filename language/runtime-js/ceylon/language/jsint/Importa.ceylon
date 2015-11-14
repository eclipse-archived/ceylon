import ceylon.language.meta.declaration { Import, Module }
import ceylon.language { AnnotationType = Annotation }

native class Importa(name, version, shared Module _cont, shared Anything _anns) satisfies Import {
    shared actual String name;
    shared actual String version;
    shared actual native Boolean shared;
    shared actual native Boolean optional;
    shared actual native Module container;
    shared actual native Boolean annotated<Annotation>()
            given Annotation satisfies AnnotationType;
    shared actual String string => "import ``name``/``version``";
}
