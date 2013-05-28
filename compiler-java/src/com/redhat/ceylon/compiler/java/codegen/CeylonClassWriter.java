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

import java.io.IOException;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.util.Context;

public class CeylonClassWriter extends ClassWriter {
    public static ClassWriter instance(Context context) {
        ClassWriter instance = context.get(classWriterKey);
        if (instance == null)
            instance = new CeylonClassWriter(context);
        return instance;
    }

    private CeyloncFileManager fileManager;
    private CeylonModelLoader ceylonModelLoader;

    public CeylonClassWriter(Context context) {
        super(context);
        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
        ceylonModelLoader = (CeylonModelLoader) CeylonModelLoader.instance(context);
    }

    @Override
    public JavaFileObject writeClass(ClassSymbol c) throws IOException,
            PoolOverflow, StringOverflow {
        String packageName = c.packge().getQualifiedName().toString();
        Package pkg = ceylonModelLoader.findPackage(packageName);
        if(pkg == null)
            throw new RuntimeException("Failed to find package: "+packageName);
        Module module = pkg.getModule();
        fileManager.setModule(module);
        return super.writeClass(c);
    }
}
