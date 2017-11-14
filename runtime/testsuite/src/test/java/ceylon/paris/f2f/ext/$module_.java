/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.paris.f2f.ext;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "ceylon.paris.f2f.ext", version = "1.0.0.Final",
    dependencies = {
        @Import(name = "ceylon.paris.f2f.iface", version = "1.0.0.Final"),
        @Import(name = "ceylon.paris.f2f.impl", version = "1.0.0.Final")
    }
)
public class $module_ {
}
