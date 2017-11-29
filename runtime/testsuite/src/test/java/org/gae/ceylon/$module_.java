/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.gae.ceylon;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;


/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(
    name = "org.gae.ceylon", version = "1.0.0",
    dependencies = {@Import(name = "com.google.appengine:appengine-api-1.0-sdk", version = "1.9.6")})
public class $module_ {
    public static void run() throws Throwable {
        run_.main(new String[0]);
    }
}
