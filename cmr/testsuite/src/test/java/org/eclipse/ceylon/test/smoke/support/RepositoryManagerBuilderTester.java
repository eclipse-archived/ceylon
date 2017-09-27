package org.eclipse.ceylon.test.smoke.support;

import java.io.File;
import java.net.Proxy;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.api.RepositoryManagerBuilder;
import org.eclipse.ceylon.common.log.Logger;

public class RepositoryManagerBuilderTester extends RepositoryManagerBuilder {
    public RepositoryManagerBuilderTester(Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides) {
    }

    public RepositoryManagerBuilderTester(Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides, boolean upgradeDist) {
    }
    
    public RepositoryManagerBuilderTester(File mainRepository, Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides) {
    }
    
    public RepositoryManagerBuilderTester(File mainRepository, Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory, Overrides overrides, boolean upgradeDist) {
    }
    
    public RepositoryManagerBuilder addRepository(CmrRepository external) {
        return this;
    }    
}
