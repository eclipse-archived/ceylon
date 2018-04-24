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
package org.eclipse.ceylon.compiler.java.test.nativecode.withjava;

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

//Compiled from run.ceylon (version 1.7 : 51.0, super bit)
@org.eclipse.ceylon.compiler.java.metadata.Ceylon(major=8)
@ceylon.language.NativeAnnotation$annotation$(backends="jvm")
@ceylon.language.SharedAnnotation$annotation$
public class NativeClass implements org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType {

// Field descriptor #9 Lorg/eclipse/ceylon/compiler/java/runtime/model/TypeDescriptor;
@org.eclipse.ceylon.compiler.java.metadata.Ignore
public static final org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $TypeDescriptor$;

// Method descriptor #13 (Ljava/lang/String;)V
// Stack: 1, Locals: 2
public NativeClass(
    @org.eclipse.ceylon.compiler.java.metadata.Name("s")
    @org.eclipse.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::String")
    java.lang.String s) {
    System.out.println(s);
}

// Method descriptor #27 ()Lorg/eclipse/ceylon/compiler/java/runtime/model/TypeDescriptor;
// Stack: 1, Locals: 1
@org.eclipse.ceylon.compiler.java.metadata.Ignore
public org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
    return $TypeDescriptor$;
}

// Method descriptor #29 ()V
// Stack: 2, Locals: 0
static {
    $TypeDescriptor$ = TypeDescriptor.klass(NativeClass.class);
}
}
