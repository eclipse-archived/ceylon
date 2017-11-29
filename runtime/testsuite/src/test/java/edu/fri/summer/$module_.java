/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package edu.fri.summer;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "edu.fri.summer",
        version = "1.0.0.Beta23",
        dependencies = {
                @Import(name = "org.jboss.modules:jboss-modules",
                        version = "1.4.4.Final")
        })
public class $module_ {
}
