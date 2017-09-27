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
interface IFormalClasses<A, B> {
    shared formal class ISimpleClassNullary(){}
    
    
    shared formal class ISimpleClassUnary(String arg=""){}
    /*shared formal class ITpClassAUnary(A arg/*=nothing*/){}
    shared formal class ITpClassBUnary(B arg/*=nothing*/){}
    shared formal class ITpClassCUnary<C>(C arg=nothing){}
    
    shared formal class ISimpleClassFunctional(void functional(String a)){}
    shared formal class ITpClassFunctionalA(String functional(A a)=>nothing){}
    shared formal class ITpClassFunctionalB(A functional(B a)/*=>nothing*/){}
    shared formal class ITpClassFunctionalC<C>(B functional(C a)/*=>nothing*/){}*/
}
abstract class CFormalClasses<A,B>() {
    shared formal class CSimpleClassNullary(){}
    shared formal class CSimpleClassUnary(String arg=nothing){}
    /*shared formal class CTpClassAUnary(A arg=nothing){}
    shared formal class CTpClassBUnary(B arg=nothing){}
    shared formal class CTpClassCUnary<C>(C arg=nothing){}
    
    shared formal class CSimpleClassFunctional(void functional(String a)=nothing){}
    shared formal class CTpClassFunctionalA(String functional(A a)=nothing){}
    shared formal class CTpClassFunctionalB(A functional(B a)=nothing){}
    shared formal class CTpClassFunctionalC<C>(B functional(C a)=nothing){}*/
}