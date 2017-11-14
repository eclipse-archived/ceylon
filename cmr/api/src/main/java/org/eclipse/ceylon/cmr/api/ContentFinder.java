

package org.eclipse.ceylon.cmr.api;


/**
 * Content finder.
 *
 * @author Stef Epardaud
 */
public interface ContentFinder {
    boolean isSearchable();
    
    void completeModules(ModuleQuery query, ModuleSearchResult result);

    void completeVersions(ModuleVersionQuery query, ModuleVersionResult result);
    
    void searchModules(ModuleQuery query, ModuleSearchResult result);
}
