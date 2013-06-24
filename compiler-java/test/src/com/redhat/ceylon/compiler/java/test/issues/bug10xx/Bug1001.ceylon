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
Integer bug1001Foo() {
    return 0;
}
@nomodel
shared class Bug1001(Integer paramUnusedMethodRef(), Integer paramMethodRef(), Integer paramMethodRefCaptured(), paramMethodRefCaptured2) {
    shared void paramMethodRefCaptured2(Integer i);
    Integer initUnusedMethodRef();
    Integer initLateMethodRef();
    initLateMethodRef = bug1001Foo;
    Integer() initLateCallableAttrib;
    initLateCallableAttrib = bug1001Foo;
    Integer initLateMethodRefCaptured();
    initLateMethodRefCaptured = bug1001Foo;
    print(paramMethodRefCaptured2(0));
    print(initLateMethodRef());
    print(initLateCallableAttrib());
    print(initLateMethodRefCaptured());
    print(paramMethodRef());
    
    void m() {
        Integer localUnusedMethodRef();
        Integer localLateMethodRef();
        localLateMethodRef = bug1001Foo;
        Integer() localLateCallableAttrib;
        localLateCallableAttrib = bug1001Foo;
        Integer localLateMethodRefCaptured();
        localLateMethodRefCaptured = bug1001Foo;
        print(localLateMethodRef());
        print(localLateCallableAttrib());
        print(localLateMethodRefCaptured());
        
        Integer x = initLateMethodRefCaptured();
        Integer y = paramMethodRefCaptured();

        void n() {
            Integer y = localLateMethodRefCaptured();
        }
    }
}
@nomodel
void bug1001bar(Bug1001 bug) {
    bug.paramMethodRefCaptured2(0);
}
