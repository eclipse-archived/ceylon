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
shared class LocalDeclarationsClassContainer() {
    class Inner(){}
    shared void m(){
        class LocalClass(){
            shared Integer attr = 2;
        }
        Integer getter {return 1;} assign getter {}
        Integer attr = 2;
        void localMethod(){}
    }
    // that creates an anonymous class
    Anything mplDeclaration2()() => nothing;
    // this too
    void privateMethod(Boolean accept()=>true) {
        object visitor {}
    }
    shared void fuckItUp(){
        value c = {for (i in {}) i};
        value f = function() => 2;
        value l = {1, 2};
        // method which takes a function argument
        // and an int
        void m(void m2(), Integer i){
        }
        // named invocation with MethodArgument and getter
        m{
            // pass that function
            void m2(){
            }
            // pass the getter too, though it's not an anonymous one
            Integer i {
                return 2;
            }
        };
        value spread = [1,2]*.string;
        fuckItUp();
        value capture = m;
        value capture2 = LocalDeclarationsClassContainer;
        value capture3 = LocalDeclarationsClassContainer.Inner;
        value capture4 = LocalDeclarationsClassContainer.m;
        value noCapture = f;
    }
    // MPL: two param lists
    shared void m2()(){
        class LocalClassInM2(){
            shared Integer attr = 2;
        }
    }
    // MPL: three param lists
    shared void m3()()(){
        class LocalClassInM3(){
            shared Integer attr = 2;
        }
    }
    // local type inside a lambda
    shared void f(){
        value g = void(){
            class LocalClassInLambda(){}
        };
    }
}
shared void localDeclarationsMethodContainer(){
    class LocalClass(){
        shared Integer attr = 2;
    }
    Integer getter {return 1;} assign getter {}
    Integer attr = 2;
    void localMethod(){}
}
shared Integer localDeclarationsGetterContainer {
    class LocalClass(){
        shared Integer attr = 2;
    }
    Integer getter {return 1;} assign getter {}
    Integer attr = 2;
    void localMethod(){}
    return 1;
}
