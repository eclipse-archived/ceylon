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
@noanno
shared native object bug1576angular {
    shared native void \imodule(String name);
}

@noanno
shared native class Bug1576Class(){}

@noanno
shared native void bug1576Method(){}

@noanno
shared native Integer bug1576Attr;

@noanno
void bug1576_1() {
    bug1576angular.\imodule("test");
}

@noanno
void bug1576_2(Bug1576Class c) {
}

@noanno
void bug1576_3() {
    Bug1576Class c = nothing;
}

@noanno
void bug1576_4() {
    bug1576Method();
}

@noanno
void bug1576_5() {
    value i = bug1576Attr;
}
