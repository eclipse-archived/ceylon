/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
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

package com.redhat.ceylon.langtools.source.util;

import java.io.IOException;

import com.redhat.ceylon.javax.lang.model.element.Element;
import com.redhat.ceylon.javax.lang.model.type.TypeMirror;
import com.redhat.ceylon.javax.lang.model.util.Elements;
import com.redhat.ceylon.javax.lang.model.util.Types;
import com.redhat.ceylon.javax.tools.JavaFileObject;
import com.redhat.ceylon.javax.tools.JavaCompiler.CompilationTask;
import com.redhat.ceylon.langtools.source.tree.CompilationUnitTree;
import com.redhat.ceylon.langtools.source.tree.Tree;

/**
 * Provides access to functionality specific to the JDK Java Compiler, javac.
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public abstract class JavacTask implements CompilationTask {

    /**
     * Parse the specified files returning a list of abstract syntax trees.
     *
     * @return a list of abstract syntax trees
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends CompilationUnitTree> parse()
        throws IOException;

    /**
     * Complete all analysis.
     *
     * @return a list of elements that were analyzed
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends Element> analyze() throws IOException;

    /**
     * Generate code.
     *
     * @return a list of files that were generated
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends JavaFileObject> generate() throws IOException;

    /**
     * The specified listener will receive events describing the progress of
     * this compilation task.
     */
    public abstract void setTaskListener(TaskListener taskListener);

    /**
     * Get a type mirror of the tree node determined by the specified path.
     */
    public abstract TypeMirror getTypeMirror(Iterable<? extends Tree> path);
    /**
     * Get a utility object for dealing with program elements.
     */
    public abstract Elements getElements();

    /**
     * Get a utility object for dealing with type mirrors.
     */
    public abstract Types getTypes();
}
