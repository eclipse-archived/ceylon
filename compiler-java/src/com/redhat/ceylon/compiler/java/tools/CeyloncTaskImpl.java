/*
 * Copyright (c) 1999, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */

package com.redhat.ceylon.compiler.java.tools;

import javax.tools.JavaFileObject;

import com.sun.tools.javac.api.JavacTaskImpl;
import com.redhat.ceylon.compiler.java.launcher.Main;
import com.sun.tools.javac.util.Context;

public class CeyloncTaskImpl extends JavacTaskImpl {
    Main compilerMain;
    
    // we're just making this constructor visible here
    CeyloncTaskImpl(Main compilerMain, Iterable<String> flags, Context context, Iterable<String> classes, Iterable<? extends JavaFileObject> fileObjects) {
        super(compilerMain, flags, context, classes, fileObjects);
        this.compilerMain = compilerMain;
    }
    
    public Main.ExitState getExitState() {
        return compilerMain.exitState;
    }
    
}
