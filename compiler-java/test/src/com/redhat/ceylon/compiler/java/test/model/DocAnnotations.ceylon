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
"Foo"
shared interface DocAnnotationsInterface{
    "Foo"
    shared formal void m();
}

"Bla bla doc"
by("Stef", "FroMage")
see(`class DocAnnotations`)
shared class DocAnnotations() satisfies DocAnnotationsInterface {
    "Bla bla doc on field"
    see(`class DocAnnotations`)
    shared Integer attr = 2;

    "Foo"
    shared Integer getter { return 1; }
    assign getter {}

    "Bla bla doc on method"
    see(`class DocAnnotations`)
    throws(`class Exception`, "when things go kaboom")
    shared actual void m(){}

    "Bla bla doc on inner class"
    see(`class InnerClass`)
    see(`interface DocAnnotationsInterface`)
    see(`package ceylon.language.meta`)
    see(`module ceylon.language`)
    by("Stef", "FroMage")
    tagged("TODO", "XXX")
    shared class InnerClass(){}
}

"Foo"
shared Integer docAnnotationsAttr = 1;

"Foo"
shared Integer docAnnotationsGetter { return 1; }
assign docAnnotationsGetter {}

"Foo"
shared void docAnnotationsMethod(){}

"Foo"
shared object docAnnotationsObject satisfies DocAnnotationsInterface{
    "Bar"
    shared actual void m() {}
}
