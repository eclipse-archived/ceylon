/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package eu.cloud.clazz;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "eu.cloud.clazz",
        version = "1.0.0.GA",
        dependencies = {
                @Import(name = "org.jboss.filtered", version = "1.0.0.Alpha1"),
                @Import(name = "ceylon.io", version = "0.5")
        })
public class $module_ {
}
