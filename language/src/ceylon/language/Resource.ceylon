import ceylon.language.meta.declaration {
    Module
}

"A file packaged within a module. A `Resource` may be 
 obtained by calling [[Module.resourceByPath]]. For example:
     
     Module mod = `module com.redhat.example`;
     assert (exists resource 
        = mod.resourceByPath(\"com/redhat/example/file.txt\"));
     print(resource.textContent());"
by("Enrique Zamudio")
see (`function Module.resourceByPath`)
shared interface Resource {
    
    "The name of the resource; usually the filename."
    shared default String name {
        value pos = uri.lastOccurrence('/');
        if (exists pos) {
            return uri[pos+1...];
        }
        return uri;
    }
    
    "The size of the resource, in bytes."
    shared formal Integer size;
    
    "The full path to the resource, expressed as a URI. For
     a resource packaged within a module archive, this 
     includes both the path to the module archive file, and
     the path of the resource within the module archive."
    shared formal String uri;
    
    "Retrieves the contents of the resource as a [[String]],
     using the specified encoding."
    shared formal String textContent(String encoding="UTF-8");
    
    string => "``className(this).split('.'.equals).last``[``uri``]";
}

