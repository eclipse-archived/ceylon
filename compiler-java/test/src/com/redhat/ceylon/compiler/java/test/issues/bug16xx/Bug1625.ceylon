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
interface Bug1625<T> {
    formal shared class Inner(){
        T[] t(T t){
           return [t];
        }
    }
}

@noanno
interface Bug1625_2<T> {
    formal shared void f(T t, T[] ts = [t]);
}

interface Bug1625_3<T> {
    formal shared void f(T t, T[] ts);
    formal shared T[] ts;
}

@noanno
class Bug1625_3Impl<T>() satisfies Bug1625_3<T> {
    shared actual void f(T t, T[] ts){}
    shared actual T[] ts => nothing;
}

// check local too
void bug1625(){
    interface Empty{}
}

// check refines
@noanno
interface Bug1625_Top<out T>{}

@noanno
class Bug1625_Middle() satisfies Bug1625_Top<Object>{}

@noanno
class Bug1625_Bottom() extends Bug1625_Middle() satisfies Bug1625_Top<String>{}
