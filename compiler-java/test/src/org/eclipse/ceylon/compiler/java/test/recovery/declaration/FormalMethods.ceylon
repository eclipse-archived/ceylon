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
interface FormalMethods<A, B> {
    shared formal String simpleMethodNullary();
    shared formal A tpMethodANullary();
    shared formal B tpMethodBNullary();
    shared formal C tpMethodCNullary<C>();
    
    shared formal void simpleMethodUnary(String arg="");
    shared formal void sequencedMethodUnary(String* arg);
    shared formal void nonEmptySequencedMethodUnary(String+ arg);
    shared formal void tpMethodAUnary(A arg=nothing);
    shared formal void tpMethodBUnary(B arg=nothing);
    shared formal void tpMethodCUnary<C>(C arg=nothing);
    
    shared formal void methodMpl<C>(A a)(B b)(C c);
    
    shared formal void simpleMethodFunctional(void functional(String a));
    shared formal void tpMethodFunctionalA(String functional(A a));
    shared formal void tpMethodFunctionalB(A functional(B a));
    shared formal void tpMethodFunctionalC<C>(B functional(C a));
}
