package com.redhat.ceylon.cmr.api;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/** Contract for a component that can create a source archive as a compilation artifact.
 * 
 * @author Enrique Zamudio
 */
public interface SourceArchiveCreator {

    /** Copy the specified source files into the .src archive, avoiding duplicate entries. */
    public Set<String> copySourceFiles(Set<String> sources) throws IOException;

    /** Return the root directories that can contain source files. */
    public Iterable<? extends File> getSourcePaths();

}
