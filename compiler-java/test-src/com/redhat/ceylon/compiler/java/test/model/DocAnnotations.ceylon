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
doc "Foo"
shared interface DocAnnotationsInterface{
    doc "Foo"
    shared formal void m();
}

doc "Bla bla doc"
by ("Stef", "FroMage")
see (DocAnnotations)
shared class DocAnnotations() satisfies DocAnnotationsInterface {
    doc "Bla bla doc on field"
    see (DocAnnotations)
    shared Integer attr = 2;

    doc "Foo"
    shared Integer getter { return 1; }
    assign getter {}

    doc "Bla bla doc on method"
    see (DocAnnotations)
    throws (Exception, "when things go kaboom")
    shared actual void m(){}

    doc "Bla bla doc on inner class"
    see (InnerClass)
    by ("Stef", "FroMage")
    shared class InnerClass(){}
}

doc "Foo"
shared Integer docAnnotationsAttr = 1;

doc "Foo"
shared Integer docAnnotationsGetter { return 1; }
assign docAnnotationsGetter {}

doc "Foo"
shared void docAnnotationsMethod(){}

doc "Foo"
shared object docAnnotationsObject satisfies DocAnnotationsInterface{
    doc "Bar"
    shared actual void m() {}
}
