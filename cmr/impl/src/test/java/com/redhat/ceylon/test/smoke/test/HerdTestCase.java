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

package com.redhat.ceylon.test.smoke.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleResult;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.cmr.impl.MavenRepositoryHelper;
import com.redhat.ceylon.cmr.impl.RemoteContentStore;
import com.redhat.ceylon.cmr.impl.RootRepositoryManager;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import com.redhat.ceylon.test.smoke.support.InMemoryContentStore;

/**
 * @author Stef Epardaud
 */
@Ignore("Required Herd running locally")
public class HerdTestCase extends AbstractTestCase {

    @Test
    public void testHerdCompleteVersions() throws URISyntaxException{
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(getRepositoryRoot(), log);
        WebDAVContentStore rcs = new WebDAVContentStore("http://localhost:9000/test", log);
        Repository repo = new DefaultRepository(rcs.createRoot());
        RepositoryManager manager = builder.appendRepository(repo).buildRepository();

        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                new ModuleVersionDetails("0.3.0", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", "Stéphane Épardaud"),
        };
        testListVersions("ceylon.collection", null, expected, manager);
    }

    @Test
    public void testHerdCompleteVersionsFiltered() throws URISyntaxException{
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(getRepositoryRoot(), log);
        WebDAVContentStore rcs = new WebDAVContentStore("http://localhost:9000/test", log);
        Repository repo = new DefaultRepository(rcs.createRoot());
        RepositoryManager manager = builder.appendRepository(repo).buildRepository();

        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        };
        testListVersions("ceylon.collection", "1.0", expected, manager);
    }
}
