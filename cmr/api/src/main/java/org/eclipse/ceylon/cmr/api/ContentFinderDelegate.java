

package org.eclipse.ceylon.cmr.api;


/**
 * Content finder but for when we're not a repo and have no access to one.
 *
 * @author Stef Epardaud
 */
public interface ContentFinderDelegate {
    boolean isSearchable();
    
    void completeModules(ModuleQuery query, ModuleSearchResult result, Overrides overrides);

    void completeVersions(ModuleVersionQuery query, ModuleVersionResult result, Overrides overrides);
    
    void searchModules(ModuleQuery query, ModuleSearchResult result, Overrides overrides);
}
