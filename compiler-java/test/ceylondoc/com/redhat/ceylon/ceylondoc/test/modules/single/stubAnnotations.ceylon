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
import ceylon.language.meta.declaration { Package, ClassDeclaration }

shared annotation StubAnnotationFoo stubAnnotationFoo(String s, Character c, Integer i, Float f, Boolean b, ClassDeclaration d, Package p, Integer[] seq1, Integer[] seq2, {Float*} it1, {Float*} it2) => StubAnnotationFoo(s, c, i, f, b, d, p, seq1, seq2, it1, it2);

shared annotation StubAnnotationBar stubAnnotationBar(StubAnnotationBaz baz) => StubAnnotationBar(baz);

shared annotation StubAnnotationBaz stubAnnotationBaz(String s) => StubAnnotationBaz(s);
 
shared final annotation class StubAnnotationFoo(String s, Character c, Integer i, Float f, Boolean b, ClassDeclaration d, Package p, Integer[] seq1, Integer[] seq2, {Float*} it1, {Float*} it2) satisfies OptionalAnnotation<StubAnnotationFoo, Annotated> {}

shared final annotation class StubAnnotationBar(StubAnnotationBaz baz) satisfies SequencedAnnotation<StubAnnotationBar, Annotated> {}

shared final annotation class StubAnnotationBaz(String s = "") satisfies OptionalAnnotation<StubAnnotationBaz, Annotated> {}

"The stub annotated function."
stubAnnotationFoo("abc", 'a', 123, 987.654, true, `class StubClass`, `package ceylon.language.meta`, [], [0, 1], {}, {0.0, 1.1}) 
stubAnnotationBar(stubAnnotationBaz("baz"))
stubAnnotationBar{baz=stubAnnotationBaz{s="baz";};}
shared void stubAnnotatedFunction() {}
