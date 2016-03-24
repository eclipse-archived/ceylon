import ceylon.language.meta.declaration {
    Module
}

"""A file packaged within a module. A `Resource` may be 
   obtained by calling [[Module.resourceByPath]], passing
   a path that identifies the resource file.
   
   The resource file itself must be placed in a _resource 
   directory_ at compilation time, in a subdirectory 
   corresponding to the module to which the resource belongs.
   The compiler is responsible for packaging the resource 
   file in a location accessible to the program at runtime:
   
   - In the case of a module compiled for execution on the 
     JVM, the resource file will be included in the `.car` 
     archive.
   - In the case of compilation to JavaScript, the resource
     file will be copied to a `module-resources` directory
     in the module repository.
   
   Suppose the following code occurs in a module named
   `com.redhat.example`:
       
       assert (exists resource 
          = `module`.resourceByPath("file.txt"));
       print(resource.textContent());
   
   Then the resource named `file.text` should be placed in 
   the subdirectory `com/redhat/example/` of the resource 
   directory.
   
   Paths with no leading `/` are relative to the module's
   subdirectory of the resource directory. Alternatively, a 
   resource may be identified by a fully-qualified path 
   beginning with `/`, for example:
   
       assert (exists resource 
          = `module`.resourceByPath("/com/redhat/example/file.txt"));
       print(resource.textContent());"""
by("Enrique Zamudio")
see (`function Module.resourceByPath`)
tagged("Environment")
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
    
    string => "``className(this)``[``uri``]";
}

