/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.api;


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
