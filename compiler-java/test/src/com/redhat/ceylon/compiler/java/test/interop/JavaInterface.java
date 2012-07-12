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
package com.redhat.ceylon.compiler.java.test.interop;

public interface JavaInterface<B,I> {
    boolean booleanMethod(boolean b);
    Boolean boxedBooleanMethod(Boolean b);
    ceylon.language.Boolean ceylonBooleanMethod(ceylon.language.Boolean b);

    B classTypeParamMethodB(B t);

    long longMethod(long i);
    Long boxedLongMethod(Long i);
    ceylon.language.Integer ceylonIntegerMethod(ceylon.language.Integer i);

    I classTypeParamMethodI(I t);

    int intMethod(int i);
    Integer boxedIntegerMethod(Integer i);

    String stringMethod(String i);
    ceylon.language.String ceylonStringMethod(ceylon.language.String i);

    <M> M methodTypeParamMethod(M m);
}
