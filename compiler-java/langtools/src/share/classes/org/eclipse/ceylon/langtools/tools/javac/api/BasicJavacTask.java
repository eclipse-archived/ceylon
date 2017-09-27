/*
 * Copyright (c) 2005, 2012, Oracle and/or its affiliates. All rights reserved.
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

package org.eclipse.ceylon.langtools.tools.javac.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import org.eclipse.ceylon.javax.annotation.processing.Processor;
import org.eclipse.ceylon.javax.lang.model.element.Element;
import org.eclipse.ceylon.javax.lang.model.type.TypeMirror;
import org.eclipse.ceylon.javax.lang.model.util.Elements;
import org.eclipse.ceylon.javax.lang.model.util.Types;
import org.eclipse.ceylon.javax.tools.JavaFileObject;
import org.eclipse.ceylon.langtools.source.tree.CompilationUnitTree;
import org.eclipse.ceylon.langtools.source.tree.Tree;
import org.eclipse.ceylon.langtools.source.util.JavacTask;
import org.eclipse.ceylon.langtools.source.util.TaskListener;
import org.eclipse.ceylon.langtools.tools.javac.model.JavacElements;
import org.eclipse.ceylon.langtools.tools.javac.model.JavacTypes;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree;
import org.eclipse.ceylon.langtools.tools.javac.util.Context;

/**
 * Provides basic functionality for implementations of JavacTask.
 *
 * <p><b>This is NOT part of any supported API.
 * If you write code that depends on this, you do so at your own
 * risk.  This code and its internal interfaces are subject to change
 * or deletion without notice.</b></p>
 */
public class BasicJavacTask extends JavacTask {
    protected Context context;
    private TaskListener taskListener;

    public static JavacTask instance(Context context) {
        JavacTask instance = context.get(JavacTask.class);
        if (instance == null)
            instance = new BasicJavacTask(context, true);
        return instance;
    }

    public BasicJavacTask(Context c, boolean register) {
        context = c;
        if (register)
            context.put(JavacTask.class, this);
    }

    @Override
    public Iterable<? extends CompilationUnitTree> parse() throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public Iterable<? extends Element> analyze() throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public Iterable<? extends JavaFileObject> generate() throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public void setTaskListener(TaskListener tl) {
        MultiTaskListener mtl = MultiTaskListener.instance(context);
        if (taskListener != null)
            mtl.remove(taskListener);
        if (tl != null)
            mtl.add(tl);
        taskListener = tl;
    }

    @Override
    public void addTaskListener(TaskListener taskListener) {
        MultiTaskListener mtl = MultiTaskListener.instance(context);
        mtl.add(taskListener);
    }

    @Override
    public void removeTaskListener(TaskListener taskListener) {
        MultiTaskListener mtl = MultiTaskListener.instance(context);
        mtl.remove(taskListener);
    }

    public Collection<TaskListener> getTaskListeners() {
        MultiTaskListener mtl = MultiTaskListener.instance(context);
        return mtl.getTaskListeners();
    }

    @Override
    public TypeMirror getTypeMirror(Iterable<? extends Tree> path) {
        // TODO: Should complete attribution if necessary
        Tree last = null;
        for (Tree node : path)
            last = node;
        return ((JCTree)last).type;
    }

    @Override
    public Elements getElements() {
        return JavacElements.instance(context);
    }

    @Override
    public Types getTypes() {
        return JavacTypes.instance(context);
    }

    public void setProcessors(Iterable<? extends Processor> processors) {
        throw new IllegalStateException();
    }

    public void setLocale(Locale locale) {
        throw new IllegalStateException();
    }

    public Boolean call() {
        throw new IllegalStateException();
    }

    /**
     * For internal use only.  This method will be
     * removed without warning.
     */
    public Context getContext() {
        return context;
    }

    /**
     * For internal use only.  This method will be
     * removed without warning.
     */
    public void updateContext(Context newContext) {
        context = newContext;
    }
}
