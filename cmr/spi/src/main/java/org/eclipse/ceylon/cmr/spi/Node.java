

package org.eclipse.ceylon.cmr.spi;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represent repository contents as nodes.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Node {
    String getLabel();

    <T> T getValue(Class<T> valueType);

    Node getChild(String label);

    Iterable<? extends Node> getChildren();

    boolean hasBinaries();

    /**
     * @deprecated see getSizedInputStream
     */
    InputStream getInputStream() throws IOException;

    SizedInputStream getSizedInputStream() throws IOException;

    <T> T getContent(Class<T> contentType) throws IOException;

    long getLastModified() throws IOException;

    long getSize() throws IOException;

    Node getParent(String label);

    Iterable<? extends Node> getParents();

    boolean isRemote();

    String getDisplayString();

    String getStoreDisplayString();
}
