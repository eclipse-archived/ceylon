/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package net.acme.logging;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@Module(
    name = "net.acme.logging",
    version = "1.0.0.CR1",
    dependencies = {
        @Import(name = "java.logging", version = "7"),
    }
)
public class $module_ {
}
