package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.api.ModuleVersionDetails;

public interface ModuleInfoCallback {
    public void storeInfo(ModuleVersionDetails mvd);
}