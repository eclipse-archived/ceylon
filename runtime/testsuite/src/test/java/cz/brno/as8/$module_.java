/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package cz.brno.as8;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "cz.brno.as8",
        version = "8.0.0.Alpha1",
        dependencies = {
                @Import(name = "com.foobar.qwert", version = "1.0.0.GA")
        })
public class $module_ {
}
