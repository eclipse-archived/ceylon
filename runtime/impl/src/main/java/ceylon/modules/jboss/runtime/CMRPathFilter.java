/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.jboss.runtime;

import org.jboss.modules.filter.PathFilter;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class CMRPathFilter implements PathFilter {
    org.eclipse.ceylon.model.cmr.PathFilter filter;

    public CMRPathFilter(org.eclipse.ceylon.model.cmr.PathFilter filter) {
        this.filter = filter;
    }

    public boolean accept(String path) {
        return filter.accept(path);
    }
}
