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
@error
void staticMethods() {
    @error
    variable File sync;
    // on Type
    @error
    sync = createTempFile("", "");
    @error
    sync = createTempFile("", "", sync);
    @error
    Object roots1 = listRoots();
    // on instances
    @error
    sync = sync.createTempFile("", "");
    @error
    sync = sync.createTempFile("", "", sync);
    @error
    Object roots2 = sync.listRoots();
}

@error
@noanno
void staticMethodsAndSubClassesOnInstance() {
    @error
    JavaWithStaticMembersSubClass inst = JavaWithStaticMembersSubClass();
    @error
    inst.method();
    @error
    inst.method(1);
    @error
    inst.method(1, 2);
    @error
    inst.topMethod();
    @error
    inst.field = inst.field;
    @error
    inst.topField = inst.topField;
}

@noanno
void staticMethodsAndSubClassesOnType() {
    @error
    smethod();
    @error
    smethod(1);
    @error
    smethod(1, 2);
    @error
    stopMethod();
    @error
    sfield = sfield;
    @error
    stopField = stopField;
}

@error
@noanno
class StaticMethodsAndSubClasses() extends JavaWithStaticMembersSubClass() {
    void test(){
        @error
        method();
        @error
        method(1);
        @error
        method(1, 2);
        @error
        topMethod();
        @error
        field = field;
        @error
        stopField = stopField;
    }
}

@noanno
@error
void staticFields() {
    JBoolean false = bFalse;
}