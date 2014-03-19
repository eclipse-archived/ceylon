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

package com.redhat.ceylon.compiler.java.loader;

import java.util.EnumSet;

import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.util.Context;

public class CeylonClassReader extends ClassReader {

    public static ClassReader instance(Context context) {
        ClassReader instance = (ClassReader) context.get(classReaderKey);
        if (instance == null)
            instance = new CeylonClassReader(context, true);
        return instance;
    }

    protected CeylonClassReader(Context context, boolean definitive) {
        super(context, definitive);
    }

    @Override
    protected JavaFileObject preferredFileObject(JavaFileObject a, JavaFileObject b) {
        return a.getKind() == Kind.CLASS ? a : b;
    }

    @Override
    protected EnumSet<JavaFileObject.Kind> getPackageFileKinds() {
        return EnumSet.of(JavaFileObject.Kind.CLASS);
    }
}
