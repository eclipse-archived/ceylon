/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.loader.TypeFactory;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.langtools.tools.javac.code.Symtab;
import com.redhat.ceylon.langtools.tools.javac.tree.TreeMaker;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.Factory;
import com.redhat.ceylon.langtools.tools.javac.util.Names;
import com.redhat.ceylon.model.loader.AbstractModelLoader;

public interface Transformation {

    public abstract TreeMaker make();

    public abstract Factory at(Node node);

    public abstract Symtab syms();

    public abstract Names names();

    public abstract AbstractModelLoader loader();

    public abstract TypeFactory typeFact();

    public abstract CeylonTransformer gen();

    public abstract ExpressionTransformer expressionGen();

    public abstract StatementTransformer statementGen();

    public abstract ClassTransformer classGen();

}
