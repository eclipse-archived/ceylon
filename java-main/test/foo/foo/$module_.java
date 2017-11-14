/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package foo.foo;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

@Module(name = "foo.foo",
        version = "1", 
        dependencies = {
        @Import(name = "a.a", version = "1"),
        @Import(name = "b.b", version = "2", export = true),
        @Import(name = "c.c", version = "3", optional = true),
})
public class $module_ {
}
