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
import com.redhat.ceylon.compiler.java.test.interop{
    JavaWithStaticMembersSubClass{smethod=method, sfield=field, stopMethod=topMethod, stopField=topField}
}

@nomodel
@error
void staticMethods() {
    @error
    variable File sync;
    // on Type
    @error
    sync := createTempFile("", "");
    @error
    sync := createTempFile("", "", sync);
    @error
    Object roots1 = listRoots();
    // on instances
    @error
    sync := sync.createTempFile("", "");
    @error
    sync := sync.createTempFile("", "", sync);
    @error
    Object roots2 = sync.listRoots();
}

@nomodel
@error
void staticMethodsNamed() {
    // currently broken because of https://github.com/ceylon/ceylon-spec/issues/208
    // This is silly really, but we test it anyway
    @error
    variable File sync;
    // on Type
    @error
    sync := createTempFile{arg0=""; arg1="";};
    @error
    sync := createTempFile{arg0=""; arg1=""; @error arg2=sync;};
    @error
    Object roots1 = listRoots{};
    // on instances
    @error
    sync := sync.createTempFile{arg0=""; arg1="";};
    @error
    sync := sync.createTempFile{arg0=""; arg1=""; @error arg2=sync;};
    @error
    Object roots2 = sync.listRoots{};
}

@error
@nomodel
void staticMethodsAndSubClassesOnInstance() {
    @error
    JavaWithStaticMembersSubClass inst = JavaWithStaticMembersSubClass();
    @error
    inst.method();
    @error
    inst.method(1);
    // this fails because of https://github.com/ceylon/ceylon-spec/issues/210
    @error
    inst.method(1, 2);
    @error
    inst.topMethod();
    @error
    inst.field := inst.field;
    @error
    inst.topField := inst.topField;
}

@nomodel
void staticMethodsAndSubClassesOnType() {
    @error
    smethod();
    @error
    smethod(1);
    // this fails because of https://github.com/ceylon/ceylon-spec/issues/210
    @error
    method(1, 2);
    @error
    stopMethod();
    @error
    sfield := sfield;
    @error
    stopField := stopField;
}

@error
@nomodel
class StaticMethodsAndSubClasses() extends JavaWithStaticMembersSubClass() {
    void test(){
        @error
        method();
        @error
        method(1);
        // this fails because of https://github.com/ceylon/ceylon-spec/issues/210
        @error
        method(1, 2);
        @error
        topMethod();
        @error
        field := field;
        @error
        stopField := stopField;
    }
}

// fails due to https://github.com/ceylon/ceylon-spec/issues/211
@error
@nomodel
class StaticMethodsOverriding() extends JavaWithStaticMembers() {
    @error
    shared actual void topMethod(){}
    @error
    shared actual variable Integer topField := 2;
}
