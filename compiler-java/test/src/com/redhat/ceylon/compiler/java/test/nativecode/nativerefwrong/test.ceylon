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
import com.redhat.ceylon.compiler.java.test.nativecode.modsample { TestClass, testMethod }
import com.redhat.ceylon.compiler.java.test.nativecode.modok { Bar, foo }

native("jvm")
void nativeMethodJvm() {}

native("js")
void nativeMethodJs() {}

void test1() {
    value x = nativeMethodJvm();
    void test() {
        value x = nativeMethodJvm();
    }
}

void test2() {
    value x = nativeMethodJs();
    void test() {
        value x = nativeMethodJs();
    }
}

void test3() {
    value x = testMethod();
    value y = TestClass();
    void test() {
        value x = testMethod();
        value y = TestClass();
    }
}

void test4() {
    value x = foo();
    value y = Bar();
    void test() {
        value x = foo();
        value y = Bar();
    }
}

native("js")
void test5() {
    value x = nativeMethodJvm();
    void test() {
        value x = nativeMethodJvm();
    }
}

native("jvm")
void test6() {
    value x = nativeMethodJs();
    void test() {
        value x = nativeMethodJs();
    }
}
