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
shared interface I1<T1A,T1B> {
    shared interface I2<T2A,T2B> {
        shared formal T1A t1a();
        shared formal T1B t1b();
        shared formal T2A t2a();
        shared formal T2A t2b();
    }
    // hides a type param
    shared interface I2Hiding<T1A> {
        shared formal T1A t1a();
    }
}
@noanno
shared interface CapturedTypeParam<OuterT1,OuterT2> {
    void foo<MethodFooT1,MethodFooT2>() {
        void bar<MethodBarT1,MethodBarT2>() {
            interface I<InnerT1,InnerT2> {
                shared OuterT1 outerT1() => nothing;
                shared OuterT2 outerT2() => nothing;
                shared MethodFooT1 methodFooT1() => nothing;
                shared MethodFooT2 methodFooT2() => nothing;
                shared MethodBarT1 methodBarT1() => nothing;
                shared MethodBarT2 methodBarT2() => nothing;
                shared InnerT1 innerT1() => nothing;
                shared InnerT2 innerT2() => nothing;
            }
            class C<CT>() satisfies I<CT,CT>{}
            C<MethodBarT1>();
        }
    }   
    void bar<OuterT1>() {
        interface I<OuterT1> {
            shared OuterT1 outerT1() => nothing;
        }
        class C<CT>() satisfies I<CT>{}
        C<OuterT1>();
    }   
}