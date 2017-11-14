

package org.eclipse.ceylon.cmr.impl;

import org.eclipse.ceylon.cmr.spi.ContentStore;
import org.eclipse.ceylon.cmr.spi.StructureBuilder;

/**
 * Root node impl.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootNode extends DefaultNode {
    private static final long serialVersionUID = 3690503975494792059L;

    public RootNode(ContentStore contentStore, StructureBuilder structureBuilder) {
        super("");
        addService(ContentStore.class, contentStore);
        addService(StructureBuilder.class, structureBuilder);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this); // should be only one
    }

    @Override
    public String toString() {
        return "RootNode for "+getService(ContentStore.class);
    }
    
    @Override
    public String getDisplayString() {
        return getService(ContentStore.class).getDisplayString();
    }
}
