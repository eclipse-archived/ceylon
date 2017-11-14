

package org.eclipse.ceylon.cmr.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.eclipse.ceylon.cmr.spi.ContentOptions;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.cmr.spi.SizedInputStream;

/**
 * Marker node.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MarkerNode extends AbstractOpenNode {

    private static final long serialVersionUID = 1L;

    public MarkerNode() {
        // serialization only
    }

    public MarkerNode(String label, Object value) {
        super(label, value);
    }

    @Override
    public void merge(OpenNode other) {
        throw new UnsupportedOperationException("Marker node doesn't support merge: " + toString());
    }

    @Override
    public OpenNode addNode(String label, Object value) {
        throw new UnsupportedOperationException("Marker node doesn't add node: " + toString());
    }

    @Override
    public OpenNode createNode(String label) {
        throw new UnsupportedOperationException("Marker node cannot create node: " + toString());
    }

    @Override
    public OpenNode addContent(String label, InputStream content, ContentOptions options) throws IOException {
        throw new UnsupportedOperationException("Marker node doesn't add content: " + toString());
    }

    @Override
    public <T extends Serializable> OpenNode addContent(String label, T content, ContentOptions options) throws IOException {
        throw new UnsupportedOperationException("Marker node doesn't add content: " + toString());
    }

    @Override
    public Node removeNode(String label) {
        throw new UnsupportedOperationException("Marker node doesn't remove node: " + toString());
    }

    @Override
    public boolean hasBinaries() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public SizedInputStream getSizedInputStream() throws IOException {
        return null;
    }

    @Override
    public long getLastModified() {
        return -1L;
    }

    @Override
    public long getSize() {
        return -1;
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public String getDisplayString() {
        return "Marker node";
    }
}
