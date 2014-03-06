"An abstraction for resources inside a module.
 A resource is a file contained within a module,
 which is accessible at runtime."
by("Enrique Zamudio")
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
    "The full path to the resource."
    shared formal String uri;
    "Retrieves the contents of the resource as a String,
     using the specified encoding."
    shared formal String textContent(String encoding="UTF-8");
    shared actual String string => "Resource[``uri``]";
}

