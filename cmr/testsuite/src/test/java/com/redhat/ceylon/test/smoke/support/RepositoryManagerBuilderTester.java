package com.redhat.ceylon.test.smoke.support;

import java.io.File;
import java.net.Proxy;

import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;

public class RepositoryManagerBuilderTester extends RepositoryManagerBuilder {
    public RepositoryManagerBuilderTester(Logger log, boolean offline, int timeout, Proxy proxy, Overrides overrides) {
    }

    public RepositoryManagerBuilderTester(File mainRepository, Logger log, boolean offline, int timeout, Proxy proxy, Overrides overrides) {
    }
    
    public RepositoryManagerBuilder addRepository(CmrRepository external) {
        return this;
    }    
}
