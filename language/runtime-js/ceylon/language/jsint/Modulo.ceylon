import ceylon.language.meta.declaration { Package, Import, Module }
import ceylon.language{AnnotationType = Annotation}

native class Modulo(shared Anything meta) satisfies Module {
    shared actual native String name;
    qualifiedName = name;
    shared actual native String version;
    shared actual native Package[] members;
    shared actual native Import[] dependencies;
    shared actual native Package? findPackage(String name);
    shared actual native Package? findImportedPackage(String name);
    shared actual native Resource? resourceByPath(String path);
    shared actual native Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
    string = "module " + name + "/" + version;
}
