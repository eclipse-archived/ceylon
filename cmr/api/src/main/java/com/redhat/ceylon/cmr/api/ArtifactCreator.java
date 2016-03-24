package com.redhat.ceylon.cmr.api;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Contract for a component that can create a compilation artifact.
 * 
 * @author Enrique Zamudio
 */
public interface ArtifactCreator {

    /** Copy the specified source files, avoiding duplicate entries. */
    public Set<String> copy(Collection<String> sources) throws IOException;

    /** Return the root directories that can contain (re)source files. */
    public Iterable<? extends File> getPaths();

}
