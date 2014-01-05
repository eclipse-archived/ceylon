package com.redhat.ceylon.test.smoke.support;

import java.io.File;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;

public class RepositoryManagerBuilderTest extends RepositoryManagerBuilder {

    public RepositoryManagerBuilderTest(Logger log, boolean offline, String mavenOverrides) {

    }
    
    public RepositoryManagerBuilderTest(File mainRepository, Logger log, boolean offline, String mavenOverrides) {

    }
    
    public RepositoryManagerBuilder addRepository(Repository external) {
        return this;
    }    
}
