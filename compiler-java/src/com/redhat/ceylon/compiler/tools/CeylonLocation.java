package com.redhat.ceylon.compiler.tools;

import javax.tools.JavaFileManager.Location;

public enum CeylonLocation implements Location {

    /**
     * Repository for loading modules
     */
    REPOSITORY;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean isOutputLocation() {
        return false;
    }
    
}
