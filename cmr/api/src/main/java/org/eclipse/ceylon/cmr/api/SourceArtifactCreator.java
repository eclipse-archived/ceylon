package org.eclipse.ceylon.cmr.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Contract for a component that can create a compilation artifact.
 * 
 * @author Enrique Zamudio
 */
public interface SourceArtifactCreator extends ArtifactCreator {

    /** Copy the specified source streams, avoiding duplicate entries. */
    public Set<String> copyStreams(Collection<SourceStream> sourceStreams) throws IOException;
}
