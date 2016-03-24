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
class IndirectInvoke<X>() {

    // Parameter primary
    void mParameter(Anything() indirect) {
        indirect();
    }
    void mParameter2(Anything(Integer) indirect) {
        indirect(1);
    }
    void mParameter3(Anything(Boolean) indirect) {
        indirect(true);
    }
    void mParameter4(Anything(String) indirect) {
        indirect("");
    }
    void mParameter5<T>(Anything(T) indirect, T t) {
        indirect(t);
    }
    
    // local member primary
    void mLocal(Anything() f) {
        Anything() indirect = f;
        indirect();
    }
    Integer mLocal2(Integer(Integer) f) {
        Integer(Integer) indirect = f;
        return indirect(1);
    }
    Boolean mLocal3(Boolean(Boolean) f) {
        Boolean(Boolean) indirect = f;
        return indirect(true);
    }
    String mLocal4(String(String) f) {
        String(String) indirect = f;
        return indirect("");
    }
    T mLocal5<T>(T(T) f, T t) {
        T(T) indirect = f;
        return indirect(t);
    }
    
    // invocation primary
    void mMethod(Anything()() indirect) {
        indirect()();
    }
    Integer mMethod2(Integer(Integer)() indirect) {
        return indirect()(1);
    }
    Boolean mMethod3(Boolean(Boolean)() indirect) {
        return indirect()(true);
    }
    String mMethod4(String(String)() indirect) {
        return indirect()("");
    }
    T mMethod5<T>(T(T)() indirect, T t) {
        return indirect()(t);
    }
    
    // getter primary
    Anything() getter1 {
        throw;
    }
    void mGetter1() {
        getter1();
    }
    Integer() getter2 {
        throw;
    }
    Integer mGetter2() {
        return getter2();
    }
    Boolean() getter3 {
        throw;
    }
    Boolean mGetter3() {
        return getter3();
    }
    String() getter4 {
        throw;
    }
    String mGetter4() {
        return getter4();
    }
    X() getter5 {
        throw;
    }
    X mGetter5() {
        return getter5();
    }
    
    // value primary
    Anything() value1 = mGetter1;
    void mValue1() {
        value1();
    }
    Integer() value2 = mGetter2;
    Integer mValue2() {
        return value2();
    }
    Boolean() value3 = mGetter3;
    Boolean mValue3() {
        return value3();
    }
    String() value4 = mGetter4;
    String mValue4() {
        return value4();
    }
    X() value5 = mGetter5;
    X mValue5() {
        return value5();
    }
}