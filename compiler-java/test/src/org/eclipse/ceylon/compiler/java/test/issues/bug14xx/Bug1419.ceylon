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
import ceylon.language.meta.model { ... }

@noanno
interface Bug1419Val<out Get,in Set> {
    shared formal Get get();
}

@noanno
interface Bug1419Mem<in Container, out Member>{
    shared formal Member bind(Container instance);
}

@noanno
interface Bug1419Attr<in Container,out Get,in Set> satisfies Bug1419Mem<Container,Bug1419Val<Get,Set>>{
    //shared formal Val<Get,Set> bind(Container instance);
}

@noanno
interface Bug1419COI<T>{
    shared formal Bug1419Attr<Container,Get,Set>? getAttribute<Container,Get,Set=Nothing>(String name);
}

@noanno
void bug1419(ClassOrInterface<Anything> t, Bug1419COI<Anything> t2) {
    value string1 = t.getAttribute<Anything, String, Nothing>("str");
    value string2 = t.getAttribute<Anything, String>("str");
    value string3 = t.getAttribute<Anything, String, String>("str");
    assert(exists string1, exists string2, exists string3);
    value a1 = string1(t);
    value a2 = string2(t);
    value a3 = string3(t);

    value string4 = t2.getAttribute<Anything, String, Nothing>("str");
    assert(exists string4);
    value a4 = string4.bind(t);
}
