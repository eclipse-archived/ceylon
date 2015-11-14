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
shared {Element+} loop<Element>(
    "The first element of the resulting stream."
    Element first)(
        "The function that produces the next element of the
         stream, given the current element."
        Element next(Element element)) {
    value start = first;
    object iterable satisfies {Element+} {
        first => start;
        empty => false;
        shared actual Nothing size {
            "stream is infinite"
            assert(false);
        }
        function nextElement(Element element) => next(element);
        shared actual Iterator<Element> iterator() {
            variable Element current = start;
            object iterator satisfies Iterator<Element> {
                shared actual Element|Finished next() {
                    Element result = current;
                    current = nextElement(current);
                    return result;
                }
            }
            return iterator;
        }
    }
    return iterable;
}
