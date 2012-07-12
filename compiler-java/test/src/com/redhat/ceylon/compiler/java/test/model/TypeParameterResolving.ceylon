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
shared class TypeParameterResolving <A> (A a) {
    shared class InnerClass<B>(A a, B b){
        shared class InnerClass2<C>(A a, B b, C c){}
        shared object innerObject{
            shared A attr1 = a;
            shared B attr2 = b;
        }
        shared A attr1 = a;
        shared B attr2 = b;
        shared A getter1 { return a; } assign getter1 {}
        shared B getter2 { return b; } assign getter2 {}
        shared A method<C>(A a, B b, C c){ return a; }
    }
    shared object innerObject {
        shared class InnerClass2<B>(A a, B b){}
        shared object innerObject{
            shared A attr1 = a;
        }
        shared A attr1 = a;
        shared A getter1 { return a; } assign getter1 {}
        shared A method<B>(A a, B b){ return a; }
    }
    shared Integer innerGetter {
        class InnerClass2<B>(A a, B b){}
        object innerObject{
            shared A attr1 = a;
        }
        A attr1 = a;
        A getter1 { return a; } assign getter1 {}
        A method<B>(A a, B b){ return a; }
        return 2;
    }
    assign innerGetter {
        class InnerClass2<B>(A a, B b){}
        object innerObject{
            shared A attr1 = a;
        }
        A attr1 = a;
        A getter1 { return a; } assign getter1 {}
        A method<B>(A a, B b){ return a; }
    }
    shared void method<B>(A a, B b){
        class InnerClass2<C>(A a, B b, C c){}
        object innerObject{
            shared A attr1 = a;
            shared B attr2 = b;
        }
        A attr1 = a;
        B attr2 = b;
        A getter1 { return a; } assign getter1 {}
        B getter2 { return b; } assign getter2 {}
        A method<C>(A a, B b, C c){ return a; }
    }
}
shared interface TypeParameterResolvingInterface <A> {
    shared formal void method<B>(A a, B b);
}
void typeParameterResolvingMethod<A>(A a){
    class InnerClass2<B>(A a, B b){}
    object innerObject{
        shared A attr1 = a;
    }
    A attr1 = a;
    A getter1 { return a; } assign getter1 {}
    A method<B>(A a, B b){ return a; }
}
