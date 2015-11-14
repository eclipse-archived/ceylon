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
import java.io{File{createTempFile, listRoots}}
import java.lang{JBoolean = Boolean{bFalse = \iFALSE}}
import com.redhat.ceylon.compiler.java.test.interop{
    JavaWithStaticMembersSubClass{smethod=method, sfield=field, stopMethod=topMethod, stopField=topField}
}

@noanno
void staticMethods() {
    variable File sync;
    // on Type
    sync = createTempFile("", "");
    sync = createTempFile("", "", sync);
    Object roots1 = listRoots();
    // on instances
    sync = sync.createTempFile("", "");
    sync = sync.createTempFile("", "", sync);
    Object roots2 = sync.listRoots();
}

@noanno
void staticMethodsAndSubClassesOnInstance() {
    JavaWithStaticMembersSubClass inst = JavaWithStaticMembersSubClass();
    inst.method();
    inst.method(1);
    inst.method(1, 2);
    inst.topMethod();
    inst.field = inst.field;
    inst.topField = inst.topField;
}

@noanno
void staticMethodsAndSubClassesOnType() {
    smethod();
    smethod(1);
    smethod(1, 2);
    stopMethod();
    sfield = sfield;
    stopField = stopField;
}

@noanno
class StaticMethodsAndSubClasses() extends JavaWithStaticMembersSubClass() {
    void test(){
        method();
        method(1);
        method(1, 2);
        topMethod();
        field = field;
        stopField = stopField;
    }
}

@noanno
void staticFields() {
    JBoolean false = bFalse;
}