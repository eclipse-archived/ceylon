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
@nomodel
interface MethodRefinementWithSpecifiers_Interface {
    shared formal void f2();
    shared formal void f3();
    shared formal void f4();
    shared formal void f5();
    shared formal void f6();
    shared formal void f7();
    shared formal void f8();
    shared formal void f9();
    shared formal void f10();
    shared formal Object f11();
    shared formal Object f12();
    shared formal Object f13();
    shared formal void f14(Integer i);
    shared formal void f15(Integer i);
    shared formal Object f16();
    shared formal Object f17();
    shared formal Object f18();
    shared formal Object f19();
}

@nomodel
void methodRefinementWithSpecifiers_returnsMethod()(){}

@nomodel
Callable<Anything,[]> methodRefinementWithSpecifiers_attributeMethod = methodRefinementWithSpecifiers_returnsMethod();

@nomodel
class MethodRefinementWithSpecifiers_ClassNoParam(){}

@nomodel
class MethodRefinementWithSpecifiers_Class(void arg()) satisfies MethodRefinementWithSpecifiers_Interface {
    
    class Inner(){}
    shared class InnerShared(){}
    
    // long decl, lazy, arg always reevaluated
    shared actual void f2() => arg();
    
    // short decl, eager, but arg is constant so it's optimised
    f3 = arg;
    
    // short decl, lazy, arg always reevaluated
    f4() => arg();

    // short decl, lazy, arg always reevaluated
    f5 = function () => arg();

    // long decl, lazy, arg always reevaluated
    shared actual void f6(){
        arg();
    }
    
    // short decl, eager, but f3 is constant so it's optimised
    f7 = f3;
    
    // short decl, lazy, f3 always reevaluated
    f8() => f3();
    
    // short decl, eager, returnsMethod() is only evaluated once
    f9 = methodRefinementWithSpecifiers_returnsMethod();
    
    // short decl, eager, attributeMethod is only evaluated once
    f10 = methodRefinementWithSpecifiers_attributeMethod;
    
    // short decl, eager, but Bug891_ClassNoParam is constant to it's optimised
    f11 = MethodRefinementWithSpecifiers_ClassNoParam;

    // short decl, eager, but Inner is constant to it's optimised
    f12 = Inner;
    
    // short decl, eager, but InnerShared is constant to it's optimised to its instantiator method
    f13 = InnerShared;
    
    void takesParam(Integer i){}
    
    // short decl, eager, but takesParam is constant to it's optimised
    f14 = takesParam;

    // short decl, eager, but f14 is constant to it's optimised
    f15 = f14;

    // short decl, lazy, returns a Callable
    f16() => takesParam;

    // short decl, lazy, returns a Callable
    f17() => Inner;

    // short decl, lazy, returns a Callable
    f18() => arg;

    // long decl, lazy, returns a Callable
    shared actual Object f19() => arg;
}

@nomodel
void arg2(){}

@nomodel
interface Bug891_Interface2 satisfies MethodRefinementWithSpecifiers_Interface {
    
    class Inner(){}
    shared class InnerShared(){}
    
    // long decl, lazy, arg always reevaluated
    shared actual void f2() => arg2();
    
    // short decl, eager, but arg is constant so it's optimised
    //f3 = arg2;
    
    // short decl, lazy, arg always reevaluated
    f4() => arg2();

    // short decl, lazy, arg always reevaluated
    //f5 = function () => arg2();

    // long decl, lazy, arg always reevaluated
    shared actual void f6(){
        arg2();
    }
    
    // short decl, eager, but f3 is constant so it's optimised
    //f7 = f3;
    
    // short decl, lazy, f3 always reevaluated
    f8() => f3();
    
    // short decl, eager, returnsMethod() is only evaluated once
    //f9 = methodRefinementWithSpecifiers_returnsMethod();
    
    // short decl, eager, attributeMethod is only evaluated once
    //f10 = methodRefinementWithSpecifiers_attributeMethod;
    
    // short decl, eager, but Bug891_ClassNoParam is constant to it's optimised
    //f11 = MethodRefinementWithSpecifiers_ClassNoParam;

    // short decl, eager, but Inner is constant to it's optimised
    //f12 = Inner;
    
    // short decl, eager, but InnerShared is constant to it's optimised to its instantiator method
    //f13 = InnerShared;
    
    void takesParam(Integer i){}
    
    // short decl, eager, but takesParam is constant to it's optimised
    //f14 = takesParam;

    // short decl, eager, but f14 is constant to it's optimised
    //f15 = f14;

    // short decl, lazy, returns a Callable
    f16() => takesParam;

    // short decl, lazy, returns a Callable
    f17() => Inner;

    // short decl, lazy, returns a Callable
    f18() => arg2;

    // long decl, lazy, returns a Callable
    shared actual Object f19() => arg2;
}
