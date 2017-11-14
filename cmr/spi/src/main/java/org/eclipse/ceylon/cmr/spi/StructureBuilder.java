

package org.eclipse.ceylon.cmr.spi;

/**
 * Build graph structure.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface StructureBuilder {

    /**
     * Get root node.
     *
     * @return root node
     */
    OpenNode createRoot();

    /**
     * Create child from parent.
     *
     * @param parent the parent
     * @param child  the child
     * @return create child, or null if it cannot be created
     */
    OpenNode create(Node parent, String child);

    /**
     * Find child from parent.
     *
     * @param parent the parent
     * @param child  the child
     * @return found child, or null if it doesn't exist
     */
    OpenNode find(Node parent, String child);

    /**
     * Find all children.
     *
     * @param parent the parent
     * @return all parent's children
     */
    Iterable<? extends OpenNode> find(Node parent);

}
